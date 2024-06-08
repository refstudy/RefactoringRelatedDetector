package lab.ref.customrefactoring.minerhandler.tool.refminer;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.model.diff.FileDiff;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;

import lab.ref.customrefactoring.minerhandler.RefactoringCollectorTool;
import lab.ref.customrefactoring.minerhandler.RefactoringToolHandler;
import lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes.ExtractMethodParser;
import lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes.InlineMethodParser;
import lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes.MoveMethodParser;
import lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes.PullUpMethodParser;
import lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes.PushDownParser;
import lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes.RefMinerParseException;
import lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes.RefactoringParser;
import lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes.RenameMethodParser;
import lab.ref.customrefactoring.model.RefactoringInstance;
import lab.ref.customrefactoring.model.RepositoryStatus;
import lab.ref.customrefactoring.util.ExceptionLogger;
import lab.ref.customrefactoring.util.Utils;

public class RefMinerTool extends RefactoringCollectorTool {

    public RefMinerTool() {
        super("RefMiner");
    }

    @Override
    public void collect(RepositoryStatus repositoryStatus, String commit, String outputpath) {
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

        miner.detectAtCommit(repositoryStatus.getRepository(), commit,
                new RefactoringHandler() {
                    @Override
                    public void handle(String commitId, List<Refactoring> refactorings) {
                        List<RefactoringInstance> refactoringInstances = new ArrayList<>();

                        try {
                            List<FileDiff> diffs = getDiffFromRefactoringInstance(repositoryStatus, commit);
                            for (Refactoring ref : refactorings) {
                                RefactoringParser parser = getRefactoringParseType(ref);
                                if (parser == null) {
                                    continue;
                                }

                                try {

                                    RefactoringInstance rInstance = parser.build(diffs);
                                    rInstance.setCommit(commit);
                                    rInstance.setProject(repositoryStatus.getName());
                                    rInstance.addTools(getName());
                                    rInstance.setMetadata(ref.toJSON());

                                    refactoringInstances.add(rInstance);

                                    // Ref counter
                                    RefactoringToolHandler.refsCount.compute(ref.getName(),
                                            (k, v) -> (v == null) ? 1 : v + 1);
                                } catch (Exception e) {
                                    if (!(e instanceof RefMinerParseException)) {
                                        System.out.println("Warning: Invalid refactoring data: " + e.getMessage());
                                        ExceptionLogger.log(e,
                                                repositoryStatus.getName() + "-" + commit
                                                        + "\nWarning: Invalid refactoring data\n" + ref.toJSON());
                                    }
                                    // Error ref counter
                                    RefactoringToolHandler.refsCount.compute(ref.getName() + "_error",
                                            (k, v) -> (v == null) ? 1 : v + 1);

                                }

                            }
                            if (!refactoringInstances.isEmpty()) {
                                Utils.fileWrite(outputpath, refactoringInstances.toString());
                            }
                        } catch (Exception e) {
                            if(!e.getMessage().contains("COMMIT_IGNORED")){
                                System.out.println("Warning: Fail to get diff from commit " + commit);
                                ExceptionLogger.log(e,
                                        repositoryStatus.getName() + "-" + commit
                                                + "\nWarning: Fail to get diff\n");
                            }                           
                        }

                    }
                }, 300); // 5 min timeout

    }

    @Override
    public void clear() {
    }

    private RefactoringParser getRefactoringParseType(Refactoring refactoring) {
        switch (refactoring.getName()) {
            case "Move Method":
                return new MoveMethodParser(refactoring);
            case "Pull Up Method": // Includes PULL_UP and PULL_UP_SIGNATURE
                return new PullUpMethodParser(refactoring);
            case "Push Down Method": // INCLUDES PUSH_DOWN and PUSH_DOWN_IMPL
                return new PushDownParser(refactoring);
            case "Rename Method":
                return new RenameMethodParser(refactoring);
            case "Extract Method":
                return new ExtractMethodParser(refactoring);
            case "Inline Method":
                return new InlineMethodParser(refactoring);
            default:
                return null;
        }
    }

}

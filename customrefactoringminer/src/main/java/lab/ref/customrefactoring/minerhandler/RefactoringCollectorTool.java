package lab.ref.customrefactoring.minerhandler;

import java.util.List;

import org.metricsminer.DiffCollector;
import org.metricsminer.control.ASTParserException;
import org.metricsminer.model.diff.FileDiff;
import org.metricsminer.util.CommandException;

import com.google.gson.Gson;

import lab.ref.customrefactoring.model.RepositoryStatus;

public abstract class RefactoringCollectorTool {

    protected Gson gson = new Gson();
    private String name;

    protected RefactoringCollectorTool(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract void collect(RepositoryStatus repositoryStatus, String commit, String outputpath);

    public abstract void clear();

    public List<FileDiff> getDiffFromRefactoringInstance(RepositoryStatus repositoryStatus, String commit) throws CommandException, ASTParserException {
        DiffCollector diff = new DiffCollector();
        return diff.runUsingGitFiles(repositoryStatus.getFullPath(), commit).getFiles();
    }

}

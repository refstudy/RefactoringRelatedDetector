package org.metricsminer;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import org.metricsminer.control.ASTHandler;
import org.metricsminer.control.ASTParserException;
import org.metricsminer.metric.collector.MetricCollector;
import org.metricsminer.model.Commit;
import org.metricsminer.model.astnodes.FileAST;
import org.metricsminer.model.diff.FileDiff;
import org.metricsminer.util.CommandException;
import org.metricsminer.util.GitUtils;
import org.metricsminer.util.Utils;

public class DiffCollector {

    private Path outputPath;
    private Path tempPath;

    public static HashMap<String, String> tempToFileMap = new HashMap<>();

    public DiffCollector() {
        this(Path.of("../files"));
    }

    public DiffCollector(Path outputPath) {
        this.outputPath = outputPath;
        outputPath.toFile().mkdirs();
        tempPath = outputPath.resolve("temp");
        tempPath.toFile().mkdirs();
    }

    public Commit runUsingGitFiles(String localRepositoryPath, String commitHash)
            throws CommandException, ASTParserException {

        Commit commit = new Commit(commitHash);
        ArrayList<String> changedFilesCommandResponse;

        changedFilesCommandResponse = GitUtils.getChangedJavaFilesInCommit(localRepositoryPath, commitHash);
        if (changedFilesCommandResponse.size() > 30) {
            throw new CommandException("COMMIT_IGNORED: Tamanho excessivo " + commitHash);
        }
        for (String changedFileCommand : changedFilesCommandResponse) {
            String changedFile[] = changedFileCommand.split("\t");

            char modificationType = changedFile[0].charAt(0);

            File before = null;
            File after = null;

            String afterName = changedFile[1];

            switch (modificationType) {
                // Rename/move
                case 'R':
                    before = GitUtils.downloadFileFromCommit(localRepositoryPath, tempPath, commitHash + "^",
                            changedFile[1]);
                    after = GitUtils.downloadFileFromCommit(localRepositoryPath, tempPath, commitHash,
                            changedFile[2]);
                    afterName = changedFile[2];
                    break;
                // OtheFile
                case 'M':
                    before = GitUtils.downloadFileFromCommit(localRepositoryPath, tempPath, commitHash + "^",
                            changedFile[1]);
                    after = GitUtils.downloadFileFromCommit(localRepositoryPath, tempPath, commitHash,
                            changedFile[1]);
                    break;
                // New File
                case 'A':
                    after = GitUtils.downloadFileFromCommit(localRepositoryPath, tempPath, commitHash,
                            changedFile[1]);
                    break;
                // File removal
                case 'D':
                    before = GitUtils.downloadFileFromCommit(localRepositoryPath, tempPath, commitHash + "^",
                            changedFile[1]);
                    break;
            }

            if (before == null) {
                before = Utils.generateEmptyJavaFile(tempPath);
            }

            if (after == null) {
                after = Utils.generateEmptyJavaFile(tempPath);
            }

            tempToFileMap.put(before.getName(), changedFile[1]);
            tempToFileMap.put(after.getName(), afterName);

            FileDiff fileDiff = runUsingLocalFile(before, after);
            commit.getFiles().add(fileDiff);
            before.delete();
            after.delete();
            tempToFileMap.remove(before.getName());
            tempToFileMap.remove(after.getName());
        }

        return commit;
    }

    public FileDiff runUsingLocalFile(File before, File after) throws ASTParserException {

        FileAST beforeFileAST = ASTHandler.setUpAST(before);
        FileAST afterFileAST = ASTHandler.setUpAST(after);
        return MetricCollector.runAllCollectors(beforeFileAST, afterFileAST);
    }

}

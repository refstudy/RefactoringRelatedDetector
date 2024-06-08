package org.metricsminer.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GitUtils {

    public static void cloneRepository(String repositoryUrl, Path repositoryLocalPath) {
        String projectName = repositoryUrl.substring(repositoryUrl.lastIndexOf("/") + 1,
                repositoryUrl.lastIndexOf("."));

        if (!Files.exists(repositoryLocalPath.resolve(projectName))) {
            System.out.println("Baixando projeto: " + projectName);
            try {
                Utils.executeCommand(
                        new String[] { "git", "clone", repositoryUrl },
                        repositoryLocalPath.toString());
                System.out.println("Projeto: " + projectName + " baixado com sucesso");
            } catch (Exception e) {
                System.out.println("Projeto: " + projectName + " teve erro ao baixar\n" + e.getMessage());
            }
        } else {
            System.out.println("Projeto ja baixado: " + projectName);
        }

    }

    public static File downloadFileFromCommit(String repositoryPath, Path savePath, String commit, String fileName)
            throws CommandException {

        File tempFile = Utils.generateEmptyJavaFile(savePath);
        Utils.fileWrite(tempFile.getAbsolutePath(), Utils.executeCommand(
                new String[] { "git", "show", commit + ":" + fileName }, repositoryPath));

        return tempFile;
    }

    public static ArrayList<String> getChangedJavaFilesInCommit(String repositoryPath, String commit)
            throws CommandException {

        String result = Utils.executeCommand(
                new String[] { "git", "log", "-1", commit, "--pretty=format:", "--name-status" }, repositoryPath);
        ArrayList<String> javaFile = new ArrayList<>();
        for (String file : result.split("\n")) {
            if (file.endsWith(".java")) {
                javaFile.add(file);
            }
        }
        return javaFile;
    }

}

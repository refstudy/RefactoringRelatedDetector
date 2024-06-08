package org.metricsminer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Utils {


    public static File generateEmptyJavaFile(Path path) {
        Random r = new Random();
        int random = r.nextInt(Integer.MAX_VALUE);
        String filePath = path.resolve(random + ".java").toString();
        Utils.fileWrite(filePath, "");
        return new File(filePath);
    }

    // Escreve um conteúdo em um arquivo
    public static synchronized void fileWrite(String fileName, String content) {
        File file = new File(fileName);

        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lê um arquivo e retorna todas as linhas em forma de lista
    public static List<String> fileRead(String filePath) {
        List<String> result = new ArrayList<String>();

        try {

            FileReader file = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(file);
            String linha = reader.readLine();

            while (linha != null) {
                result.add(linha);
                linha = reader.readLine();
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeCommand(String command, String path) {
        try {
            return executeCommand(new String[] { command }, path);
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    // Executa um comando e retorna a sua saída
    public static synchronized String executeCommand(String command[], String path) throws CommandException {
        File f;

        Process process = null;
        String commandReturnMessage = "";

        try {

            if (path != null && !path.isBlank()) {
                f = new File(path);
                process = Runtime.getRuntime().exec(command, null, f);
            } else {
                process = Runtime.getRuntime().exec(
                        command, null,
                        new File(System.getProperty("user.dir")));
            }

            // Final versions of the the params, to be used within the threads
            final BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            final BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            final CopyOnWriteArrayList<String> outputData = new CopyOnWriteArrayList<>();
            final CopyOnWriteArrayList<String> errData = new CopyOnWriteArrayList<>();
            // Thread that reads std out and feeds the writer given in input
            new Thread() {
                @Override
                public void run() {
                    String line;
                    try {
                        while ((line = stdOut.readLine()) != null) {
                            outputData.add(line + "\n");
                        }
                    } catch (Exception e) {
                        throw new Error(e);
                    }
                }
            }.start(); // Starts now

            // Thread that reads std err and feeds the writer given in input
            new Thread() {
                @Override
                public void run() {
                    String line;
                    try {
                        while ((line = stdErr.readLine()) != null) {
                            errData.add(line + "\n");
                        }
                    } catch (Exception e) {
                        throw new Error(e);
                    }
                }
            }.start(); // Starts now

            // Wait until the end of the process
            try {
                process.waitFor();
            } catch (Exception e) {
                throw new Error(e);
            }

            commandReturnMessage = String.join("", outputData);

            if (process.exitValue() != 0) {
                throw new CommandException(String.join("", errData));
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return commandReturnMessage + "\n";
    }

    public static char[] ReadFileToCharArray(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
        }

        return fileData.toString().toCharArray();
    }

}

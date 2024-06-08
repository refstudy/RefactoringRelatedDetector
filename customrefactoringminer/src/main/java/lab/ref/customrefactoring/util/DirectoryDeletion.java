package lab.ref.customrefactoring.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;

public class DirectoryDeletion {

    public static void deleteDirectory(String path) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows platform
             String command = "cmd.exe /c rd /s /q \"" + path + "\"";
            try {
                Process process = Runtime.getRuntime().exec(command);
                //System.out.println(command);
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    // System.out.println("Directory deleted successfully: " + path);
                    // final BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    // final ArrayList<String> outData = new ArrayList<>();
                    // String line;
                    // try {
                    //     while ((line = stdOut.readLine()) != null) {
                    //         outData.add(line + "\n");
                    //     }
                    //     System.out.println(String.join("", outData));
                    // } catch (Exception e) {
                    //     e.printStackTrace();
                    //     throw new Error(e);
                    // }
                    //System.exit(0);
                } else {
                    System.out.println("Failed to delete the directory " + path);
                    final BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    final ArrayList<String> errData = new ArrayList<>();
                    String line;
                    try {
                        while ((line = stdErr.readLine()) != null) {
                            errData.add(line + "\n");
                        }
                        System.out.println(String.join("", errData));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Error(e);
                    }

                }
            } catch (InterruptedException e) {
                System.out.println("An error occurred while deleting the directory: " + e.getMessage());
            }
        } else {
            // Unsupported platform
            System.out.println("Unsupported operation for the current platform.");
        }
    }

}
package lab.ref.customrefactoring;

import lab.ref.customrefactoring.minerhandler.RefactoringToolHandler;
import lab.ref.customrefactoring.minerhandler.tool.refminer.RefMinerTool;
import lab.ref.customrefactoring.util.ExceptionLogger;

public class App {

    public static String configUrl = "config.json";

    public static void main(String[] args) {
        try {
            RefactoringToolHandler
                    .build(configUrl)
                    .addTool(new RefMinerTool())
                    .collectAll();

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionLogger.log(e, "main");
        }

    }

}

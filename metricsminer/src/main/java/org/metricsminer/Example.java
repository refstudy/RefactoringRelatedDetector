package org.metricsminer;

import java.io.File;

import org.metricsminer.control.ASTParserException;
import org.metricsminer.model.diff.FileDiff;

public class Example {

    public static void main(String args[]) throws ASTParserException {
        DiffCollector diff = new DiffCollector();
        FileDiff fileDiff = diff.runUsingLocalFile(new File("test/Calculadora.java"),
                new File("test/Calculadora2.java"));
        fileDiff.printShortFileDiff();

    }

}

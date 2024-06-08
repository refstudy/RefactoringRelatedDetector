package org.metricsminer.control;

import java.io.File;
import java.util.Hashtable;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.metricsminer.DiffCollector;
import org.metricsminer.model.astnodes.FileAST;
import org.metricsminer.util.Utils;

public class ASTHandler {
    public static synchronized FileAST setUpAST(File javaFile) throws ASTParserException {
        try {

            char[] sourceCode = Utils.ReadFileToCharArray(javaFile.getAbsolutePath());

            Hashtable<String, String> options = JavaCore.getOptions();
            options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
            JavaCore.setComplianceOptions(JavaCore.VERSION_17, options);

            // JAVA 18
            ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            parser.setCompilerOptions(options);

            // For AST build even with error
            parser.setBindingsRecovery(true);
            parser.setResolveBindings(true);
            parser.setStatementsRecovery(true);

            String[] sourcePaths = { javaFile.getParent() };
            parser.setEnvironment(null, sourcePaths, null, true);

            parser.setUnitName(javaFile.getName());

            parser.setSource(sourceCode);

            CompilationUnit cu = (CompilationUnit) parser.createAST(null);

            if ((cu.getFlags() & ASTNode.MALFORMED) != 0) // bitwise flag to check if the node has a syntax error
            {
                System.out.println("deu ruim aqui");
                System.out.println(new String(sourceCode));
                System.out.println(javaFile.getAbsolutePath());
                System.out.println(DiffCollector.tempToFileMap.get(javaFile.getName().toString()));
                throw new ASTParserException("AST malformed");
            }

            CustomASTVisitor astVisitor = new CustomASTVisitor();
            cu.accept(astVisitor);

            FileAST fileAST = astVisitor.getFileElement();
            fileAST.setJavaFileContent(new String(sourceCode));
            fileAST.setPath(DiffCollector.tempToFileMap.get(javaFile.getName()));
            fileAST.updateScope();

            return fileAST;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ASTParserException(e.getMessage());
        }
    }
}

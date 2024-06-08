package org.metricsminer.metric.collector;

import java.io.IOException;
import java.io.Reader;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.model.astnodes.FileAST;

import com.github.gumtreediff.gen.TreeGenerator;
import com.github.gumtreediff.gen.jdt.AbstractJdtVisitor;
import com.github.gumtreediff.gen.jdt.JdtVisitor;
import com.github.gumtreediff.tree.TreeContext;

public class GumTreeJDTGenerator extends TreeGenerator {

    public TreeContext generateFromFileAst(FileAST fileAST) throws Exception {
        IScanner scanner = ToolFactory.createScanner(true, false, false, false);

        scanner.setSource(fileAST.getJavaFileContent().toCharArray());
        AbstractJdtVisitor v = new JdtVisitor(scanner);
        if ((fileAST.getNode().getFlags() & ASTNode.MALFORMED) != 0)
            throw new Exception("Fail to parse AST");
        fileAST.getNode().accept(v);
        return v.getTreeContext();
    }

    @Override
    public TreeContext generate(Reader r) throws IOException {
        return null;
    }

}

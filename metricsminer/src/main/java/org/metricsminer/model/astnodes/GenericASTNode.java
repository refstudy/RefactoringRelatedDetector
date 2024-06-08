package org.metricsminer.model.astnodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class GenericASTNode<T extends ASTNode> extends TreeASTNode<T> {

    public GenericASTNode(T astNode) {
        super(astNode);
    }

    @Override
    public String getFullName() {
        return getAncestralName();
    }

    @Override
    public TreeASTNode<T> newEmptyNode(AST ast) {
        return new GenericASTNode(ast.newCompilationUnit());
    }
}

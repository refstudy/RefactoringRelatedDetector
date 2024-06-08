package org.metricsminer.model.astnodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class VariableFragmentASTNode extends TreeASTNode<VariableDeclarationFragment> {

    public VariableFragmentASTNode(VariableDeclarationFragment astNode) {
        super(astNode);
    }

    // @Override
    // public String getSimpleName() {
    //     return getParent().getName();
    // }

    // @Override
    // public String getName() {
    //     return getParent().getName();
    // }

    @Override
    public String getFullName() {
        return getAncestralName();
    }

    @Override
    public String getName() {
        return "";
    }



    public String getValue() {
        return getNode().getInitializer() != null ? getNode().getInitializer().toString() : "";
    }

    @Override
    public TreeASTNode<VariableDeclarationFragment> newEmptyNode(AST ast) {
        return new VariableFragmentASTNode(ast.newVariableDeclarationFragment());
    }

}

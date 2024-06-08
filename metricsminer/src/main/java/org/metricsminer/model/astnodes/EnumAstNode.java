package org.metricsminer.model.astnodes;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class EnumAstNode extends AbstractStructureASTNode<EnumDeclaration> {

    public EnumAstNode(EnumDeclaration astNode) {
        super(astNode);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getInterfaces() {
        return ((List<org.eclipse.jdt.core.dom.Type>) getNode().superInterfaceTypes()).stream()
                .map(x -> x.toString()).toList();
    }

    @Override
    public TreeASTNode<EnumDeclaration> newEmptyNode(AST ast) {
        return new EnumAstNode(ast.newEnumDeclaration());
    }


}

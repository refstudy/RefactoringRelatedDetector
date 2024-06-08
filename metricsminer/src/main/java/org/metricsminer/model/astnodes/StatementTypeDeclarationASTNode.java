package org.metricsminer.model.astnodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

public class StatementTypeDeclarationASTNode extends AbstractStructureASTNode<AbstractTypeDeclaration> {

    private TypeDeclarationStatement astNode;

    public StatementTypeDeclarationASTNode(TypeDeclarationStatement astNode) {
        super(astNode.getDeclaration());
        this.astNode = astNode;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getInterfaces() {
        if (getNode() instanceof TypeDeclaration) {
            return ((TypeDeclaration) getNode()).superInterfaceTypes().stream()
                    .map(x -> x.toString()).toList();
        } else if (getNode() instanceof EnumDeclaration) {
            return ((EnumDeclaration) getNode()).superInterfaceTypes().stream()
                    .map(x -> x.toString()).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public String getNodeType() {
        return astNode.getClass().getSimpleName();

    }

    public boolean isInterface() {
        if (getNode() instanceof TypeDeclaration) {
            return ((TypeDeclaration) getNode()).isInterface();
        }
        return false;
    }

    @Override
    public TreeASTNode<AbstractTypeDeclaration> newEmptyNode(AST ast) {
        return new StatementTypeDeclarationASTNode(ast.newTypeDeclarationStatement(ast.newTypeDeclaration()));
    }

}

package org.metricsminer.model.astnodes;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class FieldASTNode extends AbstractDeclarationASTNode<FieldDeclaration> implements VariableHandler {

    public FieldASTNode(FieldDeclaration astNode) {
        super(astNode);
    }

    // @Override
    // public String getName() {
    //     List<VariableDeclarationFragment> declarationFragments = getNode().fragments();

    //     if (declarationFragments.size() != 1) {
    //         // It happens when creating multiple variables at same moment ex: a,b = 3
    //         // System.out.println("WARNING: It should never happens");
    //         return super.getSimpleName();
    //     }
    //     return declarationFragments.get(0).getName().toString();
    // }

    @Override
    public List<VariableDeclarationFragment> getVariables() {
        return getNode().fragments();
    }

    @Override
    public String getVarType() {
        List<Dimension> dimensions = getVariables().get(0).extraDimensions();
        String dimension = dimensions.stream()
                .map(Dimension::toString)
                .reduce((dim1, dim2) -> dim1 + dim2)
                .orElse("");
        return this.getNode().getType().toString() + dimension;
    }

    @Override
    public TreeASTNode<FieldDeclaration> newEmptyNode(AST ast) {
        return new FieldASTNode(ast.newFieldDeclaration(ast.newVariableDeclarationFragment()));
    }
}

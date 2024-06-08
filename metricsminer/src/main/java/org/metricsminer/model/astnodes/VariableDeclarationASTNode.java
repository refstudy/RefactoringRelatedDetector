package org.metricsminer.model.astnodes;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class VariableDeclarationASTNode extends TreeASTNode<VariableDeclarationStatement> implements VariableHandler, StructureModifier  {

    public VariableDeclarationASTNode(VariableDeclarationStatement astNode) {
        super(astNode);
    }

    @Override
    public String getName() {
        List<VariableDeclarationFragment> declarationFragments = getNode().fragments();

        if (declarationFragments.size() != 1) {
            return super.getSimpleName();
        }
        return declarationFragments.get(0).getName().toString();
    }

    // @Override
    // public String getSimpleName() {
    //     return "MethodVariable";
    // }

    @Override
    public String getFullName() {
        return getAncestralName();
    }

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
    public List<Modifier> getModifiers() {
        return super.getNode().modifiers().stream().filter(modifier -> modifier instanceof Modifier).toList();
    }

    @Override
    public List<Annotation> getAnnotations() {
         return super.getNode().modifiers().stream().filter(modifier -> modifier instanceof Annotation).toList();
    }

	@Override
	public TreeASTNode<VariableDeclarationStatement> newEmptyNode(AST ast) {
		return new VariableDeclarationASTNode(ast.newVariableDeclarationStatement(ast.newVariableDeclarationFragment()));
	}

}

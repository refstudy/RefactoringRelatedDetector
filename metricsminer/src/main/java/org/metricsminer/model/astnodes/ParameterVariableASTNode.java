package org.metricsminer.model.astnodes;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
public class ParameterVariableASTNode extends TreeASTNode<SingleVariableDeclaration> implements StructureModifier {

    public ParameterVariableASTNode(SingleVariableDeclaration singleVariableDeclaration) {
        super(singleVariableDeclaration);
    }

    // @Override
    // public String getName() {
    //     return getNode().getName().toString();
    // }

    // @Override
    // public String getSimpleName() {
    //     return "Parameter";
    // }

    @Override
    public List<Modifier> getModifiers() {
        return super.getNode().modifiers().stream().filter(modifier -> modifier instanceof Modifier).toList();
    }

    @Override
    public List<Annotation> getAnnotations() {
        return super.getNode().modifiers().stream().filter(modifier -> modifier instanceof Annotation).toList();
    }

    @Override
    public TreeASTNode<SingleVariableDeclaration> newEmptyNode(AST ast) {
        return new ParameterVariableASTNode(ast.newSingleVariableDeclaration());
    }

}

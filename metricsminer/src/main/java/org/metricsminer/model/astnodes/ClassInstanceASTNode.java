package org.metricsminer.model.astnodes;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;

public class ClassInstanceASTNode extends TreeASTNode<ClassInstanceCreation> {

    public ClassInstanceASTNode(ClassInstanceCreation astNode) {
        super(astNode);
    }

    public boolean hasAnonymousClassDeclaration() {
        return getNode().getAnonymousClassDeclaration() != null;
    }

    public AnonymousClassDeclaration getAnonymousClassDeclaration() {
        return getNode().getAnonymousClassDeclaration();
    }

    public List<Expression> getArguments() {
        return getNode().arguments();
    }

    @Override
    public String getName() {
        return getNode().getType().toString();
    }

    @Override
    public TreeASTNode<ClassInstanceCreation> newEmptyNode(AST ast) {
        return new ClassInstanceASTNode(ast.newClassInstanceCreation());
    }

}

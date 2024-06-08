package org.metricsminer.model.astnodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;

public class MethodCallASTNode extends TreeASTNode<MethodInvocation> {

    private List<String> argumentTypeList = new ArrayList<>();

    private final String UNKOWN_TYPE = "UNKNOWN";

    public MethodCallASTNode(MethodInvocation astNode) {
        super(astNode);
        IMethodBinding methodBinding = astNode.resolveMethodBinding();
        if (methodBinding != null) {
            ITypeBinding[] parameterTypes = methodBinding.getParameterTypes();
            for (ITypeBinding typeBinding : parameterTypes) {
                argumentTypeList.add(typeBinding.getName());
            }
        } else {
            for (int i = 0; i < getArguements().size(); i++) {
                argumentTypeList.add(UNKOWN_TYPE);
            }
        }
    }

    @Override
    public String getName() {
        return getNode().getName().toString() + "(" + String.join(", ", getArguementTypes())
                + ")";
    }

    public List<Expression> getArguements() {
        return this.getNode().arguments();
    }

    public List<String> getArguementTypes() {
        return this.argumentTypeList;
    }

    public boolean validArgumentTypes() {
        for (String argType : this.getArguementTypes()) {
            if (argType.equals(UNKOWN_TYPE)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public TreeASTNode<MethodInvocation> newEmptyNode(AST ast) {
        return new MethodCallASTNode(ast.newMethodInvocation());
    }

}

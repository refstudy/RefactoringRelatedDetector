package org.metricsminer.model.astnodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.google.gson.JsonObject;

public class MethodASTNode extends AbstractDeclarationASTNode<MethodDeclaration> {

    public MethodASTNode(MethodDeclaration astNode) {
        super(astNode);
    }

    @Override
    public String getSimpleName() {
        return "Method";
    }

    @Override
    public String additionalAncestralName() {
        return getName();
    }

    public boolean isConstructor() {
        return getNode().isConstructor() || getNode().isCompactConstructor();
    }

    public String getReturn() {
        if (isConstructor()) {
            return "this";
        }
        return getNode().getReturnType2() != null ? getNode().getReturnType2().toString() : "void";

    }

    public List<String> getReturnValues() {

        ArrayList<String> rStatements = new ArrayList<>();

        rStatements.addAll(getReturnStatements(this, new ArrayList<>()).stream()
                .map(rStatement -> rStatement.getExpression() != null ? rStatement.getExpression().toString() : "void")
                .toList());
        return rStatements;
    }

    private List<ReturnStatement> getReturnStatements(TreeASTNode<? extends ASTNode> node,
            List<ReturnStatement> returnStatements) {

        node.getChildren().forEach(child -> {
            if (!(child.getNode() instanceof ReturnStatement)) {
                getReturnStatements(child, returnStatements);
            } else {
                returnStatements.add((ReturnStatement) child.getNode());
            }
        });
        return returnStatements;
    }

    @Override
    public String getFullName() {
        return getAncestralName() + "." + getNode().getName().toString() + "(" + String.join(", ", getParameters())
                + ")";
    }

    // public String getSignature() {
    //     List<String> orderedParams = new ArrayList<>(getParameters());
    //     Collections.sort(orderedParams);
    //     return getAncestralName() + "." + getNode().getName().toString() + "(" + String.join(", ", orderedParams)
    //             + ")";
    // }

    public int getNumberOfVariables() {
        return getVariables().size();
    }

    public List<String> getPossibleExceptions() {
        return getNode().thrownExceptionTypes().stream().map(exc -> exc.toString()).toList();
    }

    public List<Type> getPossibleExceptionTypes() {
        return getNode().thrownExceptionTypes();
    }

    // public List<String> getParameters() {
    // return getNode().parameters().stream().map(par ->
    // ((SingleVariableDeclaration) par).getType().toString())
    // .toList();
    // }

    @SuppressWarnings("unchecked")
    public List<String> getParameters() {
        return getNode().parameters().stream().map(par -> {
            SingleVariableDeclaration svd = (SingleVariableDeclaration) par;
            String type = svd.getType().toString();
            if (svd.isVarargs()) {
                type += "...";
            }
            return type;
        }).toList();
    }

    public List<String> getParametersWithName() {
        return getNode().parameters().stream().map(par -> ((SingleVariableDeclaration) par).toString()).toList();
    }

    public List<String> getParametersName() {
        return getNode().parameters().stream().map(par -> ((SingleVariableDeclaration) par).getName().toString())
                .toList();
    }

    public List<Type> getParametersTypeNode() {

        List parameterTypes = getNode().parameters().stream()
                .map(par -> ((SingleVariableDeclaration) par).getType())
                .toList();
        return parameterTypes;
    }

    public boolean isCallableFrom(MethodCallASTNode methodCallASTNode) {

        if (!methodCallASTNode.getName().equals(getName())) {
            return false;
        }

        List<String> arguments = methodCallASTNode.getArguementTypes();
        List<String> parameters = getParameters();
        if (arguments.size() != parameters.size()) {
            return false;
        }

        for (int i = 0; i < arguments.size(); i++) {
            if (!arguments.get(i).equals(parameters.get(i))) {
                return false;
            }
        }
        
        return true;

    }

    @Override
    protected JsonObject particularJsonInfo(TreeASTNode element) {
        JsonObject elemObject = new JsonObject();
        elemObject.addProperty("isConstructor", isConstructor());
        elemObject.addProperty("modifiers", getModifiers().toString());
        elemObject.addProperty("name", getName());
        elemObject.addProperty("return", getReturn());
        elemObject.addProperty("parameters", getParameters().toString());
        elemObject.addProperty("parametersSize", getNode().parameters().size());
        elemObject.addProperty("extraDimensionList", getNode().extraDimensions().toString());
        elemObject.addProperty("extraDimensionSize", getNode().getExtraDimensions());
        elemObject.addProperty("exceptions", getPossibleExceptions().toString());
        elemObject.addProperty("exceptionsSize", getNode().thrownExceptionTypes().size());
        elemObject.addProperty("fullName", getFullName());
        elemObject.addProperty("numberOfVariables", getNumberOfVariables());
        return elemObject;
    }

    @Override
    public String getName() {
        return getNode().getName().toString() + "(" + String.join(", ", getParameters())
                + ")";
    }

    @Override
    public List<VariableDeclarationFragment> getVariables() {
        ArrayList<VariableDeclarationFragment> fragments = new ArrayList<>();
        getChildren().stream().forEach(child -> {
            if (child instanceof VariableDeclarationASTNode) {
                fragments.addAll(((VariableDeclarationASTNode) child).getVariables());
            }
        });

        return fragments;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public TreeASTNode<MethodDeclaration> newEmptyNode(AST ast) {
        return new MethodASTNode(ast.newMethodDeclaration());
    }

}

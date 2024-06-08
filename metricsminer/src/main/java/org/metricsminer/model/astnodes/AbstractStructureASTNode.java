package org.metricsminer.model.astnodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public abstract class AbstractStructureASTNode<T extends AbstractTypeDeclaration>
        extends AbstractDeclarationASTNode<T> {

    public AbstractStructureASTNode(T astNode) {
        super(astNode);
    }

    public List<MethodASTNode> getMethods() {
        ArrayList<MethodASTNode> methods = new ArrayList<>();
        getChildren().forEach((child) -> {
            if (child instanceof MethodASTNode) {
                methods.add((MethodASTNode) child);
            }
        });
        return methods;
    }

    public List<FieldASTNode> getFields() {
        ArrayList<FieldASTNode> fields = new ArrayList<>();
        getChildren().forEach((child) -> {
            if (child instanceof FieldASTNode) {
                fields.add((FieldASTNode) child);
            }
        });
        return fields;
    }

    @Override
    public List<VariableDeclarationFragment> getVariables() {
        ArrayList<VariableDeclarationFragment> variables = new ArrayList<>();
        getFields().forEach(field -> {
            variables.addAll(field.getVariables());
        });
        return variables;
    }

    public int getNumberOfFields() {
        return getFields().size();
    }

    public int getNumberOfMethods() {
        return getMethods().size();
    }

    @Override
    public String getName() {
        return getNode().getName().toString();
    }

    @Override
    public String additionalAncestralName() {
        return getNode().getName().toString();
    }

    public abstract List<String> getInterfaces();

}
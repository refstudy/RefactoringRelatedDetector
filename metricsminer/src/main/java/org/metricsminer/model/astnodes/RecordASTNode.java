package org.metricsminer.model.astnodes;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.RecordDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.google.gson.JsonObject;

public class RecordASTNode extends AbstractStructureASTNode<RecordDeclaration> {

    public RecordASTNode(RecordDeclaration astNode) {
        super(astNode);
    }

    public MethodDeclaration[] getNodeMethods() {
        return getNode().getMethods();
    }

    public FieldDeclaration[] getNodeFields() {
        return getNode().getFields();
    }

    public String getTypeParameters() {
        return getNode().typeParameters().toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getInterfaces() {
        return ((List<org.eclipse.jdt.core.dom.Type>) getNode().superInterfaceTypes()).stream()
                .map(x -> x.toString()).toList();
    }

    @Override
    protected JsonObject particularJsonInfo(TreeASTNode<?> element) {
        JsonObject elemObject = new JsonObject();
        elemObject.addProperty("modifiers", getModifiers().toString());
        elemObject.addProperty("annotations", getAnnotations().toString());
        elemObject.addProperty("access", getAccessModifier());
        elemObject.addProperty("name", getName());
        elemObject.addProperty("typeParameters", getTypeParameters());
        elemObject.addProperty("fullName", getFullName());
        elemObject.addProperty("numberOfFields", getNumberOfFields());
        elemObject.addProperty("numberOfMethods", getNumberOfMethods());
        elemObject.addProperty("interfaces", getInterfaces().toString());
        return elemObject;
    }

    @Override
    public TreeASTNode<RecordDeclaration> newEmptyNode(AST ast) {
        return new RecordASTNode(ast.newRecordDeclaration());
    }

}

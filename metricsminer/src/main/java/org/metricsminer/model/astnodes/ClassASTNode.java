package org.metricsminer.model.astnodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.google.gson.JsonObject;

public class ClassASTNode extends AbstractStructureASTNode<TypeDeclaration> {

    public enum Type {
        INTERFACE,
        CLASS
    }

    public ClassASTNode(TypeDeclaration astNode) {
        super(astNode);
    }

    public boolean isInterface() {
        return super.getNode().isInterface();
    }

    public ClassASTNode.Type getType() {
        if (isInterface()) {
            return Type.INTERFACE;
        }
        return Type.CLASS;
    }

    // @Override
    // public String additionalAncestralName() {
    //     return getNode().getName().toString();
    // }

    @Override
    public String getSimpleName() {
        if (isInterface()) {
            return "Interface";
        } else {
            return "Class";
        }
    }

    public MethodDeclaration[] getNodeMethods() {
        return getNode().getMethods();
    }

    public FieldDeclaration[] getNodeFields() {
        return getNode().getFields();
    }

    @Override
    public String getName() {
        return getNode().getName().toString();
    }

    public String getTypeParameters() {
        return getNode().typeParameters().toString();
    }

    // TODO: Incluir os operadores diamond
    public String getHierarchyParent() {
        org.eclipse.jdt.core.dom.Type parentClass = getNode().getSuperclassType();
        if (parentClass == null) {
            return "";
        }
        return parentClass.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getInterfaces() {
        return ((List<org.eclipse.jdt.core.dom.Type>) getNode().superInterfaceTypes()).stream()
                .map(x -> x.toString()).toList();
    }

    public String getPermittedTypes() {
        return getNode().permittedTypes().toString();
    }

    @Override
    protected JsonObject particularJsonInfo(TreeASTNode<?> element) {
        JsonObject elemObject = new JsonObject();
        elemObject.addProperty("modifiers", getModifiers().toString());
        elemObject.addProperty("annotations", getAnnotations().toString());
        elemObject.addProperty("access", getAccessModifier());
        elemObject.addProperty("type", getType().toString().toLowerCase());
        elemObject.addProperty("name", getName());
        elemObject.addProperty("typeParameters", getTypeParameters());
        elemObject.addProperty("fullName", getFullName());
        elemObject.addProperty("numberOfFields", getNumberOfFields());
        elemObject.addProperty("numberOfMethods", getNumberOfMethods());
        elemObject.addProperty("interfaces", getInterfaces().toString());
        elemObject.addProperty("hierarchy", getHierarchyParent());
        elemObject.addProperty("permitted", getPermittedTypes());
        return elemObject;
    }

    @Override
    public TreeASTNode<TypeDeclaration> newEmptyNode(AST ast) {        
        return new ClassASTNode(ast.newTypeDeclaration());
    }

}

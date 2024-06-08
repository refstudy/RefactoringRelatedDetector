package org.metricsminer.model.astnodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import com.google.gson.JsonObject;

public class FileAST extends TreeASTNode<CompilationUnit> {

    private String javaFileContent;
   
    public FileAST(CompilationUnit astNode) {
        super(astNode);
        if(super.getNode().getPackage() != null){
            super.setAncestralName(super.getNode().getPackage().getName().toString());
        }else{
            super.setAncestralName("");
        }
    }

    public void setJavaFileContent(String javaFileContent) {
        this.javaFileContent = javaFileContent;
    }

    public String getJavaFileContent() {
        return javaFileContent;
    }
    
    public String getPackage() {
        PackageDeclaration packageDeclaration = super.getNode().getPackage();

        if (packageDeclaration != null) {
            return packageDeclaration.getName().toString();
        }

        return "default";
    }

    public List getImports() {
        return super.getNode().imports();
    }

    public List<ClassASTNode> getClasses() {
        ArrayList<ClassASTNode> interfacesNodes = new ArrayList<>();
        getChildren().forEach(childrenNode -> {
            if (childrenNode instanceof ClassASTNode) {
                ClassASTNode classNode = (ClassASTNode) childrenNode;
                if (!classNode.isInterface()) {
                    interfacesNodes.add(classNode);
                }
            }
        });
        return interfacesNodes;
    }

    public List<ClassASTNode> getInterfaces() {
        ArrayList<ClassASTNode> interfacesNodes = new ArrayList<>();
        getChildren().forEach(childrenNode -> {
            if (childrenNode instanceof ClassASTNode) {
                ClassASTNode classNode = (ClassASTNode) childrenNode;
                if (classNode.isInterface()) {
                    interfacesNodes.add(classNode);
                }
            }
        });
        return interfacesNodes;
    }

    public List<RecordASTNode> getRecords() {
        ArrayList<RecordASTNode> recordNodes = new ArrayList<>();
        getChildren().forEach(childrenNode -> {
            if (childrenNode instanceof RecordASTNode) {
                recordNodes.add((RecordASTNode) childrenNode);
            }
        });
        return recordNodes;
    }

    public List<EnumAstNode> getEnums() {
        ArrayList<EnumAstNode> enumNodes = new ArrayList<>();
        getChildren().forEach(childrenNode -> {
            if (childrenNode instanceof EnumAstNode) {
                enumNodes.add((EnumAstNode) childrenNode);
            }
        });
        return enumNodes;
    }

    @Override
    public String getSimpleName() {
        return "File";
    }

    @Override
    protected JsonObject particularJsonInfo(TreeASTNode element) {
        JsonObject elemObject = new JsonObject();
        elemObject.addProperty("package", getPackage());
        return elemObject;
    }

    // @Override
    // public String toString() {
    // JsonObject object = super.toJson();
    // object.addProperty("package", getPackage());
    // return object.toString();
    // }

    @Override
    public TreeASTNode<CompilationUnit> newEmptyNode(AST ast) {
        return new FileAST(ast.newCompilationUnit());
    }
}
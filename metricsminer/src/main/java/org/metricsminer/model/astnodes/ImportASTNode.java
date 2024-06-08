package org.metricsminer.model.astnodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ImportDeclaration;

public class ImportASTNode extends TreeASTNode<ImportDeclaration> {

    public ImportASTNode(ImportDeclaration astNode) {
        super(astNode);
    }

    @Override
    public String getSimpleName() {
        return "Imports";
    }

    @Override
    public TreeASTNode<ImportDeclaration> newEmptyNode(AST ast) {
        return new ImportASTNode(ast.newImportDeclaration());
    }
}

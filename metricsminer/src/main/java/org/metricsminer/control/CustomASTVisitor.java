package org.metricsminer.control;

import java.util.Stack;

import org.eclipse.jdt.core.dom.*;
import org.metricsminer.model.astnodes.ClassASTNode;
import org.metricsminer.model.astnodes.ClassInstanceASTNode;
import org.metricsminer.model.astnodes.EnumAstNode;
import org.metricsminer.model.astnodes.FieldASTNode;
import org.metricsminer.model.astnodes.FileAST;
import org.metricsminer.model.astnodes.GenericASTNode;
import org.metricsminer.model.astnodes.ImportASTNode;
import org.metricsminer.model.astnodes.MethodASTNode;
import org.metricsminer.model.astnodes.MethodCallASTNode;
import org.metricsminer.model.astnodes.ParameterVariableASTNode;
import org.metricsminer.model.astnodes.RecordASTNode;
import org.metricsminer.model.astnodes.StatementTypeDeclarationASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.astnodes.VariableDeclarationASTNode;
import org.metricsminer.model.astnodes.VariableFragmentASTNode;

public class CustomASTVisitor extends ASTVisitor {

    private Stack<TreeASTNode> lastElements = new Stack<>();
    private FileAST fileElement;

    public CustomASTVisitor() {
        super(true);
    }

    public FileAST getFileElement() {
        return fileElement;
    }

    private void removeLastElement(ASTNode nodeToBeRemoved) {
        TreeASTNode lastElement = lastElements.peek();
        if (lastElement.getNode().getNodeType() != nodeToBeRemoved.getNodeType()) {
            System.out.println(
                    "ERROR: esperado encontrar pai do tipo " + nodeToBeRemoved.getClass().getSimpleName()
                            + " mas foi encotrado " + lastElement.getNode().getClass().getSimpleName());
        }
        lastElements.pop();
    }

    private void addToParendNode(TreeASTNode element, boolean isLastElement) {
        ASTNode parent = element.getNode().getParent();
        TreeASTNode lastElement = lastElements.peek();
        /*
         * Checks if there is a parent with the same node type as the last one added to
         * the stack. If you can't find it, then there was a problem assembling the
         * document and
         * the node is added as belonging to root.
         *
         * This method is just a validator, this indicated case should not occur.
         *
         */
        while (parent != null && parent.getNodeType() != lastElement.getNode().getNodeType()) {
            parent = parent.getParent();
        }
        if (parent == null) {
            System.out.println(
                    "ERROR: Nó do tipo " + element.getSimpleName()
                            + " não teve seu pai localizado, adicionando a raíz");
            fileElement.addChildren(element);
        } else {
            lastElement.addChildren(element);
        }

        if (isLastElement) {
            lastElements.add(element);
        }

        element.setCompilationUnit(getFileElement().getNode());
        element.updatePosInfo();
        element.setPath(fileElement.getPath());
    }

    // ---------------------------Main---------------------------

    // Main Unit
    @Override
    public boolean visit(CompilationUnit node) {
        // System.out.println(node.getClass().getSimpleName());
        this.fileElement = new FileAST(node);
        lastElements.add(fileElement);
        return super.visit(node);
    }

    // Imports
    @Override
    public boolean visit(ImportDeclaration node) {

        addToParendNode(new ImportASTNode(node), true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    // Class Declaration
    @Override
    public boolean visit(TypeDeclaration node) {
        addToParendNode(new ClassASTNode(node), true);
        return super.visit(node);
    }


    @Override
    public boolean visit(AnonymousClassDeclaration node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        // System.out.println(node.getClass().getSimpleName());
        MethodASTNode methodElement = new MethodASTNode(node);
        addToParendNode(methodElement, true);
        return super.visit(node);
    }

    @Override
    public boolean visit(EnumDeclaration node) {
        addToParendNode(new EnumAstNode(node), true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    // Occurs when there is a class or interface declaration inside a method
    @Override
    public boolean visit(TypeDeclarationStatement node) {
        StatementTypeDeclarationASTNode statementDeclarationElement = new StatementTypeDeclarationASTNode(
                node);
        addToParendNode(statementDeclarationElement, true);
        return super.visit(node);
    }

    @Override
    public boolean visit(PackageDeclaration node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    // -------------------------------Others--------------------------------------

    @Override
    public boolean visit(AnnotationTypeDeclaration node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(AnnotationTypeMemberDeclaration node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayAccess node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayCreation node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayInitializer node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayType node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(AssertStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(Assignment node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(Block node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(BlockComment node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(BooleanLiteral node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(BreakStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(CaseDefaultExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(CastExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(CatchClause node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(CharacterLiteral node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ClassInstanceCreation node) {
        ClassInstanceASTNode element = new ClassInstanceASTNode(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ConditionalExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ConstructorInvocation node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ContinueStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(CreationReference node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(Dimension node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(DoStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(EmptyStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(EnhancedForStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(EnumConstantDeclaration node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ExportsDirective node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ExpressionMethodReference node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ExpressionStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(FieldAccess node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        FieldASTNode element = new FieldASTNode(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ForStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(GuardedPattern node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(IfStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(InfixExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(Initializer node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(InstanceofExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(IntersectionType node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(Javadoc node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(JavaDocRegion node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(LabeledStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(LambdaExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(LineComment node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(MarkerAnnotation node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(MemberRef node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(MemberValuePair node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodRef node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodRefParameter node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodInvocation node) {
        MethodCallASTNode element = new MethodCallASTNode(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(Modifier node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ModuleDeclaration node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ModuleModifier node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(NameQualifiedType node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(NormalAnnotation node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(NullLiteral node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(NullPattern node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(NumberLiteral node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(OpensDirective node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ParameterizedType node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ParenthesizedExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(PatternInstanceofExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(PostfixExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(PrefixExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ProvidesDirective node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(PrimitiveType node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(QualifiedName node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(QualifiedType node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ModuleQualifiedName node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(RequiresDirective node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(RecordDeclaration node) {
        RecordASTNode element = new RecordASTNode(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ReturnStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SimpleName node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        //// System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SimpleType node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        //// System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(StringLiteral node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperConstructorInvocation node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperFieldAccess node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperMethodInvocation node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperMethodReference node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchCase node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SynchronizedStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(TagElement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(TagProperty node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(TextBlock node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(TextElement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ThisExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(ThrowStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(TryStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeLiteral node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeMethodReference node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeParameter node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(TypePattern node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(UnionType node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(UsesDirective node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(SingleVariableDeclaration node) {
        ParameterVariableASTNode element = new ParameterVariableASTNode(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationExpression node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationStatement node) {
        VariableDeclarationASTNode element = new VariableDeclarationASTNode(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        VariableFragmentASTNode element = new VariableFragmentASTNode(node);
        addToParendNode(element, true);
        return super.visit(node);
    }

    @Override
    public boolean visit(WhileStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(WildcardType node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    @Override
    public boolean visit(YieldStatement node) {
        GenericASTNode<ASTNode> element = new GenericASTNode<ASTNode>(node);
        addToParendNode(element, true);
        // System.out.println(node.getClass().getSimpleName());
        return super.visit(node);
    }

    // ------------------------------------END
    // VISIT------------------------------------
    // Todos adicionados a listagem precisam ser removidos quand oa visita termina
    // Final do escopo do método

    @Override
    public void endVisit(AnnotationTypeDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(AnnotationTypeMemberDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(AnonymousClassDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ArrayAccess node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ArrayCreation node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ArrayInitializer node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ArrayType node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(AssertStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(Assignment node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(Block node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(BlockComment node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(BooleanLiteral node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(BreakStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(CaseDefaultExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(CastExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(CatchClause node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(CharacterLiteral node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ClassInstanceCreation node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(CompilationUnit node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ConditionalExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ConstructorInvocation node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ContinueStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(CreationReference node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(DoStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(EmptyStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(EnhancedForStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(EnumConstantDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(EnumDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ExportsDirective node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ExpressionMethodReference node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ExpressionStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(Dimension node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(FieldAccess node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(FieldDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ForStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(GuardedPattern node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(IfStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ImportDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(InfixExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(InstanceofExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(Initializer node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(Javadoc node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(JavaDocRegion node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(LabeledStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(LambdaExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(LineComment node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(MarkerAnnotation node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(MemberRef node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(MemberValuePair node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(MethodRef node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(MethodRefParameter node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(MethodDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(MethodInvocation node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(Modifier node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ModuleDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ModuleModifier node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(NameQualifiedType node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(NormalAnnotation node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(NullLiteral node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(NullPattern node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(NumberLiteral node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(OpensDirective node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(PackageDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ParameterizedType node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ParenthesizedExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(PatternInstanceofExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(PostfixExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(PrefixExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(PrimitiveType node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ProvidesDirective node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(QualifiedName node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(QualifiedType node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ModuleQualifiedName node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(RequiresDirective node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(RecordDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ReturnStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SimpleName node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SimpleType node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SingleMemberAnnotation node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SingleVariableDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(StringLiteral node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SuperConstructorInvocation node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SuperFieldAccess node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SuperMethodInvocation node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SuperMethodReference node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SwitchCase node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SwitchExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SwitchStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(SynchronizedStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TagElement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TagProperty node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TextBlock node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TextElement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ThisExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(ThrowStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TryStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TypeDeclaration node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TypeDeclarationStatement node) {
        removeLastElement(node.getDeclaration());
        super.endVisit(node);
    }

    @Override
    public void endVisit(TypeLiteral node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TypeMethodReference node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TypeParameter node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(TypePattern node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(UnionType node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(UsesDirective node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(IntersectionType node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(VariableDeclarationExpression node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(VariableDeclarationStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(VariableDeclarationFragment node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(WhileStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(WildcardType node) {
        removeLastElement(node);
        super.endVisit(node);
    }

    @Override
    public void endVisit(YieldStatement node) {
        removeLastElement(node);
        super.endVisit(node);
    }

}

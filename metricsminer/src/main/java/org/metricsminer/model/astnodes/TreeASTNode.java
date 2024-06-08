package org.metricsminer.model.astnodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.model.diff.NodeDiffPair;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class TreeASTNode<T extends ASTNode> {

    private String ancestralname = "";
    private TreeASTNode<? extends ASTNode> parent;
    private int line;
    private int endLine;
    private int pos;
    private int column;
    private int endColumn;
    private transient T astNode;
    private ArrayList<TreeASTNode<? extends ASTNode>> childrens = new ArrayList<>();
    private NodeDiffPair nodeDiffPair;
    private CompilationUnit cu;
    private ArrayList<Variable> variablesInContext = new ArrayList<>();
    private TreeSet<String> usedVariables = new TreeSet<>();
    private String path;

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TreeASTNode(T astNode) {
        this.astNode = astNode;
    }

    public T getNode() {
        return astNode;
    };

    public TreeASTNode<T> newEmptyNode() {
        return newEmptyNode(AST.newAST(AST.getJLSLatest(), false));
    }

    public abstract TreeASTNode<T> newEmptyNode(AST ast);

    public void setCompilationUnit(CompilationUnit cu) {
        this.cu = cu;
    }

    public CompilationUnit getCompilationUnit() {
        return cu;
    }

    public void addChildren(TreeASTNode<? extends ASTNode> element) {
        element.parent = this;
        element.ancestralname = this.ancestralname;
        if (!additionalAncestralName().isBlank()) {
            element.ancestralname += "." + additionalAncestralName();
        }
        this.childrens.add(element);
    }

    public void updatePosInfo() {

        int[] pos = TreeASTNode.getLineFromCU(cu, astNode);
        this.line = pos[0];
        this.endLine = pos[1];
        this.column = pos[2];
        this.endColumn = pos[3];
        this.pos = astNode.getStartPosition();
    }

    public static int[] getLineFromCU(CompilationUnit cu, ASTNode astNode) {
        int[] pos = new int[4];
        pos[0] = cu.getLineNumber(astNode.getStartPosition());
        pos[1] = cu.getLineNumber(astNode.getStartPosition() + astNode.getLength() - 1); // -1 to avoid mismatch at last
                                                                                         // line
        pos[2] = cu.getColumnNumber(astNode.getStartPosition()) + 1; // Column starts with 0
        pos[3] = cu.getColumnNumber(astNode.getStartPosition() + astNode.getLength());// Column starts with 0
        return pos;
    }

    public TreeASTNode<?> getParent() {
        return this.parent;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public int getEndLine() {
        return this.endLine;
    }

    public int getEndColumn() {
        return this.endColumn;
    }

    public int getPos() {
        return this.pos;
    }

    public ArrayList<TreeASTNode<? extends ASTNode>> getChildren() {
        return childrens;
    }

    public void visitAllChildren(TreeASTVisitor astVisitor, int initialGeneration) {
        for (TreeASTNode<? extends ASTNode> child : getChildren()) {
            astVisitor.visit(child, initialGeneration);
            child.visitAllChildren(astVisitor, initialGeneration + 1);
        }
    }

    public ArrayList<TreeASTNode<? extends ASTNode>> getAllChildren() {
        ArrayList<TreeASTNode<? extends ASTNode>> descendants = new ArrayList<>();
        for (TreeASTNode<? extends ASTNode> child : getChildren()) {
            descendants.add(child);
            descendants.addAll(child.getAllChildren());
        }
        return descendants;
    }

    public String getNodeType() {
        return getNode().getClass().getSimpleName();
    }

    protected String additionalAncestralName() {
        return "";
    }

    public String getAncestralName() {
        return ancestralname;
    }

    // public String getFilePath() {

    //     // Obtem o nome do pacote
    //     String packageName = getCompilationUnit().getPackage() != null
    //             ? getCompilationUnit().getPackage().getName().toString()
    //             : "";

    //     // Obtem o primeiro tipo de declaração
    //     if (getCompilationUnit().types().isEmpty()) {
    //         return packageName;
    //     }
    //     Object type = getCompilationUnit().types().get(0);
    //     if (!(type instanceof AbstractTypeDeclaration)) {
    //         return packageName;
    //     }

    //     AbstractTypeDeclaration typeDecl = (AbstractTypeDeclaration) type;
    //     String className = typeDecl.getName().getIdentifier();

    //     // Constrói o caminho relativo usando StringBuilder
    //     StringBuilder relativePath = new StringBuilder();
    //     if (!packageName.isEmpty()) {
    //         relativePath.append(packageName.replace('.', '/')).append('/');
    //     }
    //     relativePath.append(className).append(".java");

    //     return relativePath.toString();
    // }

    public String getSimpleName() {
        return getNode().getClass().getSimpleName();
    }

    public String getName() {
        return getNode().getClass().getSimpleName();
    }

    public String getFullName() {
        return ancestralname.isBlank() ? getName() : ancestralname + "." + getName();
    }

    protected void setAncestralName(String ancestralName) {
        this.ancestralname = ancestralName;
    }

    protected JsonObject particularJsonInfo(TreeASTNode<? extends ASTNode> element) {
        return new JsonObject();
    }

    public List<NodeDiffPair> getAllChangedPairs() {
        return recursiveGetAllChangedPairs(this, new ArrayList<>());
    }

    private List<NodeDiffPair> recursiveGetAllChangedPairs(TreeASTNode<?> element, List<NodeDiffPair> nodeDiffPairs) {
        if (element.nodeDiffPair != null && !element.nodeDiffPair.getChangeMetric().isEmpty()) {
            nodeDiffPairs.add(element.nodeDiffPair);
        }

        for (TreeASTNode<?> child : element.getChildren()) {
            recursiveGetAllChangedPairs(child, nodeDiffPairs);
        }

        return nodeDiffPairs;
    }

    public String toShortJson(JsonArray jsonArray) {
        if (jsonArray == null) {
            jsonArray = new JsonArray();
        }

        JsonObject elemObject = new JsonObject();
        if (nodeDiffPair != null && !nodeDiffPair.getChangeMetric().isEmpty()) {

            elemObject.addProperty("element", getFullName());
            elemObject.addProperty("Type", getNodeType());
            elemObject.add("changeMetrics", nodeDiffPair.toJson());
            jsonArray.add(elemObject);
        }

        for (TreeASTNode<?> element : getChildren()) {
            element.toShortJson(jsonArray);
        }

        return jsonArray.toString();
    }

    public JsonObject toJson() {

        JsonObject elemObject = particularJsonInfo(this);
        elemObject.addProperty("Type", getNodeType());
        elemObject.addProperty("value", getNode().toString());
        elemObject.addProperty("line", getLine());
        elemObject.addProperty("column", getColumn());
        elemObject.addProperty("usedVariables", usedVariables.toString());
        elemObject.addProperty("variableInContext", variablesInContext.toString());
        if (nodeDiffPair != null) {
            elemObject.add("changeMetrics", nodeDiffPair.toJson());
        }

        JsonObject childrenJsonObj = new JsonObject();
        elemObject.add("children", childrenJsonObj);
        HashMap<String, JsonArray> childrenNodesPerType = new HashMap<>();
        for (TreeASTNode<?> element : getChildren()) {

            String nodeSimpleName = element.getSimpleName();
            JsonArray jsonarray = childrenNodesPerType.get(nodeSimpleName);
            if (jsonarray == null) {
                jsonarray = new JsonArray();
                childrenNodesPerType.put(nodeSimpleName, jsonarray);
                childrenJsonObj.add(nodeSimpleName, jsonarray);
            }

            JsonObject childElementJsonObj = element.toJson();

            jsonarray.add(childElementJsonObj);
        }

        return elemObject;

    }

    public void setNodePair(NodeDiffPair nodeDiffPair) {
        this.nodeDiffPair = nodeDiffPair;
    }

    public NodeDiffPair getNodeDiffPair() {
        return this.nodeDiffPair;
    }

    public boolean isRoot() {
        return parent == null;
    }

    @Override
    public String toString() {
        return getNode().toString().trim();
    }

    @Override
    public boolean equals(Object treeASTNode) {

        return treeASTNode instanceof TreeASTNode && treeASTNode.toString().equalsIgnoreCase(toString());

    }

    public TreeASTNode<?> findNearestMethodContext() {
        TreeASTNode<?> current = this;
        while (current != null && !(current instanceof MethodASTNode)) {
            current = current.getParent();
        }
        return current;
    }

    public void updateScope() {
        this.setVariablesInContext();
        getChildren().forEach(child -> {
            child.setPath(this.path);
            child.updateScope();          
           
        });
        this.setUsedVariable();
    }

    private void setUsedVariable() {
        // As variaveis que usamos pode ser todas as variaveis encontrada no contexto de
        // todos os filhos ou apenas
        // dos filhos até encontrar uma declaração/bloco

        if (getNode() instanceof QualifiedName || getNode() instanceof SimpleName) {
            Name node = (Name) getNode();
            IBinding binding = node.resolveBinding();
            if (binding != null && binding.getKind() == IBinding.VARIABLE) {
                for (Variable variablesInContext : variablesInContext) {
                    if (node.getFullyQualifiedName().toString().equals(variablesInContext.name)) {
                        usedVariables.add(variablesInContext.fullName);
                        return;
                    }
                }

            }
        } else {

            for (TreeASTNode<?> children : getChildren()) {
                usedVariables.addAll(children.usedVariables);
            }
        }

    }

    @SuppressWarnings("unchecked")
    protected void setVariablesInContext() {
        TreeASTNode parent = getParent();

        if (parent == null) {
            return;
        }

        variablesInContext = (ArrayList<Variable>) parent.variablesInContext.clone();
        // System.out.println("Analisando: "+getNode());
        // Inclui blocos em geral que tem o escopo definido por { }
        if (parent.getNode() instanceof Block) {
            getVariablesFromBlock(parent).forEach(var -> {
                addVariableInContext(var);
            });
        }

        // Inclui os parametros de um método
        if (parent instanceof MethodASTNode) {
            MethodASTNode methodNode = (MethodASTNode) parent;

            methodNode.getParametersName().forEach(varName -> {
                addVariableInContext(new Variable(varName, methodNode.getLine(), methodNode.getEndLine()));
            });

        }

        // Inclui classes, interface, enum, record e statementDeclaration
        if (parent instanceof AbstractStructureASTNode) {
            AbstractStructureASTNode absStructure = (AbstractStructureASTNode) parent;
            absStructure.getVariables().forEach(vari -> {
                String varName = ((VariableDeclarationFragment) vari).getName().toString();
                addVariableInContext(new Variable(varName, absStructure.getLine(), absStructure.getEndLine()));
            });
        }

        // Inclui criação de for e for enhanced
        if (parent.getNode() instanceof ForStatement ||
                parent.getNode() instanceof EnhancedForStatement ||
                parent.getNode() instanceof CatchClause) {
            this.getVariablesFromStatement(parent, new ArrayList<>()).forEach((vari) -> {
                addVariableInContext(new Variable(vari, parent.getLine(), parent.getEndLine()));
            });

        }

    }

    private void addVariableInContext(Variable newVariable) {
        // Se ja tiver uma variavel de mesmo nome, ignora a nova
        ArrayList<Variable> toRemoveVars = new ArrayList<>();
        for (Variable varName : variablesInContext) {
            if (newVariable.name.equals(varName)) {
                toRemoveVars.add(varName);
            }
        }
        toRemoveVars.forEach(var -> {
            variablesInContext.remove(var);
        });

        variablesInContext.add(newVariable);

    }

    @SuppressWarnings("unchecked")
    private ArrayList<Variable> getVariablesFromBlock(TreeASTNode blockNode) {
        // Se for um método, devemos considerar os irmão que foram declarados antes da
        // linha, em seguida, os parâmetros
        ArrayList<Variable> variables = new ArrayList<>();

        blockNode.getChildren().forEach((child) -> {
            TreeASTNode nodeChild = (TreeASTNode) child;
            // Checa se a linha é anterior ao nó em questão
            if (nodeChild.getLine() > getLine() ||
                    (nodeChild.getLine() == getLine() && nodeChild.getColumn() > getColumn())) {
                return;
            }

            // Variáveis de nível de método
            if (nodeChild instanceof VariableDeclarationASTNode) {
                VariableDeclarationASTNode varChild = (VariableDeclarationASTNode) nodeChild;
                variables.add(new Variable(varChild.getName(), varChild.getLine(), parent.getEndLine()));

            }

        });
        return variables;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<String> getVariablesFromStatement(TreeASTNode node, ArrayList<String> previousVars) {

        node.getChildren().forEach((child) -> {
            TreeASTNode nodeChild = (TreeASTNode) child;

            if (nodeChild.getNode() instanceof Block) {
                return;
            }

            // Variáveis de nível de método
            if (nodeChild.getNode() instanceof VariableDeclaration) {
                VariableDeclaration varChild = (VariableDeclaration) nodeChild.getNode();
                previousVars.add(varChild.getName().toString());
            } else {
                getVariablesFromStatement(nodeChild, previousVars);
            }

        });
        return previousVars;
    }

    class Variable {
        String name;
        int line;
        int endLine;
        String fullName;

        // Variable(String name) {
        // this.name = name;
        // this.line = -1;
        // this.endLine = -1;
        // setFullName();
        // }

        Variable(String name, int line, int endLine) {
            this.name = name;
            this.line = line;
            this.endLine = endLine;
            setFullName();
        }

        public void setFullName() {
            this.fullName = parent.getFullName() + ">>>" + this.name + "[" + line + "," + endLine + "]";
        }

        @Override
        public String toString() {
            return fullName;
        }
    }

    public TreeSet<String> getUsedVariables() {
        return this.usedVariables;
    }

    /**
     *  
     */
    public interface TreeASTVisitor {
        void visit(TreeASTNode astNode, int generation);
    }

}

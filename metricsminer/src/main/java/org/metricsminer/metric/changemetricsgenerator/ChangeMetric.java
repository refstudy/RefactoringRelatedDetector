package org.metricsminer.metric.changemetricsgenerator;

import java.util.ArrayList;
import java.util.TreeSet;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.metricsminer.metric.changemetricsgenerator.asttreebased.ASTSimpleDiffMetricsGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.RenameMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.annotations.AnnotationChangedMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.declarations.AccessChangeMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.declarations.MethodDeclarationMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.declarations.ReferenceTypeChangeMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.declarations.TypeDeclarationMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.hierarchy.HierarchyChangeMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.hierarchy.InterfaceChangeMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.imports.ImportChangedMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.methods.MethodParameterMetricsGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.methods.MethodReturnChangedMetricsGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.methods.MethodReturnValueChangedMetricsGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.methods.exceptionhandler.CatchClauseMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.methods.exceptionhandler.MethodThrowMetricsGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.methods.exceptionhandler.TryCatchMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.statements.ClassInstanceChangeMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.statements.IfStatementChangeMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.statements.MethodCallMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.statements.StatementChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.elementbased.GenericMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.variables.VariableTypeChangeMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.variables.VariableValueChangeMetricGen;
import org.metricsminer.metric.changemetricsgenerator.elementbased.variables.VariableDeclarationMetricGen;
import org.metricsminer.metric.changemetricsgenerator.gumtreebased.GumtreeASTDiffMetricsGen;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

import com.google.gson.JsonObject;

public class ChangeMetric {

    private NodeDiffPair nodeDiffPair;
    private DiffType diffType;
    private String name;
    private String description;
    private String validation;
    private Object value;
    private String generator;
    private int line;
    private int column;
    private int endLine;
    private int endColumn;
    private TreeASTNode closestElement;
    public static transient ArrayList<ChangeMetricGenerator> allMetricGenerators = new ArrayList<>();
    static {

        // GUMTREE Metrics
        // ChangeMetric.allMetricGenerators.add(new GumtreeASTDiffMetricsGen());

        // Old Approach
        // ChangeMetric.allMetricGenerators.add(new ASTSimpleDiffMetricsGen());

        // *************************ElementBased*************************
        // annotations
        ChangeMetric.allMetricGenerators.add(new AnnotationChangedMetricGen<>());
        // imports
        ChangeMetric.allMetricGenerators.add(new ImportChangedMetricGen());
        // Declarations
        ChangeMetric.allMetricGenerators.add(new ReferenceTypeChangeMetricGen());
        ChangeMetric.allMetricGenerators.add(new TypeDeclarationMetricGen());
        ChangeMetric.allMetricGenerators.add(new AccessChangeMetricGen());
        ChangeMetric.allMetricGenerators.add(new MethodDeclarationMetricGen());
        // Variables
        ChangeMetric.allMetricGenerators.add(new VariableDeclarationMetricGen());
        ChangeMetric.allMetricGenerators.add(new VariableTypeChangeMetricGen<>());
        ChangeMetric.allMetricGenerators.add(new VariableValueChangeMetricGen());
        // Hierarchy
        ChangeMetric.allMetricGenerators.add(new HierarchyChangeMetricGen());
        ChangeMetric.allMetricGenerators.add(new InterfaceChangeMetricGen());
        // Methods
        ChangeMetric.allMetricGenerators.add(new MethodReturnChangedMetricsGen());
        ChangeMetric.allMetricGenerators.add(new MethodReturnValueChangedMetricsGen());
        ChangeMetric.allMetricGenerators.add(new MethodThrowMetricsGen());
        ChangeMetric.allMetricGenerators.add(new MethodParameterMetricsGen());
        ChangeMetric.allMetricGenerators.add(new TryCatchMetricGen());
        ChangeMetric.allMetricGenerators.add(new CatchClauseMetricGen());
        // Statements
        ChangeMetric.allMetricGenerators.add(new MethodCallMetricGen());
        ChangeMetric.allMetricGenerators.add(new IfStatementChangeMetricGen());
        ChangeMetric.allMetricGenerators.add(new ClassInstanceChangeMetricGen());
        ChangeMetric.allMetricGenerators.add(new StatementChangeMetric());
        ChangeMetric.allMetricGenerators.add(new RenameMetricGen());

        // Complementa para garantir que tudo será coletado
        ChangeMetric.allMetricGenerators.add(new GenericMetricGen());

        // Antigos
        // ChangeMetric.allMetricGenerators.add(new ModifierChangedMetricGen<>());

    }

    public ChangeMetric(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getValidation() {
        return this.validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DiffType getDiffType() {
        return this.diffType;
    }

    public void setDiffType(DiffType diffType) {
        this.diffType = diffType;
    }

    public ChangeMetric validate(NodeDiffPair diffPair) {
        return null;
    };

    public String getGenerator() {
        return this.generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getEndLine() {
        return this.endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getEndColumn() {
        return this.endColumn;
    }

    public String getTextPos() {
        return "L[" + getLine() + "," + getEndLine() + "], C[" + getColumn() + "," + getEndColumn() + "]";
    }

    public void setPos(int[] pos) {
        this.line = pos[0];
        this.endLine = pos[1];
        this.column = pos[2];
        this.endColumn = pos[3];
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public NodeDiffPair getNodeDiffPair() {
        return this.nodeDiffPair;
    }

    public void setNodeDiffPair(NodeDiffPair nodeDiffPair) {
        this.nodeDiffPair = nodeDiffPair;
        // nodeDiffPair.addChangeMetric(this);
    }

    public TreeASTNode getClosestElement() {

        return this.closestElement;
    }

    public void setClosestElement(TreeASTNode closestElement) {
        this.closestElement = closestElement;
    }

    public JsonObject toJson() {
        JsonObject metricJson = new JsonObject();
        metricJson.addProperty("name", getName());
        if (validation != null) {
            metricJson.addProperty("validation", getValidation());
        }

        if (value != null) {
            metricJson.addProperty("value", value.toString());
        }
        metricJson.addProperty("line", getLine() + " - " + getEndLine());
        metricJson.addProperty("column", getColumn() + " - " + getEndColumn());
        return metricJson;
    }

    public JsonObject toFullJson() {
        JsonObject metricJson = toJson();
        metricJson.addProperty("parentElement", getParentElementName());
        metricJson.addProperty("element", closestElement.getFullName());
        metricJson.addProperty("description", getDescription());
        // metricJson.addProperty("parentNodeType", getParentType());
        metricJson.addProperty("elementType", getElementType());
        metricJson.addProperty("variables", getUsedVariables().toString());
        return metricJson;
    }

    @Override
    public String toString() {
        return toFullJson().toString();
    }

    public TreeASTNode getParent() {
        if (diffType.equals(DiffType.REMOVE)) {
            return nodeDiffPair.getBefore();
        } else {
            return nodeDiffPair.getAfter();
        }
    }

    private String getParentElementName() {
        return getParent().getFullName();
    }

    private String getParentType() {
        return getParent().getNodeType();
    }

    private String getElementType() {
        return closestElement.getClass().getSimpleName();
    }

    public TreeSet<String> getUsedVariables() {
        return this.getClosestElement().getUsedVariables();
    }

}

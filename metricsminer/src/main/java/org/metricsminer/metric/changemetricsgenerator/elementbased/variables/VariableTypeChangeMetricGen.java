package org.metricsminer.metric.changemetricsgenerator.elementbased.variables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.astnodes.VariableHandler;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.JsonObject;

//TODO: checar em que momento entra aqui
public class VariableTypeChangeMetricGen<S extends TreeASTNode<? extends ASTNode> & VariableHandler>
        extends ChangeMetricGenerator<S> {

    public VariableTypeChangeMetricGen(){
         super((Class) VariableHandler.class);
    }
    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair nodeDiffPair, S before, S after) {

        ArrayList<ChangeMetric> changeMetrics = new ArrayList<>();

        if (!before.getVarType().equals(after.getVarType())) {
            ChangeMetric cm;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("before", before.getVarType());
            jsonObject.addProperty("after", after.getVarType());
            cm = buildChangeMetric("VAR_TYPE_CHANGED", "The variable type has changed",
                    jsonObject);
            cm.setPos(TreeASTNode.getLineFromCU(after.getCompilationUnit(), after.getNode()));
            changeMetrics.add(cm);
        }
        return changeMetrics;
    }

    @Override
    protected Class getCompareNodeClass(TreeASTNode<? extends ASTNode> treeASTNode) {
        return treeASTNode.getClass();
    }

    @Override
    protected S getCompareNode(TreeASTNode<? extends ASTNode> treeASTNode) {
        return (S) treeASTNode;
    }


}

package org.metricsminer.metric.changemetricsgenerator.elementbased.variables;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.astnodes.VariableFragmentASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.JsonObject;

public class VariableValueChangeMetricGen extends ChangeMetricGenerator<VariableFragmentASTNode> {

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair diffPair, VariableFragmentASTNode before,
            VariableFragmentASTNode after) {
        List<ChangeMetric> changeMetrics = new ArrayList<>();

        if (!before.getValue().equals(after.getValue())) {
            ChangeMetric cm;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("before", before.getValue());
            jsonObject.addProperty("after", after.getValue());

            cm = buildChangeMetric("CHANGED_VAR_VALUE", "The variable value has changed",
                    jsonObject, after.getParent().getNodeDiffPair());
            cm.setPos(TreeASTNode.getLineFromCU(after.getCompilationUnit(), after.getNode()));
            changeMetrics.add(cm);
        }
        return changeMetrics;
    }

}

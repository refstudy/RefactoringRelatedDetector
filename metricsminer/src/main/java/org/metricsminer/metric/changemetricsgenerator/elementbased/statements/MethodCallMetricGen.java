package org.metricsminer.metric.changemetricsgenerator.elementbased.statements;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.MethodCallASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.JsonObject;

public class MethodCallMetricGen extends ChangeMetricGenerator<MethodCallASTNode> {
    @Override
    public List<ChangeMetric> insertAction(NodeDiffPair diffPair, MethodCallASTNode after) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        ChangeMetric cm = buildChangeMetric("ADDED_METHOD_CALL", "A new method call has been added",
                after, ChangeMetricGenerator.getFirstValidParent(diffPair));
        metrics.add(cm);
        return metrics;
    }

    @Override
    public List<ChangeMetric> removeAction(NodeDiffPair diffPair, MethodCallASTNode before) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        ChangeMetric cm = buildChangeMetric("REMOVED_METHOD_CALL", "A method call has been removed",
                before, ChangeMetricGenerator.getFirstValidParent(diffPair));
        metrics.add(cm);
        return metrics;
    }

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair diffPair, MethodCallASTNode mIBefore, MethodCallASTNode mIAfter) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        if (!mIBefore.getArguements().toString().equals(mIAfter.getArguements().toString())) {
             JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("before", mIBefore.getArguements().toString());
            jsonObject.addProperty("after", mIAfter.getArguements().toString());
            ChangeMetric cm = buildChangeMetric("METHOD_CALL_ARGUMENTS_CHANGE",
                    "A method call had its arguments changed",
                    jsonObject, diffPair);
            metrics.add(cm);
        }

        return metrics;
    }

}

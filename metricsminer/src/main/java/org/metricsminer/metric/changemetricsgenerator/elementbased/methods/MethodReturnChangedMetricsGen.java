package org.metricsminer.metric.changemetricsgenerator.elementbased.methods;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.MethodASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.JsonObject;

public class MethodReturnChangedMetricsGen extends ChangeMetricGenerator<MethodASTNode> {

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair nodeDiffPair, MethodASTNode before, MethodASTNode after) {

        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        ChangeMetric cm;

        if (!before.getReturn().equals(after.getReturn())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("before", before.getReturn());
            jsonObject.addProperty("after", after.getReturn());

            cm = buildChangeMetric("CHANGED_RETURN_TYPE", "The method had its return changed",
                    jsonObject.toString());
            metrics.add(cm);
        }

        return metrics;

    }

}

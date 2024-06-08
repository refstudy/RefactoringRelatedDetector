package org.metricsminer.metric.changemetricsgenerator.elementbased.methods;

import java.util.ArrayList;
import java.util.List;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.MethodASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

public class MethodParameterMetricsGen extends ChangeMetricGenerator<MethodASTNode> {

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair nodeDiffPair, MethodASTNode before, MethodASTNode after) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        actionOverUniqueString(before.getParametersTypeNode(), after.getParametersTypeNode(), s -> {
            ChangeMetric cm = buildChangeMetric("REMOVED_PARAMETER", "Some method's parameter were removed", s);
            cm.setPos(TreeASTNode.getLineFromCU(before.getCompilationUnit(), s));
            cm.setDiffType(DiffType.REMOVE);
            metrics.add(cm);
        }, s -> {
            ChangeMetric cm = buildChangeMetric("ADDED_PARAMETER", "Some method's parameter were added", s);
            cm.setPos(TreeASTNode.getLineFromCU(after.getCompilationUnit(), s));
            cm.setDiffType(DiffType.INSERT);
            metrics.add(cm);
        });

        return metrics;

    }

}

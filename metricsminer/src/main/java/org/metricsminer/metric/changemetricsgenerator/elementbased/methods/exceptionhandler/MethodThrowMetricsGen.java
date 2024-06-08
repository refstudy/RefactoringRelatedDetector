package org.metricsminer.metric.changemetricsgenerator.elementbased.methods.exceptionhandler;

import java.util.ArrayList;
import java.util.List;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.MethodASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

public class MethodThrowMetricsGen extends ChangeMetricGenerator<MethodASTNode> {

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair nodeDiffPair, MethodASTNode before, MethodASTNode after) {

        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        actionOverUniqueString(before.getPossibleExceptionTypes(), after.getPossibleExceptionTypes(), s -> {
            ChangeMetric cm = buildChangeMetric("REMOVED_EXCEPTION_THROWN",
                    "Some method's possible exceptions were removed", s);
            cm.setPos(TreeASTNode.getLineFromCU(before.getCompilationUnit(), s));
            cm.setDiffType(DiffType.REMOVE);
            metrics.add(cm);
        }, s -> {
            ChangeMetric cm = buildChangeMetric("ADDED_EXCEPTION_THROWN",
                    "Some method's possible exceptions were added", s);
            cm.setDiffType(DiffType.INSERT);
            cm.setPos(TreeASTNode.getLineFromCU(after.getCompilationUnit(), s));
            metrics.add(cm);
        });

        return metrics;

    }

}

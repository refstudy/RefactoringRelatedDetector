package org.metricsminer.metric.changemetricsgenerator.elementbased.hierarchy;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.AbstractStructureASTNode;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

public class InterfaceChangeMetricGen extends
        ChangeMetricGenerator<AbstractStructureASTNode> {

    public InterfaceChangeMetricGen() {
        super(AbstractStructureASTNode.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ChangeMetric> pairAction(NodeDiffPair nodeDiffPair,
            AbstractStructureASTNode before,
            AbstractStructureASTNode after) {

        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        actionOverUniqueString(before.getInterfaces(), after.getInterfaces(), s -> {
            ChangeMetric cm = buildChangeMetric("REMOVED_INTERFACE", "The structure lost some interface(s)", s);
             cm.setDiffType(DiffType.REMOVE);
            metrics.add(cm);
        }, s -> {
            ChangeMetric cm = buildChangeMetric("ADDED_INTERFACE", "The structure got some interface(s)", s);
            cm.setDiffType(DiffType.INSERT);
            metrics.add(cm);
        });

        return metrics;
    }

}

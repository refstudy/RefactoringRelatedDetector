package org.metricsminer.metric.changemetricsgenerator.gumtreebased;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

public class RemoveNodeMetric extends ChangeMetric {

    public RemoveNodeMetric() {
        super("REMOVE_NODE", "The element was removed");
    }

    public ChangeMetric validate(NodeDiffPair diffPair) {

        TreeASTNode treeNode = diffPair.getBefore();

        RemoveNodeMetric removeNodeMetric = new RemoveNodeMetric();
        removeNodeMetric.setValue(treeNode.getNode());
        removeNodeMetric.setValidation(
                "Removal of '" + treeNode.getNodeType() + "' [L: " + treeNode.getLine() + ", C:" + treeNode.getColumn()
                        + "]");
        insertMetric(diffPair);
        return removeNodeMetric;

    }

    public void insertMetric(NodeDiffPair diffPair) {
        NodeDiffPair parentPair = ChangeMetricGenerator.getFirstValidParent(diffPair);
        if (parentPair != null) {
            parentPair.addChangeMetric(this);
        }

    }

}

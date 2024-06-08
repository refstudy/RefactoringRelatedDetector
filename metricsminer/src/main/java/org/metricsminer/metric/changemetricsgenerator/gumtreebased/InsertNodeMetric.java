package org.metricsminer.metric.changemetricsgenerator.gumtreebased;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

public class InsertNodeMetric extends ChangeMetric {

    public InsertNodeMetric() {
        super("INSERT_NODE", "The element has been inserted");
    }

    public ChangeMetric validate(NodeDiffPair diffPair) {
        TreeASTNode treeNode = diffPair.getAfter();
        InsertNodeMetric insertNodeMetric = new InsertNodeMetric();
        insertNodeMetric.setValue(treeNode.getNode());
        insertNodeMetric.setValidation("Insertion of a '" + treeNode.getNodeType() + "' [L: " + treeNode.getLine()
                + ", C:" + treeNode.getColumn() + "]");
        insertMetric(diffPair);
        return insertNodeMetric;
    }

    public void insertMetric(NodeDiffPair diffPair) {
        if (diffPair.getAfter().isRoot()) {
            diffPair.addChangeMetric(this);
            return;
        }

        TreeASTNode firstValidParent = diffPair.getAfter().getParent();

        while (firstValidParent.getNodeDiffPair() == null) {
            firstValidParent = firstValidParent.getParent();
            if (firstValidParent.isRoot()) {
                firstValidParent.getNodeDiffPair().addChangeMetric(this);
                return;
            }
        }
        firstValidParent.getNodeDiffPair().addChangeMetric(this);
    }

}

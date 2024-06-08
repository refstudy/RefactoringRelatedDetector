package org.metricsminer.metric.changemetricsgenerator.gumtreebased;

import java.util.HashMap;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.Gson;

public class UpdateNodeMetric extends ChangeMetric {

    public UpdateNodeMetric() {
        super("UPDATE_NODE", "The element was updated");
    }

    @Override
    public ChangeMetric validate(NodeDiffPair diffPair) {

        TreeASTNode btreeNode = diffPair.getBefore();
        TreeASTNode atreeNode = diffPair.getAfter();
        UpdateNodeMetric updateNodeMetric = new UpdateNodeMetric();
        updateNodeMetric.setValidation("'" + btreeNode.getNode() + "' [L: " + btreeNode.getLine() + ", C:"
                + btreeNode.getColumn() + "] updated to '"
                + atreeNode.getNode() + "' [L: " + atreeNode.getLine() + ", C:" + atreeNode.getColumn() + "]");

        HashMap<String, String> updatedHash = new HashMap<>();
        updatedHash.put("before", btreeNode.getNode().toString());
        updatedHash.put("after", atreeNode.getNode().toString());
        Gson gson = new Gson();
        updateNodeMetric.setValue(gson.toJson(updatedHash));
        return updateNodeMetric;
    }

}

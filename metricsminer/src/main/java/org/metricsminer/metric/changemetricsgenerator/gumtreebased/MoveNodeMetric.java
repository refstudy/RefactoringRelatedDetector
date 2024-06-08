package org.metricsminer.metric.changemetricsgenerator.gumtreebased;

import java.util.HashMap;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.Gson;

public class MoveNodeMetric extends ChangeMetric {

    public MoveNodeMetric() {
        super("MOVE_NODE", "The element was moved");
    }

    public ChangeMetric validate(NodeDiffPair diffPair) {
        TreeASTNode btreeNode = diffPair.getBefore();
        TreeASTNode atreeNode = diffPair.getAfter();
        MoveNodeMetric moveNodeMetric = new MoveNodeMetric();
        moveNodeMetric.setValidation("'" + btreeNode.getNode() + "' [L: " + btreeNode.getLine() + ", C:"
                + btreeNode.getColumn() + "] moved to '"
                + atreeNode.getNode() + "' [L: " + atreeNode.getLine() + ", C:" + atreeNode.getColumn() + "]");
        HashMap<String, String> updatedHash = new HashMap<>();
        updatedHash.put("element", btreeNode.getNode().toString());
        updatedHash.put("beforeLocation", "[L: " + btreeNode.getLine() + ", C:" + btreeNode.getColumn() + "]");
        updatedHash.put("afterLocation", "[L: " + atreeNode.getLine() + ", C:" + atreeNode.getColumn() + "]");
        Gson gson = new Gson();
        moveNodeMetric.setValue(gson.toJson(updatedHash));
        return moveNodeMetric;

    }

}

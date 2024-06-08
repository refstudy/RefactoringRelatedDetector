package org.metricsminer.model.diff;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.collector.PairException;
import org.metricsminer.model.astnodes.TreeASTNode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NodeDiffPair {

    public enum DiffType {
        UPDATE,
        INSERT,
        MOVE,
        REMOVE,
        PAIR,
        DEFAULT
    }

    private TreeASTNode<? extends ASTNode> before;
    private TreeASTNode<? extends ASTNode> after;
    private DiffType diffType;
    private List<ChangeMetric> changeMetrics = new ArrayList<>();

    public NodeDiffPair(TreeASTNode<? extends ASTNode> before, TreeASTNode<? extends ASTNode> after,
            DiffType diffType) throws PairException {
        this.before = before;
        this.after = after;
        this.diffType = diffType;

        if (before != null) {
            before.setNodePair(this);
        }
        if (after != null) {
            after.setNodePair(this);
        }

        if (before == null && after == null) {
            throw new PairException("Pair without attached node");
        }

    }

    public TreeASTNode<? extends ASTNode> getBefore() {
        return this.before;
    }

    public TreeASTNode<? extends ASTNode> getAfter() {
        return this.after;
    }

    public DiffType getDiffType() {
        return this.diffType;
    }

    public void validateMetrics() {
        ChangeMetric.allMetricGenerators.stream().forEach(metricGenerator -> {
            try {
                metricGenerator.generateFromNodeDiff(this);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Fail to validate metrics");
            }

        });
    }

    public JsonElement toJson() {
        JsonArray elemObject = new JsonArray();
        changeMetrics.forEach((metric) -> {
            elemObject.add(metric.toJson());
        });
        return elemObject;
    }

    public void addChangeMetric(ChangeMetric cm) {
        // Avoid duplicate metrics
        //cm.setNodeDiffPair(this);
        for (ChangeMetric currentMetric : this.changeMetrics) {
            if ((currentMetric.getName() + currentMetric.toJson().toString())
                    .equals(cm.getName() + cm.toJson().toString())) {
                return;
            }
        }
        this.changeMetrics.add(cm);
       
    }

    public List<ChangeMetric> getChangeMetric() {
        return this.changeMetrics;
    }

    public boolean isInsert() {
        return getDiffType().equals(DiffType.INSERT);
    }

    public boolean isRemove() {
        return getDiffType().equals(DiffType.REMOVE);
    }

    public boolean isPair() {
        return getDiffType().equals(DiffType.PAIR);
    }

    public boolean isUpdate() {
        return getDiffType().equals(DiffType.UPDATE);
    }

    public boolean isMove() {
        return getDiffType().equals(DiffType.MOVE);
    }

}

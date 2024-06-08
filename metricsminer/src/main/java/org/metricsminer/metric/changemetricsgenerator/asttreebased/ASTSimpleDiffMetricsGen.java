package org.metricsminer.metric.changemetricsgenerator.asttreebased;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

public class ASTSimpleDiffMetricsGen extends ChangeMetricGenerator {

    private ArrayList<ChangeMetric> exclusiveNodes(String prefix, ArrayList<TreeASTNode<? extends ASTNode>> mainList,
            ArrayList<TreeASTNode<? extends ASTNode>> secondList) {
        ArrayList<ChangeMetric> exclusiveNodes = new ArrayList<>();
        for (TreeASTNode<? extends ASTNode> mainNode : mainList) {
            if (!secondList.contains(mainNode)) {
                ChangeMetric cm = buildChangeMetric(prefix + mainNode.getNodeType(), "Metric based on AST difference",
                        mainNode.toString());
                cm.setLine(mainNode.getLine());
                cm.setColumn(mainNode.getColumn());
                exclusiveNodes.add(cm);
            }
        }

        return exclusiveNodes;
    }

    @Override
    public List<ChangeMetric> generateFromNodeDiff(NodeDiffPair diffPair) {
        if (diffPair.getBefore() == null && diffPair.getAfter() == null) {
            return null;
        }

        ArrayList<ChangeMetric> simpleDiffMetrics = new ArrayList<>();
        final String addPrefix = "ADDED_";
        final String removePrefix = "REMOVED_";

        if (diffPair.getBefore() != null && diffPair.getAfter() == null) {
            simpleDiffMetrics
                    .addAll(exclusiveNodes(removePrefix, diffPair.getBefore().getChildren(), new ArrayList<>()));
        } else if (diffPair.getBefore() == null && diffPair.getAfter() != null) {
            simpleDiffMetrics
                    .addAll(exclusiveNodes(addPrefix, diffPair.getAfter().getChildren(), new ArrayList<>()));
        } else {

            simpleDiffMetrics.addAll(
                    exclusiveNodes(removePrefix, diffPair.getBefore().getChildren(),
                            diffPair.getAfter().getChildren()));
            simpleDiffMetrics
                    .addAll(exclusiveNodes(addPrefix, diffPair.getAfter().getChildren(),
                            diffPair.getBefore().getChildren()));
        }

        return simpleDiffMetrics;
    }

}

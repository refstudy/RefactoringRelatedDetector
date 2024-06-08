package org.metricsminer.metric.changemetricsgenerator.elementbased.annotations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.StructureModifier;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

//FIXME: Add parameter change metrics to an annotation
public class AnnotationChangedMetricGen<S extends StructureModifier>
        extends ChangeMetricGenerator<S> {

    public AnnotationChangedMetricGen() {
        super((Class<S>) StructureModifier.class);
    }

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair nodeDiffPair, StructureModifier before, StructureModifier after) {

        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        actionOverUniqueString(before.getAnnotations(), after.getAnnotations(), s -> {
            ChangeMetric cm = buildChangeMetric("REMOVED_ANNOTATION",
                    "Some method's annotation were removed", s, nodeDiffPair.getBefore().getNodeDiffPair());
            cm.setPos(TreeASTNode.getLineFromCU(nodeDiffPair.getBefore().getCompilationUnit(), s));
            cm.setDiffType(DiffType.REMOVE);
            metrics.add(cm);
        }, s -> {
            ChangeMetric cm = buildChangeMetric("ADDED_ANNOTATION",
                    "Some method's annotation exceptions were added", s,
                    nodeDiffPair.getAfter().getNodeDiffPair());
            cm.setDiffType(DiffType.INSERT);
            cm.setPos(TreeASTNode.getLineFromCU(nodeDiffPair.getAfter().getCompilationUnit(), s));
            metrics.add(cm);
        });

        return metrics;

    }

    @Override
    protected Class getCompareNodeClass(TreeASTNode<? extends ASTNode> treeASTNode) {
        return treeASTNode.getClass();
    }

    @Override
    protected S getCompareNode(TreeASTNode<? extends ASTNode> treeASTNode) {
        return (S) treeASTNode;
    }

}
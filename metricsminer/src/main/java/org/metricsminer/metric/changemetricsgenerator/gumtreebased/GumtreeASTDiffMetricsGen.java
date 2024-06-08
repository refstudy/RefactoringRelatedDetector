package org.metricsminer.metric.changemetricsgenerator.gumtreebased;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

public class GumtreeASTDiffMetricsGen extends ChangeMetricGenerator {

    @Override
    public List<ChangeMetric> generateFromNodeDiff(NodeDiffPair diffPair) {
        List<ChangeMetric> metrics = new ArrayList<>();
        ChangeMetric cm;

        if (diffPair.getDiffType() != DiffType.PAIR) {
            if (diffPair.getDiffType() == DiffType.INSERT) {
                cm = new InsertNodeMetric();
                cm.setDiffType(DiffType.INSERT);
            } else if (diffPair.getDiffType() == DiffType.MOVE) {
                cm = new MoveNodeMetric();
                cm.setDiffType(DiffType.MOVE);
            } else if (diffPair.getDiffType() == DiffType.UPDATE) {
                cm = new UpdateNodeMetric();
                cm.setDiffType(DiffType.UPDATE);
            } else {
                cm = new RemoveNodeMetric();
                cm.setDiffType(DiffType.REMOVE);
            }
            cm.setGenerator("GUMTREEDIFF");
            metrics.add(cm.validate(diffPair));
        }

        return metrics;
    }

}

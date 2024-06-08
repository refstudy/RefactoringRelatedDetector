package org.metricsminer.metric.changemetricsgenerator.elementbased.declarations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.diff.NodeDiffPair;

public class MethodDeclarationMetricGen extends ChangeMetricGenerator<MethodDeclaration> {

    public List<ChangeMetric> insertAction(NodeDiffPair diffPair, MethodDeclaration insertedMethod) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        ChangeMetric cm = buildChangeMetric(
                "ADDED_METHOD",
                "A new method has been added",
                diffPair.getAfter().getFullName());
        metrics.add(cm);
        return metrics;
    }

    public List<ChangeMetric> removeAction(NodeDiffPair diffPair, MethodDeclaration removedMethod) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        ChangeMetric cm = buildChangeMetric(
                "REMOVED_METHOD",
                "A method has been removed",
                diffPair.getBefore().getFullName());
        metrics.add(cm);
        return metrics;
    }

}

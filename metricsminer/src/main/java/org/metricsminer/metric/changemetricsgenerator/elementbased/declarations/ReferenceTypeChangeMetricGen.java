package org.metricsminer.metric.changemetricsgenerator.elementbased.declarations;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.ClassASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

/*
 * Indicates when the reference is modified at class level (class <-> interface)
 */
public class ReferenceTypeChangeMetricGen extends ChangeMetricGenerator<ClassASTNode> {

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair nodeDiffPair, ClassASTNode before, ClassASTNode after) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        if (before.isInterface() != after.isInterface()) {
            ChangeMetric metric;

            if (before.isInterface()) {
                metric = buildChangeMetric("TURNED_INTO_CLASS", "A interface became a class", null);
                metric.setValidation("The interface " + before.getName() + " has become a class");
            } else {
                metric = buildChangeMetric("TURNED_INTO_INTERFACE", "A class became an interface", null);
                metric.setValidation("The class " + before.getName() + " has become an interface");
            }
            metrics.add(metric);
        }
        return metrics;
    }

}

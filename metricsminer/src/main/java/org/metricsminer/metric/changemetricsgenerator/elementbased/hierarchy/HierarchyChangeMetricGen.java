package org.metricsminer.metric.changemetricsgenerator.elementbased.hierarchy;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.ClassASTNode;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

import com.google.gson.JsonObject;

public class HierarchyChangeMetricGen extends ChangeMetricGenerator<ClassASTNode> {

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair nodeDiffPair, ClassASTNode before, ClassASTNode after) {

        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        ChangeMetric cm;
        if (before.getHierarchyParent().isEmpty() && !after.getHierarchyParent().isEmpty()) {
            cm = buildChangeMetric("ADDED_HIERARCHY", "The class got a superclass",
                    after.getHierarchyParent());
            metrics.add(cm);
             cm.setDiffType(DiffType.INSERT);
        } else if (!before.getHierarchyParent().isEmpty() && after.getHierarchyParent().isEmpty()) {
            cm = buildChangeMetric("REMOVED_HIERARCHY", "The class lost a superclass",
                    before.getHierarchyParent());
            metrics.add(cm);
            cm.setDiffType(DiffType.REMOVE);
        } else if (!before.getHierarchyParent().equals(after.getHierarchyParent())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("before", before.getHierarchyParent());
            jsonObject.addProperty("after", after.getHierarchyParent());
            cm = buildChangeMetric("CHANGED_HIERARCHY", "The class has changed its superclass",
            jsonObject);
            metrics.add(cm);
        }

        return metrics;
    }

}

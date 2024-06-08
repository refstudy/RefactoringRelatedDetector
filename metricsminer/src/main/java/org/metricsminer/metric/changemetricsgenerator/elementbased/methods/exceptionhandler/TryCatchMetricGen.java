package org.metricsminer.metric.changemetricsgenerator.elementbased.methods.exceptionhandler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.TryStatement;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.JsonObject;

public class TryCatchMetricGen extends ChangeMetricGenerator<TryStatement> {

    @Override
    public List<ChangeMetric> insertAction(NodeDiffPair diffPair, TryStatement element) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        ChangeMetric cm = buildChangeMetric("ADDED_TRY_CATCH", "A try catch statement has been added",
                element, diffPair);
        metrics.add(cm);
        return metrics;
    }

    @Override
    public List<ChangeMetric> removeAction(NodeDiffPair diffPair, TryStatement element) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        ChangeMetric cm = buildChangeMetric("REMOVED_TRY_CATCH", "A try catch statement has been removed",
                element, ChangeMetricGenerator.getFirstValidParent(diffPair));
        metrics.add(cm);

        return metrics;
    }

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair diffPair, TryStatement belement, TryStatement aelement) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        if (!belement.toString().equals(aelement.toString())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("before", belement.toString());
            jsonObject.addProperty("after", aelement.toString());
            ChangeMetric cm = buildChangeMetric("TRY_CATCH_CHANGE",
                    "A try catch statement has been changed",
                    jsonObject, diffPair);
            metrics.add(cm);
        }
        return metrics;
    }

}

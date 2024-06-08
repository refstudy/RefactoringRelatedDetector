package org.metricsminer.metric.changemetricsgenerator.elementbased.methods.exceptionhandler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CatchClause;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.JsonObject;

public class CatchClauseMetricGen extends ChangeMetricGenerator<CatchClause> {

    @Override
    public List<ChangeMetric> insertAction(NodeDiffPair diffPair, CatchClause element) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("exception", element.getException().toString());
        jsonObject.addProperty("body", element.getBody().toString());
        ChangeMetric cm = buildChangeMetric("ADDED_CATCH_CLAUSE", "A catch clause statement has been added",
                jsonObject);
        metrics.add(cm);
        return metrics;
    }

    @Override
    public List<ChangeMetric> removeAction(NodeDiffPair diffPair, CatchClause element) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("exception", element.getException().toString());
        jsonObject.addProperty("body", element.getBody().toString());
        ChangeMetric cm = buildChangeMetric("REMOVED_CATCH_CLAUSE", "A catch clause statement has been removed",
                jsonObject, ChangeMetricGenerator.getFirstValidParent(diffPair));
        metrics.add(cm);

        return metrics;
    }

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair diffPair, CatchClause belement, CatchClause aelement) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        try {

            String bExceptionType = belement.getException().getType().toString();
            String aExceptionType = aelement.getException().getType().toString();
            JsonObject jsonObject = new JsonObject();

            if (!bExceptionType.equals(aExceptionType)) {

                if (!belement.getBody().toString().equals(aelement.getBody().toString())) {
                    metrics.addAll(insertAction(diffPair, aelement));
                    metrics.addAll(removeAction(diffPair, belement));
                } else {
                    jsonObject.addProperty("before", bExceptionType);
                    jsonObject.addProperty("after", aExceptionType);
                    ChangeMetric cm = buildChangeMetric("CATCH_CLAUSE_CHANGE",
                            "A catch handling has been changed",
                            jsonObject, diffPair);
                    metrics.add(cm);
                }

            } else if (!belement.getBody().toString().equals(aelement.getBody().toString())) {
                jsonObject.addProperty("before", belement.toString());
                jsonObject.addProperty("after", aelement.toString());
                ChangeMetric cm = buildChangeMetric("CATCH_HANDLING_CHANGE",
                        "A catch handling has been changed",
                        jsonObject, diffPair);
                metrics.add(cm);

            }
        } catch (Exception e) {
        }

        return metrics;
    }

}

package org.metricsminer.metric.changemetricsgenerator.elementbased.statements;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.ClassInstanceASTNode;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

import com.google.gson.JsonObject;

public class ClassInstanceChangeMetricGen extends ChangeMetricGenerator<ClassInstanceASTNode> {

    @Override
    protected List<ChangeMetric> insertAction(NodeDiffPair diffPair, ClassInstanceASTNode insertedObj) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        ChangeMetric cm = buildChangeMetric("ADDED_CLASS_INSTANCE", "A new class instance has been added",
                insertedObj.getName());
        metrics.add(cm);
        return metrics;
    }

    @Override
    protected List<ChangeMetric> removeAction(NodeDiffPair diffPair, ClassInstanceASTNode removedObj) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        ChangeMetric cm = buildChangeMetric("REMOVED_CLASS_INSTANCE", "A class instance has been removed",
                removedObj.getName(), getFirstValidParent(diffPair));
        metrics.add(cm);
        return metrics;
    }

    @Override
    protected List<ChangeMetric> pairAction(NodeDiffPair diffPair, ClassInstanceASTNode beforeObj,
            ClassInstanceASTNode afterObj) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        if (beforeObj.hasAnonymousClassDeclaration() && !afterObj.hasAnonymousClassDeclaration()) {
            ChangeMetric cm = buildChangeMetric("REMOVED_CLASS_ANONYMOUS_DECLARATION",
                    "A anonymous class declaration has been removed",
                    beforeObj.getAnonymousClassDeclaration().toString());
            cm.setDiffType(DiffType.REMOVE);
            metrics.add(cm);
        }

        if (!beforeObj.hasAnonymousClassDeclaration() && afterObj.hasAnonymousClassDeclaration()) {
            ChangeMetric cm = buildChangeMetric("ADDED_CLASS_ANONYMOUS_DECLARATION",
                    "A new anonymous class declaration has been added",
                    afterObj.getAnonymousClassDeclaration().toString());
            cm.setDiffType(DiffType.INSERT);
            metrics.add(cm);
        }

        if (beforeObj.hasAnonymousClassDeclaration() && afterObj.hasAnonymousClassDeclaration() &&
                !beforeObj.getAnonymousClassDeclaration().toString()
                        .equals(afterObj.getAnonymousClassDeclaration().toString())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("before", beforeObj.getAnonymousClassDeclaration().toString());
            jsonObject.addProperty("after", afterObj.getAnonymousClassDeclaration().toString());
            ChangeMetric cm = buildChangeMetric("CHANGED_ANONYMOUS_DECLARATION",
                    "A class instance had its declaration changed",
                    jsonObject);
            metrics.add(cm);
        }

        if (!beforeObj.getArguments().toString().equals(afterObj.getArguments().toString())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("before", beforeObj.getArguments().toString());
            jsonObject.addProperty("after", afterObj.getArguments().toString());
            ChangeMetric cm = buildChangeMetric("CLASS_INSTANCE_ARGUMENTS_CHANGE",
                    "A class instance had its arguments changed",
                    jsonObject);
            metrics.add(cm);
        }

        return metrics;
    }
}

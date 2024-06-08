package org.metricsminer.metric.changemetricsgenerator.elementbased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.AbstractDeclarationASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

public class RenameMetricGen
        extends ChangeMetricGenerator<AbstractDeclarationASTNode> {

    public RenameMetricGen() {
        super(AbstractDeclarationASTNode.class);
    }

    @Override
    protected List<ChangeMetric> pairAction(NodeDiffPair diffPair, AbstractDeclarationASTNode before,
            AbstractDeclarationASTNode after) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        String beforeName = before.getName();
        String afterName = after.getName();
        if (beforeName.contains("(")) {
            beforeName = beforeName.split("\\(")[0];
            afterName = afterName.split("\\(")[0];
        }

        if (!beforeName.equals(afterName)) {

            HashMap<String, String> changeModifiersHash = new HashMap<>();
            changeModifiersHash.put("before", beforeName);
            changeModifiersHash.put("after", afterName);
            ChangeMetric metric = buildChangeMetric("RENAME_ELEMENT", "The element changed its name",
                    gson.toJson(changeModifiersHash));
            metric.setValidation(
                    "The element " + before.getName() + " has been renamed to "
                            + after.getName());
            metrics.add(metric);
        }
        return metrics;
    }

}

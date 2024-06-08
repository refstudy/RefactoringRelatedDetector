package org.metricsminer.metric.changemetricsgenerator.elementbased.declarations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.AbstractDeclarationASTNode;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.JsonObject;

/*
 * Descending accessibility list public > protected > default > private.
 */
public class AccessChangeMetricGen
        extends ChangeMetricGenerator<AbstractDeclarationASTNode> {

    public AccessChangeMetricGen() {
        super(AbstractDeclarationASTNode.class);
    }

    private ArrayList<String> accessbilities = new ArrayList<>(
            Arrays.asList("public", "protected", "default", "private"));

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair nodeDiffPair, AbstractDeclarationASTNode before,
            AbstractDeclarationASTNode after) {
        ArrayList<ChangeMetric> changeMetrics = new ArrayList<>();
        if (!before.getAccessModifier().equals(after.getAccessModifier())) {

            ChangeMetric cm;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("before", before.getAccessModifier());
            jsonObject.addProperty("after", after.getAccessModifier());
            if (accessbilities.indexOf(before.getAccessModifier()) < accessbilities
                    .indexOf(after.getAccessModifier())) {
                cm = buildChangeMetric("ACCESS_REDUCED", "The privacy of the element was increased",
                        jsonObject);
            } else {
                cm = buildChangeMetric("ACCESS_INCREASED", "The privacy of the element was reduced",
                        jsonObject);
            }

            cm.setLine(after.getLine());
            cm.setColumn(after.getColumn());

            changeMetrics.add(cm);
        }
        return changeMetrics;
    }

}
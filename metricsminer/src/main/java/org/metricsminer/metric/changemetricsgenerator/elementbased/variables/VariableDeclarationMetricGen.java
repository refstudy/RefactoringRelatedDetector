package org.metricsminer.metric.changemetricsgenerator.elementbased.variables;

import org.metricsminer.metric.changemetricsgenerator.elementbased.abstraction.BasicElementChangedMetricsGen;
import org.metricsminer.model.astnodes.VariableDeclarationASTNode;

public class VariableDeclarationMetricGen
        extends BasicElementChangedMetricsGen<VariableDeclarationASTNode> {

    @Override
    protected ElementMetricChanged getElementMetricChanged(VariableDeclarationASTNode variable) {
        return new ElementMetricChanged("variable(s)", "VARIABLE", variable);
    }

}

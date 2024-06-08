package org.metricsminer.metric.changemetricsgenerator.elementbased.methods;

import org.eclipse.jdt.core.dom.ReturnStatement;
import org.metricsminer.metric.changemetricsgenerator.elementbased.abstraction.BasicElementChangedMetricsGen;

public class MethodReturnValueChangedMetricsGen extends BasicElementChangedMetricsGen<ReturnStatement> {

    @Override
    protected ElementMetricChanged getElementMetricChanged(ReturnStatement obj) {
        String value = obj.getExpression() == null ? "void" : obj.getExpression().toString();
        return new ElementMetricChanged("method return", "RETURN_VALUE", obj);
    }

}

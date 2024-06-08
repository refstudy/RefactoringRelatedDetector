package org.metricsminer.metric.changemetricsgenerator.elementbased.abstraction;

import java.util.ArrayList;
import java.util.List;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.diff.NodeDiffPair;

import com.google.gson.JsonObject;

public abstract class BasicElementChangedMetricsGen<T>
        extends ChangeMetricGenerator<T> {

   
    protected abstract ElementMetricChanged getElementMetricChanged(T obj);

    protected List<ChangeMetric> pairAction(NodeDiffPair diffPair, T beforeObj, T afterObj) {
        List<ChangeMetric> metrics = new ArrayList<>();
        ElementMetricChanged bElement = getElementMetricChanged(beforeObj);
        ElementMetricChanged aElement = getElementMetricChanged(afterObj);
        if(bElement == null || aElement == null){
            return new ArrayList<>();
        }
        if (!bElement.values.toString().equals(aElement.values.toString())) {
            JsonObject diffObj = new JsonObject();
            diffObj.addProperty("before:", bElement.values.toString());
            diffObj.addProperty("after:", aElement.values.toString());
            metrics.add(buildChangeMetric("CHANGED_" + bElement.metricName,
                    "Some " + bElement.name + " have their values changed", diffObj.toString()));
        }

        return metrics;
    }

    @Override
    protected List<ChangeMetric> insertAction(NodeDiffPair diffPair, T insertedObj) {  
        List<ChangeMetric> metrics = new ArrayList<>();
        ElementMetricChanged element = getElementMetricChanged(insertedObj);
        if(element == null){
            return new ArrayList<>();
        }
        metrics.add(buildChangeMetric("ADDED_" + element.metricName,
                "Some " + element.name + " have been added", element.values));

        return metrics;
    }

    @Override
    protected List<ChangeMetric> removeAction(NodeDiffPair diffPair, T removedObj) {
        List<ChangeMetric> metrics = new ArrayList<>();
        ElementMetricChanged element = getElementMetricChanged(removedObj);
        if(element == null){
            return new ArrayList<>();
        }
        metrics.add(buildChangeMetric("REMOVED_" + element.metricName,
                "Some " + element.name + " have been removed", element.values));
        return metrics;
    }

    protected class ElementMetricChanged {
        private String name;
        private String metricName;
        private Object values;

        public ElementMetricChanged(String name, String metricName, Object values) {
            this.name = name;
            this.metricName = metricName;
            this.values = values;
        }

    }

}

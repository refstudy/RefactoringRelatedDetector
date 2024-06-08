package org.metricsminer.metric.collector;

import java.util.ArrayList;
import java.util.Arrays;

import org.metricsminer.metric.collector.collector.ElementsDiffCollector;
import org.metricsminer.model.astnodes.FileAST;
import org.metricsminer.model.diff.FileDiff;

public abstract class MetricCollector {

    private static ArrayList<MetricCollector> collectors = new ArrayList<>();

    static {
        if (collectors.isEmpty()) {
            collectors.add(new ElementsDiffCollector());
        }
    }

    public static void addMetric(MetricCollector metricCollector) {
        collectors.add(metricCollector);
    }

    public static void addMetric(MetricCollector... metricCollectors) {
        collectors.addAll(Arrays.asList(metricCollectors));
    }

    public static FileDiff runAllCollectors(FileAST before, FileAST after) {

        FileDiff mergedFiledDiff = new FileDiff(before, after);
        collectors.stream().forEach(metricCollector -> {
            //System.out.println("Running " + metricCollector.getName() + "...");
            metricCollector.collect(mergedFiledDiff);
        });

        return mergedFiledDiff;
    }

    public abstract String getName();

    public abstract void collect(FileDiff fileDiff);
}

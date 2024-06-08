package org.metricsminer.metric.collector.collector;

import java.util.ArrayList;

import org.metricsminer.metric.collector.GumTreeDiffCollector;
import org.metricsminer.model.diff.FileDiff;
import org.metricsminer.model.diff.NodeDiffPair;

public class ElementsDiffCollector extends GumTreeDiffCollector {

    @Override
    public String getName() {
        return "ElementDiffCollector";
    }
    
    @Override
    public void collect(FileDiff fileDiff, ArrayList<NodeDiffPair> diff) {
        // TODO Auto-generated method stub
        
    }



}

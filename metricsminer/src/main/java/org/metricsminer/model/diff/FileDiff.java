package org.metricsminer.model.diff;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.metricsminer.model.astnodes.FileAST;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FileDiff {

    private transient FileAST beforeFileAst;
    private transient FileAST afterFileAst;

    public FileDiff(FileAST before, FileAST after) {
        this.beforeFileAst = before;
        this.afterFileAst = after;
    }

    public FileDiff() {
    }

    public FileAST getBeforeFileAst() {
        return this.beforeFileAst;
    }

    public FileAST getAfterFileAst() {
        return this.afterFileAst;
    }

    // public void printFileDiff(){
    // System.out.println(afterFileAst.toJson().toString());
    // }

    public void printShortFileDiff() {
        JsonArray jsonArray = new JsonArray();
        getAllChangedPairs().forEach((nodeDiffPair) -> {
            nodeDiffPair.getChangeMetric().forEach(metric -> {
                jsonArray.add(metric.toFullJson());
            });
        });
        System.out.println(jsonArray.toString());
    }

    public List<NodeDiffPair> getAllChangedPairs() {
        List<NodeDiffPair> afterPairs = afterFileAst.getAllChangedPairs();
        List<NodeDiffPair> beforePairs = beforeFileAst.getAllChangedPairs();

        Set<NodeDiffPair> uniquePairs = new HashSet<>(afterPairs);
        uniquePairs.addAll(beforePairs);

        return uniquePairs.stream().collect(Collectors.toList());
    }
}

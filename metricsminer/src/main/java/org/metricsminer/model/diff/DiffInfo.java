package org.metricsminer.model.diff;

import java.util.List;

public class DiffInfo {

    public NodeDiffPair nodeDiffPair1;
    public NodeDiffPair nodeDiffPair2;

    List<ChangePair> changePairs;

    public boolean sameClass;
    public boolean sameMethod;

    public boolean isMethodCalled;


    public DiffInfo(NodeDiffPair nodeDiffPair1, NodeDiffPair nodeDiffPair2,
                    List<ChangePair> changePairs, boolean sameClass, boolean
                            sameMethod, boolean isMethodCalled) {
        this.nodeDiffPair1 = nodeDiffPair1;
        this.nodeDiffPair2 = nodeDiffPair2;
        this.changePairs = changePairs;
        this.sameClass = sameClass;
        this.sameMethod = sameMethod;
        this.isMethodCalled = isMethodCalled;
    }

    @Override
    public String toString() {
        return "DiffInfo{" +
                //"nodeDiffPair1=" + nodeDiffPair1 +
                //", nodeDiffPair2=" + nodeDiffPair2 +
                //", changePairs=" + changePairs +
                ", sameClass=" + sameClass +
                ", sameMethod=" + sameMethod +
                ", isMethodCalled=" + isMethodCalled +
                '}';
    }
}

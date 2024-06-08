package org.metricsminer.model.diff;

public class ChangePair {
    private NodeChangeInfo changeInfo1;
    private NodeChangeInfo changeInfo2;
    private int depthDistance;

    public ChangePair(NodeChangeInfo changeInfo1, NodeChangeInfo changeInfo2) {
        this.changeInfo1 = changeInfo1;
        this.changeInfo2 = changeInfo2;
        this.depthDistance = Math.abs(changeInfo1.getDepth() - changeInfo2.getDepth());
    }

    // Getters
    public NodeChangeInfo getChangeInfo1() {
        return changeInfo1;
    }

    public NodeChangeInfo getChangeInfo2() {
        return changeInfo2;
    }

    public int getDepthDistance() {
        return depthDistance;
    }

    @Override
    public String toString() {
        return "ChangePair{" +
                "changeInfo1=" + changeInfo1 +
                ", changeInfo2=" + changeInfo2 +
                ", depthDistance=" + depthDistance +
                '}';
    }
}
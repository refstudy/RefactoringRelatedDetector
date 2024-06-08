package org.metricsminer.model.diff;

public class NodeChangeInfo {

    private int depth;
    private NodeDiffPair.DiffType changeType;
    private String nodeDetailsBefore; // Represents details before the change
    private String nodeDetailsAfter; // Represents details after the change
    // Add fields for the element types or other attributes
    private String elementTypeBefore;
    private String elementTypeAfter;

    // Adjust the constructor and add new parameters accordingly
    public NodeChangeInfo(int depth, NodeDiffPair.DiffType changeType,
                          String nodeDetailsBefore, String nodeDetailsAfter,
                          String elementTypeBefore, String elementTypeAfter) {
        this.depth = depth;
        this.changeType = changeType;
        this.nodeDetailsBefore = nodeDetailsBefore;
        this.nodeDetailsAfter = nodeDetailsAfter;
        this.elementTypeBefore = elementTypeBefore;
        this.elementTypeAfter = elementTypeAfter;
    }


    // Getters and setters
    public int getDepth() {
        return depth;
    }

    public NodeDiffPair.DiffType getChangeType() {
        return changeType;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setChangeType(NodeDiffPair.DiffType changeType) {
        this.changeType = changeType;
    }

    public String getNodeDetailsBefore() {
        return nodeDetailsBefore;
    }

    public void setNodeDetailsBefore(String nodeDetailsBefore) {
        this.nodeDetailsBefore = nodeDetailsBefore;
    }

    public String getNodeDetailsAfter() {
        return nodeDetailsAfter;
    }

    public void setNodeDetailsAfter(String nodeDetailsAfter) {
        this.nodeDetailsAfter = nodeDetailsAfter;
    }

    public String getElementTypeBefore() {
        return elementTypeBefore;
    }

    public void setElementTypeBefore(String elementTypeBefore) {
        this.elementTypeBefore = elementTypeBefore;
    }

    public String getElementTypeAfter() {
        return elementTypeAfter;
    }

    public void setElementTypeAfter(String elementTypeAfter) {
        this.elementTypeAfter = elementTypeAfter;
    }


    public String toString() {
        return "ChangeType: " + changeType + ", Depth: " + depth +
                ", Before: [" + elementTypeBefore + ", " + nodeDetailsBefore + "]" +
                ", After: [" + elementTypeAfter + ", " + nodeDetailsAfter + "]";
    }

}
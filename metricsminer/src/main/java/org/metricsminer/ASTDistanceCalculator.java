package org.metricsminer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.ChangePair;
import org.metricsminer.model.diff.DiffInfo;
import org.metricsminer.model.diff.NodeChangeInfo;
import org.metricsminer.model.diff.NodeDiffPair;

public class ASTDistanceCalculator {

    public DiffInfo getInfoBetweenPair(NodeDiffPair pair1, NodeDiffPair pair2) {

        TreeASTNode<?> contextNode1 = getContextNode(pair1);
        TreeASTNode<?> contextNode2 = getContextNode(pair2);

        List<NodeChangeInfo> distanceInfos = new ArrayList<>();
        List<ChangePair> changePairs;
        traverseAndCalculateChangeInfo(contextNode1, contextNode2, 0, distanceInfos);
        boolean sameClass = areInSameClassContext(pair1, pair2);
        boolean sameMethod = areInSameMethodContext(pair1, pair2);
        boolean isMethodCalled = isMethodInvocationDescendant(pair1, pair2);
        changePairs = calculateDepthDistances(distanceInfos);


        return new DiffInfo(pair1, pair2, changePairs, sameClass, sameMethod, isMethodCalled);
    }

    private void traverseAndCalculateChangeInfo(TreeASTNode<? extends ASTNode> node1,
                                   TreeASTNode<? extends ASTNode> node2, int currentDepth,
                                   List<NodeChangeInfo> changeInfos) {

        if (node1 == null && node2 != null) {
            // Node added
            String nodeDetailsAfter = getNodeDetails(node2);
            String elementTypeAfter = getElementType(node2);
            changeInfos.add(new NodeChangeInfo(currentDepth, NodeDiffPair.DiffType.INSERT, "", nodeDetailsAfter, "", elementTypeAfter));
        } else if (node2 == null && node1 != null) {
            // Node removed
            String nodeDetailsBefore = getNodeDetails(node1);
            String elementTypeBefore = getElementType(node1);
            changeInfos.add(new NodeChangeInfo(currentDepth, NodeDiffPair.DiffType.REMOVE, nodeDetailsBefore, "", elementTypeBefore, ""));
        } else if (node1 != null) {
            // Node updated or unchanged; additional logic needed to determine
            // You might compare specific properties of the nodes to determine if an update occurred
            if (!nodesAreEquivalent(node1, node2)) { // nodesAreEquivalent is a hypothetical method you'd implement
                String nodeDetailsBefore = getNodeDetails(node1);
                String nodeDetailsAfter = getNodeDetails(node2);
                String elementTypeBefore = getElementType(node1);
                String elementTypeAfter = getElementType(node2);
                changeInfos.add(new NodeChangeInfo(currentDepth, NodeDiffPair.DiffType.UPDATE, nodeDetailsBefore, nodeDetailsAfter, elementTypeBefore, elementTypeAfter));
            }
        }

        // Continue traversal for child nodes, if present
        int nextDepth = currentDepth + 1;
        List<TreeASTNode<? extends ASTNode>> childrenBefore = node1 != null ? node1.getChildren() : Collections.emptyList();
        List<TreeASTNode<? extends ASTNode>> childrenAfter = node2 != null ? node2.getChildren() : Collections.emptyList();

        // Assuming children are paired in some manner; this could be by index or another strategy
        for (int i = 0; i < Math.max(childrenBefore.size(), childrenAfter.size()); i++) {
            TreeASTNode<? extends ASTNode> childBefore = i < childrenBefore.size() ? childrenBefore.get(i) : null;
            TreeASTNode<? extends ASTNode> childAfter = i < childrenAfter.size() ? childrenAfter.get(i) : null;
            traverseAndCalculateChangeInfo(childBefore, childAfter, nextDepth, changeInfos);
        }
    }

    private List<ChangePair> calculateDepthDistances(List<NodeChangeInfo> changeInfos) {
        // Assuming changeInfos are ordered in the way changes occurred or based on a criterion for pairing
        List<ChangePair> changePairs = new ArrayList<>();
        for (int i = 0; i < changeInfos.size() - 1; i++) {
            // This loop pairs consecutive changes, adjust according to your needs
            NodeChangeInfo change1 = changeInfos.get(i);
            NodeChangeInfo change2 = changeInfos.get(i + 1);

            ChangePair pair = new ChangePair(change1, change2);
            changePairs.add(pair);
        }

        return changePairs;
    }

    public boolean areInSameMethodContext(NodeDiffPair pair1, NodeDiffPair pair2) {
        TreeASTNode<?> contextNode1 = getContextNode(pair1);
        TreeASTNode<?> contextNode2 = getContextNode(pair2);

        TreeASTNode<?> method1 = contextNode1.findNearestMethodContext();
        TreeASTNode<?> method2 = contextNode2.findNearestMethodContext();

        if (method1 == null || method2 == null) {
            return method1 == method2; // Both null, at top-level
        }

        //Both at method level, checking full name for both
        return method1.getFullName().equals(method2.getFullName());
    }

    public boolean areInSameClassContext(NodeDiffPair pair1, NodeDiffPair pair2) {
        TreeASTNode<?> contextNode1 = getContextNode(pair1);
        TreeASTNode<?> contextNode2 = getContextNode(pair2);

        TreeASTNode<?> method1 = contextNode1.findNearestMethodContext();
        TreeASTNode<?> method2 = contextNode2.findNearestMethodContext();

        //Above method level for method1, checking class
        if (method1 == null && method2 != null){
            if (method2.getFullName().contains(contextNode1.getFullName())){
                return true;
            }
        }

        //Above method level for method2, checking class
        if (method2 == null && method1 != null){
            if (method1.getFullName().contains(contextNode2.getFullName())){
                return true;
            }
        }

        if (method1 == null || method2 == null) {
            return method1 == method2; // Both null, at top-level
        }

        return false;
    }

    private TreeASTNode<?> getContextNode(NodeDiffPair pair) {
        return pair.getAfter() != null ? pair.getAfter() : pair.getBefore();
    }


    private String getNodeDetails(TreeASTNode<? extends ASTNode> node) {
        // Implement to extract node details
        return node != null ? node.toString() : "";
    }

    private static String getElementType(TreeASTNode<? extends ASTNode> node) {
        // Implement to extract element type or other specific attributes
        return node != null ? node.getNode().getClass().getSimpleName() : "";
    }

    private boolean nodesAreEquivalent(TreeASTNode<? extends ASTNode> node1, TreeASTNode<? extends ASTNode> node2) {
        // Check if both nodes are null (considered equivalent in this context)
        if (node1 == null && node2 == null) {
            return true;
        }

        // Check if one is null and the other is not (not equivalent)
        if (node1 == null || node2 == null) {
            return false;
        }

        // Check if nodes are of the same type
        if (!node1.getNode().getClass().equals(node2.getNode().getClass())) {
            return false;
        }


        String identifier1 = getNodeIdentifier(node1);
        String identifier2 = getNodeIdentifier(node2);

        return identifier1.equals(identifier2);
    }

    public static boolean isMethodInvocationDescendant(NodeDiffPair parentPair, NodeDiffPair childPair) {
        TreeASTNode<?> parentNode = parentPair.getAfter() != null ? parentPair.getAfter() : parentPair.getBefore();
        TreeASTNode<?> current = childPair.getAfter() != null ? childPair.getAfter() : childPair.getBefore();

        while (current != null) {
            if (getElementType(current).equals("MethodInvocation")) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }


    private String getNodeIdentifier(TreeASTNode<? extends ASTNode> node) {

        return node.getNode().toString();
    }



}

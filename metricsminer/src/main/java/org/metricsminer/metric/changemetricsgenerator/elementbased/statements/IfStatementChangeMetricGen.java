package org.metricsminer.metric.changemetricsgenerator.elementbased.statements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.astnodes.AbstractDeclarationASTNode;
import org.metricsminer.model.astnodes.GenericASTNode;
import org.metricsminer.model.astnodes.MethodASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

import com.google.gson.JsonObject;

public class IfStatementChangeMetricGen extends ChangeMetricGenerator<MethodASTNode> {

    @Override
    public List<ChangeMetric> removeAction(NodeDiffPair diffPair, MethodASTNode beforeObj) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        List<IfStatementDetail> bDetails = getAllNodesOfClass(beforeObj, new ArrayList<>(), new IfStatementDetail());
        bDetails.forEach(bDetail -> {
            metrics.add(removeIfStatement(diffPair, beforeObj, bDetail));
        });

        return metrics;
    }

    @Override
    public List<ChangeMetric> insertAction(NodeDiffPair diffPair, MethodASTNode afterObj) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        List<IfStatementDetail> aDetails = getAllNodesOfClass(afterObj, new ArrayList<>(), new IfStatementDetail());
        aDetails.forEach(aDetail -> {
            metrics.add(addIfStatement(diffPair, afterObj, aDetail));
        });

        return metrics;
    }

    @Override
    public List<ChangeMetric> pairAction(NodeDiffPair diffPair, MethodASTNode beforeObj, MethodASTNode afterObj) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();
        List<IfStatementDetail> bDetails = getAllNodesOfClass(beforeObj, new ArrayList<>(), new IfStatementDetail());
        List<IfStatementDetail> aDetails = getAllNodesOfClass(afterObj, new ArrayList<>(), new IfStatementDetail());
        bDetails.forEach(bDetail -> {
            boolean found = false;
            for (IfStatementDetail aDetail : aDetails) {
                String bThenStatement = bDetail.thenStatement.replaceAll(" ", "");
                String aThenStatement = aDetail.thenStatement.replaceAll(" ", "");

                JsonObject jsonObject = new JsonObject();

                if (bDetail.preConditions.equals(aDetail.preConditions)) {

                    found = true;
                    if (!bThenStatement.equals(aThenStatement)) {

                        jsonObject.addProperty("conditions", aDetail.preConditions.toString());
                        jsonObject.addProperty("before", bDetail.thenStatement);
                        jsonObject.addProperty("after", aDetail.thenStatement);
                        ChangeMetric cm = buildChangeMetric("CHANGED_EXISTING_" + aDetail.getType() + "_BLOCK",
                                "A (else)if block for a existing condition has been changed",
                                jsonObject, diffPair);
                        cm.setPos(TreeASTNode.getLineFromCU(afterObj.getCompilationUnit(),
                                aDetail.closestNode.getNode()));
                        cm.setClosestElement(aDetail.closestNode);
                        metrics.add(cm);
                    }
                    aDetail.newStatement = false;
                    break;
                } else if (bThenStatement.equals(aThenStatement) &&
                        !bThenStatement.replace("\n", "").trim().equals("{}")) {
                    found = true;
                    jsonObject.addProperty("before", bDetail.preConditions.toString());
                    jsonObject.addProperty("after", aDetail.preConditions.toString());
                    jsonObject.addProperty("value", bDetail.thenStatement);
                    ChangeMetric cm = buildChangeMetric("CHANGED_" + aDetail.getType() + "_CONDITION",
                            "A (else)if expression for a existing block has been changed",
                            jsonObject, diffPair);
                    cm.setPos(TreeASTNode.getLineFromCU(afterObj.getCompilationUnit(),
                            aDetail.closestNode.getNode()));
                    metrics.add(cm);
                    cm.setClosestElement(aDetail.closestNode);
                    aDetail.newStatement = false;
                    break;
                }
            }

            if (!found) {
                metrics.add(removeIfStatement(diffPair, beforeObj, bDetail));
            }
        });
        aDetails.stream().filter(aDetail -> aDetail.newStatement).forEach(aDetail -> {
            metrics.add(addIfStatement(diffPair, afterObj, aDetail));
        });
        return metrics;
    }

    private ChangeMetric addIfStatement(NodeDiffPair diffPair, MethodASTNode afterObj, IfStatementDetail aDetail) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", aDetail.thenStatement);
        jsonObject.addProperty("condition", aDetail.preConditions.toString());
        ChangeMetric cm = buildChangeMetric("ADDED_" + aDetail.getType() + "_STATEMENT",
                "A (else)if statement condition has been added",
                jsonObject, diffPair);
        cm.setPos(TreeASTNode.getLineFromCU(afterObj.getCompilationUnit(),
                aDetail.closestNode.getNode()));
        cm.setClosestElement(aDetail.closestNode);
        cm.setDiffType(DiffType.INSERT);
        return cm;
    }

    private ChangeMetric removeIfStatement(NodeDiffPair diffPair, MethodASTNode beforeObj, IfStatementDetail bDetail) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", bDetail.thenStatement);
        jsonObject.addProperty("condition", bDetail.preConditions.toString());
        ChangeMetric cm = buildChangeMetric("REMOVED_" + bDetail.getType() + "_STATEMENT",
                "A (else)if statement condition has been removed",
                jsonObject, diffPair);
        cm.setPos(TreeASTNode.getLineFromCU(beforeObj.getCompilationUnit(),
                bDetail.closestNode.getNode()));
        cm.setClosestElement(bDetail.closestNode);
        cm.setDiffType(DiffType.REMOVE);
        return cm;
    }

    private List<IfStatementDetail> getAllNodesOfClass(TreeASTNode<? extends ASTNode> baseAstNode,
            List<IfStatementDetail> ifDetails, IfStatementDetail lastIfStatementDetail) {
        if (baseAstNode.getNode() instanceof IfStatement) {
            GenericASTNode<IfStatement> ifNode = (GenericASTNode<IfStatement>) baseAstNode;

            lastIfStatementDetail = new IfStatementDetail(lastIfStatementDetail, ifNode, false);
            lastIfStatementDetail.closestNode = ifNode.getChildren().stream()
                    .filter(el -> el.getNode().equals(ifNode.getNode().getThenStatement())).toList().get(0);

            try {
                ifDetails.add(lastIfStatementDetail);
                // Has Else
                if (ifNode.getNode().getElseStatement() != null
                        && !(ifNode.getNode().getElseStatement() instanceof IfStatement)) {
                    IfStatementDetail elseStatementDetail = new IfStatementDetail(lastIfStatementDetail, ifNode, true);
                    elseStatementDetail.closestNode = ifNode.getChildren().stream()
                    .filter(el -> el.getNode().equals(ifNode.getNode().getElseStatement())).toList().get(0);
                    ifDetails.add(elseStatementDetail);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        for (TreeASTNode<? extends ASTNode> child : baseAstNode.getChildren()) {
            if (child instanceof AbstractDeclarationASTNode || child.getNode() instanceof AnonymousClassDeclaration) {
                continue;
            }
            getAllNodesOfClass(child, ifDetails, lastIfStatementDetail);
        }

        return ifDetails;
    }

    class IfStatementDetail {
        List<String> preConditions = new ArrayList<>();
        String thenStatement = "";
        boolean newStatement = true;
        TreeASTNode closestNode;
        boolean isElse;

        IfStatementDetail() {

        }

        IfStatementDetail(IfStatementDetail prevIfStatementDetail, TreeASTNode<IfStatement> elseOf, boolean isElse) {
            this.preConditions.addAll(prevIfStatementDetail.preConditions);
            this.isElse = isElse;
            int indexToChange = preConditions.size() - 1;
            TreeASTNode parent = elseOf.getParent();
            if (isElse) {
                this.thenStatement = elseOf.getNode().getElseStatement().toString();
            } else {
                this.preConditions.add(elseOf.getNode().getExpression().toString());
                this.thenStatement = elseOf.getNode().getThenStatement().toString();
                indexToChange = preConditions.size() - 2;
            }

            boolean parentIncludesChild = false;

            if (parent.getNode() instanceof IfStatement) {
                parentIncludesChild = ((IfStatement) parent.getNode())
                        .getThenStatement().toString().replaceAll(" ", "")
                        .contains(elseOf.toString().replaceAll(" ", ""));
            }

            if (parent != null && indexToChange >= 0 && !parentIncludesChild) {
                this.preConditions.set(indexToChange, "!" + preConditions.get(indexToChange));
                indexToChange--;
            }
        }

        @Override
        public String toString() {
            HashMap<String, String> hash = new HashMap<>();
            hash.put("condition", preConditions.toString());
            // hash.put("body", thenStatement.toString());
            return hash.toString();
        }

        public String getType() {
            return isElse ? "ELSE" : "IF";
        }

    }
}

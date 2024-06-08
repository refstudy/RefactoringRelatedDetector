package lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.model.astnodes.MethodASTNode;
import org.metricsminer.model.astnodes.MethodCallASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.FileDiff;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;
import org.refactoringminer.api.Refactoring;

import gr.uom.java.xmi.diff.CodeRange;
import lab.ref.customrefactoring.model.RefactoringInstance;
import lab.ref.customrefactoring.model.RelatedChange;
import lab.ref.customrefactoring.model.RefactoringInstance.Type;
import lab.ref.customrefactoring.model.RelatedChange.RelationType;

public abstract class RefactoringParser {

    public RefactoringInstance.Type refactoringType;
    protected Refactoring ref;

    public RefactoringParser(Type extractMethod, Refactoring refactoring) {
        this.refactoringType = extractMethod;
        this.ref = refactoring;
    }

    public RefactoringInstance build(List<FileDiff> diffs) throws Exception {

        ArrayList<RelatedChange> relatedChanges = new ArrayList<>();

        // Filtered sides
        List<CodeRange> right = ref.rightSide();
        List<CodeRange> left = ref.leftSide();
        removeExtraSideItem(right);
        removeExtraSideItem(left);

        ImmutablePair<List<TreeASTNode<? extends ASTNode>>, List<TreeASTNode<? extends ASTNode>>> elements = buildInstance(
                diffs);

        if (elements.left.isEmpty() || elements.right.isEmpty()) {
            throw new Exception("Couldn't locate main elements");
        }
        // STEP 1: main related
        relatedChanges.addAll(filterMainChanges(left, right, diffs, elements.left, elements.right));

        // STEP 2-4: declaration part or calls main methods
        getRelatedChanges(relatedChanges, elements.left, elements.right);

        RefactoringInstance rInstance = new RefactoringInstance(elements.left, elements.right, relatedChanges, this);

        posBuild(rInstance);
        return rInstance;

    }

    protected void posBuild(RefactoringInstance rInstance) {
    }

    protected abstract ImmutablePair<List<TreeASTNode<? extends ASTNode>>, List<TreeASTNode<? extends ASTNode>>> buildInstance(
            List<FileDiff> diffs) throws RefMinerParseException;

    public abstract void removeExtraSideItem(List<CodeRange> sideArray);

    public RefactoringInstance.Type getRefactoringType() {
        return this.refactoringType;
    }

    protected List<RelatedChange> filterMainChanges(List<CodeRange> left, List<CodeRange> right,
            List<FileDiff> diffs, List<TreeASTNode<? extends ASTNode>> sourceElements,
            List<TreeASTNode<? extends ASTNode>> targetElements) {

        List<RelatedChange> relatedChanges = new ArrayList<>();

        List<ChangeMetric> alreadyIncluded = new ArrayList<>();

        targetElements.forEach((element) -> {
            element.getParent().getNodeDiffPair().getChangeMetric().forEach(parentMetric -> {
                if (parentMetric.getClosestElement().equals(element)) {
                    RelatedChange rc = new RelatedChange(parentMetric, RelationType.MENTIONED_DECLARATION, 0);
                    rc.setRelationDetail(parentMetric.getClosestElement().getFullName());
                    relatedChanges.add(rc);
                    alreadyIncluded.add(parentMetric);
                }
            });
        });

        sourceElements.forEach((element) -> {
            element.getParent().getNodeDiffPair().getChangeMetric().forEach(parentMetric -> {
                if (parentMetric.getClosestElement().equals(element)) {
                    RelatedChange rc = new RelatedChange(parentMetric, RelationType.MENTIONED_DECLARATION, 0);
                    rc.setRelationDetail(parentMetric.getClosestElement().getFullName());
                    relatedChanges.add(rc);
                    alreadyIncluded.add(parentMetric);
                }
            });
        });

        diffs.forEach(fileDiff -> {

            fileDiff.getAllChangedPairs().forEach(pair -> {

                pair.getChangeMetric().forEach(metric -> {

                    if (alreadyIncluded.contains(metric)) {
                        return;
                    }
                    boolean related = false;
                    if (metric.getDiffType().equals(DiffType.REMOVE)) {

                        for (CodeRange leftSide : left) {
                            if (insidePosition(leftSide, metric)) {
                                related = true;
                            }
                        }

                    } else {
                        for (CodeRange rightSide : right) {
                            if (insidePosition(rightSide, metric)) {
                                related = true;
                            }
                        }
                    }

                    if (related) {
                        relatedChanges.add(new RelatedChange(metric, RelationType.MENTIONED, 0));
                    } else {
                        relatedChanges.add(new RelatedChange(metric, RelationType.NOT_RELATED, 99));                       
                    }

                });

            });

        });

        return relatedChanges;
    }

    protected boolean isMainMethod(String expectedName, TreeASTNode<?> children) {
        if (children instanceof MethodASTNode) {
            MethodASTNode mainMthdCandidate = (MethodASTNode) children;
            if (clearElementName(mainMthdCandidate.getFullName()).equals(expectedName)) {
                return true;
            }
        }

        return false;
    }

    // Col distance tolerance
    private boolean insidePosition(CodeRange side, ChangeMetric metric) {
        int et = 2;

        String metricClassPath = metric.getClosestElement().getPath();
        String sidePath = side.getFilePath();

        if (!metricClassPath.equals(sidePath)) {
            return false;
        }

        if (metric.getLine() >= side.getStartLine() && metric.getEndLine() <= side.getEndLine()) {
            if (metric.getLine() == side.getStartLine() && metric.getColumn() + et < side.getStartColumn()) {
                return false;
            } else if (metric.getEndLine() == side.getEndLine() && metric.getEndColumn() - et > side.getEndColumn()) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    protected void getRelatedChanges(List<RelatedChange> relatedChanges,
            List<TreeASTNode<? extends ASTNode>> sourceElements,
            List<TreeASTNode<? extends ASTNode>> targetElements) {

        List<RelatedChange> notRelated = relatedChanges.stream()
                .filter(change -> change.getRelationType().equals(RelationType.NOT_RELATED)).toList();

        metricsFor: for (RelatedChange rc : notRelated) {
            // STEP 2: Part of the declaration
            TreeASTNode closestElement = rc.getChangeMetric().getClosestElement();
            TreeASTNode parent = closestElement.getParent();
            while (parent != null) {
                if (parent.getNode() instanceof Block) {
                    break;
                }

                for (TreeASTNode sElement : sourceElements) {
                    if (parent.equals(sElement)) {
                        rc.setRelationType(RelationType.SIGNATURE);
                        rc.setDistance(1);
                        continue metricsFor;
                    }
                }

                for (TreeASTNode sElement : targetElements) {
                    if (parent.equals(sElement)) {
                        rc.setRelationType(RelationType.SIGNATURE);
                        rc.setDistance(1);
                        continue metricsFor;
                    }
                }

                parent = parent.getParent();
            }

            // STEP 3: call main methods
            for (TreeASTNode sElement : sourceElements) {
                checkIfCallMainMethod(sElement, closestElement, rc,
                        RelationType.CALL_SOURCE);
            }

            for (TreeASTNode tElement : targetElements) {
                checkIfCallMainMethod(tElement, closestElement, rc,
                        RelationType.CALL_TARGET);
            }

        }

        // STEP 4: use variables in common
        Map<String, Integer> usedBeforeVariables = new HashMap<>();
        Map<String, Integer> usedAfterVariables = new HashMap<>();
        relatedChanges.stream()
                .filter(change -> !change.getRelationType().equals(RelationType.NOT_RELATED) &&
                        !change.getRelationType().equals(RelationType.MENTIONED_DECLARATION))
                .forEach(rc -> {
                    if (rc.getChangeMetric().getDiffType().equals(DiffType.REMOVE)) {
                        rc.getChangeMetric().getUsedVariables()
                                .forEach(var -> usedBeforeVariables.put(var, rc.getDistance()));
                    } else {
                        rc.getChangeMetric().getUsedVariables()
                                .forEach(var -> usedAfterVariables.put(var, rc.getDistance()));
                    }
                });

        boolean hasnewrelated;
        do {
            hasnewrelated = false;

            notRelated = relatedChanges.stream()
                    .filter(change -> change.getRelationType().equals(RelationType.NOT_RELATED)).toList();
            Map<String, Integer> newBeforeVariables = new HashMap<>();
            Map<String, Integer> newAfterVariables = new HashMap<>();
            for (RelatedChange rc : notRelated) {
                Map<String, Integer> toUseVariable = usedAfterVariables;
                Map<String, Integer> newUsedVariable = newAfterVariables;
                if (rc.getChangeMetric().getDiffType().equals(DiffType.REMOVE)) {
                    toUseVariable = usedBeforeVariables;
                    newUsedVariable = newBeforeVariables;
                }

                String relatedVar = null;
                int distance = Integer.MAX_VALUE;
                for (String variableKeyString : toUseVariable.keySet()) {

                    if (rc.getChangeMetric().getUsedVariables().contains(variableKeyString)) {
                        if (distance > toUseVariable.get(variableKeyString)) {
                            relatedVar = variableKeyString;
                            distance = toUseVariable.get(variableKeyString);
                        }
                    }
                }

                if (relatedVar != null && !relatedVar.isBlank()) {
                    hasnewrelated = true;

                    rc.setRelationType(RelationType.VAR_RELATED);
                    rc.setDistance(distance + 1);
                    rc.setRelationDetail(relatedVar);
                    for (String usedVar : rc.getChangeMetric().getUsedVariables()) {
                        newUsedVariable.put(usedVar, rc.getDistance());
                    }
                }
            }
            usedBeforeVariables.putAll(newBeforeVariables);
            usedAfterVariables.putAll(newAfterVariables);
        } while (hasnewrelated);
    }

    private void checkIfCallMainMethod(TreeASTNode element, TreeASTNode closestElement, RelatedChange rc,
            RelationType rt) {
        if (!(element instanceof MethodASTNode)) {
            return;
        }

        MethodASTNode mainMethod = (MethodASTNode) element;
        if (checkCallableFrom(mainMethod, closestElement)) {
            rc.setRelationType(rt);
            rc.setDistance(1);
        }
        ;
        closestElement.visitAllChildren((TreeASTNode child, int generation) -> {
            if (checkCallableFrom(mainMethod, child)) {
                rc.setRelationType(rt);
                rc.setDistance(generation);
            }
            ;
        }, 1);
    }

    private boolean checkCallableFrom(MethodASTNode mainMethod, TreeASTNode element) {
        if (!(element instanceof MethodCallASTNode)) {
            return false;
        }
        MethodCallASTNode methodCall = (MethodCallASTNode) element;
        if (mainMethod.isCallableFrom(methodCall)) {
            return true;
        }

        return false;
    }

    protected String clearElementName(String elementName) {

        elementName = elementName.replaceAll("@\\w+\\([^)]*\\)\\s+", "") // Remove annotations
                .replaceAll("<([^<>]*((<[^<>]*>)+[^<>]*)*)>*", "") // Remove diamond operators
                .replaceAll("\\w+ +", "") // Remove everything that has an empty space after
                .replaceAll(" +", " "); // remove multile empty spaces

        int endOfMethodIndex = elementName.contains(" : ") ? elementName.indexOf(" : ") : elementName.length();
        String finalMethodName = elementName.substring(0, endOfMethodIndex);

        return finalMethodName;

    }

    public boolean hasToMerge() {
        return false;
    }

    public boolean mergeRefactoring(RefactoringInstance previousRef, RefactoringInstance newRef) {
        return false;
    }

}

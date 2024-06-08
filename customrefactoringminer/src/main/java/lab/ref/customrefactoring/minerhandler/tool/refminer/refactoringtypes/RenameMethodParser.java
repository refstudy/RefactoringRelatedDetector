package lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.FileDiff;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;
import org.refactoringminer.api.Refactoring;

import gr.uom.java.xmi.diff.CodeRange;
import lab.ref.customrefactoring.model.RefactoringInstance;
import lab.ref.customrefactoring.model.RelatedChange;

public class RenameMethodParser extends RefactoringParser {

    public RenameMethodParser(Refactoring ref) {
        super(RefactoringInstance.Type.RENAME_METHOD, ref);
    }

    @Override
    public ImmutablePair<List<TreeASTNode<? extends ASTNode>>, List<TreeASTNode<? extends ASTNode>>> buildInstance(
            List<FileDiff> diffs) throws RefMinerParseException {

        ArrayList<TreeASTNode<? extends ASTNode>> sourceElements = new ArrayList<>();
        ArrayList<TreeASTNode<? extends ASTNode>> targetElements = new ArrayList<>();
        String expectedSourceMethod = "";
        String expectedTargetMethod = "";
        String expectedSourceFilePath = "";
        String expectedTargetFilePath = "";
        String beforeClass = "";
        String afterClass = "";

        for (ImmutablePair<String, String> pair : ref.getInvolvedClassesBeforeRefactoring()) {
            beforeClass = pair.getRight();
        }
        for (ImmutablePair<String, String> pair : ref.getInvolvedClassesAfterRefactoring()) {
            afterClass = pair.getRight();
        }
        for (CodeRange leftSide : ref.leftSide()) {
            if (leftSide.getDescription().equals("original method declaration")) {
                String methodDeclaration = clearElementName(leftSide.getCodeElement());
                expectedSourceMethod = beforeClass + "." + methodDeclaration;
                expectedSourceFilePath = leftSide.getFilePath();
            }
        }

        for (CodeRange rightSide : ref.rightSide()) {
            if (rightSide.getDescription().equals("renamed method declaration")) {
                String methodDeclaration = clearElementName(rightSide.getCodeElement());
                expectedTargetMethod = afterClass + "." + methodDeclaration;
                expectedTargetFilePath = rightSide.getFilePath();

            }
        }

        for (FileDiff diff : diffs) {
            for (TreeASTNode<?> children : diff.getBeforeFileAst().getAllChildren()) {
                if (isMainMethod(expectedSourceMethod, children) && children.getPath().equals(expectedSourceFilePath)) {
                    sourceElements.add(children);
                }
            }

            for (TreeASTNode<?> children : diff.getAfterFileAst().getAllChildren()) {
                if (isMainMethod(expectedTargetMethod, children) && children.getPath().equals(expectedTargetFilePath)) {
                    targetElements.add(children);
                }
            }
        }

        // REFMINER
        if (beforeClass.contains(" ") || afterClass.contains(" ")) {
            System.out.println("Renamed Method ignored due to refminer output error");
            throw new RefMinerParseException("RefMinerParserError: Wrong expected main element");
        }
        return new ImmutablePair<List<TreeASTNode<? extends ASTNode>>, List<TreeASTNode<? extends ASTNode>>>(
                sourceElements, targetElements);

    }

    @Override
    public void removeExtraSideItem(List<CodeRange> sideArray) {
    }

    @Override
    protected void posBuild(RefactoringInstance rInstance) {

        List<RelatedChange> toRemoveList = new ArrayList<>();
        String bName = rInstance.getSourceElements().get(0).getFullName();
        String aName = rInstance.getTargetElements().get(0).getFullName();
        rInstance.getRelatedChanges().stream().forEach(related -> {
            rInstance.getRelatedChanges().forEach(related2 -> {
                if (isOppositeFrom(related, related2, bName, aName)) {
                    toRemoveList.add(related);
                    toRemoveList.add(related2);
                }
            });

        });
        List<RelatedChange> filteredList = new ArrayList<>();
        filteredList = rInstance.getRelatedChanges().stream().filter(related -> !toRemoveList.contains(related))
                .toList();
        rInstance.setRelatedChanges(new ArrayList<>(filteredList));
    }

    public boolean isOppositeFrom(RelatedChange bChange, RelatedChange aChange, String bParent, String aParent) {
        if (!(bChange.getChangeMetric().getDiffType().equals(DiffType.REMOVE) &&
                aChange.getChangeMetric().getDiffType().equals(DiffType.INSERT))) {
            return false;
        }

        TreeASTNode bParentNode = bChange.getChangeMetric().getParent();
        TreeASTNode aParentNode = aChange.getChangeMetric().getParent();
        if (bParentNode == null || aParentNode == null) {
            return false;
        }
        String bFullname = bParentNode.getFullName();
        String aFullname = aParentNode.getFullName();
        if (bFullname.startsWith(bParent) &&
                aFullname.startsWith(aParent)) {

            String bFullNameSuffix = bFullname.substring(bParent.length());
            String aFullNameSuffix = aFullname.substring(aParent.length());

            if (!bFullNameSuffix.equals(aFullNameSuffix)) {
                return false;
            }

        } else {
            return false;
        }

        return bChange.getDistance() == aChange.getDistance() &&
                bChange.getChangeMetric()
                        .getClosestElement()
                        .toString()
                        .equals(aChange.getChangeMetric().getClosestElement().toString());
    }

}

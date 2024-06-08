package lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.FileDiff;
import org.refactoringminer.api.Refactoring;

import gr.uom.java.xmi.diff.CodeRange;
import lab.ref.customrefactoring.model.RefactoringInstance;

public class PullUpMethodParser extends RefactoringParser {

    public PullUpMethodParser(Refactoring ref) {
        super(RefactoringInstance.Type.PULL_UP, ref);
    }

    @Override
    public ImmutablePair<List<TreeASTNode<? extends ASTNode>>, List<TreeASTNode<? extends ASTNode>>> buildInstance(
            List<FileDiff> diffs) {

        ArrayList<TreeASTNode<? extends ASTNode>> sourceElements = new ArrayList<>();
        ArrayList<TreeASTNode<? extends ASTNode>> targetElements = new ArrayList<>();
        String expectedSourceMethod = "";
        String expectedTargetMethod = "";
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
            }
        }

        for (CodeRange rightSide : ref.rightSide()) {
            if (rightSide.getDescription().equals("pulled up method declaration")) {
                String methodDeclaration = clearElementName(rightSide.getCodeElement());
                expectedTargetMethod = afterClass + "." + methodDeclaration;

            }
        }

        for (FileDiff diff : diffs) {
            for (TreeASTNode<?> children : diff.getBeforeFileAst().getAllChildren()) {
                if (isMainMethod(expectedSourceMethod, children)) {
                    sourceElements.add(children);
                }
            }

            for (TreeASTNode<?> children : diff.getAfterFileAst().getAllChildren()) {
                if (isMainMethod(expectedTargetMethod, children)) {
                    targetElements.add(children);
                }
            }

        }

        return new ImmutablePair<List<TreeASTNode<? extends ASTNode>>, List<TreeASTNode<? extends ASTNode>>>(
                sourceElements, targetElements);

    }

    @Override
    public void removeExtraSideItem(List<CodeRange> sideArray) {
    }

    @Override
    public boolean hasToMerge() {
        return true;
    }

    @Override
    public boolean mergeRefactoring(RefactoringInstance previousRef, RefactoringInstance newRef) {
        return false;
    }

}

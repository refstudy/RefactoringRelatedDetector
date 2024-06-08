package lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.FileDiff;
import org.refactoringminer.api.Refactoring;

import gr.uom.java.xmi.diff.CodeRange;
import lab.ref.customrefactoring.model.RefactoringInstance;

public class InlineMethodParser extends RefactoringParser {

    public InlineMethodParser(Refactoring refactoring) {
        super(RefactoringInstance.Type.INLINE_METHOD, refactoring);
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
            if (leftSide.getDescription().equals("inlined method declaration")) {
                String methodDeclaration = clearElementName(leftSide.getCodeElement());
                expectedSourceMethod = beforeClass + "." + methodDeclaration;
                expectedSourceFilePath = leftSide.getFilePath();

            }
        }

        for (CodeRange rightSide : ref.rightSide()) {
            if (rightSide.getDescription().equals("target method declaration after inline")) {
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

         // REFMINER fails to expose the class
        if (beforeClass.contains(" ") || afterClass.contains(" ")) {
            System.out.println("Inlined Method ignored due to refminer output error");
            throw new RefMinerParseException("RefMinerParserError: Wrong expected main element");
        }

        return new ImmutablePair<List<TreeASTNode<? extends ASTNode>>, List<TreeASTNode<? extends ASTNode>>>(
                sourceElements, targetElements);

    }

    @Override
    public void removeExtraSideItem(List<CodeRange> sideArray) {
        Iterator<CodeRange> iterator = sideArray.iterator();
        List<String> allowed = new ArrayList<>();
        allowed.add("inlined code from inlined method declaration");
        allowed.add("inlined method invocation");
        allowed.add("deleted statement in inlined method declaration");
        allowed.add("inlined code in target method declaration");
        while (iterator.hasNext()) {
            CodeRange jsonElement = iterator.next();
            if (!allowed.contains(jsonElement.getDescription())) {
                iterator.remove();
            } else {
                // System.out.println(jsonElement);
            }

        }

    }

}

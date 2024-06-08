package org.metricsminer.metric.changemetricsgenerator.elementbased.statements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;
import org.metricsminer.metric.changemetricsgenerator.ChangeMetricGenerator;
import org.metricsminer.model.diff.NodeDiffPair;

public class StatementChangeMetric extends ChangeMetricGenerator<Statement> {

    ArrayList<Class> forbiddenClasses = new ArrayList();

    public StatementChangeMetric() {
        forbiddenClasses.add(Block.class);
        forbiddenClasses.add(IfStatement.class);
        forbiddenClasses.add(ReturnStatement.class);
        forbiddenClasses.add(VariableDeclarationStatement.class);
        forbiddenClasses.add(TypeDeclarationStatement.class);
        forbiddenClasses.add(EmptyStatement.class);

    }

    public List<ChangeMetric> insertAction(NodeDiffPair diffPair, Statement inserted) {
        ArrayList<ChangeMetric> changeMetrics = new ArrayList<>();
      
        if (!isValidStatement(inserted)) {
            return changeMetrics;
        }

        String nodeName = diffPair.getAfter().getName().toUpperCase();
        ChangeMetric cm = buildChangeMetric("ADDED_" + nodeName, "A new statement has been added",
        inserted);
        changeMetrics.add(cm);
        return changeMetrics;
    }

    public List<ChangeMetric> removeAction(NodeDiffPair diffPair, Statement removed) {
        ArrayList<ChangeMetric> changeMetrics = new ArrayList<>();
        if (!isValidStatement(removed)) {
            return changeMetrics;
        }
        
        String nodeName = diffPair.getBefore().getName().toUpperCase();
        ChangeMetric cm = buildChangeMetric("REMOVED_" + nodeName, "A statement has been removed",
                diffPair.getBefore().getNode().toString(), ChangeMetricGenerator.getFirstValidParent(diffPair, true));
        changeMetrics.add(cm);
        return changeMetrics;
    }

    public List<ChangeMetric> pairAction(NodeDiffPair diffPair, Statement inserted, Statement removed) {
        ArrayList<ChangeMetric> changeMetrics = new ArrayList<>();

        if (!isValidStatement(inserted)) {
            return changeMetrics;
        }
        String beforeCode = diffPair.getBefore().getNode().toString();
        String afterCode = diffPair.getAfter().getNode().toString();
        if (!beforeCode.equals(afterCode)) {
            HashMap<String, String> hash = new HashMap<>();
            String nodeName = diffPair.getBefore().getName().toUpperCase();
            hash.put("before", beforeCode);
            hash.put("after", afterCode);
            ChangeMetric cm = buildChangeMetric("CHANGED_" + nodeName, "A statement has been changed",
                    hash, diffPair);
            changeMetrics.add(cm);
        }

        return changeMetrics;
    }


    private boolean isValidStatement(Statement inserted){
        if (forbiddenClasses.contains(inserted.getClass())) {
            return false;
        }

        if(inserted instanceof ExpressionStatement){
            ExpressionStatement expression = (ExpressionStatement) inserted;
            if(expression.getExpression() instanceof MethodInvocation){
                return false;
            }
        }

        return true;
    }

}

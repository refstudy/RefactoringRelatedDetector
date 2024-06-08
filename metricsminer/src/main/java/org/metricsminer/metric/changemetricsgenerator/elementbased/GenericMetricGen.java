package org.metricsminer.metric.changemetricsgenerator.elementbased;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.metricsminer.metric.changemetricsgenerator.elementbased.abstraction.BasicElementChangedMetricsGen;
import org.metricsminer.model.astnodes.TreeASTNode;

public class GenericMetricGen extends BasicElementChangedMetricsGen<TreeASTNode> {

    ArrayList<Class> forbiddenClasses = new ArrayList();

    public GenericMetricGen() {
        forbiddenClasses.add(CompilationUnit.class);
        forbiddenClasses.add(Statement.class);
        forbiddenClasses.add(NumberLiteral.class);
        forbiddenClasses.add(SimpleName.class);
        forbiddenClasses.add(QualifiedName.class);
        forbiddenClasses.add(TypeDeclaration.class);
        forbiddenClasses.add(MethodInvocation.class);
        forbiddenClasses.add(VariableDeclaration.class);
        forbiddenClasses.add(SimpleType.class);
        forbiddenClasses.add(PrimitiveType.class);
        forbiddenClasses.add(StringLiteral.class);
        forbiddenClasses.add(MethodDeclaration.class);
        forbiddenClasses.add(Assignment.class);
        forbiddenClasses.add(Modifier.class);
    }

    @Override
    protected ElementMetricChanged getElementMetricChanged(TreeASTNode element) {
        for (Class<?> clazz : this.forbiddenClasses) {
            if (clazz.isAssignableFrom(element.getNode().getClass())) {
                return null;
            }
            
        }

        return new ElementMetricChanged(element.getNodeType(), element.getNodeType(), element.getNode().toString());
    }
}

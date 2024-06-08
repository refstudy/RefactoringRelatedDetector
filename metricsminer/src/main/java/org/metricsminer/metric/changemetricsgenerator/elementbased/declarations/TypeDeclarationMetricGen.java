package org.metricsminer.metric.changemetricsgenerator.elementbased.declarations;

import org.metricsminer.metric.changemetricsgenerator.elementbased.abstraction.BasicElementChangedMetricsGen;
import org.metricsminer.model.astnodes.AbstractStructureASTNode;
import org.metricsminer.model.astnodes.ClassASTNode;
import org.metricsminer.model.astnodes.EnumAstNode;
import org.metricsminer.model.astnodes.RecordASTNode;

@SuppressWarnings("rawtypes")
public class TypeDeclarationMetricGen extends BasicElementChangedMetricsGen<AbstractStructureASTNode> {

    public TypeDeclarationMetricGen() {
    }

    protected ElementMetricChanged getElementMetricChanged(AbstractStructureASTNode abstractStructureASTNode) {
        if (abstractStructureASTNode instanceof ClassASTNode) {
            ClassASTNode classNode = (ClassASTNode) abstractStructureASTNode;
            return new ElementMetricChanged(
                    classNode.isInterface() ? "interfaces" : "classes",
                    classNode.isInterface() ? "INTERFACE" : "CLASS",
                    classNode.getName());
        } else if (abstractStructureASTNode instanceof EnumAstNode) {
            EnumAstNode enumNode = (EnumAstNode) abstractStructureASTNode;
            return new ElementMetricChanged(
                    "enums",
                    "ENUM",
                    enumNode.getName());
        } else if (abstractStructureASTNode instanceof RecordASTNode) {
            RecordASTNode recordNode = (RecordASTNode) abstractStructureASTNode;
            return new ElementMetricChanged(
                    "records",
                    "RECORD",
                    recordNode.getName());
        }
        return null;
    }

}

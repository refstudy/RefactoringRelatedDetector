package org.metricsminer.metric.changemetricsgenerator.elementbased.imports;

import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.metricsminer.metric.changemetricsgenerator.elementbased.abstraction.BasicElementChangedMetricsGen;

public class ImportChangedMetricGen extends BasicElementChangedMetricsGen<ImportDeclaration> {

    protected ElementMetricChanged getElementMetricChanged(ImportDeclaration importDeclaration) {
        return new ElementMetricChanged("imports", "IMPORT", importDeclaration);
    }

}

package org.metricsminer.model.astnodes;

import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Modifier;

public interface StructureModifier {

    public List<Modifier> getModifiers();
    public List<Annotation>  getAnnotations();

}

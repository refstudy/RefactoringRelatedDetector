package org.metricsminer.model.astnodes;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public abstract class AbstractDeclarationASTNode<T extends BodyDeclaration> extends TreeASTNode<T>
        implements StructureModifier {

    public AbstractDeclarationASTNode(T astNode) {
        super(astNode);
    }

    public List<Modifier> getModifiers() {
        return super.getNode().modifiers().stream().filter(modifier -> modifier instanceof Modifier).toList();
    }

    public List<Annotation> getAnnotations() {
        return super.getNode().modifiers().stream().filter(modifier -> modifier instanceof Annotation).toList();
    }

    public String getAccessModifier() {

        List<String> possibleAccessModifiers = Arrays.asList("public", "protected", "private");
        List accessModifiers = getModifiers().stream()
                .filter(modifier -> possibleAccessModifiers.contains(modifier.toString())).toList();
        if (!accessModifiers.isEmpty()) {
            return accessModifiers.get(0).toString();
        } else {
            return "default";
        }
    }

    public abstract List<VariableDeclarationFragment> getVariables();

}

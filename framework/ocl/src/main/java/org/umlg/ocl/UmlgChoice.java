package org.umlg.ocl;

import org.eclipse.ocl.helper.Choice;
import org.eclipse.ocl.helper.ChoiceKind;

/**
 * Date: 2014/03/07
 * Time: 3:39 PM
 */
public class UmlgChoice implements Choice {

    private ChoiceKind choiceKind;
    private String name;
    private String description;
    private Object element;

    public UmlgChoice(ChoiceKind choiceKind, String name, String description, Object element) {
        this.choiceKind = choiceKind;
        this.name = name;
        this.description = description;
        this.element = element;
    }

    public ChoiceKind getKind() {
        return this.choiceKind;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Object getElement() {
        return this.element;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Choice["); //$NON-NLS-1$
        result.append(getKind().name());
        result.append(", "); //$NON-NLS-1$
        result.append(getName());
        result.append(", "); //$NON-NLS-1$
        result.append(getDescription());
        result.append(']');

        return result.toString();
    }
}

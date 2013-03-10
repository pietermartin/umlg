package org.tuml.javageneration.util;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * Date: 2013/03/09
 * Time: 3:09 PM
 */
public class ConstraintWrapper {

    private Constraint constraint;

    public ConstraintWrapper(Constraint constraint) {
        this.constraint = constraint;
    }

    public String getName() {
        return this.constraint.getName();
    }

    public String getConstraintOclAsString() {
        Namespace context = this.constraint.getContext();
        ValueSpecification valueSpecification = this.constraint.getSpecification();
        StringBuilder sb = new StringBuilder();
        sb.append("package ");
        sb.append(Namer.nameIncludingModel(context.getNearestPackage()).replace(".", "::"));
        sb.append("\n    context ");
        sb.append(context.getName());
        sb.append(" inv:");
        sb.append("\n        ");
        sb.append(valueSpecification.stringValue());
        sb.append("\n");
        sb.append("endpackage");
        return sb.toString();
    }

}

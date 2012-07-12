package org.tuml.javageneration.ocl;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.tuml.javageneration.ocl.visitor.ToTumlJavaVisitor;

public class TumlOcl2Java {

    public static String getCollectionInterface(MultiplicityElement multiplicityElement) {
        if (multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
            return "OrderedSet";
        } else if (multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
            return "Set";
        } else if (!multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
            return "Bag";
        } else if (!multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
            return "Sequence";
        } else {
            throw new IllegalStateException("Not supported");
        }
    }

    public static String oclToJava(OCLExpression<Classifier> expr) {
        return expr.accept(ToTumlJavaVisitor.getInstance(expr));
    }
}

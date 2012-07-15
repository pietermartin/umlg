package org.tuml.javageneration.ocl;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.javageneration.ocl.visitor.Tuml2JavaVisitor;

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

    /**
     * 
     * @param ojClass this is passed in in order to make ensure the import statements are correct.
     * @param expr The ocl expression thats being visited
     * @return java code
     */
    public static String oclToJava(OJAnnotatedClass ojClass, OCLExpression<Classifier> expr) {
        return expr.accept(Tuml2JavaVisitor.getInstance(ojClass, expr));
    }
}

package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.uml.PropertyCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.visitor.HandleIteratorExp;

import java.util.List;

public class OclSortedByExpToJava implements HandleIteratorExp {

    /**
     * Generates something like below
     */
    public String handleIteratorExp(OJAnnotatedClass ojClass, IteratorExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String bodyResult) {
        if (variableResults.size() != 1) {
            throw new IllegalStateException("An ocl select iterator expression may only have on iterator, i.e. variable");
        }

        PropertyCallExp ocl = (PropertyCallExp) (callExp.getBody());
        Property p = ocl.getReferredProperty();
        StringBuilder result = new StringBuilder(sourceResult);
        result.append(".exists(");
//        result.append(variable.getName());
        result.append(" -> ");
        result.append(bodyResult);
        result.append(")");
        return result.toString();
    }

}

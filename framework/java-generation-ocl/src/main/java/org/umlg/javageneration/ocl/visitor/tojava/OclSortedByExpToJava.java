package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.uml.PropertyCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.visitor.HandleIteratorExp;
import org.umlg.javageneration.util.PropertyWrapper;

import java.util.List;

public class OclSortedByExpToJava implements HandleIteratorExp {

    /**
     * Generates something like below
     * .sortedBy((a, b) -> a.getSortBy().compareTo(b.getSortBy())
     */
    public String handleIteratorExp(OJAnnotatedClass ojClass, IteratorExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String bodyResult) {
        if (variableResults.size() != 1) {
            throw new IllegalStateException("An ocl select iterator expression may only have on iterator, i.e. variable");
        }

        PropertyCallExp ocl = (PropertyCallExp) (callExp.getBody());
        Property p = ocl.getReferredProperty();
        PropertyWrapper pWrap = new PropertyWrapper(p);
        StringBuilder result = new StringBuilder(sourceResult);
        result.append(".sortedBy((a, b) -> a.");
        result.append(pWrap.getter());
        result.append("().compareTo(b.");
        result.append(pWrap.getter());
        result.append("())");
        result.append(")");
        return result.toString();
    }

}

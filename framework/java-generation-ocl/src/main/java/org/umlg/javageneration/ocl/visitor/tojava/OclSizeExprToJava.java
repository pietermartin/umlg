package org.umlg.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.uml.PropertyCallExp;
import org.eclipse.ocl.uml.impl.PropertyCallExpImpl;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.umlg.javageneration.util.UmlgPropertyOperations;

public class OclSizeExprToJava extends BaseHandleOperationExp {

	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		StringBuilder result = new StringBuilder();
		result.append(sourceResult);
        OCLExpression<?> oclExpression = oc.getSource();
        if (oclExpression instanceof PropertyCallExp) {
            PropertyCallExpImpl propertyCallExp = (PropertyCallExpImpl)oclExpression;
            if (UmlgPropertyOperations.isString(propertyCallExp.getReferredProperty().getType())) {
                result.append(".length()");
            } else {
                result.append(".size()");
            }
        } else {
		    result.append(".size()");
        }
		return result.toString();
	}

}

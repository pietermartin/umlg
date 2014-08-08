package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;

import java.util.List;

/**
 * Date: 2013/03/09
 * Time: 3:44 PM
 */
public class OclAndExprToJava extends BaseHandleOperationExp {

    @Override
    public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
        if (argumentResults.size() != 1) {
            throw new IllegalStateException("Ocl 'and' operation call must have 1 argument!");
        }
        String arg = argumentResults.get(0);
        StringBuilder result = new StringBuilder();
        result.append(sourceResult);
        result.append(" && ");
        result.append(arg);
        return result.toString();
    }

}

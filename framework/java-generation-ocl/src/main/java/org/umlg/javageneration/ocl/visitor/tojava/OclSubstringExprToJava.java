package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;

import java.util.List;

/**
 * Date: 2013/03/09
 * Time: 3:44 PM
 */
public class OclSubstringExprToJava extends BaseHandleOperationExp {

    @Override
    public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
        StringBuilder result = new StringBuilder();
        result.append(sourceResult);
        result.append(".substring(");
        int count = 0;
        for (String arg : argumentResults) {
            count++;
            result.append(arg);
            if (count < argumentResults.size()) {
                result.append(",");
            }
        }
        result.append(")");
        return result.toString();
    }

}

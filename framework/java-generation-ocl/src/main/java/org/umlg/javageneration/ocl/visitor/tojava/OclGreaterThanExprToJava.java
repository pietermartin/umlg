package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.uml.PrimitiveType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Operation;

import java.util.List;

public class OclGreaterThanExprToJava extends BaseHandleOperationExp {

    @Override
    public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
        if (argumentResults.size() != 1) {
            throw new IllegalStateException("The greater than operation must have one and only one argument!");
        }
        StringBuilder result = new StringBuilder();
        result.append(sourceResult);
        DataType datatype = oc.getReferredOperation().getDatatype();
        if (datatype instanceof PrimitiveType) {
            PrimitiveType primitiveType = (PrimitiveType) datatype;
            if (primitiveType.getName().equals("Integer") || primitiveType.getName().equals("Real")) {
                result.append(" > ");
                result.append(argumentResults.get(0));
            } else {
                throw new IllegalStateException("Greater than operation can only be called on a PrimitiveType Integer and Real " + datatype.getName());
            }
        } else {
            throw new IllegalStateException("Greater than operation can only be called on a PrimitiveType " + datatype.getName());
        }
        return result.toString();
    }
}
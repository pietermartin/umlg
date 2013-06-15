package org.umlg.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.uml.PrimitiveType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Operation;

public class OclEqualExprToJava extends BaseHandleOperationExp {

    @Override
    public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
        if (argumentResults.size() != 1) {
            throw new IllegalStateException("The equals operation must have one and only one argument!");
        }
        StringBuilder result = new StringBuilder();
        result.append(sourceResult);
        DataType datatype = oc.getReferredOperation().getDatatype();
        if (datatype instanceof PrimitiveType) {
            PrimitiveType primitiveType = (PrimitiveType) datatype;
            if (primitiveType.getName().equals("Integer") || primitiveType.getName().equals("Real") || primitiveType.getName().equals("Boolean")) {
                result.append(" == ");
                result.append(argumentResults.get(0));
            } else if (primitiveType.getName().equals("String")) {
                result.append(".equals(");
                result.append(argumentResults.get(0));
                result.append(")");
            } else {
                throw new RuntimeException("Unhandled primitive " + primitiveType.getName());
            }
        } else {
            result.append(".equals(");
            result.append(argumentResults.get(0));
            result.append(")");
        }
        return result.toString();
    }
}
package org.umlg.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.uml.PrimitiveType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Operation;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgPropertyOperations;

public class OclEqualExprToJava extends BaseHandleOperationExp {

    @Override
    public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
        if (argumentResults.size() != 1) {
            throw new IllegalStateException("The equals operation must have one and only one argument!");
        }
        StringBuilder result = new StringBuilder();
        result.append(sourceResult);
        DataType datatype = oc.getReferredOperation().getDatatype();
        String argResult = argumentResults.get(0);
        if (datatype instanceof PrimitiveType) {
            PrimitiveType primitiveType = (PrimitiveType) datatype;
            if (primitiveType.getName().equals("Integer") || primitiveType.getName().equals("Real") || primitiveType.getName().equals("Boolean")) {
                result.append(" == ");
                result.append(argResult);
            } else if (primitiveType.getName().equals("String")) {
                result.append(".equals(");
                result.append(argResult);
                result.append(")");
            } else {
                throw new RuntimeException("Unhandled primitive " + primitiveType.getName());
            }
        } else {
            result.append(".equals(");
            if (argResult.equals("self")) {
                result.append("this");
            } else {
                result.append(argResult);
            }
            result.append(")");
        }
        return result.toString();
    }
}
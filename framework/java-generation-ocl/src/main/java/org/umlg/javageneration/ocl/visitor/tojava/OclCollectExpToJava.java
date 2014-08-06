package org.umlg.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.ocl.uml.impl.CollectionTypeImpl;
import org.eclipse.uml2.uml.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.util.UmlgOclUtil;
import org.umlg.javageneration.ocl.visitor.HandleIteratorExp;
import org.umlg.javageneration.util.DataTypeEnum;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgCollectionKindEnum;

public class OclCollectExpToJava implements HandleIteratorExp {

	/**
	 * Generates something like below
	 * 
	 * return getSet().select(
	 * 		new BooleanBodyExpressionEvaluator<OclTestCollection>() {
	 * 			@Override
	 * 			public Boolean evaluate(OclTestCollection e) {
	 * 				return getName().equals("john");
	 * 			});
	 * 		}
	 */
	public String handleIteratorExp(OJAnnotatedClass ojClass, IteratorExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String bodyResult) {
		if (variableResults.size() != 1) {
			throw new IllegalStateException("An ocl select iterator expression may only have on iterator, i.e. variable");
		}
		
		Variable<Classifier, Parameter> variable = callExp.getIterator().get(0);
		String variableType = UmlgClassOperations.className(variable.getType());
		
		OCLExpression<Classifier> body = callExp.getBody();
		String bodyType = UmlgClassOperations.className(body.getType());
        if (body.getType() instanceof CollectionType) {
            CollectionType collectionType = (CollectionType)body.getType();
            ojClass.addToImports(UmlgCollectionKindEnum.from(collectionType.getKind()).getOjPathName());
        }

        String flattenedType;
		Classifier c = body.getType();
		if (c instanceof CollectionTypeImpl) {
            CollectionTypeImpl collectionTypeImpl = (CollectionTypeImpl) c;

            Classifier elementType = collectionTypeImpl.getElementType();
            if (!(elementType instanceof PrimitiveType) && !(elementType instanceof Enumeration) && elementType instanceof DataType) {
                flattenedType = DataTypeEnum.getPathNameFromDataType((DataType) elementType).getLast();
            } else {
                flattenedType = UmlgClassOperations.className(elementType);
            }

		} else {
            if (!(c instanceof PrimitiveType) && !(c instanceof Enumeration) && c instanceof DataType) {
                flattenedType = DataTypeEnum.getPathNameFromDataType((DataType) c).getLast();
            } else {
                flattenedType = UmlgClassOperations.className(c);
            }
		}
		
		StringBuilder result = new StringBuilder(sourceResult);
		result.append(".<");
		result.append(flattenedType);
		result.append(", ");
		result.append(bodyType);
		result.append(">collect(");
		result.append("new BodyExpressionEvaluator<");
		result.append(bodyType);
		result.append(", ");
		result.append(variableType);
		result.append(">() {\n");
		result.append("    @Override\n");
		result.append("    public ");
		result.append(bodyType);
		result.append(" evaluate(");
		result.append(UmlgOclUtil.removeVariableInit(variableResults.get(0)));
		result.append(") {\n");
		result.append("        return ");
		result.append(bodyResult);
		result.append(";\n    }");
		result.append("\n})");
		return result.toString();
	}
	
}

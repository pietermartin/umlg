package org.tuml.javageneration.ocl.visitor;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.utilities.PredefinedType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.tuml.javageneration.ocl.visitor.java.OclAsBagExprToJava;
import org.tuml.javageneration.ocl.visitor.java.OclAsOrderedSetExprToJava;
import org.tuml.javageneration.ocl.visitor.java.OclAsSequenceExprToJava;
import org.tuml.javageneration.ocl.visitor.java.OclAsSetExprToJava;
import org.tuml.javageneration.ocl.visitor.java.OclDefaultToStringExprToJava;
import org.tuml.javageneration.ocl.visitor.java.OclEqualExprToJava;
import org.tuml.javageneration.ocl.visitor.java.OclFlattenExprToJava;
import org.tuml.javageneration.ocl.visitor.java.OclNotEqualExprToJava;

public enum OclOperationExpEnum implements HandleOperationExp {

	EQUAL(new OclEqualExprToJava()), NOT_EQUAL(new OclNotEqualExprToJava()), AS_SET(new OclAsSetExprToJava()), AS_SEQUENCE(new OclAsSequenceExprToJava()), AS_ORDERED_SET(
			new OclAsOrderedSetExprToJava()), AS_BAG(new OclAsBagExprToJava()), FLATTEN(new OclFlattenExprToJava()), DEFAULT(new OclDefaultToStringExprToJava());
	private static Logger logger = Logger.getLogger(OclOperationExpEnum.class.getPackage().getName());
	private HandleOperationExp implementor;

	private OclOperationExpEnum(HandleOperationExp implementor) {
		this.implementor = implementor;
	}

	public static OclOperationExpEnum from(String name) {
		if (name.equals(PredefinedType.EQUAL_NAME)) {
			return EQUAL;
		} else if (name.equals(PredefinedType.NOT_EQUAL_NAME)) {
			return NOT_EQUAL;
		} else if (name.equals(PredefinedType.AS_SET_NAME)) {
			return AS_SET;
		} else if (name.equals(PredefinedType.AS_SEQUENCE_NAME)) {
			return AS_SEQUENCE;
		} else if (name.equals(PredefinedType.AS_ORDERED_SET_NAME)) {
			return AS_ORDERED_SET;
		} else if (name.equals(PredefinedType.AS_BAG_NAME)) {
			return AS_BAG;
		} else if (name.equals(PredefinedType.FLATTEN_NAME)) {
			return FLATTEN;
		} else {
			logger.warning(String.format("Not yet implemented, '%s'", name));
			return DEFAULT;
		}
	}

	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		return implementor.handleOperationExp(oc, sourceResult, argumentResults);
	}
}

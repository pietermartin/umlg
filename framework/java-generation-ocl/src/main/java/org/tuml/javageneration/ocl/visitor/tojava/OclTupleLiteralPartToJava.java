package org.tuml.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.TupleLiteralPart;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.javageneration.ocl.visitor.HandleTupleLiteralPart;
import org.tuml.javageneration.util.TumlClassOperations;

public class OclTupleLiteralPartToJava implements HandleTupleLiteralPart {

	protected OJAnnotatedClass ojClass;


	@Override
	public HandleTupleLiteralPart setOJClass(OJAnnotatedClass ojClass) {
		this.ojClass = ojClass;
		return this;
	}


	@Override
	public String handleTupleLiteralPart(TupleLiteralPart<Classifier, Property> part, String valueResult) {
		String varName = part.getName();
//		Classifier type = part.getType();

		StringBuilder result = new StringBuilder();

		result.append(varName);

//		if (type != null) {
//			
//			result.append(" : ").append(TumlClassOperations.className(type) /*getName(type)*/);//$NON-NLS-1$
//		}

		if (valueResult != null) {
			result.append("::").append(valueResult);//$NON-NLS-1$
		}

		return result.toString();
//		return null;
	}


}

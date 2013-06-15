package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.TupleLiteralPart;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.visitor.HandleTupleLiteralPart;

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

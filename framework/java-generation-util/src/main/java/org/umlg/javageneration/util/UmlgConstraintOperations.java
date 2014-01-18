package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.internal.operations.ConstraintOperations;

public class UmlgConstraintOperations extends ConstraintOperations {

	public static String getAsOcl(Constraint constraint) {
		Operation oper = (Operation) constraint.getContext();
		StringBuilder sb = new StringBuilder();
		sb.append("package ");
		sb.append(Namer.nameIncludingModel(oper.getNearestPackage()).replace(".", "::"));
		sb.append("\ncontext ");
		sb.append(UmlgOperationOperations.asOclSignature(oper));
		sb.append("\n");
		sb.append("body: ");
		sb.append(constraint.getSpecification().stringValue());
		sb.append("\n");
		sb.append("endpackage");
		return sb.toString();
	}
	
}

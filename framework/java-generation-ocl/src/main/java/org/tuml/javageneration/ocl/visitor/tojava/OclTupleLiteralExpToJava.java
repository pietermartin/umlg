package org.tuml.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.TupleLiteralExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.javageneration.ocl.visitor.HandleTupleLiteralExp;

public class OclTupleLiteralExpToJava implements HandleTupleLiteralExp {

	protected OJAnnotatedClass ojClass;

	@Override
	public HandleTupleLiteralExp setOJClass(OJAnnotatedClass ojClass) {
		this.ojClass = ojClass;
		return this;
	}

	@Override
	public String handleTupleLiteralExp(TupleLiteralExp<Classifier, Property> literalExp, List<String> partResults) {
		// // construct the appropriate collection from the parts
		// // based on the collection kind.
		// StringBuilder result = new StringBuilder();
		//		result.append("Tuple{");//$NON-NLS-1$
		//
		// for (Iterator<String> iter = partResults.iterator(); iter.hasNext();)
		// {
		// result.append(iter.next());
		//
		// if (iter.hasNext()) {
		//				result.append(", ");//$NON-NLS-1$
		// }
		// }
		//
		// result.append('}');
		//
		// return result.toString();

		StringBuilder sb = new StringBuilder();
		sb.append("new HashMap<String, Object>();\n");
		int count = 1;
		for (String partResult : partResults) {
			String[] keyValue = partResult.split("::");
			String key = keyValue[0];
			String value = keyValue[1];
			if (value.equals("self")) {
				value = "this";
			}
			sb.append("result.put(\"" + key + "\", " + value + ");");
			if (count++ != partResults.size()) {
				sb.append("\n");	
			}
		}
//		sb.append("\nresult = tuple");
		return sb.toString();
	}
}

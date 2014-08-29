package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.util.UmlgOclUtil;
import org.umlg.javageneration.ocl.visitor.HandleIteratorExp;
import org.umlg.javageneration.util.UmlgClassOperations;

import java.util.List;

public class OclIsUniqueExpToJava implements HandleIteratorExp {

	/**
	 * Generates something like below
	 *
     * if ( (getOclIsUnique2().isUnique(temp1 -> temp1.getOclIsUnique3())) == false ) {
     *     result.add(new UmlgConstraintViolation("Constraint", "umlgtest::org::umlg::ocl::ocloperator::OclIsUnique1", "ocl\npackage umlgtest::org::umlg::ocl::ocloperator\n    context OclIsUnique1 inv:\n        self.oclIsUnique2->isUnique(oclIsUnique3)\nendpackage\nfails!"));
     * }
	 */
	public String handleIteratorExp(OJAnnotatedClass ojClass, IteratorExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String bodyResult) {
		if (variableResults.size() != 1) {
			throw new IllegalStateException("An ocl select iterator expression may only have on iterator, i.e. variable");
		}
		Variable<Classifier, Parameter> variable = callExp.getIterator().get(0);
		StringBuilder result = new StringBuilder(sourceResult);
		result.append(".isUnique(");
        result.append(variable.getName());
        result.append(" -> ");
        result.append(bodyResult);
        result.append(")");
		return result.toString();
	}
	
}

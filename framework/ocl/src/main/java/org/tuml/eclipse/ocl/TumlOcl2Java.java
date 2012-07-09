package org.tuml.eclipse.ocl;

import java.io.File;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.OCLInput;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.SemanticException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.uml.ExpressionInOCL;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.SendSignalAction;
import org.eclipse.uml2.uml.State;
import org.tuml.framework.ModelLoader;

public class TumlOcl2Java {

	protected OCL<Package, Classifier, Operation, Property, EnumerationLiteral, Parameter, State, CallOperationAction, SendSignalAction, Constraint, Class, EObject> ocl;
	protected Environment<Package, Classifier, Operation, Property, EnumerationLiteral, Parameter, State, CallOperationAction, SendSignalAction, Constraint, Class, EObject> environment;
	protected OCLHelper<Classifier, Operation, Property, Constraint> helper;

	public static void main(String[] args) {
		TumlOcl2Java tumlOcl2Java = new TumlOcl2Java();
		tumlOcl2Java.parseOcl(new File("/home/pieter/workspace-tuml/tuml/test/tuml-test-ocl/src/main/model/test-ocl.uml"));
	}

	public void parseOcl(File modelFile) {
		Model model = ModelLoader.loadModel(modelFile);
		this.ocl = org.eclipse.ocl.uml.OCL.newInstance(ModelLoader.RESOURCE_SET);
		this.environment = this.ocl.getEnvironment();
		helper = ocl.createOCLHelper();

		StringBuilder sb = new StringBuilder();
		sb.append("package Model::org::tuml::testocl\n");
		sb.append("context OclTest1::derivedProperty1 : String\n");
		sb.append("derive :\n");
		sb.append("self.property1\n");
		sb.append("endpackage\n");

		OCLExpression<Classifier> constraint = parseConstraint(sb.toString());
		System.out.println(constraint);
	}

	protected OCLExpression<Classifier> parseConstraint(String text) {
		OCLExpression<Classifier> result = parseConstraintUnvalidated(text);
		validate(result);
		return result;
	}

	protected OCLExpression<Classifier> parseConstraintUnvalidated(String text) {
		List<Constraint> constraints;
		Constraint constraint = null;

		try {
			constraints = ocl.parse(new OCLInput(text));
			constraint = constraints.get(0);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}

		OCLExpression<Classifier> result = null;
		result = getBodyExpression(constraint);

		return result;
	}

	protected void validate(Constraint constraint) {
		try {
			ocl.validate(constraint);
		} catch (SemanticException e) {
			throw new RuntimeException(e);
		}
	}

	protected void validate(OCLExpression<Classifier> expr) {
		try {
			EObject eContainer = expr.eContainer();
			if ((eContainer != null) && Constraint.class.isAssignableFrom(eContainer.eContainer().getClass())) {
				// start validation from the constraint, for good measure
				Constraint eContainerContainer = (Constraint) eContainer.eContainer();
				validate(eContainerContainer);
			} else {
				ocl.validate(expr);
			}
		} catch (SemanticException e) {
			throw new RuntimeException(e);
		}
	}

	public OCLExpression<Classifier> getBodyExpression(Constraint constraint) {
		return ((ExpressionInOCL) constraint.getSpecification()).getBodyExpression();
	}

}

package org.umlg.ocl;

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
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.SendSignalAction;
import org.eclipse.uml2.uml.State;
import org.umlg.framework.ModelLoadedEvent;
import org.umlg.framework.ModelLoader;

public class TumlOcl2Parser implements ModelLoadedEvent {

	private OCL<Package, Classifier, Operation, Property, EnumerationLiteral, Parameter, State, CallOperationAction, SendSignalAction, Constraint, Class, EObject> ocl;
	private Environment<Package, Classifier, Operation, Property, EnumerationLiteral, Parameter, State, CallOperationAction, SendSignalAction, Constraint, Class, EObject> environment;
	private OCLHelper<Classifier, Operation, Property, Constraint> helper;
	public final static TumlOcl2Parser INSTANCE = new TumlOcl2Parser();

	private TumlOcl2Parser() {
        ModelLoader.INSTANCE.subscribeModelLoaderEvent(this);
		org.eclipse.ocl.uml.OCL.initialize(ModelLoader.INSTANCE.getRESOURCE_SET());
		this.ocl = org.eclipse.ocl.uml.OCL.newInstance(ModelLoader.INSTANCE.getRESOURCE_SET());
		this.environment = this.ocl.getEnvironment();
		this.helper = this.ocl.createOCLHelper();
//		NamedElement hierarchy = ModelLoader.findNamedElement("tumlLib::org::umlg::hierarchy::Hierarchy");
//		System.out.println(hierarchy);
//		NamedElement god = ModelLoader.findNamedElement("tumltest::org::umlg::concretetest::God");
//		System.out.println(god);
//		Package p = OCLUMLUtil.findPackage(Collections.singletonList("tumlLib::org::umlg::hierarchy"), ModelLoader.RESOURCE_SET);
//		Package p = OCLUMLUtil.findPackage(Arrays.asList("tumlLib","org","umlg","hierarchy"), ModelLoader.RESOURCE_SET);
//		System.out.println(p);
	}

	public OCLHelper<Classifier, Operation, Property, Constraint> getHelper() {
		return helper;
	}

	public static void main(String[] args) {
		Model model = ModelLoader.INSTANCE.loadModel(new File("/home/pieter/intellij-projects/umlg/test/umlg-test-ocl/src/main/model/test-ocl.uml"));
		TumlOcl2Parser parser = new TumlOcl2Parser();
		StringBuilder sb = new StringBuilder();
		sb.append("package testoclmodel::org::umlg::testocl\n");
		sb.append("context OclTest1::derivedProperty1 : String\n");
		sb.append("derive :\n");
		sb.append("self.property1\n");
		sb.append("endpackage\n");
		OCLExpression<Classifier> expr = parser.parseOcl(sb.toString());
		System.out.println("Success 1 " + expr);

		sb = new StringBuilder();
		sb.append("package testoclmodel::org::umlg::testocl\n");
		sb.append("context OclTest1\n");
		sb.append("inv testInv : property1 = 'halo'\n");
		sb.append("endpackage\n");
		expr = parser.parseOcl(sb.toString());
		System.out.println("Success 2 " + expr);

		sb = new StringBuilder();
		sb.append("package testoclmodel::org::umlg::testocl\n");
		sb.append("context OclTest1\n");
		sb.append("inv testInv : (property1 = 'halo1') and (property1 = 'halo2') and (property1 = 'halo3')\n");
		sb.append("endpackage\n");
		expr = parser.parseOcl(sb.toString());
		System.out.println("Success 3 " + expr);

		NamedElement c = ModelLoader.INSTANCE.findNamedElement("testoclmodel::org::umlg::qualifier::Bank");
		parser.helper.setContext((Classifier) c);
		try {
			expr = parser.helper.createQuery("employee");
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
		System.out.println("wow");

	}

	public OCLExpression<Classifier> parseOcl(String oclText) {
		return parseConstraint(oclText);
	}

	private OCLExpression<Classifier> parseConstraint(String text) {
		OCLExpression<Classifier> result = parseConstraintUnvalidated(text);
		validate(result);
		return result;
	}

	private OCLExpression<Classifier> parseConstraintUnvalidated(String text) {
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

		OCLExpression<Classifier> result = getBodyExpression(constraint);
		return result;
	}

	private void validate(Constraint constraint) {
		try {
			ocl.validate(constraint);
		} catch (SemanticException e) {
			throw new RuntimeException(e);
		}
	}

	private void validate(OCLExpression<Classifier> expr) {
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

	private OCLExpression<Classifier> getBodyExpression(Constraint constraint) {
		return ((ExpressionInOCL) constraint.getSpecification()).getBodyExpression();
	}

    @Override
    public void loaded(Model model) {
        //Reload the newly loaded model
        org.eclipse.ocl.uml.OCL.initialize(ModelLoader.INSTANCE.getRESOURCE_SET());
        this.ocl = org.eclipse.ocl.uml.OCL.newInstance(ModelLoader.INSTANCE.getRESOURCE_SET());
        this.environment = this.ocl.getEnvironment();
        this.helper = this.ocl.createOCLHelper();
    }
}

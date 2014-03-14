package org.umlg.ocl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.OCLInput;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.SemanticException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.Choice;
import org.eclipse.ocl.helper.ChoiceKind;
import org.eclipse.ocl.helper.ConstraintKind;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.uml.ExpressionInOCL;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.umlg.framework.ModelLoadedEvent;
import org.umlg.framework.ModelLoader;

public class UmlgOcl2Parser implements ModelLoadedEvent {

    private OCL<Package, Classifier, Operation, Property, EnumerationLiteral, Parameter, State, CallOperationAction, SendSignalAction, Constraint, Class, EObject> ocl;
    private Environment<Package, Classifier, Operation, Property, EnumerationLiteral, Parameter, State, CallOperationAction, SendSignalAction, Constraint, Class, EObject> environment;
    private OCLHelper<Classifier, Operation, Property, Constraint> helper;
    public final static UmlgOcl2Parser INSTANCE = new UmlgOcl2Parser();

    public Model init(URI modelFile) {
        Model model = ModelLoader.INSTANCE.loadModel(modelFile);
        ModelLoader.INSTANCE.subscribeModelLoaderEvent(this);
        org.eclipse.ocl.uml.OCL.initialize(ModelLoader.INSTANCE.getRESOURCE_SET());
        this.ocl = org.eclipse.ocl.uml.OCL.newInstance(ModelLoader.INSTANCE.getRESOURCE_SET());
        this.environment = this.ocl.getEnvironment();
        this.helper = this.ocl.createOCLHelper();
        return model;
    }

    public Model init(String modelFileName) {
        URL modelFileURL = Thread.currentThread().getContextClassLoader().getResource(modelFileName);
        if (modelFileURL == null) {
            throw new IllegalStateException(String.format("Model file %s not found. The model's file name must be on the classpath.", modelFileName));
        }
        URI modelFileURI;
        try {
            modelFileURI = modelFileURL.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return init(modelFileURI);
    }

    private UmlgOcl2Parser() {
    }

    public OCLHelper<Classifier, Operation, Property, Constraint> getHelper() {
        return helper;
    }

    public List<Choice> getCodeInsights(ConstraintKind invariant, String query) {
        List<Choice> insights = UmlgOcl2Parser.INSTANCE.getHelper().getSyntaxHelp(ConstraintKind.INVARIANT, query);
        UmlgOcl2Parser.INSTANCE.addAssociationMemberEnds(insights);
        return insights;
    }

    public void addAssociationMemberEnds(List<Choice> choices) {
        Set<Type> contextElements = new HashSet<Type>();
        List<Choice> ownedMemberEnds = new ArrayList<Choice>();
        for (Choice choice : choices) {
            if (choice.getElement() instanceof Element) {
                Element context = ((Element) choice.getElement()).getOwner();
                if (context instanceof Type) {
                    contextElements.add((Type) context);
                }
            }
        }
        for (Type context : contextElements) {
            List<Association> associations = context.getAssociations();
            for (Association association : associations) {
                for (Property ownedMemberEnd : association.getOwnedEnds()) {
                    //ignore the property pointing back to the context
                    if (!ownedMemberEnd.getType().equals(context)) {
                        ownedMemberEnds.add(new UmlgChoice(
                                ChoiceKind.PROPERTY,
                                ownedMemberEnd.getName(),
                                ownedMemberEnd.getType().getName(),
                                ownedMemberEnd));
                    }
                }
            }
        }
        choices.addAll(ownedMemberEnds);
        Collections.sort(choices, new Comparator<Choice>() {
            @Override
            public int compare(Choice o1, Choice o2) {
                return o1.getKind().compareTo(o2.getKind());
            }
        });
    }

    public static void main(String[] args) {
        Model model = ModelLoader.INSTANCE.loadModel(new File("/home/pieter/Downloads/umlg/test/umlg-test-ocl/src/main/model/test-ocl.uml").toURI());
        UmlgOcl2Parser parser = new UmlgOcl2Parser();
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

    public OCLExpression<Classifier> parseOcl(String oclText, boolean validate) {
        if (validate) {
            return parseConstraint(oclText);
        } else {
            return parseConstraintUnvalidated(oclText);
        }
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
        Constraint constraint;
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

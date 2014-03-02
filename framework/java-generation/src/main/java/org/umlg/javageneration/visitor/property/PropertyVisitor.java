package org.umlg.javageneration.visitor.property;

import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.ocl.UmlgOcl2Java;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.javageneration.visitor.clazz.ClassBuilder;
import org.umlg.ocl.UmlgOcl2Parser;

public class PropertyVisitor extends BaseVisitor implements Visitor<Property> {

    private static Logger logger = Logger.getLogger(PropertyVisitor.class.getPackage().getName());

    public PropertyVisitor(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper propertyWrapper = new PropertyWrapper(p);
        OJAnnotatedClass owner = findOJClass(p);
        validateProperty(propertyWrapper);
        if (!propertyWrapper.isDerived() && !propertyWrapper.isQualifier() && !propertyWrapper.isForQualifier()) {
            buildField(owner, propertyWrapper);
            buildRemover(owner, propertyWrapper);
            buildClearer(owner, propertyWrapper);
        }
        if (!propertyWrapper.isDerived() && propertyWrapper.getDefaultValue() != null) {
            addInitialization(owner, propertyWrapper);
        }
    }

    @Override
    public void visitAfter(Property element) {
    }

    private void validateProperty(PropertyWrapper propertyWrapper) {
        //non navigable properties may not be required.
        //This creates to many problems in capturing the data model, especially for the gui.
        if (!propertyWrapper.isDerived() && !propertyWrapper.isPrimitive() && !propertyWrapper.isDataType() && !propertyWrapper.isNavigable() && propertyWrapper.getLower() > 0) {
            throw new IllegalStateException(String.format("Property %s is not navigable and yet is required. its lower multiplicity is %d. This is not supported.", new Object[]{propertyWrapper.getQualifiedName(), propertyWrapper.getLower()}));
        }

    }

    private void addInitialization(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedOperation initVariables;
        if (owner instanceof OJAnnotatedInterface) {
            Interface inf = (Interface) propertyWrapper.getOwner();
            Set<Classifier> concreteClassifiers = UmlgClassOperations.getConcreteRealization(inf);
            for (Classifier concreteClassifier : concreteClassifiers) {
                OJAnnotatedClass infOwner = findOJClass(concreteClassifier);
                initVariables = infOwner.findOperation(ClassBuilder.INIT_VARIABLES);
                buildInitialization(propertyWrapper, initVariables, owner);
            }
        }  else {
            initVariables = owner.findOperation(ClassBuilder.INIT_VARIABLES);
            buildInitialization(propertyWrapper, initVariables, owner);
        }
    }

    private void buildInitialization(PropertyWrapper propertyWrapper, OJAnnotatedOperation initVariables, OJAnnotatedClass owner) {
        String java;
        if (propertyWrapper.hasOclDefaultValue()) {
            String ocl = propertyWrapper.getOclDerivedValue();
            initVariables.setComment(String.format("Implements the ocl statement for initialization variable '%s'\n<pre>\n%s\n</pre>", propertyWrapper.getName(), ocl));
            logger.info(String.format("About to parse ocl expression \n%s", new Object[]{ocl}));
            OCLExpression<Classifier> constraint = UmlgOcl2Parser.INSTANCE.parseOcl(ocl);
            java = UmlgOcl2Java.oclToJava(owner, constraint);
            if (propertyWrapper.isMany()) {
                //This is used in the initial value
                owner.addToImports("java.util.Arrays");
            }
//			java = "//TODO " + constraint.toString();
            initVariables.getBody().addToStatements(propertyWrapper.setter() + "(" + java + ")");
        } else {
            java = propertyWrapper.getDefaultValueAsString();
            initVariables.getBody().addToStatements(propertyWrapper.setter() + "(" + java + ")");
        }
    }

}

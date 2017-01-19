package org.umlg.javageneration.visitor.property;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.*;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.ocl.UmlgOcl2Java;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.javageneration.visitor.clazz.ClassBuilder;
import org.umlg.ocl.UmlgOcl2Parser;

import java.util.Set;
import java.util.logging.Logger;

public class PropertyVisitor extends BaseVisitor implements Visitor<Property> {

    private static Logger logger = Logger.getLogger(PropertyVisitor.class.getPackage().getName());

    public PropertyVisitor(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper propertyWrapper = new PropertyWrapper(p);
        if (!propertyWrapper.isRefined()) {
            OJAnnotatedClass owner = findOJClass(p);
            validateProperty(propertyWrapper);
            if (!propertyWrapper.isDerived() && !propertyWrapper.isQualifier() && !propertyWrapper.isForQualifier() && !(propertyWrapper.getOwner() instanceof Enumeration)) {
                buildField(owner, propertyWrapper);
                buildRemover(owner, propertyWrapper);
                buildClearer(owner, propertyWrapper);
            }
            if (!propertyWrapper.isDerived() && propertyWrapper.getDefaultValue() != null) {
                addInitialization(owner, propertyWrapper);
            }
            if (propertyWrapper.isMemberOfAssociationClass() && propertyWrapper.isOrdered()) {
                //build a move method
                //this is needed for association class as a move is more complex than a remove and add.
                //A move needs to retain the original association class and just move it.
                //in a move the association is not destroyed
                buildMovePropertyInstanceForAssociationClass(owner, propertyWrapper);
            }
        }
    }

    @Override
    public void visitAfter(Property element) {
    }

    private void validateProperty(PropertyWrapper propertyWrapper) {
        //non navigable properties may not be required.
        //This creates to many problems in capturing the data model, especially for the gui.

        //TODO this needs more thought
//        if (!propertyWrapper.isDerived() && !propertyWrapper.isPrimitive() && !propertyWrapper.isDataType() && propertyWrapper.getOtherEnd() != null && propertyWrapper.getOtherEnd().isComposite() && !propertyWrapper.isNavigable() && propertyWrapper.getLower() > 0) {
//            throw new IllegalStateException(String.format("Property %s is composite not navigable and yet is required. its lower multiplicity is %d. This is not supported.", new Object[]{propertyWrapper.getQualifiedName(), propertyWrapper.getLower()}));
//        }

    }

    private void addInitialization(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedOperation initVariables;
        OJAnnotatedOperation initPrimitiveVariablesWithDefaultValues;
        if (owner instanceof OJAnnotatedInterface) {
            Interface inf = (Interface) propertyWrapper.getOwner();
            Set<Classifier> concreteClassifiers = UmlgClassOperations.getConcreteRealization(inf);
            for (Classifier concreteClassifier : concreteClassifiers) {
                OJAnnotatedClass infOwner = findOJClass(concreteClassifier);
                initVariables = infOwner.findOperation(ClassBuilder.INIT_VARIABLES);
                buildInitialization(propertyWrapper, initVariables, owner);
                initPrimitiveVariablesWithDefaultValues = infOwner.findOperation(ClassBuilder.INIT_PRIMITIVE_VARIABLES_WITH_DEFAULT_VALUES);
                buildInitializationPrimitiveVariablesWithDefaultValues(propertyWrapper, initPrimitiveVariablesWithDefaultValues, owner);
            }
        } else {
            initVariables = owner.findOperation(ClassBuilder.INIT_VARIABLES);
            buildInitialization(propertyWrapper, initVariables, owner);
            initPrimitiveVariablesWithDefaultValues = owner.findOperation(ClassBuilder.INIT_PRIMITIVE_VARIABLES_WITH_DEFAULT_VALUES);
            buildInitializationPrimitiveVariablesWithDefaultValues(propertyWrapper, initPrimitiveVariablesWithDefaultValues, owner);
        }
    }

    private void buildInitialization(PropertyWrapper propertyWrapper, OJAnnotatedOperation initVariables, OJAnnotatedClass owner) {
        String java;
        if (propertyWrapper.hasOclDefaultValue()) {
            String ocl = propertyWrapper.getOclDerivedValue();
            initVariables.setComment(String.format("Implements the ocl statement for initialization variable '%s'\n<pre>\n%s\n</pre>", propertyWrapper.getName(), ocl));
            logger.fine(String.format("About to parse ocl expression \n%s", new Object[]{ocl}));
            OCLExpression<Classifier> constraint = UmlgOcl2Parser.INSTANCE.parseOcl(ocl);
            java = UmlgOcl2Java.oclToJava(propertyWrapper, owner, constraint);
            if (propertyWrapper.isMany()) {
                //This is used in the initial value
                owner.addToImports("java.util.Arrays");
            }
//			java = "//TODO " + constraint.toString();
            initVariables.getBody().addToStatements(propertyWrapper.setter() + "(" + java + ")");
        } else {
            //java default values are initialized in the constructor via z_internalPrimitivePropertiesWithDefaultValues
//            if (!propertyWrapper.isOne() || !propertyWrapper.isPrimitive()) {
//                if (propertyWrapper.isDateTime()) {
//                    java = propertyWrapper.getDefaultValueAsJava();
//                    if (!java.equals("new DateTime()")) {
//                        initVariables.getBody().addToStatements(propertyWrapper.setter() + "(" + java + ")");
//                    }
//                } else {
//                    java = propertyWrapper.getDefaultValueAsJava();
//                    initVariables.getBody().addToStatements(propertyWrapper.setter() + "(" + java + ")");
//                }
//            }
        }
    }

    private void buildInitializationPrimitiveVariablesWithDefaultValues(PropertyWrapper propertyWrapper, OJAnnotatedOperation initVariables, OJAnnotatedClass owner) {
        String java;
        if (!propertyWrapper.hasOclDefaultValue()) {
            if (propertyWrapper.isDataType()) {
                java = propertyWrapper.getDefaultValueAsJava();
                initVariables.getBody().addToStatements( "this." + ClassBuilder.INTERNAL_ADD_DATATYPE_TO_COLLECTION + "(" + UmlgClassOperations.propertyEnumName(propertyWrapper.getOwningType()) + "." + propertyWrapper.fieldname() + ", " + java + ")");
            }
        }
    }

    private static void buildMovePropertyInstanceForAssociationClass(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedOperation mover = new OJAnnotatedOperation(propertyWrapper.associationClassMoverForProperty());
        mover.addParam("index", "Integer");
        mover.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());

        OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");
        ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".move(index, " + propertyWrapper.fieldname() + ", this." + propertyWrapper.associationClassGetterForProperty() + "(" + propertyWrapper.fieldname() + "))");
        ifNotNull.addToThenPart("this." + propertyWrapper.getAssociationClassFakePropertyName() + " = " + propertyWrapper.javaDefaultInitialisationForAssociationClass((BehavioredClassifier) propertyWrapper.getOtherEnd().getType()));

        mover.getBody().addToStatements(ifNotNull);
        owner.addToOperations(mover);
    }

}

package org.umlg.javageneration.visitor.property;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.VisitFilter;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedField;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.util.TumlPropertyOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.List;

public class OnePropertyVisitor extends BaseVisitor implements Visitor<Property> {

    public OnePropertyVisitor(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper propertyWrapper = new PropertyWrapper(p);
        // TODO qualifiers
        if (propertyWrapper.isOne() && !propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
            OJAnnotatedClass owner = findOJClass(p);
            buildGetter(owner, propertyWrapper);

            if (propertyWrapper.isMemberOfAssociationClass()) {
                buildGetterForAssociationClass(findAssociationClassOJClass(propertyWrapper), propertyWrapper);
                OJAnnotatedClass associationOJClass = findAssociationClassOJClass(propertyWrapper);
                OnePropertyVisitor.buildOneAdder(associationOJClass, propertyWrapper, true);
            }

            buildOneAdder(owner, propertyWrapper, false);
            buildSetter(owner, propertyWrapper);
        }
    }

    @Override
    public void visitAfter(Property element) {

    }

    /*
     * ToOne properties are stored in a List similar to toMany. The first
     * element is returned
     */
    public static void buildGetter(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedOperation getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaBaseTypePath());
        OJAnnotatedField tmpField = new OJAnnotatedField("tmp", propertyWrapper.javaTumlTypePath());
        getter.getBody().addToLocals(tmpField);
        tmpField.setInitExp("this." + propertyWrapper.fieldname());
        OJIfStatement ifFieldNotEmpty = new OJIfStatement("!" + tmpField.getName() + ".isEmpty()");
        if (propertyWrapper.isOrdered()) {
            ifFieldNotEmpty.addToThenPart("return " + tmpField.getName() + ".get(0)");
        } else {
            ifFieldNotEmpty.addToThenPart("return " + tmpField.getName() + ".iterator().next()");
        }
        ifFieldNotEmpty.addToElsePart("return null");
        getter.getBody().addToStatements(ifFieldNotEmpty);
        owner.addToOperations(getter);

        if (propertyWrapper.isMemberOfAssociationClass()) {

            getter = new OJAnnotatedOperation("get" + propertyWrapper.getAssociationClassPathName().getLast(), propertyWrapper.getAssociationClassPathName());
            tmpField = new OJAnnotatedField("tmp", propertyWrapper.getAssociationClassJavaTumlTypePath());
            getter.getBody().addToLocals(tmpField);
            tmpField.setInitExp("this." + propertyWrapper.getAssociationClassFakePropertyName());
            ifFieldNotEmpty = new OJIfStatement("!" + tmpField.getName() + ".isEmpty()");
            if (propertyWrapper.isOrdered()) {
                ifFieldNotEmpty.addToThenPart("return " + tmpField.getName() + ".get(0)");
            } else {
                ifFieldNotEmpty.addToThenPart("return " + tmpField.getName() + ".iterator().next()");
            }
            ifFieldNotEmpty.addToElsePart("return null");
            getter.getBody().addToStatements(ifFieldNotEmpty);
            owner.addToOperations(getter);

        }
    }

    public static void buildGetterForAssociationClass(OJAnnotatedClass ac, PropertyWrapper propertyWrapper) {
        PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());
        OJAnnotatedOperation getter = new OJAnnotatedOperation(otherEnd.getter(), otherEnd.javaBaseTypePath());
        OJAnnotatedField tmpField = new OJAnnotatedField("tmp", otherEnd.javaTumlTypePath(true));
        getter.getBody().addToLocals(tmpField);
        tmpField.setInitExp("this." + otherEnd.fieldname());
        OJIfStatement ifFieldNotEmpty = new OJIfStatement("!" + tmpField.getName() + ".isEmpty()");
        if (otherEnd.isOrdered()) {
            ifFieldNotEmpty.addToThenPart("return " + tmpField.getName() + ".get(0)");
        } else {
            ifFieldNotEmpty.addToThenPart("return " + tmpField.getName() + ".iterator().next()");
        }
        ifFieldNotEmpty.addToElsePart("return null");
        getter.getBody().addToStatements(ifFieldNotEmpty);
        ac.addToOperations(getter);
    }

    public static void buildOneAdder(OJAnnotatedClass owner, PropertyWrapper propertyWrapper, boolean isAssociationClass) {
        OJAnnotatedOperation singleAdder = new OJAnnotatedOperation(propertyWrapper.adder());
        singleAdder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
        if (!isAssociationClass && propertyWrapper.isMemberOfAssociationClass()) {
            singleAdder.addParam(StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()), TumlClassOperations.getPathName(propertyWrapper.getAssociationClass()));
        }

        if (!propertyWrapper.isDataType()) {

            OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");

            OJBlock ojBlock1 = new OJBlock();
            ifNotNull.addToThenPart(ojBlock1);

            OJBlock ojBlock2 = new OJBlock();
            ifNotNull.addToThenPart(ojBlock2);

            OJIfStatement ifExist = new OJIfStatement("!this." + propertyWrapper.fieldname() + ".isEmpty()");
            ifExist.addToThenPart("throw new RuntimeException(\"Property is a one and already has a value!\")");
            ojBlock1.addToStatements(ifExist);

            if (isAssociationClass || !propertyWrapper.isMemberOfAssociationClass()) {
                ojBlock2.addToStatements("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");
            } else {
                ojBlock2.addToStatements("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ", " + StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()) + ")");
            }
            List<Constraint> constraints = TumlPropertyOperations.getConstraints(propertyWrapper.getProperty());
            if (!constraints.isEmpty()) {

                //Check the constraints
                OJField failedConstraints = new OJField("violations", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
                failedConstraints.setInitExp("new ArrayList<" + TinkerGenerationUtil.TumlConstraintViolation.getLast() + ">()");
                ojBlock2.addToLocals(failedConstraints);

                for (Constraint constraint : constraints) {
                    ojBlock2.addToStatements("violations.addAll(" + propertyWrapper.checkConstraint(constraint) + "())");
                }

                OJIfStatement ifConstraintsFail = new OJIfStatement("!violations.isEmpty()");
                ifConstraintsFail.addToThenPart("this." + propertyWrapper.fieldname() + ".clear()");
                ifConstraintsFail.addToThenPart("throw new TumlConstraintViolationException(violations)");
                ojBlock2.addToStatements(ifConstraintsFail);

            }
            singleAdder.getBody().addToStatements(ifNotNull);
        } else {
            //Check if already has a value
            OJIfStatement ifAlreadySet = new OJIfStatement("!this." + propertyWrapper.fieldname() + ".isEmpty()");
            ifAlreadySet.addToThenPart("throw new RuntimeException(\"Property is a one and already has value, first clear it before adding!\")");
            singleAdder.getBody().addToStatements(ifAlreadySet);

            OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");

            OJField failedConstraints = new OJField("violations", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
            failedConstraints.setInitExp(propertyWrapper.validator() + "(" + propertyWrapper.fieldname() + ")");
            ifNotNull.getThenPart().addToLocals(failedConstraints);

            OJIfStatement ifValidated = new OJIfStatement("violations.isEmpty()");

            //Set the new value
            ifValidated.addToThenPart("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");

            //Check the constraints
            List<Constraint> constraints = TumlPropertyOperations.getConstraints(propertyWrapper.getProperty());
            for (Constraint constraint : constraints) {
                ifValidated.getThenPart().addToStatements("violations.addAll(" + propertyWrapper.checkConstraint(constraint) + "())");
            }

            if (!constraints.isEmpty()) {
                OJIfStatement ifConstraintsFail = new OJIfStatement("!violations.isEmpty()");
                ifConstraintsFail.addToThenPart("this." + propertyWrapper.fieldname() + ".clear()");
                ifConstraintsFail.addToThenPart("throw new TumlConstraintViolationException(violations)");
                ifValidated.addToThenPart(ifConstraintsFail);
            }

            ifNotNull.addToThenPart(ifValidated);

            singleAdder.getBody().addToStatements(ifNotNull);
            ifValidated.addToElsePart("throw new TumlConstraintViolationException(violations)");
            owner.addToImports(TinkerGenerationUtil.TumlConstraintViolationException);
        }
        owner.addToOperations(singleAdder);
    }

    public static void buildSetter(OJAnnotatedClass owner, PropertyWrapper pWrap) {
        OJAnnotatedOperation setter = new OJAnnotatedOperation(pWrap.setter());
        setter.addParam(pWrap.fieldname(), pWrap.javaBaseTypePath());
        if (pWrap.isMemberOfAssociationClass()) {
            setter.addParam(StringUtils.uncapitalize(pWrap.getAssociationClass().getName()), TumlClassOperations.getPathName(pWrap.getAssociationClass()));
        }
        if (pWrap.isReadOnly()) {
            setter.setVisibility(OJVisibilityKind.PROTECTED);
        }
        PropertyWrapper otherEnd = new PropertyWrapper(pWrap.getOtherEnd());
        if (pWrap.hasOtherEnd() && !pWrap.isEnumeration() && pWrap.isOneToOne()) {
            OJIfStatement ifNotNull = new OJIfStatement(pWrap.fieldname() + " != null");
            ifNotNull.addToThenPart(pWrap.fieldname() + "." + otherEnd.clearer() + "()");
            ifNotNull.addToThenPart(pWrap.fieldname() + ".initialiseProperty(" + TumlClassOperations.propertyEnumName(otherEnd.getOwningType()) + "."
                    + otherEnd.fieldname() + ", false)");
            owner.addToImports(TumlClassOperations.getPathName(otherEnd.getOwningType()).append(TumlClassOperations.propertyEnumName(otherEnd.getOwningType())));
            setter.getBody().addToStatements(ifNotNull);
        }
        setter.getBody().addToStatements(pWrap.clearer() + "()");
        if (!pWrap.isMemberOfAssociationClass()) {
            setter.getBody().addToStatements(pWrap.adder() + "(" + pWrap.fieldname()  + ")");
        } else {
            setter.getBody().addToStatements(pWrap.adder() + "(" + pWrap.fieldname()  + ", " + StringUtils.uncapitalize(pWrap.getAssociationClass().getName()) + ")");
        }
        owner.addToOperations(setter);
    }
}

package org.umlg.javageneration.visitor.property;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedField;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;

public class ManyPropertyVisitor extends BaseVisitor implements Visitor<Property> {

    public ManyPropertyVisitor(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper propertyWrapper = new PropertyWrapper(p);
        if (propertyWrapper.isMany() && !propertyWrapper.isDerived() && !propertyWrapper.isQualifier() && !propertyWrapper.isRefined() && !(propertyWrapper.getOwner() instanceof Enumeration)) {
            OJAnnotatedClass owner = findOJClass(p);
            buildGetter(owner, propertyWrapper);
            if (propertyWrapper.isMemberOfAssociationClass()) {
                buildGetterForAssociationClass(findAssociationClassOJClass(propertyWrapper), propertyWrapper);
                OJAnnotatedClass associationOJClass = findAssociationClassOJClass(propertyWrapper);
                OnePropertyVisitor.buildOneAdder(associationOJClass, propertyWrapper, true, false);
            }
            buildManyAdder(owner, propertyWrapper, false, false);
            buildManyAdder(owner, propertyWrapper, false, true);
            if (propertyWrapper.isOrdered()) {
                buildManyAdder(owner, propertyWrapper, true, false);
            }
            buildSetter(owner, propertyWrapper);
        }
    }

    @Override
    public void visitAfter(Property element) {

    }

    public static void buildGetter(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedOperation getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaTumlTypePath());
        getter.getBody().addToStatements("return this." + propertyWrapper.fieldname());
        owner.addToOperations(getter);

        if (propertyWrapper.isMemberOfAssociationClass()) {
            getter = new OJAnnotatedOperation(propertyWrapper.associationClassGetter(), propertyWrapper.getAssociationClassJavaTumlTypePath());
            getter.getBody().addToStatements("return this." + propertyWrapper.getAssociationClassFakePropertyName());
            owner.addToOperations(getter);

            //Build getter that returns the AssociationClass for an instance of the other end
            buildGetAssociationClassForPropertyInstance(owner, propertyWrapper);
        }

        //If the property is subsetting another property then add @Override to the getter.
        //The subsetted property's implementation will add an protected getter with the subsetting property's name
        if (!propertyWrapper.getSubsettedProperties().isEmpty()) {
            //if subsetted property is on an interface then there is no fake implementation, so no override
            Property subsettedProperty = propertyWrapper.getSubsettedProperties().get(0);
            PropertyWrapper subsettedPropertyWrapper = new PropertyWrapper(subsettedProperty);
            if (!(subsettedProperty.getType() instanceof Interface) && subsettedPropertyWrapper.getOtherEnd().getType() != propertyWrapper.getOtherEnd().getType()) {
                UmlgGenerationUtil.addOverrideAnnotation(getter);
            }
        }

    }

    private static void buildGetAssociationClassForPropertyInstance(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedOperation getAC = new OJAnnotatedOperation(propertyWrapper.associationClassGetterForProperty(), propertyWrapper.getAssociationClassPathName());
        getAC.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
        OJForStatement forAC = new OJForStatement("ac", propertyWrapper.getAssociationClassPathName(), "this." + propertyWrapper.getAssociationClassFakePropertyName());
        OJIfStatement ifStatement = new OJIfStatement("ac." + propertyWrapper.getter() + "().equals(" + propertyWrapper.fieldname() + ")", "return ac");
        forAC.getBody().addToStatements(ifStatement);
        getAC.getBody().addToStatements(forAC);
        getAC.getBody().addToStatements("return null");
        owner.addToOperations(getAC);
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

    public static void buildManyAdder(OJAnnotatedClass owner, PropertyWrapper propertyWrapper, boolean indexed, boolean ignoreInverse) {
        OJAnnotatedOperation adder = new OJAnnotatedOperation(propertyWrapper.adder());
        if (!propertyWrapper.isMemberOfAssociationClass()) {
            if (indexed) {
                adder.addParam("index", "int");
            }
            adder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaTypePath());
        } else {
            if (indexed) {
                adder.addParam("index", "int");
            }
            adder.addParam(propertyWrapper.getAssociationClassFakePropertyName(), propertyWrapper.javaTypePathWithAssociationClass());
        }

        if (!(owner instanceof OJAnnotatedInterface)) {
            if (!propertyWrapper.hasQualifiers()) {
                if (!propertyWrapper.isMemberOfAssociationClass()) {
                    OJIfStatement ifNotNull = new OJIfStatement("!" + propertyWrapper.fieldname() + ".isEmpty()");
                    ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".addAll(" + propertyWrapper.fieldname() + ")");
                    adder.getBody().addToStatements(ifNotNull);
                } else {
                    //iterate the association class set as it contains a Pair
                    OJForStatement associationClassPairs = new OJForStatement("pair", propertyWrapper.getAssociationClassPair(), propertyWrapper.getAssociationClassFakePropertyName());
                    associationClassPairs.getBody().addToStatements(propertyWrapper.adder() + "(pair.getFirst(), pair.getSecond())");
                    adder.getBody().addToStatements(associationClassPairs);
                }
            } else {
                String elementName = "_" + propertyWrapper.fieldname().substring(0, 1);
                OJForStatement forAll = new OJForStatement(elementName, propertyWrapper.javaBaseTypePath(), propertyWrapper.fieldname());
                forAll.getBody().addToStatements("this." + propertyWrapper.adder() + "(" + elementName + ")");
                adder.getBody().addToStatements(forAll);
            }
        }
        owner.addToOperations(adder);

        OJAnnotatedOperation singleAdder = new OJAnnotatedOperation(!ignoreInverse ? propertyWrapper.adder() : propertyWrapper.adderIgnoreInverse());
        if (indexed) {
            singleAdder.addParam("index", "int");
        }
        singleAdder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
        if (propertyWrapper.isMemberOfAssociationClass()) {
            singleAdder.addParam(StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()), UmlgClassOperations.getPathName(propertyWrapper.getAssociationClass()));
        }
        if (!(owner instanceof OJAnnotatedInterface)) {

            PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());
            if (/*For bags the one side can have many edges to the same element*/propertyWrapper.isUnique() && propertyWrapper.hasOtherEnd() && !propertyWrapper.isEnumeration() && otherEnd.isOne()) {
                OJIfStatement ifNotNull2 = new OJIfStatement(propertyWrapper.fieldname() + " != null");
                ifNotNull2.addToThenPart(propertyWrapper.fieldname() + "." + otherEnd.clearer() + "()");
                ifNotNull2.addToThenPart(propertyWrapper.fieldname() + ".initialiseProperty(" + UmlgClassOperations.propertyEnumName(otherEnd.getOwningType()) + "."
                        + otherEnd.fieldname() + ", false, true)");
                ifNotNull2.addToThenPart(propertyWrapper.remover() + "(" + propertyWrapper.fieldname() + ")");
                owner.addToImports(UmlgClassOperations.getPathName(otherEnd.getOwningType()).append(UmlgClassOperations.propertyEnumName(otherEnd.getOwningType())));
                singleAdder.getBody().addToStatements(ifNotNull2);
            }
            OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");

            //Add in validations

            OJBlock block;
            if (propertyWrapper.isDataType()) {
                OJField failedConstraints = new OJField("violations", new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UmlgConstraintViolation));
                failedConstraints.setInitExp(propertyWrapper.validator() + "(" + propertyWrapper.fieldname() + ")");
                ifNotNull.getThenPart().addToLocals(failedConstraints);
                OJIfStatement ifValidated = new OJIfStatement("violations.isEmpty()");
                ifValidated.addToElsePart("throw new " + UmlgGenerationUtil.UmlgConstraintViolationException.getLast() + "(violations)");
                owner.addToImports(UmlgGenerationUtil.UmlgConstraintViolationException);
                ifNotNull.addToThenPart(ifValidated);
                block = ifValidated.getThenPart();
            } else {
                block = ifNotNull.getThenPart();
            }

            if (!propertyWrapper.isMemberOfAssociationClass()) {
                if (!indexed) {
                    if (!ignoreInverse) {
                        block.addToStatements("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");
                    } else {
                        block.addToStatements("this." + propertyWrapper.fieldname() + ".addIgnoreInverse(" + propertyWrapper.fieldname() + ")");
                    }
                } else {
                    block.addToStatements("this." + propertyWrapper.fieldname() + ".add(index, " + propertyWrapper.fieldname() + ")");
                }
            } else {
                if (!indexed) {
                    block.addToStatements("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ", " + StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()) + ")");
                } else {
                    block.addToStatements("this." + propertyWrapper.fieldname() + ".add(index, " + propertyWrapper.fieldname() + ", " + StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()) + ")");
                }
            }
            singleAdder.getBody().addToStatements(ifNotNull);
        }
        owner.addToOperations(singleAdder);

//        //Add change listener
//        if (propertyWrapper.isChangedListener()) {
//            PropertyChangeNotificationBuilder.buildChangeNotification(owner, singleAdder, propertyWrapper, PropertyChangeNotificationBuilder.CHANGE_TYPE.ADD);
//        }

    }

    public static OJAnnotatedOperation buildSetter(OJAnnotatedClass owner, PropertyWrapper pWrap) {
        OJAnnotatedOperation setter = new OJAnnotatedOperation(pWrap.setter());
        if (pWrap.isReadOnly()) {
            setter.setVisibility(OJVisibilityKind.PROTECTED);
        }
        if (!pWrap.isMemberOfAssociationClass()) {
            setter.addParam(pWrap.fieldname(), pWrap.javaTypePath());
        } else {
            setter.addParam(pWrap.getAssociationClassFakePropertyName(), pWrap.javaTypePathWithAssociationClass());
        }
        setter.getBody().addToStatements(pWrap.clearer() + "()");
        OJIfStatement ifNotNull;
        if (!pWrap.isMemberOfAssociationClass()) {
            ifNotNull = new OJIfStatement(pWrap.fieldname() + " != null");
            ifNotNull.addToThenPart(pWrap.adder() + "(" + pWrap.fieldname() + ")");
        } else {
            ifNotNull = new OJIfStatement(pWrap.getAssociationClassFakePropertyName() + " != null");
            ifNotNull.addToThenPart(pWrap.adder() + "(" + pWrap.getAssociationClassFakePropertyName() + ")");
        }
        setter.getBody().addToStatements(ifNotNull);
        owner.addToOperations(setter);
        return setter;
    }

}

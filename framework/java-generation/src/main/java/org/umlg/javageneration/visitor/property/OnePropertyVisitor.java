package org.umlg.javageneration.visitor.property;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.*;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedField;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.DataTypeEnum;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;

public class OnePropertyVisitor extends BaseVisitor implements Visitor<Property> {

    public OnePropertyVisitor(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper propertyWrapper = new PropertyWrapper(p);
        // TODO qualifiers
        if (propertyWrapper.isOne() && !propertyWrapper.isDerived() && !propertyWrapper.isQualifier() && !propertyWrapper.isRefined() && !(propertyWrapper.getOwner() instanceof Enumeration)) {
            OJAnnotatedClass owner = findOJClass(p);
            buildGetter(owner, propertyWrapper);

            if (propertyWrapper.isMemberOfAssociationClass()) {
                buildGetterForAssociationClass(findAssociationClassOJClass(propertyWrapper), propertyWrapper);
                OJAnnotatedClass associationOJClass = findAssociationClassOJClass(propertyWrapper);
                OnePropertyVisitor.buildOneAdder(associationOJClass, propertyWrapper, true, false);
            }

            OnePropertyVisitor.buildOneAdder(owner, propertyWrapper, false, false);
            OnePropertyVisitor.buildOneAdder(owner, propertyWrapper, false, true);
            OJAnnotatedOperation setter = buildSetter(owner, propertyWrapper);

            if (propertyWrapper.isDataType() && propertyWrapper.getOwner() instanceof AssociationClass) {
                //Add the property to copyOnePrimitivePropertiesToEdge
                OJAnnotatedOperation copyOnePrimitivePropertiesToEdge = owner.findOperation("z_internalCopyOnePrimitivePropertiesToEdge", UmlgGenerationUtil.edgePathName);
                OJIfStatement ifPropertyNotNull = new OJIfStatement(propertyWrapper.getter() + "() != null");
                if (propertyWrapper.isEnumeration()) {
                    ifPropertyNotNull.addToThenPart("edge.property(\"" + propertyWrapper.getPersistentName() + "\", " + propertyWrapper.getter() + "().name())");
                } else if (!propertyWrapper.isPrimitive()) {
                    ifPropertyNotNull.addToThenPart("edge.property(\"" + propertyWrapper.getPersistentName() + "\", " +
                            UmlgGenerationUtil.umlgFormatter.getLast() + ".format(" +
                            UmlgGenerationUtil.DataTypeEnum.getLast() + "." + DataTypeEnum.fromDataType((DataType) p.getType()).name() + ", " +
                            propertyWrapper.getter() + "()))");
                } else {
                    ifPropertyNotNull.addToThenPart("edge.property(\"" + propertyWrapper.getPersistentName() + "\", " + propertyWrapper.getter() + "())");
                }
                copyOnePrimitivePropertiesToEdge.getBody().addToStatements(ifPropertyNotNull);
            }

//            //Add change listener
//            if (propertyWrapper.isChangedListener()) {
//                PropertyChangeNotificationBuilder.buildChangeNotification(owner, setter, propertyWrapper, PropertyChangeNotificationBuilder.CHANGE_TYPE.UPDATE);
//            }

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

            getter = new OJAnnotatedOperation(propertyWrapper.associationClassGetter(), propertyWrapper.getAssociationClassPathName());
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

    public static void buildOneAdder(OJAnnotatedClass owner, PropertyWrapper propertyWrapper, boolean isAssociationClass, boolean ignoreInverse) {
        OJAnnotatedOperation singleAdder = new OJAnnotatedOperation(!ignoreInverse ? propertyWrapper.adder() : propertyWrapper.adderIgnoreInverse());
        singleAdder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
        if (!isAssociationClass && propertyWrapper.isMemberOfAssociationClass()) {
            singleAdder.addParam(StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()), UmlgClassOperations.getPathName(propertyWrapper.getAssociationClass()));
        }

        if (!propertyWrapper.isDataType()) {

            OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");

            OJBlock ojBlock1 = new OJBlock();
            ifNotNull.addToThenPart(ojBlock1);

            OJBlock ojBlock2 = new OJBlock();
            ifNotNull.addToThenPart(ojBlock2);

            OJIfStatement ifExist = new OJIfStatement("!this." + propertyWrapper.fieldname() + ".isEmpty()");
            ifExist.addToThenPart("throw new RuntimeException(\"Property " + propertyWrapper.getQualifiedName() + "is a one and already has a value!\")");
            ojBlock1.addToStatements(ifExist);

            if (isAssociationClass || !propertyWrapper.isMemberOfAssociationClass()) {
                if (!ignoreInverse) {
                    ojBlock2.addToStatements("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");
                } else {
                    ojBlock2.addToStatements("this." + propertyWrapper.fieldname() + ".addIgnoreInverse(" + propertyWrapper.fieldname() + ")");
                }
            } else {
                ojBlock2.addToStatements("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ", " + StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()) + ")");
            }
            singleAdder.getBody().addToStatements(ifNotNull);
        } else {
            //Check if already has a value
            OJIfStatement ifAlreadySet = new OJIfStatement("!this." + propertyWrapper.fieldname() + ".isEmpty()");
            ifAlreadySet.addToThenPart("throw new RuntimeException(\"Property is a one and already has value, first clear it before adding!\")");
            singleAdder.getBody().addToStatements(ifAlreadySet);

            OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");

            OJField failedConstraints = new OJField("violations", new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UmlgConstraintViolation));
            failedConstraints.setInitExp(propertyWrapper.validator() + "(" + propertyWrapper.fieldname() + ")");
            ifNotNull.getThenPart().addToLocals(failedConstraints);

            OJIfStatement ifValidated = new OJIfStatement("violations.isEmpty()");

            //Set the new value
            ifValidated.addToThenPart("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");

            ifNotNull.addToThenPart(ifValidated);

            singleAdder.getBody().addToStatements(ifNotNull);
            ifValidated.addToElsePart("throw new UmlgConstraintViolationException(violations)");
            owner.addToImports(UmlgGenerationUtil.UmlgConstraintViolationException);
        }
        owner.addToOperations(singleAdder);
    }

    public static OJAnnotatedOperation buildSetter(OJAnnotatedClass owner, PropertyWrapper pWrap) {
        OJAnnotatedOperation setter = new OJAnnotatedOperation(pWrap.setter());
        setter.addParam(pWrap.fieldname(), pWrap.javaBaseTypePath());
        if (pWrap.isMemberOfAssociationClass()) {
            setter.addParam(StringUtils.uncapitalize(pWrap.getAssociationClass().getName()), UmlgClassOperations.getPathName(pWrap.getAssociationClass()));
        }
        if (pWrap.isReadOnly()) {
            setter.setVisibility(OJVisibilityKind.PROTECTED);
        }
        PropertyWrapper otherEnd = new PropertyWrapper(pWrap.getOtherEnd());
        if (pWrap.hasOtherEnd() && !pWrap.isEnumeration() && pWrap.isOneToOne()) {
            OJIfStatement ifNotNull = new OJIfStatement(pWrap.fieldname() + " != null");
            ifNotNull.addToThenPart(pWrap.fieldname() + "." + otherEnd.clearer() + "()");
            ifNotNull.addToThenPart(pWrap.fieldname() + ".initialiseProperty(" + UmlgClassOperations.propertyEnumName(otherEnd.getOwningType()) + "."
                    + otherEnd.fieldname() + ", false)");
            owner.addToImports(UmlgClassOperations.getPathName(otherEnd.getOwningType()).append(UmlgClassOperations.propertyEnumName(otherEnd.getOwningType())));
            setter.getBody().addToStatements(ifNotNull);
        }
        setter.getBody().addToStatements(pWrap.clearer() + "()");
        if (!pWrap.isMemberOfAssociationClass()) {
            setter.getBody().addToStatements(pWrap.adder() + "(" + pWrap.fieldname() + ")");
        } else {
            setter.getBody().addToStatements(pWrap.adder() + "(" + pWrap.fieldname() + ", " + StringUtils.uncapitalize(pWrap.getAssociationClass().getName()) + ")");
        }
        owner.addToOperations(setter);
        return setter;
    }
}

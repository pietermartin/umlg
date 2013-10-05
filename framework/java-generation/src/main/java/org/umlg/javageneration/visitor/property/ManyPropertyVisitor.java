package org.umlg.javageneration.visitor.property;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJForStatement;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedField;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class ManyPropertyVisitor extends BaseVisitor implements Visitor<Property> {

    public ManyPropertyVisitor(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper propertyWrapper = new PropertyWrapper(p);
        if (propertyWrapper.isMany() && !propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
            OJAnnotatedClass owner = findOJClass(p);
            buildGetter(owner, propertyWrapper);
            if (propertyWrapper.isMemberOfAssociationClass()) {
                buildGetterForAssociationClass(findAssociationClassOJClass(propertyWrapper), propertyWrapper);
                OJAnnotatedClass associationOJClass = findAssociationClassOJClass(propertyWrapper);
                OnePropertyVisitor.buildOneAdder(associationOJClass, propertyWrapper, true);
            }
            buildManyAdder(owner, propertyWrapper, false);
            if (propertyWrapper.isOrdered()) {
                buildManyAdder(owner, propertyWrapper, true);
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
            getter = new OJAnnotatedOperation("get" + propertyWrapper.getAssociationClassPathName().getLast(), propertyWrapper.getAssociationClassJavaTumlTypePath());
            getter.getBody().addToStatements("return this." + propertyWrapper.getAssociationClassFakePropertyName());
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

    public static void buildManyAdder(OJAnnotatedClass owner, PropertyWrapper propertyWrapper, boolean indexed) {
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
                String elementName = propertyWrapper.fieldname().substring(0, 1);
                OJForStatement forAll = new OJForStatement(elementName, propertyWrapper.javaBaseTypePath(), propertyWrapper.fieldname());
                forAll.getBody().addToStatements("this." + propertyWrapper.adder() + "(" + elementName + ")");
                adder.getBody().addToStatements(forAll);
            }
        }
        owner.addToOperations(adder);

        OJAnnotatedOperation singleAdder = new OJAnnotatedOperation(propertyWrapper.adder());
        if (indexed) {
            singleAdder.addParam("index", "int");
        }
        singleAdder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
        if (propertyWrapper.isMemberOfAssociationClass()) {
            singleAdder.addParam(StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()), TumlClassOperations.getPathName(propertyWrapper.getAssociationClass()));
        }
        if (!(owner instanceof OJAnnotatedInterface)) {

            PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());
            if (/*For bags the one side can have many edges to the same element*/propertyWrapper.isUnique() && propertyWrapper.hasOtherEnd() && !propertyWrapper.isEnumeration() && otherEnd.isOne()) {
                OJIfStatement ifNotNull2 = new OJIfStatement(propertyWrapper.fieldname() + " != null");
                ifNotNull2.addToThenPart(propertyWrapper.fieldname() + "." + otherEnd.clearer() + "()");
                ifNotNull2.addToThenPart(propertyWrapper.fieldname() + ".initialiseProperty(" + TumlClassOperations.propertyEnumName(otherEnd.getOwningType()) + "."
                        + otherEnd.fieldname() + ", false)");
                ifNotNull2.addToThenPart(propertyWrapper.remover() + "(" + propertyWrapper.fieldname() + ")");
                owner.addToImports(TumlClassOperations.getPathName(otherEnd.getOwningType()).append(TumlClassOperations.propertyEnumName(otherEnd.getOwningType())));
                singleAdder.getBody().addToStatements(ifNotNull2);
            }
            OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");
            if (!propertyWrapper.isMemberOfAssociationClass()) {
                if (!indexed) {
                    ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");
                } else {
                    ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".add(index, " + propertyWrapper.fieldname() + ")");
                }
            } else {
                if (!indexed) {
                    ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ", " + StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()) + ")");
                } else {
                    ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".add(index, " + propertyWrapper.fieldname() + ", " + StringUtils.uncapitalize(propertyWrapper.getAssociationClass().getName()) + ")");
                }
            }
            singleAdder.getBody().addToStatements(ifNotNull);
        }
        owner.addToOperations(singleAdder);

    }

    public static void buildSetter(OJAnnotatedClass owner, PropertyWrapper pWrap) {
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
    }

}

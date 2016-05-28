package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.*;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.List;

public class ClassRuntimePropertyImplementorVisitor extends BaseVisitor implements Visitor<Classifier> {

    public static final String INITIALISE_PROPERTY = "initialiseProperty";

    public ClassRuntimePropertyImplementorVisitor(Workspace workspace) {
        super(workspace);
    }

    /**
     * Enumeration needs the runtime properties for the collection that implement the getters
     *
     * @param classifier
     */
    @Override
    @VisitSubclasses({Enumeration.class, Class.class, AssociationClass.class})
    public void visitBefore(Classifier classifier) {
        OJAnnotatedClass annotatedClass = findOJClass(classifier);
        if (!(classifier instanceof Enumeration)) {
            addInitialiseProperty(annotatedClass, classifier);
            addInternalInverseAdder(annotatedClass, (Class) classifier);
            addZInternalCollectionAdder(annotatedClass, (Class) classifier);
            if (UmlgClassOperations.isAssociationClass((Class) classifier)) {
                addInternalAdder(annotatedClass, (AssociationClass) classifier);
            }
            addGetMetaDataAsJson(annotatedClass, (Class) classifier);
        }
        RuntimePropertyImplementor.addTumlRuntimePropertyEnum(
                annotatedClass,
                UmlgClassOperations.propertyEnumName(classifier),
                classifier,
                UmlgClassOperations.getAllProperties(classifier),
                UmlgClassOperations.hasCompositeOwner(classifier),
                classifier.getModel().getName());
        if (!(classifier instanceof Enumeration)) {
            addGetQualifiers(annotatedClass, (Class) classifier);
            addGetSize(annotatedClass, (Class) classifier);
        }
    }

    @Override
    public void visitAfter(Classifier clazz) {
    }


    private void addInternalAdder(OJAnnotatedClass annotatedClass, AssociationClass associationClass) {
        OJAnnotatedOperation initialiseProperty = new OJAnnotatedOperation("internalAdder");
        UmlgGenerationUtil.addOverrideAnnotation(initialiseProperty);
        initialiseProperty.setReturnType(UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
        initialiseProperty.addParam("tumlRuntimeProperty", UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
        initialiseProperty.addParam("inverse", "boolean");
        initialiseProperty.addParam("umlgNode", UmlgGenerationUtil.UMLG_NODE);
        annotatedClass.addToOperations(initialiseProperty);

        OJField runtimeProperty = new OJField("runtimeProperty", new OJPathName(UmlgClassOperations.propertyEnumName(associationClass)));
        if (UmlgAssociationClassOperations.hasAssociationClassAsGeneralization(associationClass)) {

            OJField fromSuperRuntimeProperty = new OJField("fromSuperRuntimeProperty", UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
            fromSuperRuntimeProperty.setInitExp("super.internalAdder(tumlRuntimeProperty, inverse, umlgNode)");
            initialiseProperty.getBody().addToLocals(fromSuperRuntimeProperty);

        }
        initialiseProperty.getBody().addToLocals(runtimeProperty);

        OJIfStatement ifRuntimeNull = null;
        if (UmlgAssociationClassOperations.hasAssociationClassAsGeneralization(associationClass)) {
            ifRuntimeNull = new OJIfStatement("fromSuperRuntimeProperty != null", "return fromSuperRuntimeProperty");
        }

        OJIfStatement ifInverse = new OJIfStatement("!inverse");
        ifInverse.addToThenPart("runtimeProperty = " + "(" + UmlgClassOperations.propertyEnumName(associationClass)
                + ".fromQualifiedName(tumlRuntimeProperty.getQualifiedName()))");
        ifInverse.addToElsePart("runtimeProperty = " + "(" + UmlgClassOperations.propertyEnumName(associationClass)
                + ".fromQualifiedName(tumlRuntimeProperty.getInverseQualifiedName()))");

        OJIfStatement ifNotNull = new OJIfStatement("runtimeProperty != null");
        OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
        ojSwitchStatement.setCondition("runtimeProperty");
        ifNotNull.addToThenPart(ojSwitchStatement);
        ifNotNull.addToThenPart("return runtimeProperty");
        ifNotNull.addToElsePart("return null");

        for (Property p : associationClass.getMemberEnds()) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && pWrap.getOtherEnd() != null && !pWrap.isEnumeration()) {
                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(pWrap.fieldname());
                OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.adder() + "((" + pWrap.javaBaseTypePath().getLast() + ")umlgNode)");
                statement.setName(pWrap.fieldname());
                ojSwitchCase.getBody().addToStatements(statement);
                annotatedClass.addToImports(pWrap.javaImplTypePath());
                ojSwitchStatement.addToCases(ojSwitchCase);
            }
        }

        if (UmlgAssociationClassOperations.hasAssociationClassAsGeneralization(associationClass)) {
            ifRuntimeNull.addToElsePart(ifInverse);
            ifRuntimeNull.addToElsePart(ifNotNull);
            initialiseProperty.getBody().addToStatements(ifRuntimeNull);
        } else {
            initialiseProperty.getBody().addToStatements(ifInverse);
            initialiseProperty.getBody().addToStatements(ifNotNull);
        }
    }

    /**
     * inverseAdded is called from the collection classes to manage the inverse side of an association.
     * It must be called before the edge is created. It will attempt to load the collection. Then it will add the element without creating an edge to it.
     *
     * @param annotatedClass
     * @param clazz
     */
    private void addInternalInverseAdder(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation initialiseProperty = new OJAnnotatedOperation("inverseAdder");
        UmlgGenerationUtil.addOverrideAnnotation(initialiseProperty);
        initialiseProperty.setReturnType(UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
        initialiseProperty.addParam("tumlRuntimeProperty", UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
        initialiseProperty.addParam("inverse", "boolean");
        initialiseProperty.addParam("umlgNode", UmlgGenerationUtil.UMLG_NODE);
        annotatedClass.addToOperations(initialiseProperty);

        OJField runtimeProperty = new OJField("runtimeProperty", new OJPathName(UmlgClassOperations.propertyEnumName(clazz)));
        if (!clazz.getGeneralizations().isEmpty()) {

            OJField fromSuperRuntimeProperty = new OJField("fromSuperRuntimeProperty", UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
            fromSuperRuntimeProperty.setInitExp("super.inverseAdder(tumlRuntimeProperty, inverse, umlgNode)");
            initialiseProperty.getBody().addToLocals(fromSuperRuntimeProperty);

        }
        initialiseProperty.getBody().addToLocals(runtimeProperty);

        OJIfStatement ifRuntimeNull = null;
        if (!clazz.getGeneralizations().isEmpty()) {
            ifRuntimeNull = new OJIfStatement("fromSuperRuntimeProperty != null", "return fromSuperRuntimeProperty");
        }

        OJIfStatement ifInverse = new OJIfStatement("!inverse");
        ifInverse.addToThenPart("runtimeProperty = " + "(" + UmlgClassOperations.propertyEnumName(clazz)
                + ".fromQualifiedName(tumlRuntimeProperty.getQualifiedName()))");
        ifInverse.addToElsePart("runtimeProperty = " + "(" + UmlgClassOperations.propertyEnumName(clazz)
                + ".fromQualifiedName(tumlRuntimeProperty.getInverseQualifiedName()))");

        OJIfStatement ifNotNull = new OJIfStatement("runtimeProperty != null");
        OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
        ojSwitchStatement.setCondition("runtimeProperty");
        ifNotNull.addToThenPart(ojSwitchStatement);
        ifNotNull.addToThenPart("return runtimeProperty");
        ifNotNull.addToElsePart("return null");

        for (Property p : UmlgClassOperations.getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && pWrap.getOtherEnd() != null && !pWrap.isEnumeration() && !pWrap.isRefined()) {
                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(pWrap.fieldname());
                OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.fieldname() + ".inverseAdder((" + pWrap.javaBaseTypePath().getLast() + ")umlgNode)");
                statement.setName(pWrap.fieldname());
                ojSwitchCase.getBody().addToStatements(statement);
                annotatedClass.addToImports(pWrap.javaImplTypePath());
                ojSwitchStatement.addToCases(ojSwitchCase);

                if (pWrap.isMemberOfAssociationClass()) {
                    ojSwitchCase = new OJSwitchCase();
                    ojSwitchCase.setLabel(pWrap.getAssociationClassFakePropertyName());
                    statement = new OJSimpleStatement("this." + pWrap.getAssociationClassFakePropertyName() + ".inverseAdder((" + pWrap.getAssociationClassPathName().getLast() + ")umlgNode)");
                    statement.setName(pWrap.fieldname());
                    ojSwitchCase.getBody().addToStatements(statement);
                    annotatedClass.addToImports(pWrap.javaImplTypePath());
                    ojSwitchStatement.addToCases(ojSwitchCase);
                }
            }
        }

        if (!clazz.getGeneralizations().isEmpty()) {
            ifRuntimeNull.addToElsePart(ifInverse);
            ifRuntimeNull.addToElsePart(ifNotNull);
            initialiseProperty.getBody().addToStatements(ifRuntimeNull);
        } else {
            initialiseProperty.getBody().addToStatements(ifInverse);
            initialiseProperty.getBody().addToStatements(ifNotNull);
        }
    }

    private void addZInternalCollectionAdder(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation z_addToInternalCollection= new OJAnnotatedOperation("z_addToInternalCollection");
        UmlgGenerationUtil.addOverrideAnnotation(z_addToInternalCollection);
        z_addToInternalCollection.addToParameters(new OJParameter("umlgRuntimeProperty", UmlgGenerationUtil.umlgRuntimePropertyPathName));
        z_addToInternalCollection.addToParameters(new OJParameter("umlgNode", UmlgGenerationUtil.UMLG_NODE));
        annotatedClass.addToOperations(z_addToInternalCollection);

        OJField runtimeProperty = new OJField("runtimeProperty", new OJPathName(UmlgClassOperations.propertyEnumName(clazz)));
        if (!clazz.getGeneralizations().isEmpty()) {

            OJSimpleStatement ojSimpleStatement = new OJSimpleStatement();
            ojSimpleStatement.setExpression("super.z_addToInternalCollection(umlgRuntimeProperty, umlgNode)");
            z_addToInternalCollection.getBody().addToStatements(ojSimpleStatement);

        }
        z_addToInternalCollection.getBody().addToLocals(runtimeProperty);

        OJSimpleStatement ojSimpleStatement = new OJSimpleStatement();
        ojSimpleStatement.setExpression("runtimeProperty = " + "(" + UmlgClassOperations.propertyEnumName(clazz)
                + ".fromQualifiedName(umlgRuntimeProperty.getQualifiedName()))");
        z_addToInternalCollection.getBody().addToStatements(ojSimpleStatement);

        OJIfStatement ojIfStatement = new OJIfStatement();
        ojIfStatement.setCondition("runtimeProperty != null");
        OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
        ojSwitchStatement.setCondition("runtimeProperty");

        for (Property p : UmlgClassOperations.getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
//            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && !pWrap.isRefined()) {
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && !pWrap.isEnumeration() && !pWrap.isRefined() && !pWrap.isPrimitive() && !pWrap.isDataType()) {
                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(pWrap.fieldname());
                OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.fieldname() + ".z_internalAdder((" + pWrap.javaBaseTypePath().getLast() + ")umlgNode)");
                statement.setName(pWrap.fieldname());
                ojSwitchCase.getBody().addToStatements(statement);
                annotatedClass.addToImports(pWrap.javaImplTypePath());
                ojSwitchStatement.addToCases(ojSwitchCase);

                if (pWrap.isMemberOfAssociationClass()) {
                    ojSwitchCase = new OJSwitchCase();
                    ojSwitchCase.setLabel(pWrap.getAssociationClassFakePropertyName());
                    statement = new OJSimpleStatement("this." + pWrap.getAssociationClassFakePropertyName() + ".z_internalAdder((" + pWrap.getAssociationClassPathName().getLast() + ")umlgNode)");
                    statement.setName(pWrap.fieldname());
                    ojSwitchCase.getBody().addToStatements(statement);
                    annotatedClass.addToImports(pWrap.javaImplTypePath());
                    ojSwitchStatement.addToCases(ojSwitchCase);
                }
            }
        }

        ojIfStatement.addToThenPart(ojSwitchStatement);
        z_addToInternalCollection.getBody().addToStatements(ojIfStatement);

        if (clazz instanceof AssociationClass) {
            //Do the property for the association class
            AssociationClass associationClass = (AssociationClass) clazz;
            List<Property> memberEnds = associationClass.getMemberEnds();
            for (Property memberEnd : memberEnds) {
                PropertyWrapper pWrap = new PropertyWrapper(memberEnd);

                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(pWrap.fieldname());

                OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.fieldname() + ".z_internalAdder((" + pWrap.javaBaseTypePath().getLast() + ")umlgNode)");
                statement.setName(pWrap.fieldname());
                ojSwitchCase.getBody().addToStatements(statement);
                ojSwitchStatement.addToCases(ojSwitchCase);
                annotatedClass.addToImports(UmlgPropertyOperations.getDefaultTinkerCollection(memberEnd, true));
                annotatedClass.addToImports(UmlgGenerationUtil.PropertyTree);
            }
        }
    }

    private void addInitialiseProperty(OJAnnotatedClass annotatedClass, Classifier classifier) {
        OJAnnotatedOperation initialiseProperty = new OJAnnotatedOperation(INITIALISE_PROPERTY);
        UmlgGenerationUtil.addOverrideAnnotation(initialiseProperty);
        initialiseProperty.addParam("tumlRuntimeProperty", UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
        initialiseProperty.addParam("inverse", "boolean");
        if (!classifier.getGeneralizations().isEmpty()) {
            initialiseProperty.getBody().addToStatements("super.initialiseProperty(tumlRuntimeProperty, inverse)");
        }
        annotatedClass.addToOperations(initialiseProperty);

        OJField runtimePropoerty = new OJField("runtimeProperty", new OJPathName(UmlgClassOperations.propertyEnumName(classifier)));
        initialiseProperty.getBody().addToLocals(runtimePropoerty);
        OJIfStatement ifInverse = new OJIfStatement("!inverse");
        ifInverse.addToThenPart("runtimeProperty = " + "(" + UmlgClassOperations.propertyEnumName(classifier)
                + ".fromQualifiedName(tumlRuntimeProperty.getQualifiedName()))");
        ifInverse.addToElsePart("runtimeProperty = " + "(" + UmlgClassOperations.propertyEnumName(classifier)
                + ".fromQualifiedName(tumlRuntimeProperty.getInverseQualifiedName()))");
        initialiseProperty.getBody().addToStatements(ifInverse);

        OJIfStatement ifNotNull = new OJIfStatement("runtimeProperty != null");
        initialiseProperty.getBody().addToStatements(ifNotNull);
        OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
        ojSwitchStatement.setCondition("runtimeProperty");
        ifNotNull.addToThenPart(ojSwitchStatement);

        for (Property p : UmlgClassOperations.getAllOwnedProperties(classifier)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && !pWrap.isRefined()) {
                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(pWrap.fieldname());
                OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.fieldname() + " = " + pWrap.javaDefaultInitialisation(classifier));
                statement.setName(pWrap.fieldname());
                ojSwitchCase.getBody().addToStatements(statement);

                if (pWrap.isMemberOfAssociationClass()) {
                    statement = new OJSimpleStatement("this." + pWrap.getAssociationClassFakePropertyName() + " = " + pWrap.javaDefaultInitialisationForAssociationClass(classifier));
                    statement.setName(pWrap.getAssociationClassFakePropertyName());
                    ojSwitchCase.getBody().addToStatements(statement);
                }

                annotatedClass.addToImports(pWrap.javaImplTypePath());
                ojSwitchStatement.addToCases(ojSwitchCase);
            }
        }

        if (classifier instanceof AssociationClass) {
            //Do the property for the association class
            AssociationClass associationClass = (AssociationClass) classifier;
            List<Property> memberEnds = associationClass.getMemberEnds();
            for (Property memberEnd : memberEnds) {
                PropertyWrapper pWrap = new PropertyWrapper(memberEnd);

                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(pWrap.fieldname());

                OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.fieldname() + " = " + pWrap.javaDefaultInitialisation(classifier, true));
                statement.setName(pWrap.fieldname());
                ojSwitchCase.getBody().addToStatements(statement);
                ojSwitchStatement.addToCases(ojSwitchCase);
                annotatedClass.addToImports(UmlgPropertyOperations.getDefaultTinkerCollection(memberEnd, true));
                annotatedClass.addToImports(UmlgGenerationUtil.PropertyTree);
            }
        }

    }

    private void addGetQualifiers(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation getQualifiers = new OJAnnotatedOperation("getQualifiers");
        UmlgGenerationUtil.addOverrideAnnotation(getQualifiers);
        getQualifiers.setComment("getQualifiers is called from the collection in order to update the index used to implement the qualifier");
        getQualifiers.addParam("tumlRuntimeProperty", UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
        getQualifiers.addParam("node", UmlgGenerationUtil.UMLG_NODE);
        getQualifiers.addParam("inverse", "boolean");
        getQualifiers.setReturnType(new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UmlgQualifierPathName));
        annotatedClass.addToOperations(getQualifiers);

        OJField result = null;
        if (!clazz.getGeneralizations().isEmpty()) {
            result = new OJField(getQualifiers.getBody(), "result", getQualifiers.getReturnType(), "super.getQualifiers(tumlRuntimeProperty, node, inverse)");
        } else {
            result = new OJField(getQualifiers.getBody(), "result", getQualifiers.getReturnType(), "Collections.emptyList()");
        }

        OJField runtimeProperty = new OJField(getQualifiers.getBody(), "runtimeProperty", new OJPathName(UmlgClassOperations.propertyEnumName(clazz)));
        OJIfStatement ifInverse = new OJIfStatement("!inverse");
        ifInverse.addToThenPart("runtimeProperty = " + UmlgClassOperations.propertyEnumName(clazz)
                + ".fromQualifiedName(tumlRuntimeProperty.getQualifiedName())");
        ifInverse.addToElsePart("runtimeProperty = " + UmlgClassOperations.propertyEnumName(clazz)
                + ".fromQualifiedName(tumlRuntimeProperty.getInverseQualifiedName())");
        getQualifiers.getBody().addToStatements(ifInverse);

        OJIfStatement ifRuntimePropertyNotNull = new OJIfStatement(runtimeProperty.getName() + " != null && result.isEmpty()");
        getQualifiers.getBody().addToStatements(ifRuntimePropertyNotNull);

        OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
        ojSwitchStatement.setCondition("runtimeProperty");
        ifRuntimePropertyNotNull.addToThenPart(ojSwitchStatement);

        for (Property p : UmlgClassOperations.getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (pWrap.isQualified()) {
                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(pWrap.fieldname());
                OJSimpleStatement statement = new OJSimpleStatement("result = " + pWrap.getQualifiedGetterName() + "((" + pWrap.getType().getName() + ")node)");
                statement.setName(pWrap.fieldname());
                ojSwitchCase.getBody().addToStatements(statement);
                annotatedClass.addToImports(pWrap.javaImplTypePath());
                ojSwitchStatement.addToCases(ojSwitchCase);
            }
        }
        OJSwitchCase ojSwitchCase = new OJSwitchCase();
        ojSwitchCase.getBody().addToStatements("result = Collections.emptyList()");
        ojSwitchStatement.setDefCase(ojSwitchCase);

        getQualifiers.getBody().addToStatements("return " + result.getName());
        annotatedClass.addToImports("java.util.Collections");
    }

    private void addGetSize(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation getSize = new OJAnnotatedOperation("getSize");
        UmlgGenerationUtil.addOverrideAnnotation(getSize);
        getSize.setComment("getSize is called from the BaseCollection.addInternal in order to save the size of the inverse collection to update the edge's sequence order");
        getSize.addParam("inverse", new OJPathName("boolean"));
        getSize.addParam("tumlRuntimeProperty", UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
        getSize.setReturnType(new OJPathName("int"));
        annotatedClass.addToOperations(getSize);

        OJField result;
        if (!clazz.getGeneralizations().isEmpty()) {
            result = new OJField(getSize.getBody(), "result", getSize.getReturnType(), "super.getSize(inverse, tumlRuntimeProperty)");
        } else {
            result = new OJField(getSize.getBody(), "result", getSize.getReturnType(), "0");
        }

        OJField runtimeProperty = new OJField(getSize.getBody(), "runtimeProperty", new OJPathName(UmlgClassOperations.propertyEnumName(clazz)));
        OJIfStatement ojIfInverse = new OJIfStatement(
                "!inverse",
                "runtimeProperty = " + UmlgClassOperations.propertyEnumName(clazz) + ".fromQualifiedName(tumlRuntimeProperty.getQualifiedName())",
                "runtimeProperty = " + UmlgClassOperations.propertyEnumName(clazz) + ".fromQualifiedName(tumlRuntimeProperty.getInverseQualifiedName())"
        );
        getSize.getBody().addToStatements(ojIfInverse);

        OJIfStatement ifRuntimePropertyNotNull = new OJIfStatement(runtimeProperty.getName() + " != null && result == 0");
        getSize.getBody().addToStatements(ifRuntimePropertyNotNull);

        OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
        ojSwitchStatement.setCondition("runtimeProperty");
        ifRuntimePropertyNotNull.addToThenPart(ojSwitchStatement);

        for (Property p : UmlgClassOperations.getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isDerived() && !pWrap.isRefined()) {
                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(pWrap.fieldname());
                OJSimpleStatement statement = new OJSimpleStatement("result = " + pWrap.fieldname() + ".size()");
                statement.setName(pWrap.fieldname());
                ojSwitchCase.getBody().addToStatements(statement);
                annotatedClass.addToImports(pWrap.javaImplTypePath());
                ojSwitchStatement.addToCases(ojSwitchCase);
            }
        }
        OJSwitchCase ojSwitchCase = new OJSwitchCase();
        ojSwitchCase.getBody().addToStatements("result = 0");
        ojSwitchStatement.setDefCase(ojSwitchCase);

        getSize.getBody().addToStatements("return " + result.getName());
        annotatedClass.addToImports("java.util.Collections");
    }

    private void addGetMetaDataAsJson(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation getMetaDataAsJSon = new OJAnnotatedOperation("getMetaDataAsJson", new OJPathName("String"));
        getMetaDataAsJSon.getBody().addToStatements("return " + UmlgClassOperations.className(clazz) + "." + UmlgClassOperations.propertyEnumName(clazz) + ".asJson()");
        annotatedClass.addToOperations(getMetaDataAsJSon);
        UmlgGenerationUtil.addOverrideAnnotation(getMetaDataAsJSon);
    }

}

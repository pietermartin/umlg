package org.umlg.javageneration.visitor.property;

import java.util.Iterator;
import java.util.List;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.umlg.framework.ModelLoader;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.java8.ForEachStatement;
import org.umlg.javageneration.util.*;
import org.umlg.javageneration.visitor.BaseVisitor;

public class QualifierVisitor extends BaseVisitor implements Visitor<Property> {

    public QualifierVisitor(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        if (pWrap.hasQualifiers() && !pWrap.isRefined()) {
            //This generates the method that returns the umlg qualifier, i.e. org.umlg.runtime.collection.Qualifier
            generateQualifierGetter(findOJClass(pWrap), pWrap);
            //This generates the getter that takes qualifier value as input
            //In this case they are the same as the qualifier is not refined
            generateQualifiedGetter(pWrap, pWrap);
            //update the index on setters of the properties as specified on the QualifierListener stereotype
            generateUpdateOfIndex(pWrap);

            List<Property> refinedQualifieds = pWrap.getRefinedQualifieds();
            for (Property refinedQualified : refinedQualifieds) {
                generateQualifiedGetter(pWrap, new PropertyWrapper(refinedQualified));
                generateUpdateOfIndex(new PropertyWrapper(refinedQualified));
            }

        }
    }

    @Override
    public void visitAfter(Property p) {
    }

    private void validateHasCorrespondingDerivedProperty(PropertyWrapper qualifier) {
        if (!qualifier.haveQualifierCorrespondingDerivedProperty()) {
            throw new IllegalStateException(String.format("Qualifier %s on %s does not have a corresponding derived property on %s",
                    new Object[]{qualifier.getName(), qualifier.getOwner(), qualifier.getQualifierContext().getName()}));
        }
    }

    private void generateQualifierGetter(OJAnnotatedClass ojClass, PropertyWrapper qualified) {
        OJAnnotatedOperation qualifierGetter = new OJAnnotatedOperation(qualified.getQualifiedGetterName());
        qualifierGetter.addParam("context", qualified.getQualifierContextPathName());
        ojClass.addToOperations(qualifierGetter);
        OJField result = new OJField();
        result.setName("result");
        result.setType(new OJPathName("java.util.List"));
        result.getType().addToElementTypes(UmlgGenerationUtil.UmlgQualifierPathName);
        result.setInitExp("new ArrayList<" + UmlgGenerationUtil.UmlgQualifierPathName.getLast() + ">()");
        ojClass.addToImports("java.util.ArrayList");
        qualifierGetter.setReturnType(result.getType());
        qualifierGetter.getBody().addToLocals(result);

        buildUMLGQualifier(ojClass, qualified, qualifierGetter);
        List<Property> refinedQualifiers = qualified.getRefinedQualifieds();
        for (Property refinedQualifier : refinedQualifiers) {
            buildUMLGQualifier(ojClass, new PropertyWrapper(refinedQualifier), qualifierGetter);
        }

        qualifierGetter.getBody().addToStatements("return result");
        ojClass.addToImports(UmlgGenerationUtil.UmlgQualifierIdFactory);
        ojClass.addToImports(UmlgGenerationUtil.UmlgQualifierPathName);
        ojClass.addToImports(UmlgGenerationUtil.umlgMultiplicityPathName);
    }

    private void buildUMLGQualifier(OJAnnotatedClass ojClass, PropertyWrapper qualified, OJAnnotatedOperation qualifierGetter) {
        StringBuilder sb = new StringBuilder();
        sb.append("result.add(");
        sb.append("new ");
        sb.append(UmlgGenerationUtil.UmlgQualifierPathName.getLast());
        sb.append("(new String[]{");

        for (Iterator<Property> iterator = qualified.getQualifiers().iterator(); iterator.hasNext(); ) {
            PropertyWrapper qWrap = new PropertyWrapper(iterator.next());
            sb.append(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast());
            sb.append(".getUmlgLabelConverter().convert(");
            sb.append("\"");
            sb.append(qWrap.getPersistentName());
            sb.append("\")");
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        ojClass.addToImports(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName);
        sb.append("}, new String[]{");
        for (Iterator<Property> iterator = qualified.getQualifiers().iterator(); iterator.hasNext(); ) {
            PropertyWrapper qWrap = new PropertyWrapper(iterator.next());
            sb.append("context.");
            sb.append(qWrap.getter());
            sb.append("() == null ? ");
            sb.append(UmlgGenerationUtil.UmlgQualifierIdFactory.getLast() + ".getUmlgQualifierId().getId(this) + \"___NULL___\" : " + UmlgGenerationUtil.UmlgQualifierIdFactory.getLast() + ".getUmlgQualifierId().getId(this) + ");
            sb.append("context.");
            sb.append(qWrap.getter());
            sb.append("().toString() ");
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }

        sb.append("}, ");
        sb.append(UmlgGenerationUtil.calculateMultiplcity(qualified));
        sb.append("))");
        qualifierGetter.getBody().addToStatements(sb.toString());
    }

    private void generateQualifiedGetter(PropertyWrapper qualified, PropertyWrapper refinedQualifier) {
        List<PropertyWrapper> qualifiers = refinedQualifier.getQualifiersAsPropertyWrappers();
        for (PropertyWrapper qualifier : qualifiers) {
            validateHasCorrespondingDerivedProperty(qualifier);
        }

        //TODO might be a intermittent bug in getting owning type logic for many to manies
        Type qualifiedClassifier = qualified.getOwningType();
        OJAnnotatedClass ojClass = findOJClass(qualifiedClassifier);

        OJAnnotatedOperation qualifierValue = new OJAnnotatedOperation(refinedQualifier.getQualifiedNameFor(qualifiers));
        if (refinedQualifier.isUnqualifiedOne()) {
            qualifierValue.setReturnType(refinedQualifier.javaBaseTypePath());
        } else {
            // This needs to only return a Set or Bag for now, not sorting the
            // result
            // by index as yet
            qualifierValue.setReturnType(refinedQualifier.javaTypePath());
        }
        for (PropertyWrapper qualifier : qualifiers) {
            qualifierValue.addParam(qualifier.fieldname(), qualifier.javaBaseTypePath());
        }
//        ojClass.addToImports(UmlgGenerationUtil.tinkerDirection);
        ojClass.addToImports(UmlgGenerationUtil.edgePathName);

        OJBlock elseBlock = new OJBlock();
        OJField indexKey = new OJField(elseBlock, "indexKey", new OJPathName("String"));
        StringBuilder init = new StringBuilder();
        int count = 0;

        for (PropertyWrapper qualifier : qualifiers) {
            count++;
            init.append(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast());
            init.append(".getUmlgLabelConverter().convert(");
            init.append("\"" + qualifier.getPersistentName() + "\")");
            if (count != qualifiers.size()) {
                init.append(" + ");
            }
        }
        indexKey.setInitExp(init.toString());
        elseBlock.addToLocals(indexKey);

        OJField indexValue = new OJField(elseBlock, "indexValue", new OJPathName("String"));
        boolean first = true;
        for (PropertyWrapper qualifier : qualifiers) {
            if (first) {
                first = false;
                indexValue.setInitExp(qualifier.fieldname() + " == null ? " + UmlgGenerationUtil.UmlgQualifierIdFactory.getLast() + ".getUmlgQualifierId().getId(this) + \"___NULL___\" : " + UmlgGenerationUtil.UmlgQualifierIdFactory.getLast() + ".getUmlgQualifierId().getId(this) + " + qualifier.fieldname());
            } else {
                elseBlock.addToStatements("indexValue += " + qualifier.fieldname() + " == null ? " + UmlgGenerationUtil.UmlgQualifierIdFactory.getLast() + ".getUmlgQualifierId().getId(this) + \"___NULL___\" : " + UmlgGenerationUtil.UmlgQualifierIdFactory.getLast() + ".getUmlgQualifierId().getId(this) + " + qualifier.fieldname());
            }
        }

        elseBlock.addToStatements("Iterator<Edge> iterator = " + UmlgGenerationUtil.UMLGAccess + ".V().has(indexKey, indexValue).bothE()");

        qualifierValue.getBody().addToStatements(elseBlock);
        ojClass.addToImports("java.util.Iterator");
        OJIfStatement ifHasNext = new OJIfStatement("iterator.hasNext()");
        if (refinedQualifier.isUnqualifiedOne()) {
            OJIfStatement ifControllingSide = new OJIfStatement();
            ifControllingSide.setCondition(UmlgClassOperations.propertyEnumName(qualifiedClassifier) + "." + qualified.fieldname() + ".isControllingSide()");
            ifControllingSide.addToThenPart("return new " + qualified.javaBaseTypePath().getLast() + "(iterator.next().inV().next())");
            ifControllingSide.addToElsePart("return new " + qualified.javaBaseTypePath().getLast() + "(iterator.next().outV().next())");
            ifHasNext.addToThenPart(ifControllingSide);
            ifHasNext.addToElsePart("return null");
        } else {
            OJSimpleStatement ojSimpleStatement;
            ojSimpleStatement = new OJSimpleStatement("return new "
                    + qualified.javaClosableIteratorTypePath().getCopy().getLast());
            ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + "(iterator, " + qualified.getTumlRuntimePropertyEnum() + ")");
            ojClass.addToImports(qualified.javaClosableIteratorTypePath());
            ifHasNext.addToThenPart(ojSimpleStatement);
            ifHasNext.addToElsePart("return " + qualified.emptyCollection());
            ojClass.addToImports(UmlgGenerationUtil.umlgUmlgCollections);
        }

        qualifierValue.getBody().addToStatements(ifHasNext);
        ojClass.addToOperations(qualifierValue);
    }

    private void generateUpdateOfIndex(PropertyWrapper qualified) {

        for (Iterator<Property> iterator = qualified.getQualifiers().iterator(); iterator.hasNext(); ) {
            PropertyWrapper qualifierPWrap = new PropertyWrapper(iterator.next());
            OJAnnotatedClass contextOJClass = findOJClass(qualifierPWrap);
            //find the property to listen on via the qualifierListener stereotype

            Classifier targetClassifier = (Classifier) qualified.getType();
            Property derivedQualifierProperty = targetClassifier.getAttribute(qualifierPWrap.getName(), qualifierPWrap.getType());
            if (!derivedQualifierProperty.isDerived()) {
                throw new IllegalStateException(String.format("Property %p for qualifier %s must be derived", new String[]{derivedQualifierProperty.getQualifiedName(), qualifierPWrap.getQualifiedName()}));
            }

            Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgProfileEnum.QualifierListener.name());
            if (derivedQualifierProperty.isStereotypeApplied(stereotype)) {
                List<Property> properties = (List<Property>) derivedQualifierProperty.getValue(stereotype, "property");
                for (Property p : properties) {
                    PropertyWrapper propertyToListenOn = new PropertyWrapper(p);
                    OJAnnotatedOperation setter;
                    if (propertyToListenOn.isMany()) {
                        setter = contextOJClass.findOperation(propertyToListenOn.setter(), propertyToListenOn.javaTypePath());
                    } else {
                        setter = contextOJClass.findOperation(propertyToListenOn.setter(), propertyToListenOn.javaBaseTypePath());
                    }

                    generateUpdateMethodToAddToSetter(setter, qualified, qualifierPWrap, propertyToListenOn);
                    generateUpdateIndexMethodForQualifier(contextOJClass, qualified, qualifierPWrap, propertyToListenOn);

                }
            } else {
                throw new IllegalStateException(String.format("Qualified property %s does not have a the QualifierListener stereotype applied on its corresponding derived property %s.", new String[]{qualified.getQualifiedName(), derivedQualifierProperty.getQualifiedName()}));
            }
        }

    }

    private void generateUpdateIndexMethodForQualifier(OJAnnotatedClass contextOJClass, PropertyWrapper qualified, PropertyWrapper qualifier, PropertyWrapper propertyToListenOn) {
        OJAnnotatedOperation updateIndexForQualifier = new OJAnnotatedOperation(qualifier.updateIndexForQualifierName());

        PropertyWrapper otherEnd = new PropertyWrapper(qualified.getOtherEnd());

        OJForStatement forElementsOnOtherSide = null;
        if (otherEnd.isMany()) {
            forElementsOnOtherSide = new OJForStatement(otherEnd.fieldname(), otherEnd.javaBaseTypePath(), "this." + otherEnd.getter() + "()");
        }

        OJField qualifierField = new OJField("qualifier", UmlgGenerationUtil.UmlgQualifierPathName);
        StringBuilder sb = new StringBuilder();
        sb.append("new ");
        sb.append(UmlgGenerationUtil.UmlgQualifierPathName.getLast());
        sb.append("(new String[]{");
        sb.append(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast());
        sb.append(".getUmlgLabelConverter().convert(\"");
        sb.append(qualifier.getPersistentName());
        sb.append("\")}, new String[]{");
        sb.append(qualifier.getter());
        sb.append("() == null ? ");
        sb.append(UmlgGenerationUtil.UmlgQualifierIdFactory.getLast());
        sb.append(".getUmlgQualifierId().getId(");
        if (otherEnd.isMany()) {
            sb.append(otherEnd.fieldname());
        } else {
            sb.append("this.");
            sb.append(otherEnd.getter());
            sb.append("()");
        }
        sb.append(") + \"___NULL___\" : ");
        sb.append(UmlgGenerationUtil.UmlgQualifierIdFactory.getLast());
        sb.append(".getUmlgQualifierId().getId(");
        if (otherEnd.isMany()) {
            sb.append(otherEnd.fieldname());
        } else {
            sb.append("this.");
            sb.append(otherEnd.getter());
            sb.append("()");
        }
        sb.append(") + this.");
        sb.append(qualifier.getter());
        sb.append("().toString()}, ");
        sb.append(UmlgGenerationUtil.calculateMultiplcity(qualified));
        sb.append(")");
        qualifierField.setInitExp(sb.toString());

        if (otherEnd.isMany()) {
            forElementsOnOtherSide.getBody().addToLocals(qualifierField);
        } else {
            updateIndexForQualifier.getBody().addToLocals(qualifierField);
        }

        sb = new StringBuilder();
        sb.append("this.vertex.edgesFor(");
        sb.append(UmlgClassOperations.propertyEnumName(qualified.getType()));
        sb.append(".");
        sb.append(new PropertyWrapper(qualified.getOtherEnd()).fieldname());
        sb.append(".isControllingSide() ? ");
        sb.append(UmlgGenerationUtil.tinkerDirection.getLast());
        sb.append(".OUT : ");
        sb.append(UmlgGenerationUtil.tinkerDirection.getLast());
        sb.append(".IN, ");
        sb.append(UmlgClassOperations.propertyEnumName(qualified.getType()));
        sb.append(".");
        sb.append(otherEnd.fieldname());
        sb.append(".getLabel())");

        ForEachStatement forEachStatement = new ForEachStatement(sb.toString(), "edge");
        OJSimpleStatement ojSimpleStatement = new OJSimpleStatement();
        ojSimpleStatement.addToExpression("edge.property(qualifier.getKey(), qualifier.getValue())");
        forEachStatement.addStatement(ojSimpleStatement);

//        OJAnnotatedField edgesIter = new OJAnnotatedField("iter", new OJPathName("java.lang.Iterable").addToGenerics(UmlgGenerationUtil.edgePathName));
//        sb = new StringBuilder();
//        sb.append("this.vertex.getEdges(");
//        sb.append(UmlgClassOperations.propertyEnumName(qualified.getType()));
//        sb.append(".");
//        sb.append(new PropertyWrapper(qualified.getOtherEnd()).fieldname());
//        sb.append(".isControllingSide() ? ");
//        sb.append(UmlgGenerationUtil.tinkerDirection.getLast());
//        sb.append(".OUT : ");
//        sb.append(UmlgGenerationUtil.tinkerDirection.getLast());
//        sb.append(".IN, ");
//        sb.append(UmlgClassOperations.propertyEnumName(qualified.getType()));
//        sb.append(".");
//        sb.append(otherEnd.fieldname());
//        sb.append(".getLabel())");
//        edgesIter.setInitExp(sb.toString());
//        updateIndexForQualifier.getBody().addToLocals(edgesIter);
//        OJForStatement forIndexedEdges = new OJForStatement("edge", UmlgGenerationUtil.edgePathName, "iter");
//        forIndexedEdges.getBody().addToStatements("edge.setProperty(qualifier.getKey(), qualifier.getValue())");

        if (otherEnd.isMany()) {
            forElementsOnOtherSide.getBody().addToStatements(forEachStatement);
            updateIndexForQualifier.getBody().addToStatements(forElementsOnOtherSide);
        } else {
            updateIndexForQualifier.getBody().addToStatements(forEachStatement);
        }

        contextOJClass.addToOperations(updateIndexForQualifier);
        contextOJClass.addToImports(UmlgGenerationUtil.tinkerDirection);
        contextOJClass.addToImports(UmlgGenerationUtil.umlgMultiplicityPathName);
        contextOJClass.addToImports(UmlgGenerationUtil.UmlgQualifierIdFactory);
        contextOJClass.addToImports(UmlgGenerationUtil.UmlgQualifierPathName);
    }

    private void generateUpdateMethodToAddToSetter(OJAnnotatedOperation setter, PropertyWrapper qualified, PropertyWrapper qualifier, PropertyWrapper propertyToListenOn) {

        OJIfStatement ifOtherSideIsNotNull = new OJIfStatement(
                "this." + new PropertyWrapper(qualified.getOtherEnd()).getter() + "() != null",
                "this." + qualifier.updateIndexForQualifierName() + "()");
        setter.getBody().addToStatements(ifOtherSideIsNotNull);


    }

}

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
import org.umlg.javageneration.visitor.clazz.ClassBuilder;

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
            if (pWrap.getQualifiers().size() > 1) {
                //Partial getters only make sense if there are more than one qualifier.
                //Some qualifiers can be left null then and  the search will be partial
                generateQualifiedPartialGetter(pWrap, pWrap);
            }

            List<Property> refinedQualifieds = pWrap.getRefinedQualifieds();
            for (Property refinedQualified : refinedQualifieds) {
                generateQualifiedGetter(pWrap, new PropertyWrapper(refinedQualified));
            }

            //if the qualified properties corresponding property does not have a default value give it one.
            //This is needed to ensure that the qui works when adding a new qualified classifier in which case the qualified value is still empty.
            addDefaultValue(pWrap);

        }
    }

    private void addDefaultValue(PropertyWrapper qualified) {
        List<PropertyWrapper> qualifiers = qualified.getQualifiersAsPropertyWrappers();
        for (PropertyWrapper qualifier : qualifiers) {
            PropertyWrapper correspondingProperty = qualifier.getQualifierCorrespondingQualifierStereotypedProperty();
            if (correspondingProperty.getDefaultValue() == null) {
                OJAnnotatedClass infOwner = findOJClass(correspondingProperty);
                OJAnnotatedOperation initVariables = infOwner.findOperation(ClassBuilder.INIT_VARIABLES);
                initVariables.getBody().addToStatements(
                        correspondingProperty.setter() + "(" + correspondingProperty.getQualifierJippoDefaultValue() + ")"
                );

            }
        }

    }

    @Override
    public void visitAfter(Property p) {
    }

    private void validateHasCorrespondingDerivedProperty(PropertyWrapper qualifier) {
        if (!qualifier.hasQualifierCorrespondingQualifierVisitorStereotypedProperty()) {
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
        result.setInitExp("new ArrayList<>()");
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
        ojClass.addToImports(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName);

        for (Iterator<Property> iterator = qualified.getQualifiers().iterator(); iterator.hasNext(); ) {
            StringBuilder sb = new StringBuilder();
            sb.append("result.add(");
            sb.append("new ");
            sb.append(UmlgGenerationUtil.UmlgQualifierPathName.getLast());
            sb.append("(");

            PropertyWrapper qWrap = new PropertyWrapper(iterator.next());
            sb.append("\"");
            sb.append(qWrap.getQualifierCorrespondingQualifierStereotypedProperty().getPersistentName());
            sb.append("\", ");
            sb.append("context.");
            sb.append(new PropertyWrapper(qWrap.getQualifierCorrespondingQualifierStereotypedProperty()).getter());
            sb.append("()");
            sb.append(", ");
            sb.append(UmlgGenerationUtil.calculateMultiplcity(qualified));
            sb.append(", ");
            sb.append(UmlgClassOperations.getPathName(qWrap.getQualifierCorrespondingQualifierStereotypedProperty().getOwningType()).getLast());
            sb.append(".");
            sb.append(UmlgClassOperations.propertyEnumName(qWrap.getQualifierCorrespondingQualifierStereotypedProperty().getOwningType()));
            sb.append(".");
            sb.append(qWrap.getQualifierCorrespondingQualifierStereotypedProperty().fieldname());
            sb.append("))");
            qualifierGetter.getBody().addToStatements(sb.toString());
        }
    }

    private void generateQualifiedGetter(PropertyWrapper qualified, PropertyWrapper refinedQualified) {
        List<PropertyWrapper> qualifiers = refinedQualified.getQualifiersAsPropertyWrappers();
        qualifiers.forEach(this::validateHasCorrespondingDerivedProperty);

        //TODO might be a intermittent bug in getting owning type logic for many to manies
        Type qualifiedClassifier = qualified.getOwningType();
        OJAnnotatedClass ojClass = findOJClass(qualifiedClassifier);

        OJAnnotatedOperation qualifierValue = new OJAnnotatedOperation(refinedQualified.getQualifiedNameFor(qualifiers));
        //partialQualifierValue is the same as qualifierValue but returns a List even if the multiplicity is one.
        //This allows some qualifier values to be left null, i.e. not part of the query
        if (refinedQualified.isUnqualifiedOne()) {
            qualifierValue.setReturnType(refinedQualified.javaBaseTypePath());
        } else {
            // This needs to only return a Set or Bag for now, not sorting the
            // result
            // by index as yet
            qualifierValue.setReturnType(refinedQualified.javaTypePath());
        }
        for (PropertyWrapper qualifier : qualifiers) {
            qualifierValue.addParam(qualifier.fieldname(), UmlgGenerationUtil.Pair.getCopy().addToGenerics(UmlgGenerationUtil.token).addToGenerics(qualifier.javaBaseTypePath()));
        }
        ojClass.addToImports(UmlgGenerationUtil.edgePathName);

        OJField graphTraversal = new OJField(qualifierValue.getBody(), "graphTraversal", UmlgGenerationUtil.GraphTraversal.getCopy().addToGenerics(UmlgGenerationUtil.vertexPathName).addToGenerics(UmlgGenerationUtil.vertexPathName));

        StringBuilder hasStatement = new StringBuilder();
        //build the has containers
        hasStatement.append("this.vertex.to(\n        ");
        hasStatement.append(UmlgClassOperations.propertyEnumName(qualifiedClassifier) + "." + qualified.fieldname() + ".isControllingSide() ? ");
        hasStatement.append(UmlgGenerationUtil.tinkerDirection.getLast());
        hasStatement.append(".OUT : ");
        hasStatement.append(UmlgGenerationUtil.tinkerDirection.getLast());
        hasStatement.append(".IN,\n        ");
        hasStatement.append(UmlgClassOperations.propertyEnumName(qualifiedClassifier) + "." + qualified.fieldname() + ".getLabel())");
        graphTraversal.setInitExp(hasStatement.toString());

        hasStatement.append("\n        .has(T.label, \"");
        hasStatement.append(qualifiers.get(0).getQualifierCorrespondingQualifierStereotypedProperty().getOwningType().getName());
        hasStatement.append("\");");


        for (PropertyWrapper qualifier : qualifiers) {
            StringBuilder sb = new StringBuilder();
            sb.append("graphTraversal.has(T.label, \"");
            PropertyWrapper otherEnd = PropertyWrapper.from(qualified.getOtherEnd());
            sb.append(UmlgClassOperations.getPathName(otherEnd.getOwningType()).getLast());
            sb.append("\")");
            qualifierValue.getBody().addToStatements(sb.toString());

            qualifierValue.getBody().addToStatements("graphTraversal.has(" + "\"" +
                    qualifier.getQualifierCorrespondingQualifierStereotypedProperty().getPersistentName() +
                    "\", " +
                    buildSecondFormatter(ojClass, qualifier));
        }

        ojClass.addToImports(UmlgGenerationUtil.Element);
        ojClass.addToImports(UmlgGenerationUtil.tinkerDirection);
        ojClass.addToImports("java.util.Iterator");
        OJIfStatement ifHasNext = new OJIfStatement("graphTraversal.hasNext()");
        if (refinedQualified.isUnqualifiedOne()) {
            ifHasNext.addToThenPart("return new " + qualified.javaBaseTypePath().getLast() + "(graphTraversal.next())");
            ifHasNext.addToElsePart("return null");
        } else {
            OJSimpleStatement ojSimpleStatement;
            ojSimpleStatement = new OJSimpleStatement("return new "
                    + qualified.javaClosableIteratorTypePath().getCopy().getLast());
            ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + "(graphTraversal, " + qualified.getTumlRuntimePropertyEnum() + ")");
            ojClass.addToImports(qualified.javaClosableIteratorTypePath());
            ifHasNext.addToThenPart(ojSimpleStatement);
            ifHasNext.addToElsePart("return " + qualified.emptyCollection());
            ojClass.addToImports(UmlgGenerationUtil.umlgUmlgCollections);
        }

        qualifierValue.getBody().addToStatements(ifHasNext);
        ojClass.addToOperations(qualifierValue);
    }

    private void generateQualifiedPartialGetter(PropertyWrapper qualified, PropertyWrapper refinedQualified) {
        List<PropertyWrapper> qualifiers = refinedQualified.getQualifiersAsPropertyWrappers();
        qualifiers.forEach(this::validateHasCorrespondingDerivedProperty);

        //TODO might be a intermittent bug in getting owning type logic for many to manies
        Type qualifiedClassifier = qualified.getOwningType();
        OJAnnotatedClass ojClass = findOJClass(qualifiedClassifier);

        OJAnnotatedOperation qualifierValue = new OJAnnotatedOperation(refinedQualified.getQualifiedNameForPartial(qualifiers));
        //partialQualifierValue is the same as qualifierValue but returns a List even if the multiplicity is one.
        //This allows some qualifier values to be left null, i.e. not part of the query
        // This needs to only return a Set or Bag for now, not sorting the
        // result
        // by index as yet
        qualifierValue.setReturnType(refinedQualified.javaTypePath());
        for (PropertyWrapper qualifier : qualifiers) {
            qualifierValue.addParam(qualifier.fieldname(), UmlgGenerationUtil.Pair.getCopy().addToGenerics(UmlgGenerationUtil.token).addToGenerics(qualifier.javaBaseTypePath()));
        }
        ojClass.addToImports(UmlgGenerationUtil.edgePathName);

        OJField graphTraversal = new OJField(qualifierValue.getBody(), "graphTraversal", UmlgGenerationUtil.GraphTraversal.getCopy().addToGenerics(UmlgGenerationUtil.vertexPathName).addToGenerics(UmlgGenerationUtil.vertexPathName));

        StringBuilder hasStatement = new StringBuilder();
        //build the has containers
        hasStatement.append("this.vertex.to(\n        ");
        hasStatement.append(UmlgClassOperations.propertyEnumName(qualifiedClassifier) + "." + qualified.fieldname() + ".isControllingSide() ? ");
        hasStatement.append(UmlgGenerationUtil.tinkerDirection.getLast());
        hasStatement.append(".OUT : ");
        hasStatement.append(UmlgGenerationUtil.tinkerDirection.getLast());
        hasStatement.append(".IN,\n        ");
        hasStatement.append(UmlgClassOperations.propertyEnumName(qualifiedClassifier) + "." + qualified.fieldname() + ".getLabel())");
        hasStatement.append("\n        .has(T.label, \"");
        hasStatement.append(qualifiers.get(0).getQualifierCorrespondingQualifierStereotypedProperty().getOwningType().getName());
        hasStatement.append("\")");
        graphTraversal.setInitExp(hasStatement.toString());
        for (PropertyWrapper qualifier : qualifiers) {

            OJIfStatement ifHasNotNull = new OJIfStatement();
            ifHasNotNull.setCondition(qualifier.fieldname() + " != null");
            ifHasNotNull.addToThenPart("graphTraversal.has(" + "\"" +
                    qualifier.getQualifierCorrespondingQualifierStereotypedProperty().getPersistentName() +
                    "\", " +
                    buildSecondFormatter(ojClass, qualifier));
            qualifierValue.getBody().addToStatements(ifHasNotNull);

        }

        ojClass.addToImports(UmlgGenerationUtil.Element);
        ojClass.addToImports(UmlgGenerationUtil.tinkerDirection);
        ojClass.addToImports("java.util.Iterator");
        OJIfStatement ifHasNext = new OJIfStatement("graphTraversal.hasNext()");
        OJSimpleStatement ojSimpleStatement;
        ojSimpleStatement = new OJSimpleStatement("return new "
                + qualified.javaClosableIteratorTypePath().getCopy().getLast());
        ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + "(graphTraversal, " + qualified.getTumlRuntimePropertyEnum() + ")");
        ojClass.addToImports(qualified.javaClosableIteratorTypePath());
        ifHasNext.addToThenPart(ojSimpleStatement);
        ifHasNext.addToElsePart("return " + qualified.emptyCollection());
        ojClass.addToImports(UmlgGenerationUtil.umlgUmlgCollections);

        qualifierValue.getBody().addToStatements(ifHasNext);
        ojClass.addToOperations(qualifierValue);
    }

    private String buildSecondFormatter(OJAnnotatedClass ojClass, PropertyWrapper qualifier) {
        if (qualifier.isEnumeration()) {
            return qualifier.fieldname() + ".getSecond().name())";
        } else if (qualifier.isDataType() && !qualifier.isPrimitive()) {
            ojClass.addToImports(UmlgGenerationUtil.umlgFormatter);
            return UmlgGenerationUtil.umlgFormatter.getLast() + ".format(" +
                    qualifier.getDataTypeEnum().getInitExpression() +
                    ", " +
                    qualifier.fieldname() +
                    ".getSecond()))";
        } else {
            return qualifier.fieldname() + ".getSecond())";
        }
    }

}

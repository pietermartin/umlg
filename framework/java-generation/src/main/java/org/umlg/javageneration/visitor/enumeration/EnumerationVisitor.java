package org.umlg.javageneration.visitor.enumeration;

import org.eclipse.uml2.uml.*;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.java.metamodel.generated.OJVisibilityKindGEN;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.javageneration.visitor.clazz.ClassBuilder;

import java.util.*;

public class EnumerationVisitor extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Enumeration> {

    public EnumerationVisitor(Workspace workspace) {
        super(workspace);
    }

    public void visitBefore(org.eclipse.uml2.uml.Enumeration enumeration) {
        OJEnum ojEnum = new OJEnum(enumeration.getName());
        ojEnum.addToImplementedInterfaces(UmlgGenerationUtil.UmlgEnum);
        OJPackage ojPackage = new OJPackage(Namer.name(enumeration.getNearestPackage()));
        ojEnum.setMyPackage(ojPackage);

        for (EnumerationLiteral literal : enumeration.getOwnedLiterals()) {
            List<Slot> slots = literal.getSlots();
            for (Slot slot : slots) {
                StructuralFeature structuralFeature = slot.getDefiningFeature();
                OJField literalField = new OJField(ojEnum, structuralFeature.getName(), UmlgClassOperations.getPathName(structuralFeature.getType()));
                literalField.setVisibility(OJVisibilityKindGEN.PRIVATE);
            }
            //all literals are assumed to have the same feature
            break;
        }

        ojEnum.implementGetter();
        ojEnum.createConstructorFromFields();

        //build the getter to the other end of the association
        //get a unique list of defining features
        Set<StructuralFeature> definingFeatures = new HashSet<>();
        for (EnumerationLiteral literal : enumeration.getOwnedLiterals()) {
            //build getter for enumeration that have slots pointing towards this enumeration
            List<Slot> incomingSlots = ModelLoader.INSTANCE.findSlotsForThisDataType(literal);
            for (Slot slot : incomingSlots) {
                definingFeatures.add(slot.getDefiningFeature());
            }
        }

        Map<StructuralFeature, OJAnnotatedOperation> definingFeatureGetterMap = new HashMap<>();
        for (StructuralFeature definingFeature : definingFeatures) {
            Property definingFeatureProperty = (Property) definingFeature;
            PropertyWrapper propertyWrapper = PropertyWrapper.from(definingFeatureProperty);
            PropertyWrapper otherEnd = PropertyWrapper.from(propertyWrapper.getOtherEnd());
            OJAnnotatedOperation getter = new OJAnnotatedOperation(
                    otherEnd.getter(),
                    new OJPathName("java.util.List").addToGenerics(UmlgClassOperations.getPathName(otherEnd.getType())));
            OJField result = new OJField(getter.getBody(), "result", new OJPathName("java.util.List").addToGenerics(UmlgClassOperations.getPathName(otherEnd.getType())));
            result.setInitExp("new ArrayList()");
            ojEnum.addToImports("java.util.ArrayList");
            //build a getter for the incoming slot
            OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
            ojSwitchStatement.setCondition("this");
            getter.getBody().addToStatements(ojSwitchStatement);
            getter.getBody().addToStatements("return result");
            ojEnum.addToOperations(getter);

            definingFeatureGetterMap.put(definingFeature, getter);
        }


        //for each literal add a case statement to the getters switch
        for (EnumerationLiteral literal : enumeration.getOwnedLiterals()) {

            List<Slot> incomingSlots = ModelLoader.INSTANCE.findSlotsForThisDataType(literal);
            for (StructuralFeature definingFeature : definingFeatures) {
                OJAnnotatedOperation getter = definingFeatureGetterMap.get(definingFeature);
                OJSwitchStatement ojSwitchStatement = (OJSwitchStatement)getter.getBody().getStatements().get(0);
                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(literal.getName());
                ojSwitchStatement.addToCases(ojSwitchCase);

                for (Slot slot : incomingSlots) {
                    InstanceValue instanceValue = (InstanceValue)slot.getValues().get(0);
                    if (instanceValue.getInstance().equals(literal)) {
                        ojSwitchCase.getBody().addToStatements(
                                "result.add(" +
                                        slot.getOwningInstance().getClassifiers().get(0).getName() +
                                        "." +
                                        slot.getOwningInstance().getName() +
                                        ")"
                        );
                    }
                }
            }

        }

        for (EnumerationLiteral literal : enumeration.getOwnedLiterals()) {
            OJEnumLiteral ojLiteral = new OJEnumLiteral(literal.getName());
            ojEnum.addToLiterals(ojLiteral);
            List<Slot> slots = literal.getSlots();
            for (Slot slot : slots) {
                StructuralFeature structuralFeature = slot.getDefiningFeature();
                for (ValueSpecification valueSpecification : slot.getValues()) {
                    OJField literalField = new OJField(UmlgClassOperations.getPathName(structuralFeature.getType()).getLast().toLowerCase(), UmlgClassOperations.getPathName(structuralFeature.getType()));
                    literalField.setVisibility(OJVisibilityKindGEN.PRIVATE);
                    if (valueSpecification instanceof InstanceValue) {
                        InstanceValue instanceValue = (InstanceValue) valueSpecification;
                        literalField.setInitExp(UmlgClassOperations.getPathName(structuralFeature.getType()).getLast() + "." + instanceValue.getInstance().getName());
                    } else if (valueSpecification instanceof LiteralString) {
                        literalField.setInitExp("\"" + ((LiteralString) valueSpecification).getValue() + "\"");
                    } else if (valueSpecification instanceof LiteralBoolean) {
                        literalField.setInitExp(String.valueOf(((LiteralBoolean) valueSpecification).isValue()));
                    } else if (valueSpecification instanceof LiteralReal) {
                        literalField.setInitExp(String.valueOf(((LiteralReal) valueSpecification).getValue()));
                    } else {
                        throw new RuntimeException(String.format("Enumeration attribute of type %s not yet implemented!", valueSpecification.getName()));
                    }
                    ojLiteral.addToAttributeValues(literalField);
                }
            }
        }

        addToSource(ojEnum);
        ClassBuilder.addGetQualifiedName(ojEnum, enumeration);
    }

    public void visitAfter(org.eclipse.uml2.uml.Enumeration enumeration) {

    }

}

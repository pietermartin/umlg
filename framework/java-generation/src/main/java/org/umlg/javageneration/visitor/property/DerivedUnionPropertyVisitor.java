package org.umlg.javageneration.visitor.property;

import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.generated.OJVisibilityKindGEN;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DerivedUnionPropertyVisitor extends BaseVisitor implements Visitor<Property> {

    private static Logger logger = Logger.getLogger(DerivedUnionPropertyVisitor.class.getPackage().getName());

    public DerivedUnionPropertyVisitor(Workspace workspace) {
        super(workspace);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.umlg.framework.Visitor#visitBefore(org.eclipse.uml2.uml.Element)
     */
    @Override
    public void visitBefore(Property subsettedProperty) {
        PropertyWrapper subsettedPropertyWrapper = new PropertyWrapper(subsettedProperty);
        if (subsettedPropertyWrapper.isDerived() && subsettedPropertyWrapper.isDerivedUnion()) {
            OJAnnotatedClass owner = findOJClass(subsettedProperty);
            OJAnnotatedOperation getter;
            if (subsettedPropertyWrapper.isOne()) {
                getter = new OJAnnotatedOperation(subsettedPropertyWrapper.getter(), subsettedPropertyWrapper.javaBaseTypePath());
            } else {
                getter = new OJAnnotatedOperation(subsettedPropertyWrapper.getter(), subsettedPropertyWrapper.javaTypePath());
            }

            //Get the subsetting properties
            List<Property> subsettingProperties = ModelLoader.INSTANCE.findSubsettingProperties(subsettedProperty);
            //Validate conformTo constraint
            validatePropertyConstraint3(subsettedProperty, subsettingProperties);

            if (subsettedProperty.getType() instanceof Interface) {
                owner.addToOperations(getter);
                buildDerivedUnionForInterface(subsettedPropertyWrapper, subsettingProperties);
            } else {
                buildDerivedUnionForClass(subsettedPropertyWrapper, owner, getter, subsettingProperties);
                owner.addToOperations(getter);
            }


        }
    }

    private void buildDerivedUnionForInterface(PropertyWrapper subsettedPropertyWrapper, List<Property> subsettingProperties) {

        //Find all the interface's realization.
        //For each realization find its properties that are subsetting the subsetted property
        //Add the subsetting property to its implementation of the derived union
        Interface subsettedPropertyType = (Interface)subsettedPropertyWrapper.getType();
        List<InterfaceRealization> interfaceRealizations = ModelLoader.INSTANCE.getInterfaceRealization(subsettedPropertyType);
        for (InterfaceRealization interfaceRealization : interfaceRealizations) {
            BehavioredClassifier behavioredClassifier = interfaceRealization.getImplementingClassifier();

            OJAnnotatedClass realizedInterfaceOJClass = findOJClass(behavioredClassifier);
            OJAnnotatedOperation getter;
            if (subsettedPropertyWrapper.isOne()) {
                getter = new OJAnnotatedOperation(subsettedPropertyWrapper.getter(), subsettedPropertyWrapper.javaBaseTypePath());
            } else {
                getter = new OJAnnotatedOperation(subsettedPropertyWrapper.getter(), subsettedPropertyWrapper.javaTypePath());
            }
            //Each subsetting property gets unioned to form the derived union
            OJField result = new OJField("result", subsettedPropertyWrapper.javaTypePath());
            result.setInitExp("new "+subsettedPropertyWrapper.javaTumlMemoryTypePath()+"<" + subsettedPropertyWrapper.javaBaseTypePath().getLast() + ">()");
            getter.getBody().addToLocals(result);

            Set<Property> allOwnedProperties = UmlgClassOperations.getAllOwnedProperties((Class) behavioredClassifier);
            for (Property ownedProperty : allOwnedProperties) {

                //For each realization find its properties that are subsettingProperties and add them to the derivedUnion
                for (Property subsettingProperty : subsettingProperties) {
                    if (ownedProperty.equals(subsettingProperty)) {

                        PropertyWrapper propertyWrapper = new PropertyWrapper(subsettingProperty);
                        if (subsettedPropertyWrapper.isOne()) {
                            OJIfStatement ojIfStatement = new OJIfStatement(propertyWrapper.getter() + "() != null", "result.add(this." + propertyWrapper.getter() + "())");
                            getter.getBody().addToStatements(ojIfStatement);
                        } else {
                            OJIfStatement ojIfStatement = new OJIfStatement("!" + propertyWrapper.getter() + "().isEmpty()", "result.addAll(this." + propertyWrapper.getter() + "())");
                            getter.getBody().addToStatements(ojIfStatement);
                        }

                    }
                }

            }

            if (subsettedPropertyWrapper.isOne()) {
                //TODO validate multiplicity
                OJIfStatement ojIfStatement = new OJIfStatement("!result.isEmpty()");
                ojIfStatement.addToThenPart("return result.iterator().next()");
                ojIfStatement.addToElsePart("return null");
                getter.getBody().addToStatements(ojIfStatement);
            } else {
                getter.getBody().addToStatements("return result");
            }
            realizedInterfaceOJClass.addToOperations(getter);
        }

    }

    private void buildDerivedUnionForClass(PropertyWrapper subsettedPropertyWrapper, OJAnnotatedClass owner, OJAnnotatedOperation getter, List<Property> subsettingProperties) {
        //Each subsetting property gets unioned to form the derived union
        OJField result = new OJField("result", subsettedPropertyWrapper.javaTypePath());
        result.setInitExp("new "+subsettedPropertyWrapper.javaTumlMemoryTypePath()+"<" + subsettedPropertyWrapper.javaBaseTypePath().getLast() + ">()");
        getter.getBody().addToLocals(result);

        for (Property subsettingProperty : subsettingProperties) {
            PropertyWrapper subsettingPropertyWrapper = new PropertyWrapper(subsettingProperty);

            //Add a protected empty getter for each subsettingProperty
            if (subsettingPropertyWrapper.isOne()) {
                OJAnnotatedOperation subsettingPropertyGetter = new OJAnnotatedOperation(subsettingPropertyWrapper.getter(), subsettingPropertyWrapper.javaBaseTypePath());
                subsettingPropertyGetter.setVisibility(OJVisibilityKindGEN.PROTECTED);
                subsettingPropertyGetter.setComment("Fake getter to help out the derivedUnion " + subsettedPropertyWrapper.getName());
                subsettingPropertyGetter.getBody().addToStatements("return null");
                owner.addToOperations(subsettingPropertyGetter);

                OJIfStatement ojIfStatement = new OJIfStatement(subsettingPropertyWrapper.getter() + "() != null", "result.add(this." + subsettingPropertyWrapper.getter() + "())");
                getter.getBody().addToStatements(ojIfStatement);
            } else {
                OJAnnotatedOperation subsettingPropertyGetter = new OJAnnotatedOperation(subsettingPropertyWrapper.getter(), subsettingPropertyWrapper.javaTumlTypePath());
                subsettingPropertyGetter.setVisibility(OJVisibilityKindGEN.PROTECTED);
                subsettingPropertyGetter.setComment("Fake getter to help out the derivedUnion " + subsettedPropertyWrapper.getName());
                subsettingPropertyGetter.getBody().addToStatements("return new " + subsettingPropertyWrapper.javaTumlMemoryTypePath().getLast() + "()");
                owner.addToImports(subsettingPropertyWrapper.javaTumlMemoryTypePath());
                owner.addToOperations(subsettingPropertyGetter);

                OJIfStatement ojIfStatement = new OJIfStatement("!" + subsettingPropertyWrapper.getter() + "().isEmpty()", "result.addAll(this." + subsettingPropertyWrapper.getter() + "())");
                getter.getBody().addToStatements(ojIfStatement);
            }
        }
        if (subsettedPropertyWrapper.isOne()) {
            //TODO validate multiplicity
            OJIfStatement ojIfStatement = new OJIfStatement("!result.isEmpty()");
            ojIfStatement.addToThenPart("return result.iterator().next()");
            ojIfStatement.addToElsePart("return null");
            getter.getBody().addToStatements(ojIfStatement);
        } else {
            getter.getBody().addToStatements("return result");
        }
    }

    @Override
    public void visitAfter(Property p) {

    }

    /**
     * Subsetting may only occur when the context of the subsetting property conforms to the context of the subsetted property.
     * subsettedProperty->notEmpty() implies
     * (subsettingContext()->notEmpty() and subsettingContext()->forAll (sc |
     * subsettedProperty->forAll(sp |
     * sp.subsettingContext()->exists(c | sc.conformsTo(c)))))
     */
    private void validatePropertyConstraint3(Property subsettedProperty, List<Property> subsettingProperties) {
        for (Property subsettingProperty : subsettingProperties) {
            if (!subsettingProperty.subsettingContext().isEmpty()) {

                for (Type sc : subsettingProperty.subsettingContext()) {
                    for (Type sp : subsettedProperty.subsettingContext()) {
                        if (sp instanceof org.eclipse.uml2.uml.Class && !((Class) sp).getAllImplementedInterfaces().contains(sc) && !sc.conformsTo(sp)) {
                            throw new IllegalStateException(String.format("The context of the subsetting property %s does not conform to the context of the subsetted property %s.", subsettedProperty.getType().getName(), subsettingProperty.getType().getName()));
                        }
                    }
                }

            } else {
                throw new IllegalStateException("???");
            }
        }
    }

}

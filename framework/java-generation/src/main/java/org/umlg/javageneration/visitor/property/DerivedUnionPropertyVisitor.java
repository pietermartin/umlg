package org.umlg.javageneration.visitor.property;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.generated.OJVisibilityKindGEN;
import org.umlg.javageneration.ocl.TumlOcl2Java;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.ocl.UmlgOcl2Parser;

import java.util.List;
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
    public void visitBefore(Property p) {
        PropertyWrapper subsettedPropertyWrapper = new PropertyWrapper(p);
        if (subsettedPropertyWrapper.isDerived() && subsettedPropertyWrapper.isDerivedUnion()) {
            OJAnnotatedClass owner = findOJClass(p);
            OJAnnotatedOperation getter;
            if (subsettedPropertyWrapper.isOne()) {
                getter = new OJAnnotatedOperation(subsettedPropertyWrapper.getter(), subsettedPropertyWrapper.javaBaseTypePath());
            } else {
                getter = new OJAnnotatedOperation(subsettedPropertyWrapper.getter(), subsettedPropertyWrapper.javaTypePath());
            }

            //Get the subsetting properties
            List<Property> subsettingProperties = ModelLoader.INSTANCE.findSubsettingProperties(p);
            //Validate conformTo constraint
            validatePropertyConstraint3(p, subsettingProperties);

            //Each subsetting property gets unioned to form the derived union
            OJField result = new OJField("result", new OJPathName("java.util.List").addToGenerics(subsettedPropertyWrapper.javaBaseTypePath()));
            result.setInitExp("new ArrayList<" + subsettedPropertyWrapper.javaBaseTypePath().getLast() + ">()");
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

                    getter.getBody().addToStatements("result.add(this." + subsettingPropertyWrapper.getter() + "())");
                } else {
                    OJAnnotatedOperation subsettingPropertyGetter = new OJAnnotatedOperation(subsettingPropertyWrapper.getter(), subsettingPropertyWrapper.javaTumlTypePath());
                    subsettingPropertyGetter.setVisibility(OJVisibilityKindGEN.PROTECTED);
                    subsettingPropertyGetter.setComment("Fake getter to help out the derivedUnion " + subsettedPropertyWrapper.getName());
                    subsettingPropertyGetter.getBody().addToStatements("return new " + subsettingPropertyWrapper.javaTumlMemoryTypePath().getLast() + "()");
                    owner.addToImports(subsettingPropertyWrapper.javaTumlMemoryTypePath());
                    owner.addToOperations(subsettingPropertyGetter);

                    getter.getBody().addToStatements("result.addAll(this." + subsettingPropertyWrapper.getter() + "())");
                }
            }
            if (subsettedPropertyWrapper.isOne()) {
                //TODO validate multiplicity
                OJIfStatement ojIfStatement = new OJIfStatement("!result.isEmpty()");
                ojIfStatement.addToThenPart("return result.get(0)");
                ojIfStatement.addToElsePart("return null");
                getter.getBody().addToStatements(ojIfStatement);
            } else {
                getter.getBody().addToStatements("return result");
            }


            owner.addToOperations(getter);
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
                        if (!sc.conformsTo(sp)) {
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

package org.umlg.javageneration.visitor.property;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.operations.PropertyOperations;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgPropertyOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RedefinitionPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	private static Logger logger = Logger.getLogger(RedefinitionPropertyVisitor.class.getPackage().getName());

	public RedefinitionPropertyVisitor(Workspace workspace) {
		super(workspace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umlg.framework.Visitor#visitBefore(org.eclipse.uml2.uml.Element)
	 */
	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (!propertyWrapper.getRedefinedProperties().isEmpty()) {

            //Validate UML spec constraints
            for (Property redefinedProperty : propertyWrapper.getRedefinedProperties()) {

                PropertyWrapper redefinedPropertyWrapper = new PropertyWrapper(redefinedProperty);
                BasicDiagnostic diagnostics = new BasicDiagnostic();
                Map<Object, Object> context = new HashMap<Object, Object>();
                context.put("redefiningProperty", p);
                if (!UmlgPropertyOperations.validateRedefinedPropertyInherited(redefinedProperty, diagnostics, context)) {
                    throw new IllegalStateException(diagnostics.getMessage());
                }

                if (!PropertyOperations.isConsistentWith(redefinedProperty, p)) {
                    throw new IllegalStateException(String.format("query isConsistentWith returns false for %s and %s", redefinedProperty.getQualifiedName(), p.getQualifiedName()));
                }

                //Validate signature possible in java
                //Validate that the redefined property's return type is the same and that the multiplicity did not go from many to one
                if (redefinedProperty.getName().equals(propertyWrapper.getName())) {
                    if (redefinedPropertyWrapper.isMany() && (propertyWrapper.getUpper() == 1)) {
                        throw new IllegalStateException(String.format("UMLG does not support redefinition where the property's name remains the same but the multiplicity has changed from many to one! \nProperty %s is redefined by %s!", redefinedProperty.getQualifiedName(), propertyWrapper.getQualifiedName()));
                    }

                } else {
                    if (!redefinedProperty.getVisibility().equals(OJVisibilityKind.PRIVATE)) {
                        //Ensure that if the property has been renamed that the old name can not be invoked
                        OJAnnotatedClass owner = findOJClass(p);
                        OJAnnotatedOperation getter;
                        if (redefinedPropertyWrapper.isOne()) {
                            getter = new OJAnnotatedOperation(redefinedPropertyWrapper.getter(), redefinedPropertyWrapper.javaBaseTypePath());
                        } else {
                            getter = new OJAnnotatedOperation(redefinedPropertyWrapper.getter(), redefinedPropertyWrapper.javaTypePath());
                        }
                        getter.getBody().addToStatements(String.format("throw new IllegalStateException(\"Property %s has been redefined by %s, please invole the redefined property.\")", redefinedPropertyWrapper.getQualifiedName(), p.getQualifiedName()));
                        UmlgGenerationUtil.addOverrideAnnotation(getter);
                        owner.addToOperations(getter);
                    }
                }


            }


		}
	}

	@Override
	public void visitAfter(Property p) {

	}

}

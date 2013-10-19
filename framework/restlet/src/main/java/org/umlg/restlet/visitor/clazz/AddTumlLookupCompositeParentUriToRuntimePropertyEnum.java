package org.umlg.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.OJConstructor;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class AddTumlLookupCompositeParentUriToRuntimePropertyEnum extends BaseVisitor implements Visitor<Class> {

	public AddTumlLookupCompositeParentUriToRuntimePropertyEnum(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
    @VisitSubclasses({Class.class, AssociationClass.class})
	public void visitBefore(Class clazz) {
		Set<Property> allOwnedProperties = TumlClassOperations.getAllProperties(clazz);
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJEnum ojEnum = annotatedClass.findEnum(TumlClassOperations.propertyEnumName(clazz));
		if (TumlClassOperations.hasLookupProperty(clazz)) {
			// There is only one tumlCompositeParentLookupUri field on the
			// enum
			// even if there are many properties with lookups
			OJField uriPrimitiveField = ojEnum.findField("tumlCompositeParentLookupUri");
			uriPrimitiveField = new OJField();
			uriPrimitiveField.setType(new OJPathName("String"));
			uriPrimitiveField.setName("tumlCompositeParentLookupUri");
			ojEnum.addToFields(uriPrimitiveField);
			OJConstructor constructor = ojEnum.getConstructors().iterator().next();
			constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
			constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

			for (Property p : allOwnedProperties) {
				PropertyWrapper propertyWrapper = new PropertyWrapper(p);
				if (!TumlClassOperations.isEnumeration(propertyWrapper.getOwningType()) && propertyWrapper.hasLookup()) {
					OJAnnotatedOperation getter = new OJAnnotatedOperation("getTumlCompositeParentLookupUri", uriPrimitiveField.getType());
					getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
					ojEnum.addToOperations(getter);
				}
				doWithLiteral(clazz, propertyWrapper, ojEnum.findLiteral(propertyWrapper.fieldname()));

                if (propertyWrapper.isMemberOfAssociationClass() && !(clazz instanceof AssociationClass)) {
                    doWithLiteral(clazz, propertyWrapper, ojEnum.findLiteral(propertyWrapper.getAssociationClassFakePropertyName()), true);
                }
			}

			// Add the lookups to the id property
			doWithLiteral(clazz, null, ojEnum.findLiteral("id"));

			if (!TumlClassOperations.hasCompositeOwner(clazz)) {
				// Add the lookups to the root property
				doWithLiteral(clazz, null, ojEnum.findLiteral(clazz.getModel().getName()));
			}
		}
	}

    private void doWithLiteral(Class clazz, PropertyWrapper propertyWrapper, OJEnumLiteral literal) {
        doWithLiteral(clazz, propertyWrapper, literal, false);
    }

    private void doWithLiteral(Class clazz, PropertyWrapper propertyWrapper, OJEnumLiteral literal, boolean asAssociationClass) {
		String uri;
		if (!TumlClassOperations.getOtherEndToComposite(clazz).isEmpty() && propertyWrapper != null && propertyWrapper.hasLookup()) {
            String contextPath;
            contextPath = ModelLoader.INSTANCE.getModel().getName();
            if (!asAssociationClass) {
                uri = "\"/" + contextPath + "/"
                        + TumlClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "s/{"
                        + TumlClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "Id}/"
                        + propertyWrapper.lookupCompositeParent() + "\"";
            } else {
                uri = "\"/" + contextPath + "/"
                        + propertyWrapper.getAssociationClassPathName().getLast().toLowerCase() + "s/{"
                        + propertyWrapper.getAssociationClassPathName().getLast().toLowerCase() + "Id}/"
                        + propertyWrapper.lookupCompositeParent() + "\"";
            }
		} else {
			// The non lookup fields carry a empty string
			uri = "\"\"";
		}
		OJField uriAttribute = new OJField();
		uriAttribute.setName("tumlCompositeParentLookupUri");
		uriAttribute.setType(new OJPathName("String"));
		uriAttribute.setInitExp(uri);
		literal.addToAttributeValues(uriAttribute);

		OJField jsonField = literal.findAttributeValue("json");
		StringBuilder sb = new StringBuilder();
        sb.append(", \\\"tumlCompositeParentLookupUri\\\": \\");
		// if (literal.getName().equals(propertyWrapper.fieldname())) {
		sb.append(uri.substring(0, uri.length() - 1) + "\\\"");
		String initExp = jsonField.getInitExp();
		int indexOf = initExp.lastIndexOf("}");
		initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";
		jsonField.setInitExp(initExp);
		// } else {
		// sb.append("\"\\\"");
		// String initExp = jsonField.getInitExp();
		// int indexOf = initExp.lastIndexOf("}");
		// initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";
		// jsonField.setInitExp(initExp);
		// }
	}

	@Override
	public void visitAfter(Class element) {
	}

}

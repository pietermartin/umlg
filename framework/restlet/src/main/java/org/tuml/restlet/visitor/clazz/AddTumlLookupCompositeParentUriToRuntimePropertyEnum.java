package org.tuml.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.opaeum.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class AddTumlLookupCompositeParentUriToRuntimePropertyEnum extends BaseVisitor implements Visitor<Class> {

	public AddTumlLookupCompositeParentUriToRuntimePropertyEnum(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Class clazz) {
		Set<Property> allOwnedProperties = TumlClassOperations.getAllProperties(clazz);
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJEnum ojEnum = annotatedClass.findEnum(TumlClassOperations.propertyEnumName(clazz));
		for (Property p : allOwnedProperties) {
			PropertyWrapper propertyWrapper = new PropertyWrapper(p);
			if (propertyWrapper.hasLookup()) {

				// There is only one tumlCompositeParentLookupUri field on the
				// enum
				// even if there are many properties with lookups
				OJField uriPrimitiveField = ojEnum.findField("tumlCompositeParentLookupUri");
				if (uriPrimitiveField == null) {
					uriPrimitiveField = new OJField();
					uriPrimitiveField.setType(new OJPathName("String"));
					uriPrimitiveField.setName("tumlCompositeParentLookupUri");
					ojEnum.addToFields(uriPrimitiveField);
					OJAnnotatedOperation getter = new OJAnnotatedOperation("getTumlCompositeParentLookupUri", uriPrimitiveField.getType());
					getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
					ojEnum.addToOperations(getter);
					OJConstructor constructor = ojEnum.getConstructors().iterator().next();
					constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
					constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());
				}

				doWithLiteral("tumlCompositeParentLookupUri", clazz, propertyWrapper, ojEnum.findLiteral(propertyWrapper.fieldname()), false);

				uriPrimitiveField = ojEnum.findField("tumlCompositeParentLookupOnCompositeParentUri");
				if (uriPrimitiveField == null) {
					uriPrimitiveField = new OJField();
					uriPrimitiveField.setType(new OJPathName("String"));
					uriPrimitiveField.setName("tumlCompositeParentLookupOnCompositeParentUri");
					ojEnum.addToFields(uriPrimitiveField);

					OJAnnotatedOperation getter = new OJAnnotatedOperation("getTumlCompositeParentLookupOnCompositeParentUri", uriPrimitiveField.getType());
					getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
					ojEnum.addToOperations(getter);

					OJConstructor constructor = ojEnum.getConstructors().iterator().next();
					constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
					constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());
				}
				doWithLiteral("tumlCompositeParentLookupOnCompositeParentUri", clazz, propertyWrapper, ojEnum.findLiteral(propertyWrapper.fieldname()), true);

			} else if (!TumlClassOperations.isEnumeration(propertyWrapper.getOwningType())) {
				doWithLiteral("tumlCompositeParentLookupUri", clazz, propertyWrapper, ojEnum.findLiteral(propertyWrapper.fieldname()), false);
				doWithLiteral("tumlCompositeParentLookupOnCompositeParentUri", clazz, propertyWrapper, ojEnum.findLiteral(propertyWrapper.fieldname()), true);
			}
		}

		// Add the lookups to the id property
		doWithLiteral("tumlCompositeParentLookupUri", clazz, null, ojEnum.findLiteral("id"), false);
		doWithLiteral("tumlCompositeParentLookupUriOnCompositeParent", clazz, null, ojEnum.findLiteral("id"), false);

		if (!TumlClassOperations.hasCompositeOwner(clazz)) {
			// Add the lookups to the root property
			doWithLiteral("tumlCompositeParentLookupUri", clazz, null, ojEnum.findLiteral(clazz.getModel().getName()), false);
			doWithLiteral("tumlCompositeParentLookupUriOnCompositeParent", clazz, null, ojEnum.findLiteral(clazz.getModel().getName()), false);
		}

	}

	private void doWithLiteral(String literalName, Class clazz, PropertyWrapper propertyWrapper, OJEnumLiteral literal, boolean onCompositeParent) {
		String uri;
		if (TumlClassOperations.getOtherEndToComposite(clazz) != null && propertyWrapper != null && propertyWrapper.hasLookup()) {
			// if (literal.getName().equals(propertyWrapper.fieldname())) {
			if (!onCompositeParent) {
				uri = "\"/" + propertyWrapper.getModel().getName() + "/"
						+ TumlClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "s/{"
						+ TumlClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "Id}/"
						+ propertyWrapper.lookupCompositeParent() + "\"";
			} else {
				Type type = TumlClassOperations.getOtherEndToComposite(clazz).getType();
				uri = "\"/" + propertyWrapper.getModel().getName() + "/" + TumlClassOperations.getPathName(type).getLast().toLowerCase() + "s/{"
						+ TumlClassOperations.getPathName(type).getLast().toLowerCase() + "Id}/" + propertyWrapper.lookupCompositeParent() + "\"";
			}
		} else {
			// The non lookup fields carry a empty string
			uri = "\"\"";
		}
		// } else {
		// }
		OJField uriAttribute = new OJField();
		uriAttribute.setName(literalName);
		uriAttribute.setType(new OJPathName("String"));
		uriAttribute.setInitExp(uri);
		literal.addToAttributeValues(uriAttribute);

		OJField jsonField = literal.findAttributeValue("json");
		StringBuilder sb = new StringBuilder();
		if (!onCompositeParent) {
			sb.append(", \\\"tumlCompositeParentLookupUri\\\": \\");
		} else {
			sb.append(", \\\"tumlCompositeParentLookupUriOnCompositeParent\\\": \\");
		}
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

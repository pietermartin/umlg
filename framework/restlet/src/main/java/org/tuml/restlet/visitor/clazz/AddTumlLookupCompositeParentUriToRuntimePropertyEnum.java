package org.tuml.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.tuml.framework.ModelLoader;
import org.tuml.java.metamodel.OJConstructor;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJEnum;
import org.tuml.java.metamodel.annotation.OJEnumLiteral;
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

			uriPrimitiveField = new OJField();
			uriPrimitiveField.setType(new OJPathName("String"));
			uriPrimitiveField.setName("tumlCompositeParentLookupOnCompositeParentUri");
			ojEnum.addToFields(uriPrimitiveField);

			constructor = ojEnum.getConstructors().iterator().next();
			constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
			constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

			for (Property p : allOwnedProperties) {
				PropertyWrapper propertyWrapper = new PropertyWrapper(p);
				if (!TumlClassOperations.isEnumeration(propertyWrapper.getOwningType()) && propertyWrapper.hasLookup()) {
					OJAnnotatedOperation getter = new OJAnnotatedOperation("getTumlCompositeParentLookupUri", uriPrimitiveField.getType());
					getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
					ojEnum.addToOperations(getter);

					getter = new OJAnnotatedOperation("getTumlCompositeParentLookupOnCompositeParentUri", uriPrimitiveField.getType());
					getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
					ojEnum.addToOperations(getter);
					
				}
				doWithLiteral("tumlCompositeParentLookupUri", clazz, propertyWrapper, ojEnum.findLiteral(propertyWrapper.fieldname()), false);
				doWithLiteral("tumlCompositeParentLookupOnCompositeParentUri", clazz, propertyWrapper, ojEnum.findLiteral(propertyWrapper.fieldname()), true);
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
	}

	private void doWithLiteral(String literalName, Class clazz, PropertyWrapper propertyWrapper, OJEnumLiteral literal, boolean onCompositeParent) {
		String uri;
		if (TumlClassOperations.getOtherEndToComposite(clazz) != null && propertyWrapper != null && propertyWrapper.hasLookup()) {
            String contextPath;
//            if (ModelLoader.getImportedModelLibraries().contains(propertyWrapper.getModel())) {
//                contextPath = ModelLoader.getModel().getName() + "/" + propertyWrapper.getModel().getName();
//            } else {
                contextPath = ModelLoader.INSTANCE.getModel().getName();
//            }

            if (!onCompositeParent) {
				uri = "\"/" + contextPath + "/"
						+ TumlClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "s/{"
						+ TumlClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "Id}/"
						+ propertyWrapper.lookupCompositeParent() + "\"";
			} else {
				Type type = TumlClassOperations.getOtherEndToComposite(clazz).getType();
				uri = "\"/" + contextPath + "/" + TumlClassOperations.getPathName(type).getLast().toLowerCase() + "s/{"
						+ TumlClassOperations.getPathName(type).getLast().toLowerCase() + "Id}/" + propertyWrapper.lookupCompositeParent() + "\"";
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

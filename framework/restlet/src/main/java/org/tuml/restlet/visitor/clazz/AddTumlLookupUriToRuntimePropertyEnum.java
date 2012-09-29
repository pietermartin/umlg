package org.tuml.restlet.visitor.clazz;

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

public class AddTumlLookupUriToRuntimePropertyEnum extends BaseVisitor implements Visitor<Property> {

	public AddTumlLookupUriToRuntimePropertyEnum(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (propertyWrapper.hasLookup()) {
			OJAnnotatedClass annotatedClass = findOJClass(p);
			OJEnum ojEnum = annotatedClass.findEnum(TumlClassOperations.propertyEnumName(propertyWrapper.getOwningType()));

			OJField uriPrimitiveField = new OJField();
			uriPrimitiveField.setType(new OJPathName("String"));
			uriPrimitiveField.setName("tumlLookupUri");
			ojEnum.addToFields(uriPrimitiveField);

			OJAnnotatedOperation getter = new OJAnnotatedOperation("getTumlLookupUri", uriPrimitiveField.getType());
			getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
			ojEnum.addToOperations(getter);

			OJConstructor constructor = ojEnum.getConstructors().iterator().next();
			constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
			constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

			for (OJEnumLiteral literal : ojEnum.getLiterals()) {
				doWithLiteral(p, propertyWrapper, literal, false);
			}
			
			// Need to create a lookup uri on the parent, to be called by new
			// objects, Seeing as the new object has not yet persisted(in a pass
			// by value gui) it can not call the lookup on itself.
			Property otherEndToComposite = TumlClassOperations.getOtherEndToComposite((Class) propertyWrapper.getOwningType());
			if (otherEndToComposite != null) {

				uriPrimitiveField = new OJField();
				uriPrimitiveField.setType(new OJPathName("String"));
				uriPrimitiveField.setName("tumlLookupOnCompositeParentUri");
				ojEnum.addToFields(uriPrimitiveField);

				getter = new OJAnnotatedOperation("getTumlLookupOnCompositeParentUri", uriPrimitiveField.getType());
				getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
				ojEnum.addToOperations(getter);

				constructor = ojEnum.getConstructors().iterator().next();
				constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
				constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

				for (OJEnumLiteral literal : ojEnum.getLiterals()) {
					doWithLiteral(p, propertyWrapper, literal, true);
				}
			}

		}
	}

	private void doWithLiteral(Property p, PropertyWrapper propertyWrapper, OJEnumLiteral literal, boolean onCompositeParent) {
		// Add tumlLookupUri to literal
		String uri;
		if (literal.getName().equals(propertyWrapper.fieldname())) {
			if (!onCompositeParent) {
				uri = "\"/" + p.getModel().getName() + "/" + TumlClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "s/{"
						+ TumlClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "Id}/" + propertyWrapper.lookup() + "\"";
			} else {
				Type type = TumlClassOperations.getOtherEndToComposite((Class) propertyWrapper.getOwningType()).getType();
				uri = "\"/" + p.getModel().getName() + "/" + TumlClassOperations.getPathName(type).getLast().toLowerCase() + "s/{"
						+ TumlClassOperations.getPathName(type).getLast().toLowerCase() + "Id}/" + propertyWrapper.lookup() + "\"";
			}
		} else {
			// The non lookup fields carry a empty string
			uri = "\"\"";
		}
		OJField uriAttribute = new OJField();
		uriAttribute.setType(new OJPathName("String"));
		uriAttribute.setInitExp(uri);
		literal.addToAttributeValues(uriAttribute);

		OJField jsonField = literal.findAttributeValue("json");
		StringBuilder sb = new StringBuilder();
		if (!onCompositeParent) {
			sb.append(", \\\"tumlLookupUri\\\": \\");
		} else {
			sb.append(", \\\"tumlLookupUriOnCompositeParent\\\": \\");			
		}
		if (literal.getName().equals(propertyWrapper.fieldname())) {
			sb.append(uri.substring(0, uri.length() - 1) + "\\\"");
			String initExp = jsonField.getInitExp();
			int indexOf = initExp.lastIndexOf("}");
			initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";
			jsonField.setInitExp(initExp);
		} else {
			sb.append("\"\\\"");
			String initExp = jsonField.getInitExp();
			int indexOf = initExp.lastIndexOf("}");
			initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";
			jsonField.setInitExp(initExp);
		}
	}

	@Override
	public void visitAfter(Property element) {
	}

}

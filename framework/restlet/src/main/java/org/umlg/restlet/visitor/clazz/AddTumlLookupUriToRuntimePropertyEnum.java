package org.umlg.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
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

public class AddTumlLookupUriToRuntimePropertyEnum extends BaseVisitor implements Visitor<Class> {

	public AddTumlLookupUriToRuntimePropertyEnum(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
    @VisitSubclasses({Class.class, AssociationClass.class})
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJEnum ojEnum = annotatedClass.findEnum(TumlClassOperations.propertyEnumName(clazz));
		
		if (TumlClassOperations.hasLookupProperty(clazz)) {
			OJField tumlUriLookup = ojEnum.findField("tumlLookupUri");
			if (tumlUriLookup == null ) {
				tumlUriLookup = new OJField();
				tumlUriLookup.setType(new OJPathName("String"));
				tumlUriLookup.setName("tumlLookupUri");
				ojEnum.addToFields(tumlUriLookup);

				OJConstructor constructor = ojEnum.getConstructors().iterator().next();
				constructor.addParam(tumlUriLookup.getName(), tumlUriLookup.getType());
				constructor.getBody().addToStatements("this." + tumlUriLookup.getName() + " = " + tumlUriLookup.getName());
			}

			// Need to create a lookup uri on the immediate composite
			// parent, to
			// be called by new
			// objects, Seeing as the new object has not yet been persisted
			// it
			// can not call the lookup on itself.
			tumlUriLookup = ojEnum.findField("tumlLookupOnCompositeParentUri");
			if (tumlUriLookup == null) {
				tumlUriLookup = new OJField();
				tumlUriLookup.setType(new OJPathName("String"));
				tumlUriLookup.setName("tumlLookupOnCompositeParentUri");
				ojEnum.addToFields(tumlUriLookup);

				OJConstructor constructor = ojEnum.getConstructors().iterator().next();
				constructor.addParam(tumlUriLookup.getName(), tumlUriLookup.getType());
				constructor.getBody().addToStatements("this." + tumlUriLookup.getName() + " = " + tumlUriLookup.getName());
			}

			Set<Property> allOwnedProperties = TumlClassOperations.getAllProperties(clazz);
			for (Property p : allOwnedProperties) {
				PropertyWrapper pWrap = new PropertyWrapper(p);
				if (!TumlClassOperations.isEnumeration(pWrap.getOwningType()) && pWrap.hasLookup()) {
					tumlUriLookup = ojEnum.findField("tumlLookupUri");
					OJAnnotatedOperation getter = new OJAnnotatedOperation(pWrap.lookupGetter(), tumlUriLookup.getType());
					getter.getBody().addToStatements("return this." + tumlUriLookup.getName());
					ojEnum.addToOperations(getter);

					getter = new OJAnnotatedOperation(pWrap.lookupOnCompositeParentGetter(), tumlUriLookup.getType());
					getter.getBody().addToStatements("return this." + tumlUriLookup.getName());
					ojEnum.addToOperations(getter);
					
				}
				doWithLiteral("lookup", clazz, pWrap, ojEnum.findLiteral(pWrap.fieldname()), false);
				doWithLiteral("lookupOnCompositeParent", clazz, pWrap, ojEnum.findLiteral(pWrap.fieldname()), true);
			}
			// Add the lookups to the id property
			doWithLiteral("tumlLookupUri", clazz, null, ojEnum.findLiteral("id"), false);
			doWithLiteral("tumlLookupOnCompositeParentUri", clazz, null, ojEnum.findLiteral("id"), false);

			if (!TumlClassOperations.hasCompositeOwner(clazz)) {
				// Add the lookups to the root property
				doWithLiteral("tumlLookupUri", clazz, null, ojEnum.findLiteral(clazz.getModel().getName()), false);
				doWithLiteral("tumlLookupOnCompositeParentUri", clazz, null, ojEnum.findLiteral(clazz.getModel().getName()), false);
			}
		}
	}

	private void doWithLiteral(String literalName, Class clazz, PropertyWrapper propertyWrapper, OJEnumLiteral literal, boolean onCompositeParent) {
		// Add tumlLookupUri to literal
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
						+ TumlClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "Id}/" + propertyWrapper.lookup() + "\"";
			} else {
				Type type = TumlClassOperations.getOtherEndToComposite(clazz).getType();
				uri = "\"/" + contextPath + "/" +
                        TumlClassOperations.getPathName(type).getLast().toLowerCase() + "s/{"
						+ TumlClassOperations.getPathName(type).getLast().toLowerCase() + "Id}/" + propertyWrapper.lookup() + "\"";
			}
		} else {
			// The non lookup fields carry a empty string
			uri = "\"\"";
		}
		OJField uriAttribute = new OJField();
		uriAttribute.setName("tumlLookupUri");
		uriAttribute.setType(new OJPathName("String"));
		uriAttribute.setInitExp(uri);
		literal.addToAttributeValues(uriAttribute);

		OJField jsonField = literal.findAttributeValue("json");
		StringBuilder sb = new StringBuilder();
		if (!onCompositeParent) {
			sb.append(", \\\"tumlLookupUri\\\": \\");
		} else {
			sb.append(", \\\"tumlLookupOnCompositeParentUri\\\": \\");
		}
		sb.append(uri.substring(0, uri.length() - 1) + "\\\"");
		String initExp = jsonField.getInitExp();
		int indexOf = initExp.lastIndexOf("}");
		initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";
		jsonField.setInitExp(initExp);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

}

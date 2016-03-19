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
import org.umlg.javageneration.util.UmlgAssociationClassOperations;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class AddUmlgLookupUriToRuntimePropertyEnum extends BaseVisitor implements Visitor<Class> {

    public AddUmlgLookupUriToRuntimePropertyEnum(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJEnum ojEnum = annotatedClass.findEnum(UmlgClassOperations.propertyEnumName(clazz));

        if (UmlgClassOperations.hasLookupProperty(clazz)) {
            OJField tumlUriLookup = ojEnum.findField("tumlLookupUri");
            if (tumlUriLookup == null) {
                tumlUriLookup = new OJField();
                tumlUriLookup.setType(new OJPathName("String"));
                tumlUriLookup.setName("tumlLookupUri");
                ojEnum.addToFields(tumlUriLookup);

                OJConstructor constructor = ojEnum.getConstructors().iterator().next();
                constructor.addParam(tumlUriLookup.getName(), tumlUriLookup.getType());
                constructor.getBody().addToStatements("this." + tumlUriLookup.getName() + " = " + tumlUriLookup.getName());
            } else {
                throw new RuntimeException("wtf");
            }

            Set<Property> allOwnedProperties = UmlgClassOperations.getAllProperties(clazz);
            for (Property p : allOwnedProperties) {
                PropertyWrapper pWrap = new PropertyWrapper(p);
                if (!UmlgClassOperations.isEnumeration(pWrap.getOwningType()) && pWrap.hasLookup()) {
                    tumlUriLookup = ojEnum.findField("tumlLookupUri");
                    OJAnnotatedOperation getter = new OJAnnotatedOperation(pWrap.lookupGetter(), tumlUriLookup.getType());
                    getter.getBody().addToStatements("return this." + tumlUriLookup.getName());
                    ojEnum.addToOperations(getter);
                }
                doWithLiteral(clazz, pWrap, ojEnum.findLiteral(pWrap.fieldname()));

                if (pWrap.isMemberOfAssociationClass() && !(UmlgAssociationClassOperations.extendsAssociationClass(clazz))) {
                    doWithLiteral(clazz, pWrap, ojEnum.findLiteral(pWrap.getAssociationClassFakePropertyName()), true);
                }

            }
            // Add the lookups to the id property
            doWithLiteral(clazz, null, ojEnum.findLiteral("id"));

            if (!UmlgClassOperations.hasCompositeOwner(clazz)) {
                // Add the lookups to the root property
                doWithLiteral(clazz, null, ojEnum.findLiteral(clazz.getModel().getName()));
            }
        }
    }

    private void doWithLiteral(Class clazz, PropertyWrapper propertyWrapper, OJEnumLiteral literal) {
        doWithLiteral(clazz, propertyWrapper, literal, false);
    }

    private void doWithLiteral(Class clazz, PropertyWrapper propertyWrapper, OJEnumLiteral literal, boolean asAssociationClass) {
        // Add tumlLookupUri to literal
        String uri;
//        if (UmlgClassOperations.getOtherEndToComposite(clazz) != null && propertyWrapper != null && propertyWrapper.hasLookup()) {
        if (propertyWrapper != null && propertyWrapper.hasLookup()) {
            String contextPath = ModelLoader.INSTANCE.getModel().getName();
            if (!asAssociationClass) {
                uri = "\"/" + contextPath + "/"
                        + UmlgClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "s/{"
                        + UmlgClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "Id}/" + propertyWrapper.lookup() + "\"";
            } else {
                uri = "\"/" + contextPath + "/"
                        + UmlgClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "s/{"
                        + UmlgClassOperations.getPathName(propertyWrapper.getOwningType()).getLast().toLowerCase() + "Id}/" + propertyWrapper.lookup() + "\"";
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
        sb.append(", \\\"tumlLookupUri\\\": \\");
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

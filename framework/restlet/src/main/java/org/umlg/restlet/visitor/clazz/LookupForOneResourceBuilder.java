package org.umlg.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;

public class LookupForOneResourceBuilder extends BaseServerResourceBuilder implements Visitor<Property> {

    public LookupForOneResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        if (pWrap.hasLookup() && pWrap.isOne() && !pWrap.isRefined()) {

            OJAnnotatedClass owner = findOJClass(p);
            OJPackage ojPackage = owner.getMyPackage();

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
                    + pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_lookUpForOne" + "_ServerResourceImpl");
            annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            addToSource(annotatedClass);
            addDefaultConstructor(annotatedClass);

            addCompositeParentIdField(pWrap, annotatedClass);
            addGetObjectRepresentation(pWrap/*, annotatedInf*/, annotatedClass);
            addServerResourceToRouterEnum(pWrap, annotatedClass);
        }
    }

    @Override
    public void visitAfter(Property p) {
    }

    private void addGetObjectRepresentation(PropertyWrapper pWrap/*, OJAnnotatedInterface annotatedInf*/, OJAnnotatedClass annotatedClass) {

//        OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
//        annotatedInf.addToOperations(getInf);
//        getInf.addAnnotationIfNew(new OJAnnotationValue(UmlgRestletGenerationUtil.Get, "json"));

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
        get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(get);

        OJTryStatement ojTryStatement = new OJTryStatement();

        OJPathName parentPathName = UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType());
        ojTryStatement.getTryPart().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\"))"
        );
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);

        ojTryStatement.getTryPart().addToStatements(
                parentPathName.getLast() + " resource = " + UmlgGenerationUtil.UMLGAccess + "." + UmlgGenerationUtil.getEntity + "(this." + parentPathName.getLast().toLowerCase() + "Id"
                        + ")"
        );
        annotatedClass.addToImports(parentPathName);
        buildToJson(pWrap, annotatedClass, ojTryStatement.getTryPart());
        ojTryStatement.setCatchPart(null);

        ojTryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");
        get.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void buildToJson(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJBlock block) {
        block.addToStatements("StringBuilder json = new StringBuilder()");
        block.addToStatements("json.append(\"{\\\"data\\\": [\")");
        block.addToStatements("json.append(" + UmlgRestletGenerationUtil.UmlgRestletToJsonUtil.getLast() + ".toJson(resource." + pWrap.lookup() + "()))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgRestletToJsonUtil);
        block.addToStatements("json.append(\"],\")");
        block.addToStatements("json.append(\" \\\"meta\\\" : {\")");
        block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"\")");
        block.addToStatements("json.append(\", \\\"to\\\": \")");
        block.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType()) + ".asJson())");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType()).append(
                UmlgClassOperations.propertyEnumName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType())));
        block.addToStatements("json.append(\", \\\"from\\\": \")");
        block.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(pWrap.getOwningType()) + ".asJson())");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(pWrap.getOwningType()).append(UmlgClassOperations.propertyEnumName(pWrap.getOwningType())));

        block.addToStatements("json.append(\"}}\")");
        block.addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
    }

    private void addServerResourceToRouterEnum(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toUpperCase() + "_" + pWrap.lookup());

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
                + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id}/" + pWrap.lookup() + "\"");
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(annotatedClass.getPathName());
        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }

}

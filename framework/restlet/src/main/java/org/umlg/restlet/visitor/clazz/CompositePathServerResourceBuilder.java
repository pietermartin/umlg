package org.umlg.restlet.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJTryStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotationValue;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.restlet.util.TumlRestletGenerationUtil;

public class CompositePathServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Class> {

    public CompositePathServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));
        OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.className(clazz) + "CompositePathServerResourceImpl");
        annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
        annotatedClass.setMyPackage(ojPackage);
        annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
        addToSource(annotatedClass);
        addPrivateIdVariable(clazz, annotatedClass);
        addDefaultConstructor(annotatedClass);
        addGetRepresentation(clazz, annotatedClass);
        addToRouterEnum(clazz, annotatedClass);
    }

    @Override
    public void visitAfter(Class clazz) {
    }

    private void addGetRepresentation(Class clazz, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
        get.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
        TinkerGenerationUtil.addOverrideAnnotation(get);

        OJTryStatement ojTryStatement = new OJTryStatement();
        ojTryStatement.setCatchPart(null);
        ojTryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".rollback()");
        get.getBody().addToStatements(ojTryStatement);

        ojTryStatement.getTryPart().addToStatements(
                "this." + getIdFieldName(clazz) + "= "+TumlRestletGenerationUtil.UmlgURLDecoder.getLast()+".decode((String)getRequestAttributes().get(\"" + getIdFieldName(clazz) + "\"));");
        annotatedClass.addToImports(TumlRestletGenerationUtil.UmlgURLDecoder);
        ojTryStatement.getTryPart().addToStatements(
                TumlClassOperations.className(clazz) + " c = GraphDb.getDb().instantiateClassifier(this." + getIdFieldName(clazz) + ")");
        annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));

        ojTryStatement.getTryPart().addToStatements("StringBuilder json = new StringBuilder()");
        ojTryStatement.getTryPart().addToStatements("json.append(\"{\\\"data\\\": [\")");

        StringBuilder pathToCompositionRootCalc = new StringBuilder("json.append(RestletToJsonUtil.pathToCompositionRootAsJson(");
        annotatedClass.addToImports(TumlRestletGenerationUtil.RestletToJsonUtil);

        pathToCompositionRootCalc.append("c.<" + TumlRestletGenerationUtil.UmlgRestletNode.getLast() + ">getPathToCompositionalRoot(), ");
        annotatedClass.addToImports(TumlRestletGenerationUtil.UmlgRestletNode);
        pathToCompositionRootCalc.append("\"Root\", \"/" + clazz.getModel().getName() + "\"))");
        ojTryStatement.getTryPart().addToStatements(pathToCompositionRootCalc.toString());
        ojTryStatement.getTryPart().addToStatements("json.append(\"]}\")");
        ojTryStatement.getTryPart().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addToRouterEnum(Class clazz, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(TumlRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(TumlClassOperations.className(clazz).toUpperCase() + "_compositePath");

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + TumlClassOperations.className(clazz).toLowerCase() + "s/{" + TumlClassOperations.className(clazz).toLowerCase()
                + "Id}/compositePathToRoot\"");
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(annotatedClass.getPathName());
        routerEnum.addToImports(TumlRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", TumlRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }

    private String getIdFieldName(Class clazz) {
        return TumlClassOperations.className(clazz).toLowerCase() + "Id";
    }

}

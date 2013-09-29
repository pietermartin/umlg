package org.umlg.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.*;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.restlet.util.TumlRestletGenerationUtil;

import java.util.Set;

/**
 * Date: 2013/03/30
 * Time: 6:49 PM
 */
public class MetaDataResourceBuilder extends BaseServerResourceBuilder implements Visitor<Classifier> {

    public MetaDataResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({org.eclipse.uml2.uml.Interface.class,  org.eclipse.uml2.uml.Class.class, AssociationClass.class})
    public void visitBefore(Classifier c) {
        if (c instanceof Interface || c instanceof org.eclipse.uml2.uml.Class) {
            OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(getServerResourceMetaDataName(c));
            OJPackage ojPackage = new OJPackage(Namer.name(c.getNearestPackage()));
            annotatedInf.setMyPackage(ojPackage);
            addToSource(annotatedInf);
            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(getServerResourceMetatDataImplName(c));
            annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
            annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
            annotatedClass.setMyPackage(ojPackage);
            annotatedClass.setVisibility(TumlClassOperations.getVisibility(c.getVisibility()));
            addToSource(annotatedClass);
            addDefaultConstructor(annotatedClass);
            addGetRepresentation(c, annotatedInf, annotatedClass);
            addToRouterEnum(c, annotatedClass);
        }
    }

    private void addGetRepresentation(Classifier clazz, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
        annotatedInf.addToOperations(getInf);
        getInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Get, "json"));

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
        get.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
        TinkerGenerationUtil.addOverrideAnnotation(get);

        OJTryStatement tryStatement = new OJTryStatement();
        Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteImplementations(clazz);
        OJBlock block = tryStatement.getTryPart();
        block.addToStatements("StringBuilder json = new StringBuilder()");
        block.addToStatements("json.append(\"[\")");

        int count = 1;
        for (Classifier classifier : concreteImplementations) {
            block.addToStatements("json.append(\"{\\\"data\\\": [],\")");
            block.addToStatements("json.append(\" \\\"meta\\\" : {\")");

            block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
            block.addToStatements("json.append(\", \\\"to\\\": \")");
            block.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(classifier) + ".asJson())");
            block.addToStatements("json.append(\", \\\"from\\\": \")");
            block.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(clazz) + ".asJson())");
            block.addToStatements("json.append(\"} \")");
            annotatedClass.addToImports(TumlClassOperations.getPathName(classifier).append(TumlClassOperations.propertyEnumName(classifier)));
            if (count++ != concreteImplementations.size()) {
                block.addToStatements("json.append(\"}, \")");
            }
        }
        annotatedClass.addToImports(TumlClassOperations.getPathName(clazz).append(TumlClassOperations.propertyEnumName(clazz)));
        block.addToStatements("json.append(\"}]\")");

        annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));
        tryStatement.getTryPart().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        get.getBody().addToStatements(tryStatement);
        tryStatement.setCatchPart(null);
        tryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".rollback()");

        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addToRouterEnum(Classifier clazz, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(TumlRestletGenerationUtil.RestletRouterEnum.toJavaString());

        OJEnumLiteral ojLiteralMeta = new OJEnumLiteral(TumlClassOperations.className(clazz).toUpperCase() + "_META");
        OJField uriMeta = new OJField();
        uriMeta.setType(new OJPathName("String"));
        uriMeta.setInitExp("\"/" + StringUtils.uncapitalize(TumlClassOperations.className(clazz)) + "MetaData\"");
        ojLiteralMeta.addToAttributeValues(uriMeta);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
        ojLiteralMeta.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(annotatedClass.getPathName());
        routerEnum.addToImports(TumlRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteralMeta);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", TumlRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteralMeta.getName() + ".attach(router)");
    }

    @Override
    public void visitAfter(Classifier element) {
    }
}

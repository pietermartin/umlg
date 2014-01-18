package org.umlg.restlet.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;
import org.umlg.restlet.visitor.clazz.BaseServerResourceBuilder;

public class QueryExecuteResourceBuilder extends BaseServerResourceBuilder implements Visitor<Model> {

	public QueryExecuteResourceBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
//		OJAnnotatedInterface queryExecuteInf = new OJAnnotatedInterface(UmlgRestletGenerationUtil.QueryExecuteServerResource.getLast());
//		OJPackage ojPackage = new OJPackage(UmlgGenerationUtil.UmlgRootPackage.toJavaString());
//		queryExecuteInf.setMyPackage(ojPackage);
//		addToSource(queryExecuteInf);
//
//		OJAnnotatedClass queryExecute = new OJAnnotatedClass(UmlgRestletGenerationUtil.QueryExecuteServerResourceImpl.getLast());
//		queryExecute.setMyPackage(ojPackage);
//		queryExecute.addToImplementedInterfaces(UmlgRestletGenerationUtil.QueryExecuteServerResource);
//		queryExecute.setSuperclass(UmlgRestletGenerationUtil.BaseOclExecutionServerResourceImpl);
//		addToSource(queryExecute);
//
//		addDefaultConstructor(queryExecute);
//
//		addGetRepresentation(queryExecuteInf, queryExecute);

		addToRouterEnum("QUERY_EXECUTE", "\"/{contextId}/oclExecuteQuery\"");
        addToRouterEnum("QUERY_EXECUTE_STATIC", "\"/oclExecuteQuery\"");

        addToClassQueryRouterEnum(model, UmlgRestletGenerationUtil.UmlgMetaQueryServerResourceImpl, "CLASS_QUERY", "\"/classquery/{contextId}/query\"");

	}

    protected void addToClassQueryRouterEnum(Model model, OJPathName ojPathName, String name, String path) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(name);

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp(path);
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(ojPathName.getLast() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(ojPathName);
        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }

    protected void addToRouterEnum(String name, String path) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(name);

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp(path);
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(UmlgRestletGenerationUtil.QueryExecuteServerResourceImpl.getLast() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(UmlgRestletGenerationUtil.QueryExecuteServerResourceImpl);
        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }




    @Override
	public void visitAfter(Model element) {
		
	}

	private void addGetRepresentation(OJAnnotatedInterface queryExecuteInf, OJAnnotatedClass queryExecute) {
		OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
		queryExecuteInf.addToOperations(getInf);
		getInf.addAnnotationIfNew(new OJAnnotationValue(UmlgRestletGenerationUtil.Get, "json"));
		queryExecuteInf.addToOperations(getInf);
		
		OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
		get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
		queryExecute.addToImports(UmlgRestletGenerationUtil.ResourceException);
		UmlgGenerationUtil.addOverrideAnnotation(get);
		queryExecute.addToOperations(get);

        OJField type = new OJField("type", "String");
        type.setInitExp("getQuery().getFirstValue(\"type\")");
		OJField query = new OJField("query", "String");
		query.setInitExp("getQuery().getFirstValue(\"query\")");
		OJField contextId = new OJField("context", "Object");
        contextId.setInitExp(UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\"contextId\"))");
        queryExecute.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);

        OJIfStatement ifContextNull = new OJIfStatement("context != null");
        ifContextNull.addToThenPart("Object contextId = (String)context");
        ifContextNull.addToThenPart("return execute(query, contextId, type)");
        ifContextNull.addToElsePart("return execute(query)");
        get.getBody().addToStatements(ifContextNull);

        get.getBody().addToLocals(type);
		get.getBody().addToLocals(query);
		get.getBody().addToLocals(contextId);
	}

}

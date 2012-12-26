package org.tuml.restlet.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJPackage;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.annotation.*;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.restlet.util.TumlRestletGenerationUtil;
import org.tuml.restlet.visitor.clazz.BaseServerResourceBuilder;

public class QueryExecuteResourceBuilder extends BaseServerResourceBuilder implements Visitor<Model> {

	public QueryExecuteResourceBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
		OJAnnotatedInterface queryExecuteInf = new OJAnnotatedInterface(TumlRestletGenerationUtil.QueryExecuteServerResource.getLast());
		OJPackage ojPackage = new OJPackage(TinkerGenerationUtil.TumlRootPackage.toJavaString());
		queryExecuteInf.setMyPackage(ojPackage);
		addToSource(queryExecuteInf);

		OJAnnotatedClass queryExecute = new OJAnnotatedClass(TumlRestletGenerationUtil.QueryExecuteServerResourceImpl.getLast());
		queryExecute.setMyPackage(ojPackage);
		queryExecute.addToImplementedInterfaces(TumlRestletGenerationUtil.QueryExecuteServerResource);
		queryExecute.setSuperclass(TumlRestletGenerationUtil.BaseOclExecutionServerResourceImpl);
		addToSource(queryExecute);
		
		addDefaultConstructor(queryExecute);
		
		addGetRepresentation(queryExecuteInf, queryExecute);

		addToRouterEnum(model, queryExecute, "QUERY_EXECUTE", "\"/{contextId}/oclExecuteQuery\"");


        addToClassQueryRouterEnum(model, TumlRestletGenerationUtil.TumlMetaQueryServerResourceImpl, "CLASS_QUERY", "\"classquery/{contextId}/query\"");

	}

    protected void addToClassQueryRouterEnum(Model model, OJPathName ojPathName, String name, String path) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
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
        routerEnum.addToImports(TumlRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", TumlRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }



    @Override
	public void visitAfter(Model element) {
		
	}

	private void addGetRepresentation(OJAnnotatedInterface queryExecuteInf, OJAnnotatedClass queryExecute) {
		OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		queryExecuteInf.addToOperations(getInf);
		getInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Get, "json"));
		queryExecuteInf.addToOperations(getInf);
		
		OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		get.addToThrows(TumlRestletGenerationUtil.ResourceException);
		queryExecute.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(get);
		queryExecute.addToOperations(get);
		
		OJField ocl = new OJField("ocl", "String");
		ocl.setInitExp("getQuery().getFirstValue(\"ocl\")");
		OJField contextId = new OJField("contextId", "Integer");
		contextId.setInitExp("Integer.parseInt((String)getRequestAttributes().get(\"contextId\"))");
		
//		OJField oclResult = new OJField("oclResult", "String");
//		oclResult.setInitExp("return execute(ocl, contextId)");

        get.getBody().addToStatements("return execute(ocl, contextId)");

		get.getBody().addToLocals(ocl);
		get.getBody().addToLocals(contextId);
//		get.getBody().addToLocals(oclResult);
		
//		get.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(oclResult)");
		queryExecute.addToImports(TumlRestletGenerationUtil.JsonRepresentation);

	}

}

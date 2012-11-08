package org.tuml.restlet.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedInterface;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
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
		
		OJField oclResult = new OJField("oclResult", "String");
		oclResult.setInitExp("execute(ocl, contextId)");
		
		get.getBody().addToLocals(ocl);
		get.getBody().addToLocals(contextId);
		get.getBody().addToLocals(oclResult);
		
		get.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(oclResult)");
		queryExecute.addToImports(TumlRestletGenerationUtil.JsonRepresentation);

	}

}

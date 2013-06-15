package org.umlg.restlet.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotationValue;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.restlet.util.TumlRestletGenerationUtil;
import org.umlg.restlet.visitor.clazz.BaseServerResourceBuilder;

public class AppResourceServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Model> {

	public AppResourceServerResourceBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {

		OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface("RootServerResource");
		OJPackage org = new OJPackage("org");
        OJPackage tuml = new OJPackage("umlg");
        tuml.setParent(org);
        OJPackage ojPackage = new OJPackage("restlet");
        ojPackage.setParent(tuml);
		annotatedInf.setMyPackage(ojPackage);
		addToSource(annotatedInf);

		OJAnnotatedClass annotatedClass = new OJAnnotatedClass("RootServerResourceImpl");
		annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
		annotatedClass.setMyPackage(ojPackage);
		annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
		addToSource(annotatedClass);

		addDefaultConstructor(annotatedClass);
		addGetRootObjectRepresentation(model, annotatedInf, annotatedClass);
		addToRouterEnum(model, annotatedClass, "ROOT", "\"\"");
	}

	@Override
	public void visitAfter(Model model) {
	}

	private void addGetRootObjectRepresentation(Model model, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {
		OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		annotatedInf.addToOperations(getInf);
		getInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Get, "json"));

		OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		get.addToThrows(TumlRestletGenerationUtil.ResourceException);
		annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(get);

		OJField json = new OJField("json", new OJPathName("java.lang.StringBuilder"));
		json.setInitExp("new StringBuilder()");
		get.getBody().addToLocals(json);
//		get.getBody().addToStatements("json.append(\"[{\\\"data\\\": [{\\\"name\\\": \\\"APP\\\", \\\"id\\\":\" + Root.INSTANCE.getId() + \"}]\")");
        get.getBody().addToStatements("json.append(\"[{\\\"data\\\": [{\\\"name\\\": \\\"APP\\\"}]\")");
		get.getBody().addToStatements("json.append(\", \\\"meta\\\": \")");
		get.getBody().addToStatements("json.append(\"{\\\"qualifiedName\\\": \\\"" + model.getQualifiedName() + "\\\"\")");
		get.getBody().addToStatements("json.append(\", \\\"to\\\": \")");
		get.getBody().addToStatements("json.append(RootRuntimePropertyEnum.asJson())");
        annotatedClass.addToImports("org.umlg.root.Root");
		annotatedClass.addToImports("org.umlg.root.Root.RootRuntimePropertyEnum");
		get.getBody().addToStatements("json.append(\"}}]\")");
		get.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
		
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(get);
	}

}

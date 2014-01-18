package org.umlg.restlet.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;
import org.umlg.restlet.visitor.clazz.BaseServerResourceBuilder;

public class AppResourceServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Model> {

	public AppResourceServerResourceBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {

//		OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface("RootServerResource");
		OJPackage org = new OJPackage("org");
        OJPackage tuml = new OJPackage("umlg");
        tuml.setParent(org);
        OJPackage ojPackage = new OJPackage("restlet");
        ojPackage.setParent(tuml);
//		annotatedInf.setMyPackage(ojPackage);
//		addToSource(annotatedInf);

		OJAnnotatedClass annotatedClass = new OJAnnotatedClass("RootServerResourceImpl");
		annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
		annotatedClass.setMyPackage(ojPackage);
//		annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
		addToSource(annotatedClass);

		addDefaultConstructor(annotatedClass);
		addGetRootObjectRepresentation(model, annotatedClass);
        addOptionsRootObjectRepresentation(model, annotatedClass);
		addToRouterEnum(model, annotatedClass, "ROOT", "\"\"");
	}

    @Override
	public void visitAfter(Model model) {
	}

	private void addGetRootObjectRepresentation(Model model, OJAnnotatedClass annotatedClass) {

		OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
		get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
		annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
		UmlgGenerationUtil.addOverrideAnnotation(get);

		OJField json = new OJField("json", new OJPathName("java.lang.StringBuilder"));
		json.setInitExp("new StringBuilder()");
		get.getBody().addToLocals(json);
        get.getBody().addToStatements("json.append(\"[{\\\"data\\\": [{\\\"name\\\": \\\"APP\\\"}]\")");
		get.getBody().addToStatements("json.append(\", \\\"meta\\\": \")");
		get.getBody().addToStatements("json.append(\"{\\\"qualifiedName\\\": \\\"" + model.getQualifiedName() + "\\\"\")");
//		get.getBody().addToStatements("json.append(\", \\\"to\\\": \")");
//		get.getBody().addToStatements("json.append(RootRuntimePropertyEnum.asJson())");
//        annotatedClass.addToImports("org.umlg.root.Root");
//		annotatedClass.addToImports("org.umlg.root.Root.RootRuntimePropertyEnum");
		get.getBody().addToStatements("json.append(\"}}]\")");
		get.getBody().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
		
		annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(get);
	}

    private void addOptionsRootObjectRepresentation(Model model, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation options = new OJAnnotatedOperation("options", UmlgRestletGenerationUtil.Representation);
        options.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(options);

        OJField json = new OJField("json", new OJPathName("java.lang.StringBuilder"));
        json.setInitExp("new StringBuilder()");
        options.getBody().addToLocals(json);
//        get.getBody().addToStatements("json.append(\"[{\\\"data\\\": [{\\\"name\\\": \\\"APP\\\"}]\")");
        options.getBody().addToStatements("json.append(\"[{\\\"meta\\\": \")");
        options.getBody().addToStatements("json.append(\"{\\\"qualifiedName\\\": \\\"" + model.getQualifiedName() + "\\\"\")");
        options.getBody().addToStatements("json.append(\", \\\"to\\\": \")");
        options.getBody().addToStatements("json.append(RootRuntimePropertyEnum.asJson())");
        annotatedClass.addToImports("org.umlg.root.Root");
        annotatedClass.addToImports("org.umlg.root.Root.RootRuntimePropertyEnum");
        options.getBody().addToStatements("json.append(\"}}]\")");
        options.getBody().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(options);
    }

}

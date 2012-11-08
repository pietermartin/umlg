package org.tuml.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedInterface;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.opaeum.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.restlet.util.TumlRestletGenerationUtil;

public class CompositePathServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Class> {

	public CompositePathServerResourceBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Class clazz) {
		// if (!clazz.isAbstract()) {
		OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.className(clazz) + "CompositePathServerResource");
		OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".restlet");
		annotatedInf.setMyPackage(ojPackage);
		addToSource(annotatedInf);
		OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.className(clazz) + "CompositePathServerResourceImpl");
		annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
		annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
		annotatedClass.setMyPackage(ojPackage);
		annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
		addToSource(annotatedClass);
		addPrivateIdVariable(clazz, annotatedClass);
		addDefaultConstructor(annotatedClass);
		addGetRepresentation(clazz, annotatedInf, annotatedClass);
		addToRouterEnum(clazz, annotatedClass);
		// }
	}

	@Override
	public void visitAfter(Class clazz) {
	}

	private void addGetRepresentation(Class clazz, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {

		OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		annotatedInf.addToOperations(getInf);
		getInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Get, "json"));

		OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		get.addToThrows(TumlRestletGenerationUtil.ResourceException);
		annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(get);
		get.getBody().addToStatements(
				"this." + getIdFieldName(clazz) + "= Integer.parseInt((String)getRequestAttributes().get(\"" + getIdFieldName(clazz) + "\"));");
		get.getBody().addToStatements(
				TumlClassOperations.className(clazz) + " c = GraphDb.getDb().instantiateClassifier(Long.valueOf(this." + getIdFieldName(clazz) + "))");
		annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));

		get.getBody().addToStatements("StringBuilder json = new StringBuilder()");
		get.getBody().addToStatements("json.append(\"{\\\"data\\\": [\")");

		StringBuilder pathToCompositionRootCalc = new StringBuilder("json.append(RestletToJsonUtil.pathToCompositionRootAsJson(");
		annotatedClass.addToImports(TumlRestletGenerationUtil.RestletToJsonUtil);

		pathToCompositionRootCalc.append("c.<" + TumlRestletGenerationUtil.TumlRestletNode.getLast() + ">getPathToCompositionalRoot(), ");
		annotatedClass.addToImports(TumlRestletGenerationUtil.TumlRestletNode);
		pathToCompositionRootCalc.append("\"Root\", \"/restAndJson\"))");
		get.getBody().addToStatements(pathToCompositionRootCalc.toString());
		get.getBody().addToStatements("json.append(\"]}\")");
		get.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(get);
	}

	private void addToRouterEnum(Class clazz, OJAnnotatedClass annotatedClass) {
		OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
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

	private void addPrivateIdVariable(Class clazz, OJAnnotatedClass annotatedClass) {
		OJField privateId = new OJField(getIdFieldName(clazz), new OJPathName("int"));
		privateId.setVisibility(OJVisibilityKind.PRIVATE);
		annotatedClass.addToFields(privateId);
	}

	private String getIdFieldName(Class clazz) {
		return TumlClassOperations.className(clazz).toLowerCase() + "Id";
	}

}

package org.tuml.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
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

public class RootResourceServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Class> {

	public RootResourceServerResourceBuilder(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		if (!clazz.isAbstract() && !TumlClassOperations.hasCompositeOwner(clazz)) {
			
			OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.className(clazz) + "ServerResource");
			OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".restlet");
			annotatedInf.setMyPackage(ojPackage);
			addToSource(annotatedInf);
			
			OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.className(clazz) + "sServerResourceImpl");
			annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
			annotatedClass.setMyPackage(ojPackage);
			annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
			annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
			addToSource(annotatedClass);
			
			addPrivateIdVariable(clazz, annotatedClass);
			addDefaultConstructor(annotatedClass);
			addGetRootObjectRepresentation(clazz, annotatedInf, annotatedClass);
			addToRouterEnum(clazz, annotatedClass);
			
		}
	}

	@Override
	public void visitAfter(Class clazz) {
	}
	
	private void addGetRootObjectRepresentation(Class clazz, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {
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
		OJField resource = new OJField("resource", new OJPathName("java.util.List").addToGenerics(TumlClassOperations.getPathName(clazz)));
		resource.setInitExp("Root.INSTANCE.get" + TumlClassOperations.className(clazz) + "()");
		get.getBody().addToLocals(resource);
		annotatedClass.addToImports("org.tuml.root.Root");
		get.getBody().addToStatements("json.append(\"[\")");
		get.getBody().addToStatements("json.append(ToJsonUtil.toJson(resource))");
		annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
		
		get.getBody().addToStatements("json.append(\",\")");
		get.getBody().addToStatements("json.append(\" {\\\"meta\\\" : \")");
		get.getBody().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(clazz) + ".asJson())");
		annotatedClass.addToImports(TumlClassOperations.getPathName(clazz).append(TumlClassOperations.propertyEnumName(clazz)));
		get.getBody().addToStatements("json.append(\"}]\")");
		get.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(get);
	}
	
	private void addToRouterEnum(Class clazz, OJAnnotatedClass annotatedClass) {
		OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
		OJEnumLiteral ojLiteral = new OJEnumLiteral(TumlClassOperations.className(clazz).toUpperCase() + "S");

		OJField uri = new OJField();
		uri.setType(new OJPathName("String"));
		uri.setInitExp("\"/" + TumlClassOperations.className(clazz).toLowerCase() + "s\"");
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
		return StringUtils.uncapitalize(TumlClassOperations.className(clazz)) + "Id";
	}

}

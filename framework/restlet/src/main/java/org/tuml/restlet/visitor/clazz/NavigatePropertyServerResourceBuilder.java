package org.tuml.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJForStatement;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJParameter;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJTryStatement;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedInterface;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.opaeum.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.restlet.util.TumlRestletGenerationUtil;

public class NavigatePropertyServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Property> {

	public NavigatePropertyServerResourceBuilder(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper pWrap = new PropertyWrapper(p);
		if (!pWrap.isComponent() && !pWrap.isPrimitive() && !pWrap.isEnumeration()) {
			OJAnnotatedClass owner = findOJClass(p);

			OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.getOtherEnd().getName()
					+ "_" + pWrap.getName() + "_ServerResource");
			OJPackage ojPackage = new OJPackage(owner.getMyPackage().toString() + ".restlet");
			annotatedInf.setMyPackage(ojPackage);
			addToSource(annotatedInf);

			OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.getOtherEnd().getName() + "_"
					+ pWrap.getName() + "_ServerResourceImpl");
			annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
			annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
			annotatedClass.setMyPackage(ojPackage);
			addToSource(annotatedClass);
			addDefaultConstructor(annotatedClass);

			addCompositeParentIdField(pWrap, annotatedClass);
			addGetObjectRepresentation(pWrap, annotatedInf, annotatedClass);
			// Put must be Idempotence, i.e. calling it many times must make no
			// difference to server state
			// non unique sequence or a bag can not put as adding the same value
			// more than once changes the state
			addPutPostObjectRepresentation(pWrap, annotatedInf, annotatedClass);
			addServerResourceToRouterEnum(pWrap, annotatedClass);
		}
	}

	@Override
	public void visitAfter(Property p) {
	}

	private void addGetObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {

		OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		annotatedInf.addToOperations(getInf);
		getInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Get, "json"));

		OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		get.addToThrows(TumlRestletGenerationUtil.ResourceException);
		annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(get);

		OJPathName parentPathName = TumlClassOperations.getPathName(pWrap.getOtherEnd().getType());
		get.getBody().addToStatements(
				"this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\"" + parentPathName.getLast().toLowerCase()
						+ "Id\"))");
		get.getBody().addToStatements(
				parentPathName.getLast() + " parentResource = new " + parentPathName.getLast() + "(GraphDb.getDb().getVertex(this." + parentPathName.getLast().toLowerCase() + "Id"
						+ "))");
		annotatedClass.addToImports(parentPathName);
		buildToJson(pWrap, annotatedClass, get.getBody());
		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(get);
	}

	private void addPutPostObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {

		OJAnnotatedOperation putInf = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
		putInf.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		annotatedInf.addToOperations(putInf);
		putInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Post, "json"));

		OJAnnotatedOperation post = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
		post.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		post.addToThrows(TumlRestletGenerationUtil.ResourceException);
		annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(post);
		TinkerGenerationUtil.addSuppressWarning(post);

		PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());

		OJPathName parentPathName = otherEndPWrap.javaBaseTypePath();
		post.getBody().addToStatements(
				"this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\"" + parentPathName.getLast().toLowerCase()
						+ "Id\"))");
		post.getBody().addToStatements(
				parentPathName.getLast() + " parentResource = new " + parentPathName.getLast() + "(GraphDb.getDb().getVertex(this." + parentPathName.getLast().toLowerCase() + "Id"
						+ "))");

		post.getBody().addToStatements("GraphDb.getDb().startTransaction()");
		OJTryStatement ojTryStatement = new OJTryStatement();
		OJField mapper = new OJField("mapper", TinkerGenerationUtil.ObjectMapper);
		mapper.setInitExp("new ObjectMapper()");
		ojTryStatement.getTryPart().addToLocals(mapper);
		OJAnnotatedField objectO = new OJAnnotatedField("o", new OJPathName("Object"));
		objectO.setInitExp("mapper.readValue(entity.getText(), Object.class)");
		ojTryStatement.getTryPart().addToLocals(objectO);
		OJIfStatement ifArray = new OJIfStatement("o instanceof ArrayList");
		OJPathName genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
		OJField array = new OJField("array", new OJPathName("java.util.ArrayList").addToGenerics(genericsForArray));
		array.setInitExp("(ArrayList<Map<String, Object>>)o");
		ifArray.getThenPart().addToLocals(array);
		ojTryStatement.getTryPart().addToStatements(ifArray);
		OJForStatement forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(new OJPathName("Object")),
				"array");
		ifArray.addToThenPart(forArray);
		forArray.getBody().addToStatements("add(parentResource, map)");

		OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
		map.setInitExp("(Map<String, Object>) o;");
		ifArray.setElsePart(new OJBlock());
		ifArray.getElsePart().addToLocals(map);
		ifArray.getElsePart().addToStatements("add(parentResource, map)");

		addAddResource(pWrap, annotatedClass, parentPathName);

		ojTryStatement.getTryPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.SUCCESS)");
		annotatedClass.addToImports(TinkerGenerationUtil.tinkerConclusionPathName);

		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.FAILURE)");
		ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
		post.getBody().addToStatements(ojTryStatement);

		annotatedClass.addToImports(parentPathName);

		buildToJson(pWrap, annotatedClass, post.getBody());

		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(post);
	}

	private void addAddResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
		OJAnnotatedOperation add = new OJAnnotatedOperation("add");
		add.setVisibility(OJVisibilityKind.PRIVATE);
		add.addToParameters(new OJParameter("parentResource", parentPathName));
		add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
		annotatedClass.addToOperations(add);
		OJField childResource = new OJField("childResource", pWrap.javaBaseTypePath());
		add.getBody().addToLocals(childResource);
		add.getBody().addToStatements("Object id = propertyMap.get(\"id\")");
		
		OJIfStatement ifIdNull = new OJIfStatement("id != null");
		ifIdNull.addToThenPart("childResource = new " + pWrap.javaBaseTypePath().getLast() + "(GraphDb.getDb().getVertex(id))");

		if (pWrap.isComposite()) {
			ifIdNull.addToElsePart("childResource = new " + pWrap.javaBaseTypePath().getLast() + "(true)");
		} else {
			ifIdNull.addToElsePart("throw new IllegalStateException(\"A resource can only be created on a compositional relationship. Id field is required.\")");
		}
		
		add.getBody().addToStatements(ifIdNull);

		add.getBody().addToStatements("childResource.fromJson(propertyMap)");
		if (pWrap.isOrdered()) {
			annotatedClass.addToImports(TumlRestletGenerationUtil.Parameter);
			add.getBody().addToStatements("Parameter indexParameter = getQuery().getFirst(\"index\")");
			OJIfStatement ifIndexNull = new OJIfStatement("indexParameter != null");
			ifIndexNull.addToThenPart("int index = Integer.valueOf(indexParameter.getValue())");
			ifIndexNull.addToThenPart("parentResource." + pWrap.getter() + "().add(index, childResource)");
			ifIndexNull.addToElsePart("parentResource." + pWrap.adder() + "(childResource)");
			add.getBody().addToStatements(ifIndexNull);
		} else {
			add.getBody().addToStatements("parentResource." + pWrap.adder() + "(childResource)");
		}
	}

	private void buildToJson(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJBlock block) {
		block.addToStatements("StringBuilder json = new StringBuilder()");
		block.addToStatements("json.append(\"{\\\"data\\\": [\")");
		block.addToStatements("json.append(ToJsonUtil.toJson(parentResource." + pWrap.getter() + "()))");
		annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
		block.addToStatements("json.append(\"],\")");
		block.addToStatements("json.append(\" \\\"meta\\\" : [\")");
		block.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(pWrap.getOwningType()) + ".asJson())");
		annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getOwningType()).append(TumlClassOperations.propertyEnumName(pWrap.getOwningType())));
		block.addToStatements("json.append(\", \")");

		block.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType()) + ".asJson())");
		annotatedClass.addToImports(TumlClassOperations.getPathName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType()).append(
				TumlClassOperations.propertyEnumName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType())));
		block.addToStatements("json.append(\"]}\")");

		block.addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
	}
	private void addServerResourceToRouterEnum(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
		OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
		OJEnumLiteral ojLiteral = new OJEnumLiteral(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toUpperCase() + "_" + pWrap.getName());

		OJField uri = new OJField();
		uri.setType(new OJPathName("String"));
		uri.setInitExp("\"/" + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
				+ TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id}/" + pWrap.getName() + "\"");
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

	private void addCompositeParentIdField(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
		OJField compositeParentFieldId = new OJField(TumlClassOperations.getPathName(pWrap.getOtherEnd().getType()).getLast().toLowerCase() + "Id", new OJPathName("int"));
		compositeParentFieldId.setVisibility(OJVisibilityKind.PRIVATE);
		annotatedClass.addToFields(compositeParentFieldId);
	}

}

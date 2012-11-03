package org.tuml.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
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

			OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.className(clazz) + "sServerResource");
			OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".restlet");
			annotatedInf.setMyPackage(ojPackage);
			addToSource(annotatedInf);

			OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.className(clazz) + "sServerResourceImpl");
			annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
			annotatedClass.setMyPackage(ojPackage);
			annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
			annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
			addToSource(annotatedClass);

			addDefaultConstructor(annotatedClass);
			addGetRootObjectRepresentation(clazz, annotatedInf, annotatedClass);
			addPutDeleteObjectRepresentation(clazz, annotatedInf, annotatedClass, true);
			addPutDeleteObjectRepresentation(clazz, annotatedInf, annotatedClass, false);
//			addPostRootObjectRepresentation(clazz, annotatedInf, annotatedClass);
			addPostObjectRepresentation(clazz, annotatedInf, annotatedClass);
			addToRouterEnum(clazz, annotatedClass);

		}
	}

	@Override
	public void visitAfter(Class clazz) {
	}
	
	private void addPostObjectRepresentation(Classifier concreteClassifier, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {
		OJAnnotatedOperation postInf = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
		postInf.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		annotatedInf.addToOperations(postInf);
		postInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Post, "json"));

		OJAnnotatedOperation post = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
		post.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		post.addToThrows(TumlRestletGenerationUtil.ResourceException);
		annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(post);
		TinkerGenerationUtil.addSuppressWarning(post);

		OJPathName parentPathName = TumlClassOperations.getPathName(concreteClassifier);

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
		OJForStatement forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
				new OJPathName("Object")), "array");
		ifArray.addToThenPart(forArray);
		forArray.getBody().addToStatements("add(map)");

		OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
		map.setInitExp("(Map<String, Object>) o");
		ifArray.setElsePart(new OJBlock());
		ifArray.getElsePart().addToLocals(map);
		ifArray.getElsePart().addToStatements("add(map)");

		addPostResource(concreteClassifier, annotatedClass, parentPathName);

		ojTryStatement.getTryPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.SUCCESS)");
		annotatedClass.addToImports(TinkerGenerationUtil.tinkerConclusionPathName);

		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.FAILURE)");
		ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
		post.getBody().addToStatements(ojTryStatement);

		annotatedClass.addToImports(parentPathName);

		post.getBody().addToStatements("return get()");

		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(post);
	}
	
	private void addPostResource(Classifier concreteClassifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
		OJAnnotatedOperation add = new OJAnnotatedOperation("add");
		add.setVisibility(OJVisibilityKind.PRIVATE);
		add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
		annotatedClass.addToOperations(add);
		add.getBody().addToStatements(TumlClassOperations.getPathName(concreteClassifier).getLast() + " childResource = new " + TumlClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
		annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifier));
		add.getBody().addToStatements("childResource.fromJson(propertyMap)");
	}

	private void addPutDeleteObjectRepresentation(Classifier classifier, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass, boolean put) {
		OJAnnotatedOperation putOrDeleteInf = new OJAnnotatedOperation(put ? "put" : "delete", TumlRestletGenerationUtil.Representation);
		putOrDeleteInf.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		annotatedInf.addToOperations(putOrDeleteInf);
		putOrDeleteInf.addAnnotationIfNew(new OJAnnotationValue(put ? TumlRestletGenerationUtil.Put : TumlRestletGenerationUtil.Delete, "json"));

		OJAnnotatedOperation putOrDelete = new OJAnnotatedOperation(put ? "put" : "delete", TumlRestletGenerationUtil.Representation);
		putOrDelete.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		putOrDelete.addToThrows(TumlRestletGenerationUtil.ResourceException);
		annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(putOrDelete);
		TinkerGenerationUtil.addSuppressWarning(putOrDelete);

		putOrDelete.getBody().addToStatements("GraphDb.getDb().startTransaction()");
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
		OJForStatement forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
				new OJPathName("Object")), "array");
		ifArray.addToThenPart(forArray);
		forArray.getBody().addToStatements(put ? "put(map)" : "delete(map)");

		OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
		map.setInitExp("(Map<String, Object>) o");
		ifArray.setElsePart(new OJBlock());
		ifArray.getElsePart().addToLocals(map);
		ifArray.getElsePart().addToStatements(put ? "put(map)" : "delete(map)");

		OJPathName parentPathName = TumlClassOperations.getPathName(classifier);

		if (put) {
			addPutResource(classifier, annotatedClass, parentPathName);
		} else {
			addDeleteResource(classifier, annotatedClass, parentPathName);
		}

		ojTryStatement.getTryPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.SUCCESS)");
		annotatedClass.addToImports(TinkerGenerationUtil.tinkerConclusionPathName);

		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.FAILURE)");
		ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
		putOrDelete.getBody().addToStatements(ojTryStatement);

		annotatedClass.addToImports(parentPathName);

		putOrDelete.getBody().addToStatements("return get()");

		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(putOrDelete);
	}

	private void addPutResource(Classifier classifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
		OJAnnotatedOperation put = new OJAnnotatedOperation("put");
		put.setVisibility(OJVisibilityKind.PRIVATE);
		put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
		annotatedClass.addToOperations(put);
		put.getBody().addToStatements("Integer id = (Integer)propertyMap.get(\"id\")");
		put.getBody().addToStatements(
				TumlClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(Long.valueOf(id))");
		annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
		put.getBody().addToStatements("childResource.fromJson(propertyMap)");
	}

	private void addDeleteResource(Classifier classifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {

		OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
		delete.setVisibility(OJVisibilityKind.PRIVATE);
		delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
		annotatedClass.addToOperations(delete);
		delete.getBody().addToStatements("Integer id = (Integer)propertyMap.get(\"id\")");
		delete.getBody().addToStatements(
				TumlClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(Long.valueOf(id))");
		annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
		delete.getBody().addToStatements("childResource.delete()");

	}

	private void addPostRootObjectRepresentation(Class clazz, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {
		OJAnnotatedOperation postInf = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
		postInf.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		annotatedInf.addToOperations(postInf);
		postInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Post, "json"));

		OJAnnotatedOperation post = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
		post.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		post.addToThrows(TumlRestletGenerationUtil.ResourceException);
		annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(post);
		TinkerGenerationUtil.addSuppressWarning(post);

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
		OJForStatement forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
				new OJPathName("Object")), "array");
		ifArray.addToThenPart(forArray);
		OJPathName classPathName = TumlClassOperations.getPathName(clazz);
		OJField resource = new OJField("resource", classPathName);
		forArray.getBody().addToLocals(resource);
		OJIfStatement ifResourceExist = new OJIfStatement("map.get(\"id\") != null");
		ifResourceExist.addToThenPart("resource = new " + classPathName.getLast() + "(GraphDb.getDb().getVertex(map.get(\"id\")))");
		ifResourceExist.addToElsePart("resource = new " + classPathName.getLast() + "(true)");
		forArray.getBody().addToStatements(ifResourceExist);
		forArray.getBody().addToStatements("resource.fromJson(map)");

		OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
		map.setInitExp("(Map<String, Object>) o");
		ifArray.setElsePart(new OJBlock());
		ifArray.getElsePart().addToLocals(map);
		ifArray.getElsePart().addToStatements(classPathName.getLast() + " resource = new " + classPathName.getLast() + "(true)");
		ifArray.getElsePart().addToStatements("resource.fromJson(map)");

		ojTryStatement.getTryPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.SUCCESS)");
		annotatedClass.addToImports(TinkerGenerationUtil.tinkerConclusionPathName);

		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.FAILURE)");
		ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
		post.getBody().addToStatements(ojTryStatement);

		buildToJson(clazz, annotatedClass, post.getBody());

		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(post);
	}

	private void buildToJson(Class clazz, OJAnnotatedClass annotatedClass, OJBlock block) {
		block.addToStatements("StringBuilder json = new StringBuilder()");
		block.addToStatements("json.append(\"{\\\"data\\\": [\")");
		block.addToStatements("json.append(ToJsonUtil.toJsonWithoutCompositeParent(Root.INSTANCE.get" + TumlClassOperations.className(clazz) + "()))");
		annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
		block.addToStatements("json.append(\"],\")");
		block.addToStatements("json.append(\" \\\"meta\\\" : [\")");
		block.addToStatements("json.append(" + TinkerGenerationUtil.RootRuntimePropertyEnum.getLast() + ".asJson())");
		annotatedClass.addToImports(TinkerGenerationUtil.RootRuntimePropertyEnum);
		block.addToStatements("json.append(\", \")");
		block.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(clazz) + ".asJson())");
		annotatedClass.addToImports(TumlClassOperations.getPathName(clazz).append(TumlClassOperations.propertyEnumName(clazz)));
		block.addToStatements("json.append(\"]}\")");
		block.addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
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
		Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteImplementations(clazz);
		int count = 1;
		for (Classifier classifier : concreteImplementations) {

			get.getBody().addToStatements("json.append(\"{\\\"data\\\": [\")");

			if (concreteImplementations.size() > 1) {
				get.getBody().addToStatements(
						"json.append(ToJsonUtil.toJson(resource.select(new "
								+ TinkerGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(TumlClassOperations.getPathName(clazz)).getLast()
								+ "() {\n			@Override\n			public Boolean evaluate(" + TumlClassOperations.getPathName(clazz).getLast()
								+ " e) {\n				return e instanceof " + TumlClassOperations.getPathName(classifier).getLast() + ";\n			}\n		})))");
				annotatedClass.addToImports(TinkerGenerationUtil.BooleanExpressionEvaluator);
				annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));
				annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
			} else {
				get.getBody().addToStatements("json.append(ToJsonUtil.toJson(resource))");
			}

			annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);

			get.getBody().addToStatements("json.append(\"], \\\"meta\\\": [\")");

			get.getBody().addToStatements("json.append(\"{\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"}\")");
			get.getBody().addToStatements("json.append(\", \")");

			// Meta data remains for the root object as viewing a many list does
			// not
			// change the context
			get.getBody().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(clazz) + ".asJson())");
			get.getBody().addToStatements("json.append(\", \")");
			get.getBody().addToStatements("json.append(" + TinkerGenerationUtil.RootRuntimePropertyEnum.getLast() + ".asJson())");
			annotatedClass.addToImports(TinkerGenerationUtil.RootRuntimePropertyEnum);
			annotatedClass.addToImports(TumlClassOperations.getPathName(clazz).append(TumlClassOperations.propertyEnumName(clazz)));
			if (concreteImplementations.size() != 1 && count++ != concreteImplementations.size()) {
				get.getBody().addToStatements("json.append(\",\")");
			}
		}
		get.getBody().addToStatements("json.append(\"]}]\")");
		get.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

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

}

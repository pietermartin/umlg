package org.tuml.restlet.visitor.clazz;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Classifier;
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
		if (!pWrap.isComponent() && !pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isNavigable()) {

			Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
			for (Classifier classifier : concreteImplementations) {
				OJAnnotatedClass owner = findOJClass(classifier);

				OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
						+ pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_" + StringUtils.capitalize(classifier.getName()) + "_ServerResource");
				OJPackage ojPackage = new OJPackage(owner.getMyPackage().toString() + ".restlet");
				annotatedInf.setMyPackage(ojPackage);
				addToSource(annotatedInf);

				OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
						+ pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_" + StringUtils.capitalize(classifier.getName()) + "_ServerResourceImpl");
				annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
				annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
				annotatedClass.setMyPackage(ojPackage);
				addToSource(annotatedClass);
				addDefaultConstructor(annotatedClass);

				addCompositeParentIdField(pWrap, annotatedClass);
				addGetObjectRepresentation(pWrap, annotatedInf, annotatedClass);

				// Put must be Idempotence, i.e. calling it many times must make
				// no
				// difference to server state
				// non unique sequence or a bag can not put as adding the same
				// value
				// more than once changes the state
				addPostObjectRepresentation(classifier, pWrap, annotatedInf, annotatedClass);
				addPutDeleteObjectRepresentation(pWrap, annotatedInf, annotatedClass, true);
				addPutDeleteObjectRepresentation(pWrap, annotatedInf, annotatedClass, false);
				addServerResourceToRouterEnum(classifier, pWrap, annotatedClass);
			}

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
				"this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\""
						+ parentPathName.getLast().toLowerCase() + "Id\"))");
		get.getBody().addToStatements(
				parentPathName.getLast() + " parentResource = GraphDb.getDb().instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");
		annotatedClass.addToImports(parentPathName);
		buildToJson(pWrap, annotatedClass, get.getBody());
		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(get);
	}

	private void addPutDeleteObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass, boolean put) {
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

		PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());

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

		OJPathName parentPathName = otherEndPWrap.javaBaseTypePath();

		if (put) {
			addPutResource(pWrap, annotatedClass, parentPathName);
		} else {
			addDeleteResource(pWrap, annotatedClass, parentPathName);
		}

		ojTryStatement.getTryPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.SUCCESS)");
		annotatedClass.addToImports(TinkerGenerationUtil.tinkerConclusionPathName);

		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.FAILURE)");
		ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
		putOrDelete.getBody().addToStatements(ojTryStatement);

		annotatedClass.addToImports(parentPathName);

		putOrDelete.getBody().addToStatements(
				"this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\""
						+ parentPathName.getLast().toLowerCase() + "Id\"))");
		putOrDelete.getBody().addToStatements(
				parentPathName.getLast() + " parentResource = GraphDb.getDb().instantiateClassifier( " + parentPathName.getLast().toLowerCase() + "Id" + ")");

		buildToJson(pWrap, annotatedClass, putOrDelete.getBody());

		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(putOrDelete);
	}

	private void addDeleteResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {

		OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
		delete.setVisibility(OJVisibilityKind.PRIVATE);
		delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
		annotatedClass.addToOperations(delete);
		delete.getBody().addToStatements("Integer id = (Integer)propertyMap.get(\"id\")");
		delete.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(Long.valueOf(id))");
		annotatedClass.addToImports(pWrap.javaBaseTypePath());
		delete.getBody().addToStatements("childResource.delete()");

	}

	private void addPostObjectRepresentation(Classifier concreteClassifier, PropertyWrapper pWrap, OJAnnotatedInterface annotatedInf,
			OJAnnotatedClass annotatedClass) {
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

		PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());

		OJPathName parentPathName = otherEndPWrap.javaBaseTypePath();
		post.getBody().addToStatements(
				"this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\""
						+ parentPathName.getLast().toLowerCase() + "Id\"))");
		post.getBody().addToStatements(
				parentPathName.getLast() + " parentResource = GraphDb.getDb().instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");

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
		forArray.getBody().addToStatements("add(parentResource, map)");

		OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
		map.setInitExp("(Map<String, Object>) o");
		ifArray.setElsePart(new OJBlock());
		ifArray.getElsePart().addToLocals(map);
		ifArray.getElsePart().addToStatements("add(parentResource, map)");

		addPostResource(concreteClassifier, pWrap, annotatedClass, parentPathName);

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

	private void addPutResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
		OJAnnotatedOperation put = new OJAnnotatedOperation("put");
		put.setVisibility(OJVisibilityKind.PRIVATE);
		put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
		annotatedClass.addToOperations(put);
		put.getBody().addToStatements("Integer id = (Integer)propertyMap.get(\"id\")");
		put.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(Long.valueOf(id))");
		annotatedClass.addToImports(pWrap.javaBaseTypePath());
		put.getBody().addToStatements("childResource.fromJson(propertyMap)");
	}

	private void addPostResource(Classifier concreteClassifier, PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
		OJAnnotatedOperation add = new OJAnnotatedOperation("add");
		add.setVisibility(OJVisibilityKind.PRIVATE);
		add.addToParameters(new OJParameter("parentResource", parentPathName));
		add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
		annotatedClass.addToOperations(add);
		add.getBody().addToStatements(
				pWrap.javaBaseTypePath().getLast() + " childResource = new " + TumlClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
		annotatedClass.addToImports(pWrap.javaBaseTypePath());
		annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifier));
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
		annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
		block.addToStatements("StringBuilder json = new StringBuilder()");
		block.addToStatements("json.append(\"[\")");

		Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
		int count = 1;
		OJIfStatement ifIsOneIfStatement = null;
		if (pWrap.isOne()) {
			block.addToStatements(pWrap.javaBaseTypePath().getLast() + " " + pWrap.fieldname() + " = parentResource." + pWrap.getter() + "()");
			ifIsOneIfStatement = new OJIfStatement();
			block.addToStatements("json.append(\"{\\\"data\\\": \")");
			block.addToStatements("json.append(ToJsonUtil.toJsonWithoutCompositeParent(" + pWrap.fieldname() + "))");
			block.addToStatements("json.append(\",\")");
			block.addToStatements("json.append(\" \\\"meta\\\" : [\")");
			block.addToStatements("json.append(\"{\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"}\")");
			block.addToStatements("json.append(\", \")");
		}
		//For meta data, put where one is navigating to first, then where on is navigating from
		//This is consistent with navigating to a entity with a vertex where there is no navigating from. 
		//i.e. the first meta data in the array is the entity navigating to.
		for (Classifier classifier : concreteImplementations) {
			annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
			if (pWrap.isOne()) {
				OJBlock conditionBlock = new OJBlock();
				String condition = pWrap.fieldname() + " instanceof " + TumlClassOperations.getPathName(classifier).getLast();
				if (count == 1) {
					ifIsOneIfStatement.setCondition(condition);
					ifIsOneIfStatement.setThenPart(conditionBlock);
				} else if (count == concreteImplementations.size()) {
					ifIsOneIfStatement.setElsePart(conditionBlock);
				} else {
					conditionBlock = ifIsOneIfStatement.addToElseIfCondition(condition, "");
				}
				conditionBlock.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(classifier) + ".asJson())");
				conditionBlock.addToStatements("json.append(\", \")");
				conditionBlock.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(pWrap.getOwningType()) + ".asJson())");
				annotatedClass.addToImports(TumlClassOperations.getPathName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType()).append(
						TumlClassOperations.propertyEnumName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType())));
				annotatedClass.addToImports(TumlClassOperations.getPathName(classifier).append(TumlClassOperations.propertyEnumName(classifier)));
				annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getOwningType()).append(TumlClassOperations.propertyEnumName(pWrap.getOwningType())));
				block.addToStatements(ifIsOneIfStatement);
			} else {
				block.addToStatements("json.append(\"{\\\"data\\\": [\")");
				block.addToStatements("json.append(ToJsonUtil.toJsonWithoutCompositeParent(parentResource."
						+ pWrap.getter()
						+ "().select(new "
						+ TinkerGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(TumlClassOperations.getPathName(pWrap.getType()))
								.getLast() + "() {\n			@Override\n			public Boolean evaluate(" + TumlClassOperations.getPathName(pWrap.getType()).getLast()
						+ " e) {\n				return e instanceof " + TumlClassOperations.getPathName(classifier).getLast() + ";\n			}\n		})))");
				annotatedClass.addToImports(TinkerGenerationUtil.BooleanExpressionEvaluator);
				annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getType()));
				block.addToStatements("json.append(\"],\")");
				block.addToStatements("json.append(\" \\\"meta\\\" : [\")");
				block.addToStatements("json.append(\"{\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"}\")");
				block.addToStatements("json.append(\", \")");
			}

			if (!pWrap.isOne()) {
				block.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(classifier) + ".asJson())");
				block.addToStatements("json.append(\", \")");
				block.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(pWrap.getOwningType()) + ".asJson())");
				annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getOwningType()).append(
						TumlClassOperations.propertyEnumName(pWrap.getOwningType())));
				annotatedClass.addToImports(TumlClassOperations.getPathName(classifier).append(TumlClassOperations.propertyEnumName(classifier)));
			}
			if (!pWrap.isOne()) {
				block.addToStatements("json.append(\"]}\")");
				if (concreteImplementations.size() != 1 && count != concreteImplementations.size()) {
					block.addToStatements("json.append(\",\")");
				}
			}
			count++;
		}
		if (pWrap.isOne()) {
			block.addToStatements("json.append(\"]}\")");
		}
		block.addToStatements("json.append(\"]\")");
		block.addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
	}

	private void addServerResourceToRouterEnum(Classifier concreteClassifier, PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
		OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
		OJEnumLiteral ojLiteral = new OJEnumLiteral(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toUpperCase() + "_" + pWrap.getName()
				+ "_" + concreteClassifier.getName());

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
		OJField compositeParentFieldId = new OJField(TumlClassOperations.getPathName(pWrap.getOtherEnd().getType()).getLast().toLowerCase() + "Id",
				new OJPathName("long"));
		compositeParentFieldId.setVisibility(OJVisibilityKind.PRIVATE);
		annotatedClass.addToFields(compositeParentFieldId);
	}

}

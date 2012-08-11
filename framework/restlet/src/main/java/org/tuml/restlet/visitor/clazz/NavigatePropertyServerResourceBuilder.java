package org.tuml.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJField;
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

			OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.getName()
					+ "_ServerResource");
			OJPackage ojPackage = new OJPackage(owner.getMyPackage().toString() + ".restlet");
			annotatedInf.setMyPackage(ojPackage);
			addToSource(annotatedInf);

			OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.getName() + "_ServerResourceImpl");
			annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
			annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
			annotatedClass.setMyPackage(ojPackage);
			addToSource(annotatedClass);
			addDefaultConstructor(annotatedClass);

			addCompositeParentIdField(pWrap, annotatedClass);
			addGetObjectRepresentation(pWrap, annotatedInf, annotatedClass);
			//TODO think about
			//Put must be Idempotence, i.e. calling it many times must make no difference to server state
			
			//non unique sequence or a bag can not put as adding the same value more than once changes the state
			if (!pWrap.isUnique()) {
				//Use post
			} else {
				addPutObjectRepresentation(pWrap, annotatedInf, annotatedClass);
				addServerResourceToRouterEnum(pWrap, annotatedClass);
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
				"this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\"" + parentPathName.getLast().toLowerCase()
						+ "Id\"))");
		get.getBody().addToStatements(
				parentPathName.getLast() + " parentResource = new " + parentPathName.getLast() + "(GraphDb.getDb().getVertex(this." + parentPathName.getLast().toLowerCase() + "Id"
						+ "))");
		annotatedClass.addToImports(parentPathName);
		get.getBody().addToStatements("StringBuilder json = new StringBuilder()");
		get.getBody().addToStatements("json.append(\"[\")");
		get.getBody().addToStatements("json.append(ToJsonUtil.toJson(parentResource." + pWrap.getter() + "()))");
		annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
		get.getBody().addToStatements("json.append(\",\")");
		get.getBody().addToStatements("json.append(\" {\\\"meta\\\" : \")");
		get.getBody().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(pWrap.getOwningType()) + ".asJson())");
		annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getOwningType()).append(TumlClassOperations.propertyEnumName(pWrap.getOwningType())));
		get.getBody().addToStatements("json.append(\"}, \")");

		get.getBody().addToStatements("json.append(\" {\\\"meta\\\" : \")");
		get.getBody().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType()) + ".asJson())");
		annotatedClass.addToImports(TumlClassOperations.getPathName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType()).append(
				TumlClassOperations.propertyEnumName(new PropertyWrapper(pWrap.getOtherEnd()).getOwningType())));
		get.getBody().addToStatements("json.append(\"}]\")");

		get.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(get);
	}

	private void addPutObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {

		OJAnnotatedOperation putInf = new OJAnnotatedOperation("put", TumlRestletGenerationUtil.Representation);
		putInf.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		annotatedInf.addToOperations(putInf);
		putInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Put, "json"));

		OJAnnotatedOperation put = new OJAnnotatedOperation("put", TumlRestletGenerationUtil.Representation);
		put.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
		put.addToThrows(TumlRestletGenerationUtil.ResourceException);
		annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(put);

		PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());

		OJPathName parentPathName = otherEndPWrap.javaBaseTypePath();
		put.getBody().addToStatements(
				"this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\"" + parentPathName.getLast().toLowerCase()
						+ "Id\"))");
		put.getBody().addToStatements(
				parentPathName.getLast() + " parentResource = new " + parentPathName.getLast() + "(GraphDb.getDb().getVertex(this." + parentPathName.getLast().toLowerCase() + "Id"
						+ "))");

		put.getBody().addToStatements("GraphDb.getDb().startTransaction()");
		OJTryStatement ojTryStatement = new OJTryStatement();
		OJField mapper = new OJField("mapper", TinkerGenerationUtil.ObjectMapper);
		mapper.setInitExp("new ObjectMapper()");
		ojTryStatement.getTryPart().addToLocals(mapper);
		OJAnnotatedField propertyMap = new OJAnnotatedField("propertyMap", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
				new OJPathName("Object")));
		propertyMap.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.SuppressWarnings"), "unchecked"));
		propertyMap.setInitExp("mapper.readValue(entity.getText(), Map.class)");
		ojTryStatement.getTryPart().addToLocals(propertyMap);
		OJField childResource = new OJField("childResource", pWrap.javaBaseTypePath());
		childResource.setInitExp("new " + pWrap.javaBaseTypePath().getLast() + "(GraphDb.getDb().getVertex(propertyMap.get(\"id\")))");
		ojTryStatement.getTryPart().addToLocals(childResource);

		ojTryStatement.getTryPart().addToStatements("childResource.fromJson(propertyMap)");

		ojTryStatement.getTryPart().addToStatements("parentResource." + pWrap.adder() + "(childResource)");

		ojTryStatement.getTryPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.SUCCESS)");
		annotatedClass.addToImports(TinkerGenerationUtil.tinkerConclusionPathName);

		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.FAILURE)");
		ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
		put.getBody().addToStatements(ojTryStatement);

		annotatedClass.addToImports(parentPathName);
		put.getBody().addToStatements("StringBuilder json = new StringBuilder()");
		put.getBody().addToStatements("json.append(\"[\")");
		put.getBody().addToStatements("json.append(ToJsonUtil.toJson(parentResource." + pWrap.getter() + "()))");
		annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
		put.getBody().addToStatements("json.append(\",\")");
		put.getBody().addToStatements("json.append(\" {\\\"meta\\\" : \")");
		put.getBody().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(pWrap.getOwningType()) + ".asJson())");
		annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getOwningType()).append(TumlClassOperations.propertyEnumName(pWrap.getOwningType())));
		put.getBody().addToStatements("json.append(\"}, \")");

		put.getBody().addToStatements("json.append(\" {\\\"meta\\\" : \")");
		put.getBody().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(otherEndPWrap.getOwningType()) + ".asJson())");
		annotatedClass.addToImports(TumlClassOperations.getPathName(otherEndPWrap.getOwningType()).append(TumlClassOperations.propertyEnumName(otherEndPWrap.getOwningType())));
		put.getBody().addToStatements("json.append(\"}]\")");

		put.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
		annotatedClass.addToOperations(put);
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

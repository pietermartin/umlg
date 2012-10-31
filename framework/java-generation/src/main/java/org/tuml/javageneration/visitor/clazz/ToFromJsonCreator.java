package org.tuml.javageneration.visitor.clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJParameter;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJTryStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.DataTypeEnum;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ToFromJsonCreator extends BaseVisitor implements Visitor<Class> {

	public static final String URI_FOR_RESTFULL = "uri";

	public ToFromJsonCreator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		addToJson(clazz, "toJson", TumlClassOperations.getPrimitiveOrEnumOrComponentsProperties(clazz));
		addToJson(clazz, "toJsonWithoutCompositeParent", TumlClassOperations.getPrimitiveOrEnumOrComponentsPropertiesExcludingCompositeParent(clazz));
		addFromJson(clazz);
		addFromJsonWithMapper(clazz);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

	private void addToJson(Class clazz, String operationName, Set<Property> propertiesForToJson) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJAnnotatedOperation toJson = new OJAnnotatedOperation(operationName, new OJPathName("String"));
		TinkerGenerationUtil.addOverrideAnnotation(toJson);
		if (clazz.getGenerals().isEmpty()) {
			toJson.getBody().addToStatements("StringBuilder sb = new StringBuilder()");
			toJson.getBody().addToStatements("sb.append(\"\\\"id\\\": \" + getId() + \", \")");
		} else {
			toJson.getBody().addToStatements("String result = super." + operationName + "()");
			toJson.getBody().addToStatements("result = result.substring(1, result.length() - 1)");
			toJson.getBody().addToStatements("StringBuilder sb = new StringBuilder(result)");
			if (!propertiesForToJson.isEmpty()) {
				toJson.getBody().addToStatements("sb.append(\", \")");
			}
		}
		int count = 1;
		for (Property p : propertiesForToJson) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (pWrap.isMany()) {
				OJIfStatement ifEmpty = new OJIfStatement(pWrap.getter() + "().isEmpty()");
				ifEmpty.addToThenPart("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + " + pWrap.getter() + "().toJson() + \"" + "\")");
				ifEmpty.addToElsePart("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + " + pWrap.getter() + "().toJson() + \"" + "\")");
				toJson.getBody().addToStatements(ifEmpty);
			} else if (pWrap.isEnumeration()) {
				toJson.getBody().addToStatements(
						"sb.append(\"\\\"" + pWrap.getName() + "\\\": \\\"\" + (" + pWrap.getter() + "() == null ? \"null\" : " + pWrap.getter() + "().toJson()) + \"\\\"" + "\")");
			} else {
				if (pWrap.isNumber() || pWrap.isBoolean()) {
					toJson.getBody().addToStatements("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + " + pWrap.getter() + "() + \"" + "\")");
				} else if (!pWrap.isDataType()) {
					toJson.getBody().addToStatements(
							"sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"{\\\"id\\\": \" + " + pWrap.getter()
									+ "().getId() + \", \\\"displayName\\\": \\\"\" + " + pWrap.getter()
									+ "().getName() + \"\\\"}\" : \"{\\\"id\\\": \" + null + \", \\\"displayName\\\": \" + null + \"}\") + \"" + "\")");
				} else if (pWrap.isDateTime()) {
					toJson.getBody().addToStatements(
							"sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + " + TinkerGenerationUtil.tumlFormatter.getLast()
									+ ".format(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
					annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
				} else if (pWrap.isDate()) {
					toJson.getBody().addToStatements(
							"sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + " + TinkerGenerationUtil.tumlFormatter.getLast()
									+ ".format(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
					annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
				} else if (pWrap.isTime()) {
					toJson.getBody().addToStatements(
							"sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + " + TinkerGenerationUtil.tumlFormatter.getLast()
									+ ".format(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
					annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
				} else if (pWrap.isImage()) {
					toJson.getBody().addToStatements(
							"sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + " + TinkerGenerationUtil.tumlFormatter.getLast()
									+ ".encode(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
					annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
				} else if (pWrap.isVideo()) {
					toJson.getBody().addToStatements(
							"sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + " + TinkerGenerationUtil.tumlFormatter.getLast()
									+ ".encode(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
					annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
				} else if (pWrap.isAudio()) {
					toJson.getBody().addToStatements(
							"sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + TinkerFormatter.encode(" + pWrap.getter()
									+ "()) + \"\\\"\" : null " + "))");
					annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
				} else {
					toJson.getBody().addToStatements(
							"sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + " + pWrap.getter() + "() + \"\\\"\" : null " + "))");
					// toJson.getBody().addToStatements("sb.append(\"\\\"" +
					// pWrap.getName() + "\\\": \\\"\" + " + pWrap.getter() +
					// "() + \"\\\"" + "\")");
				}
			}
			if (count++ != propertiesForToJson.size()) {
				toJson.getBody().addToStatements("sb.append(\", \")");
			}
		}
		if (clazz.getGenerals().isEmpty()) {
			toJson.getBody().addToStatements("sb.append(\", \")");
			toJson.getBody().addToStatements(URI_FOR_RESTFULL, "//PlaceHolder for restfull\nsb.append(\"\\\"uri\\\": {}\")");
		}
		toJson.getBody().addToStatements("sb.insert(0, \"{\")");
		toJson.getBody().addToStatements("sb.append(\"}\")");
		toJson.getBody().addToStatements("return sb.toString()");
		annotatedClass.addToOperations(toJson);
	}

	private void addFromJson(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJAnnotatedOperation fromJson = new OJAnnotatedOperation("fromJson");
		fromJson.addParam("json", new OJPathName("String"));
		TinkerGenerationUtil.addOverrideAnnotation(fromJson);
		annotatedClass.addToOperations(fromJson);

		OJField objectMapper = new OJField("mapper", TinkerGenerationUtil.ObjectMapper);
		objectMapper.setInitExp("new ObjectMapper()");
		fromJson.getBody().addToLocals(objectMapper);

		OJTryStatement tryS = new OJTryStatement();

		OJAnnotatedField propertyMap = new OJAnnotatedField("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
		propertyMap.setInitExp("mapper.readValue(json, Map.class)");
		propertyMap.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.SuppressWarnings"), "unchecked"));
		tryS.getTryPart().addToLocals(propertyMap);

		tryS.getTryPart().addToStatements("fromJson(propertyMap)");

		tryS.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		tryS.getCatchPart().addToStatements("throw new RuntimeException(e)");

		fromJson.getBody().addToStatements(tryS);
	}

	private void addFromJsonWithMapper(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJAnnotatedOperation fromJson = new OJAnnotatedOperation("fromJson");
		fromJson.addParam("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
		TinkerGenerationUtil.addOverrideAnnotation(fromJson);
		annotatedClass.addToOperations(fromJson);
		if (!clazz.getGenerals().isEmpty()) {
			fromJson.getBody().addToStatements("super.fromJson(propertyMap)");
		}
		List<Property> propertiesForToJson = new ArrayList<Property>(TumlClassOperations.getPrimitiveOrEnumOrComponentsProperties(clazz));
		if (!propertiesForToJson.isEmpty()) {
			for (Property property : propertiesForToJson) {
				PropertyWrapper pWrap = new PropertyWrapper(property);
				OJField field;
				if (pWrap.isMany()) {
					field = new OJField(pWrap.getName(), pWrap.javaTypePath());
					field.setInitExp("new " + pWrap.javaTumlMemoryTypePath().getLast() + "((Collection<" + pWrap.javaBaseTypePath() + ">)propertyMap.get(\"" + pWrap.getName()
							+ "\"))");
					annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
					annotatedClass.addToImports(new OJPathName("java.util.Collection"));
				} else if (pWrap.isEnumeration()) {
					field = new OJField(pWrap.getName(), pWrap.javaBaseTypePath());
					field.setInitExp(pWrap.javaBaseTypePath().getLast() + ".fromJson((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
				} else {
					if (pWrap.isNumber()) {
						OJField fieldNumber = new OJField(pWrap.getName() + "AsNumber", new OJPathName("Number"));
						fieldNumber.setInitExp("(" + new OJPathName("Number") + ")propertyMap.get(\"" + pWrap.getName() + "\")");
						field = new OJField(pWrap.getName(), pWrap.javaBaseTypePath());
						if (pWrap.isInteger()) {
							field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".intValue() : null");
						} else if (pWrap.isLong()) {
							field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".longValue() : null");
						} else {
							throw new RuntimeException("Not yet implemented!");
						}
						fromJson.getBody().addToLocals(fieldNumber);
					} else if (!pWrap.isDataType()) {
						field = new OJField(pWrap.getName() + "Map", new OJPathName("Map<String, Number>"));
						field.setInitExp("(Map<String, Number>)propertyMap.get(\"" + pWrap.getName() + "\")");
						TinkerGenerationUtil.addSuppressWarning(fromJson);
					} else if (pWrap.isDate()) {
						field = new OJField(pWrap.getName(), DataTypeEnum.Date.getPathName());
						field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".parseDate((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
						annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
					} else if (pWrap.isDateTime()) {
						field = new OJField(pWrap.getName(), DataTypeEnum.DateTime.getPathName());
						field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".parseDateTime((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
						annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
					} else if (pWrap.isTime()) {
						field = new OJField(pWrap.getName(), DataTypeEnum.Time.getPathName());
						field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".parseTime((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
						annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
					} else if (pWrap.isImage()) {
						field = new OJField(pWrap.getName(), DataTypeEnum.Image.getPathName());
						field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".decode((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
						annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
					} else {
						field = new OJField(pWrap.getName(), pWrap.javaBaseTypePath());
						field.setInitExp("(" + pWrap.javaBaseTypePath() + ")propertyMap.get(\"" + pWrap.getName() + "\")");
					}
				}
				fromJson.getBody().addToLocals(field);
				// if (!pWrap.isEnumeration()) {
				OJIfStatement ifNotNull = new OJIfStatement(field.getName() + " != null");
				if (pWrap.isOne() && !pWrap.isDataType() && !pWrap.isEnumeration()) {
					OJIfStatement ifSetToNull = new OJIfStatement(field.getName() + ".isEmpty() || " + field.getName() + ".get(\"id\") == null", pWrap.setter() + "(null)");
					ifSetToNull.addToElsePart(pWrap.setter() + "((" + pWrap.javaBaseTypePath().getLast() + ")GraphDb.getDb().instantiateClassifier(" + pWrap.fieldname()
							+ "Map.get(\"id\").longValue()))");
					annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
					ifNotNull.addToThenPart(ifSetToNull);
					fromJson.getBody().addToStatements(ifNotNull);
				} else if (pWrap.isOne() && pWrap.isDataType()) {
					fromJson.getBody().addToStatements(pWrap.setter() + "(" + pWrap.fieldname() + ")");
				} else {
					// TODO that null is not going to work with anything
					// other
					// than
					// a string
					OJIfStatement ifSetToNull = new OJIfStatement(field.getName() + ".equals(\"null\")", pWrap.setter() + "(null)");
					ifSetToNull.addToElsePart(pWrap.setter() + "(" + field.getName() + ")");
					ifNotNull.addToThenPart(ifSetToNull);
					fromJson.getBody().addToStatements(ifNotNull);
				}
				// }
			}

		}
	}

}

package org.tuml.javageneration.visitor.clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJForStatement;
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
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ToFromJsonCreator extends BaseVisitor implements Visitor<Class> {

	public ToFromJsonCreator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		addToJson(clazz);
		addFromJson(clazz);
		addFromJsonWithMapper(clazz);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

	private void addToJson(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJAnnotatedOperation toJson = new OJAnnotatedOperation("toJson", new OJPathName("String"));
		TinkerGenerationUtil.addOverrideAnnotation(toJson);
		if (clazz.getGenerals().isEmpty()) {
			toJson.getBody().addToStatements("StringBuilder sb = new StringBuilder()");
		} else {
			toJson.getBody().addToStatements("String result = super.toJson()");
			toJson.getBody().addToStatements("result = result.substring(1, result.length() - 1)");
			toJson.getBody().addToStatements("StringBuilder sb = new StringBuilder(result)");
		}
		toJson.getBody().addToStatements("sb.append(\"{\")");
		toJson.getBody().addToStatements("sb.append(\"\\\"id\\\": \" + getId() + \", \")");
		Set<Property> propertiesForToJson = TumlClassOperations.getPrimitiveOrEnumOrComponentsProperties(clazz);
		int count = 0;
		for (Property p : propertiesForToJson) {
			count++;
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (pWrap.isMany()) {
				toJson.getBody().addToStatements("sb.append(\"" + "\\\"" + pWrap.getName() + "\\\": \" + " + pWrap.getter() + "().toJson() + \"" + "\")");
			} else if (pWrap.isEnumeration()) {
				toJson.getBody().addToStatements(
						"sb.append(\"" + "\\\"" + pWrap.getName() + "\\\": \\\"\" + (" + pWrap.getter() + "() == null ? \"null\" : " + pWrap.getter() + "().toJson()) + \"\\\"" + "\")");
			} else {
				toJson.getBody().addToStatements("sb.append(\"" + "\\\"" + pWrap.getName() + "\\\": \\\"\" + " + pWrap.getter() + "() + \"\\\"" + "\")");
			}
			if (count < propertiesForToJson.size()) {
				toJson.getBody().addToStatements("sb.append(\", \")");
			}
		}
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
			OJForStatement forS = new OJForStatement("propertyName", new OJPathName("String"), "propertyMap.keySet()");
			OJIfStatement ifS = new OJIfStatement();
			forS.getBody().addToStatements(ifS);
			boolean first = true;
			for (Property property : propertiesForToJson) {
				PropertyWrapper pWrap = new PropertyWrapper(property);
				if (first) {
					first = false;
					// TODO what about manies
					if (pWrap.isMany()) {
						ifS.setCondition("propertyName.equals(\"" + pWrap.getName() + "\")");
						OJAnnotatedField field = new OJAnnotatedField(pWrap.getName(), pWrap.javaTypePath());
						field.setInitExp("new " + pWrap.javaTumlMemoryTypePath().getLast() + "((Collection<" + pWrap.javaBaseTypePath() + ">)propertyMap.get(propertyName))");
						field.suppressUncheckedWarning();
						annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
						annotatedClass.addToImports(new OJPathName("java.util.Collection"));
						ifS.getThenPart().addToLocals(field);
						ifS.addToThenPart(pWrap.setter() + "(" + pWrap.getName() + ")");
					} else if (pWrap.isEnumeration()) {
						ifS.setCondition("propertyName.equals(\"" + pWrap.getName() + "\")");
						ifS.addToThenPart(pWrap.setter() + "(" + pWrap.javaBaseTypePath().getLast() + ".fromJson((String)propertyMap.get(propertyName)))");
					} else {
						ifS.setCondition("propertyName.equals(\"" + pWrap.getName() + "\")");
						ifS.addToThenPart(pWrap.setter() + "((" + pWrap.javaBaseTypePath() + ")propertyMap.get(propertyName))");
					}
				} else {
					if (pWrap.isMany()) {

						OJAnnotatedField field = new OJAnnotatedField(pWrap.getName(), pWrap.javaTypePath());
						field.setInitExp("new " + pWrap.javaTumlMemoryTypePath().getLast() + "((Collection<" + pWrap.javaBaseTypePath() + ">)propertyMap.get(propertyName))");
						field.suppressUncheckedWarning();
						OJBlock b = ifS.addToElseIfCondition("propertyName.equals(\"" + pWrap.getName() + "\")", field);
						b.addToStatements(pWrap.setter() + "(" + pWrap.getName() + ")");
						annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
						annotatedClass.addToImports(new OJPathName("java.util.Collection"));

					} else if (pWrap.isEnumeration()) {
						ifS.addToElseIfCondition("propertyName.equals(\"" + pWrap.getName() + "\")", pWrap.setter() + "(" + pWrap.javaBaseTypePath().getLast()
								+ ".fromJson((String)propertyMap.get(propertyName)))");
					} else {
						ifS.addToElseIfCondition("propertyName.equals(\"" + pWrap.getName() + "\")", pWrap.setter() + "((" + pWrap.javaBaseTypePath()
								+ ")propertyMap.get(propertyName))");
					}
				}
			}
			ifS.addToElseIfCondition("propertyName.equals(\"id\")", "//Ignored");
			ifS.addToElsePart("throw new IllegalStateException()");
			fromJson.getBody().addToStatements(forS);
		}
	}

}

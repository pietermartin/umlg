package org.tuml.javageneration.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ToJsonCreator extends BaseVisitor implements Visitor<Class> {

	public ToJsonCreator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
	}

	@Override
	public void visitAfter(Class clazz) {
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
			toJson.getBody().addToStatements("sb.append(\"" + pWrap.toJson() + "\")");
			if (count < propertiesForToJson.size()) {
				toJson.getBody().addToStatements("sb.append(\", \")");
			}
		}
		toJson.getBody().addToStatements("sb.append(\"}\")");
		toJson.getBody().addToStatements("return sb.toString()");
		annotatedClass.addToOperations(toJson);
	}

}

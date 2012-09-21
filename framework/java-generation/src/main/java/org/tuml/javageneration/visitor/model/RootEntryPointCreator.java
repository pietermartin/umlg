package org.tuml.javageneration.visitor.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJStatement;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.Condition;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlModelOperations;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.clazz.RuntimePropertyImplementor;

public class RootEntryPointCreator extends BaseVisitor implements Visitor<Model> {

	public RootEntryPointCreator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Model model) {
		OJAnnotatedClass root = new OJAnnotatedClass("Root");
		OJPackage ojPackage = new OJPackage("org.tuml.root");
		root.setMyPackage(ojPackage);
		addToSource(root);

		root.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
		root.getDefaultConstructor().getBody().addToStatements("v = GraphDb.getDb().getRoot()");
		root.addToImports(TinkerGenerationUtil.graphDbPathName);
		root.addToImports(TinkerGenerationUtil.vertexPathName);

		OJField INSTANCE = new OJField("INSTANCE", root.getPathName());
		INSTANCE.setStatic(true);
		INSTANCE.setInitExp("new Root()");
		root.addToFields(INSTANCE);

		OJField vertex = new OJField("v", TinkerGenerationUtil.vertexPathName);
		root.addToFields(vertex);

		OJEnum ojEnum = RuntimePropertyImplementor.addTumlRuntimePropertyEnum(root, "RootRuntimePropertyEnum", "Root", new HashSet<Property>(), false, model.getName());

		@SuppressWarnings("unchecked")
		List<Class> result = (List<Class>) TumlModelOperations.findElements(model, new Condition() {
			@Override
			public boolean evaluateOn(Element e) {
				if (!(e instanceof Class)) {
					return false;
				}
				Class clazz = (Class) e;
				return !clazz.isAbstract() && !TumlClassOperations.hasCompositeOwner(clazz);
			}
		});

		// Rebuild asJson
		OJAnnotatedOperation asJson = ojEnum.findOperation("asJson");
		asJson.getBody().removeAllFromStatements();
		asJson.getBody().addToStatements("StringBuilder sb = new StringBuilder();");

		asJson.getBody().addToStatements("sb.append(\"{\\\"name\\\": \\\"Root\\\", \")");
		asJson.getBody().addToStatements("uri", "sb.append(\"\\\"uri\\\": \\\"TODO\\\", \")");
		asJson.getBody().addToStatements("properties", "sb.append(\"\\\"properties\\\": [\")");

		asJson.getBody().addToStatements("sb.append(" + TinkerGenerationUtil.RootRuntimePropertyEnum.getLast() + "." + model.getName() + ".toJson())");
		asJson.getBody().addToStatements("sb.append(\",\")");

		OJAnnotatedOperation fromLabel = ojEnum.findOperation("fromLabel", new OJPathName("String"));
		int count = 0;
		// Add root entities as though they are fake properties to App root
		for (Class clazz : result) {
			count++;
			RuntimePropertyImplementor.addEnumLiteral(ojEnum, fromLabel, StringUtils.uncapitalize(TumlClassOperations.className(clazz)), false, false, false, true, false, true,
					false, false, false, false, true, false, false, false, true, false, false, false, false, -1, 0, false, false, true, false, true,
					"root" + TumlClassOperations.className(clazz));

			asJson.getBody().addToStatements("sb.append(" + ojEnum.getName() + "." + StringUtils.uncapitalize(TumlClassOperations.className(clazz)) + ".toJson())");
			if (count != result.size()) {
				asJson.getBody().addToStatements("sb.append(\",\")");
			}
		}
		asJson.getBody().addToStatements("sb.append(\"]}\")");
		asJson.getBody().addToStatements("return sb.toString()");

		// Move fromLabel's return null from first line to last line
		count = 0;
		List<Integer> toRemove = new ArrayList<Integer>();
		for (OJStatement s : fromLabel.getBody().getStatements()) {
			if (s.toJavaString().equals("return null;")) {
				toRemove.add(count);
			}
			count++;
		}
		for (Integer integer : toRemove) {
			fromLabel.getBody().getStatements().remove(integer.intValue());
		}
		fromLabel.getBody().addToStatements("return null");
	}

	@Override
	public void visitAfter(Model model) {
	}

}

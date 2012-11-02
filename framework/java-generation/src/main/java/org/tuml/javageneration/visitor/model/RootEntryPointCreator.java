package org.tuml.javageneration.visitor.model;

import java.util.ArrayList;
import java.util.Collections;
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
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.Condition;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlModelOperations;
import org.tuml.javageneration.validation.Validation;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.clazz.RuntimePropertyImplementor;

public class RootEntryPointCreator extends BaseVisitor implements Visitor<Model> {

	public RootEntryPointCreator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Model model) {
		OJAnnotatedClass root = new OJAnnotatedClass("Root");
		root.addToImplementedInterfaces(TinkerGenerationUtil.TumlRootNode);
		OJPackage ojPackage = new OJPackage("org.tuml.root");
		root.setMyPackage(ojPackage);
		addToSource(root);

		root.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
		root.addToImports(TinkerGenerationUtil.graphDbPathName);
		root.addToImports(TinkerGenerationUtil.vertexPathName);

		addINSTANCE(root);
		addGetRootVertex(root);
		rebuildAsJson(model, root);
		implementTumlRootNode(root);
	}


	private void implementTumlRootNode(OJAnnotatedClass root) {
		OJAnnotatedOperation getId = new OJAnnotatedOperation("getId");
		getId.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
		getId.setReturnType(new OJPathName("java.lang.Long"));
		getId.getBody().addToStatements("return TinkerIdUtilFactory.getIdUtil().getId(getRootVertex())");
		root.addToOperations(getId);
		root.addToImports(TinkerGenerationUtil.tinkerIdUtilFactoryPathName);
		
		OJAnnotatedOperation toJson = new OJAnnotatedOperation("toJson");
		toJson.setReturnType("String");
		toJson.getBody().addToStatements("return \"{\\\"id\\\": \" + getId() + \"}\"");
		root.addToOperations(toJson);
	}

	private void rebuildAsJson(Model model, OJAnnotatedClass root) {
		OJEnum ojEnum = RuntimePropertyImplementor.addTumlRuntimePropertyEnum(root, "RootRuntimePropertyEnum", model, new HashSet<Property>(), false, model.getName());

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
		List<Class> result = findRootEntities(model);
		// Add root entities as though they are fake properties to App root
		for (Class clazz : result) {
			count++;
			RuntimePropertyImplementor.addEnumLiteral(ojEnum, fromLabel, StringUtils.uncapitalize(TumlClassOperations.className(clazz)), model.getQualifiedName(), false, null,
					Collections.<Validation> emptyList(), true, false, false, false, true, false, false, false, false, -1, 0, false, false, true, false, true, "root"
							+ TumlClassOperations.className(clazz));

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

	private List<Class> findRootEntities(Model model) {
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
		return result;
	}

	private void addGetRootVertex(OJAnnotatedClass root) {
		OJAnnotatedOperation getRootVertex = new OJAnnotatedOperation("getRootVertex");
		getRootVertex.setReturnType(TinkerGenerationUtil.vertexPathName);
		getRootVertex.setVisibility(OJVisibilityKind.PRIVATE);
		getRootVertex.getBody().addToStatements("return GraphDb.getDb().getRoot()");
		root.addToOperations(getRootVertex);
	}

	private void addINSTANCE(OJAnnotatedClass root) {
		OJField INSTANCE = new OJField("INSTANCE", root.getPathName());
		INSTANCE.setStatic(true);
		INSTANCE.setInitExp("new Root()");
		root.addToFields(INSTANCE);
	}

	@Override
	public void visitAfter(Model model) {
	}

}

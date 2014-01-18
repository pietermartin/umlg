package org.umlg.javageneration.visitor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJStatement;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotationValue;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Condition;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgModelOperations;
import org.umlg.javageneration.validation.Validation;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.javageneration.visitor.clazz.RuntimePropertyImplementor;

public class RootEntryPointCreator extends BaseVisitor implements Visitor<Model> {

    public RootEntryPointCreator(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Model model) {
        OJAnnotatedClass root = new OJAnnotatedClass(StringUtils.capitalize(model.getName()));
        root.setComment(String.format("This class represents the model %s.\nIt is a singleton and allows on access to all the root classes in the model.", model.getQualifiedName()));
        root.addToImplementedInterfaces(UmlgGenerationUtil.UmlgApplicationNode);
        OJPackage ojPackage = new OJPackage(UmlgGenerationUtil.UmlgRootPackage.toJavaString());
        root.setMyPackage(ojPackage);
        addToSource(root);

        root.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
        root.addToImports(UmlgGenerationUtil.graphDbPathName);
        root.addToImports(UmlgGenerationUtil.vertexPathName);

        addINSTANCE(root, model);
        addGetRootVertex(root);
        addModelAndRebuildAsJson(model, root);
        implementTumlRootNode(root);
    }

    private void implementTumlRootNode(OJAnnotatedClass root) {
        OJAnnotatedOperation getId = new OJAnnotatedOperation("getId");
        getId.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
        getId.setReturnType(new OJPathName("Object"));
        getId.getBody().addToStatements("return getRootVertex().getId()");
        root.addToOperations(getId);

        OJAnnotatedOperation toJson = new OJAnnotatedOperation("toJson");
        toJson.setReturnType("String");
        toJson.getBody().addToStatements("return \"{\\\"id\\\": \" + getId() + \"}\"");
        root.addToOperations(toJson);
    }

    private void addModelAndRebuildAsJson(Model model, OJAnnotatedClass root) {
        OJEnum ojEnum = RuntimePropertyImplementor.addTumlRuntimePropertyEnum(root, StringUtils.capitalize(model.getName()) + "RuntimePropertyEnum", model, new HashSet<Property>(), false,
                model.getName());

        // Rebuild asJson
        OJAnnotatedOperation asJson = ojEnum.findOperation("asJson");
        asJson.getBody().removeAllFromStatements();
        asJson.getBody().addToStatements("StringBuilder sb = new StringBuilder();");

        asJson.getBody().addToStatements("sb.append(\"{\\\"name\\\": \\\"" + model.getName() + "\\\", \")");
        asJson.getBody().addToStatements("uri", "sb.append(\"\\\"uri\\\": \\\"TODO\\\", \")");
        asJson.getBody().addToStatements("properties", "sb.append(\"\\\"properties\\\": [\")");

        asJson.getBody().addToStatements("sb.append(" + StringUtils.capitalize(model.getName()) + "RuntimePropertyEnum." + model.getName() + ".toJson())");
        asJson.getBody().addToStatements("sb.append(\",\")");

        OJAnnotatedOperation fromLabel = ojEnum.findOperation("fromLabel", new OJPathName("String"));
        OJAnnotatedOperation fromQualifiedName = ojEnum.findOperation("fromQualifiedName", new OJPathName("String"));
        OJAnnotatedOperation fromInverseQualifiedName = ojEnum.findOperation("fromInverseQualifiedName", new OJPathName("String"));
        int count = 0;
        List<Class> result = findRootEntities(model);
        // Add root entities as though they are fake properties to App root
        for (Class clazz : result) {
            count++;
            RuntimePropertyImplementor.addEnumLiteral(false, false, null, null, false, ojEnum, fromLabel, fromQualifiedName, fromInverseQualifiedName,
                    StringUtils.uncapitalize(UmlgClassOperations.className(clazz)), clazz.getQualifiedName(), "inverseOf::" + clazz.getName(), "inverseOf::" + clazz.getQualifiedName(), false, false,
                    null, Collections.<Validation>emptyList(), true, false, false, false, true, false, false, true, false, -1, 0, 1, false, false, true, false, true,
                    true, "root" + UmlgClassOperations.className(clazz));

            asJson.getBody().addToStatements(
                    "sb.append(" + ojEnum.getName() + "." + StringUtils.uncapitalize(UmlgClassOperations.className(clazz)) + ".toJson())");
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

        // Move fromQualifiedName's return null from first line to last line
        count = 0;
        toRemove = new ArrayList<Integer>();
        for (OJStatement s : fromQualifiedName.getBody().getStatements()) {
            if (s.toJavaString().equals("return null;")) {
                toRemove.add(count);
            }
            count++;
        }
        for (Integer integer : toRemove) {
            fromQualifiedName.getBody().getStatements().remove(integer.intValue());
        }
        fromQualifiedName.getBody().addToStatements("return null");

    }

    private List<Class> findRootEntities(Model model) {
        @SuppressWarnings("unchecked")
        List<Class> result = (List<Class>) UmlgModelOperations.findElements(model, new Condition() {
            @Override
            public boolean evaluateOn(Element e) {
                if (!(e instanceof Class)) {
                    return false;
                }
                Class clazz = (Class) e;
                return !clazz.isAbstract() && !UmlgClassOperations.hasCompositeOwner(clazz) && !(clazz instanceof AssociationClass);
            }
        });
        return result;
    }

    private void addGetRootVertex(OJAnnotatedClass root) {
        OJAnnotatedOperation getRootVertex = new OJAnnotatedOperation("getRootVertex");
        getRootVertex.setReturnType(UmlgGenerationUtil.vertexPathName);
        getRootVertex.setVisibility(OJVisibilityKind.PRIVATE);
        getRootVertex.getBody().addToStatements("return GraphDb.getDb().getRoot()");
        root.addToOperations(getRootVertex);
    }

    private void addINSTANCE(OJAnnotatedClass root, Model model) {
        OJField INSTANCE = new OJField("INSTANCE", root.getPathName());
        INSTANCE.setStatic(true);
        INSTANCE.setInitExp("new " + StringUtils.capitalize(model.getName()) + "()");
        root.addToFields(INSTANCE);
    }

    @Override
    public void visitAfter(Model model) {
    }

}

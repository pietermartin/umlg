package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.*;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Date: 2013/03/29
 * Time: 9:31 AM
 */
public class SchemaCreator extends BaseVisitor implements Visitor<Class> {

    public SchemaCreator(Workspace workspace) {
        super(workspace);
    }


    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJAnnotatedClass schemaCreatorImpl = this.workspace.findOJClass(TinkerGenerationUtil.UmlgSchemaCreatorImpl.toJavaString());
        if (schemaCreatorImpl == null) {
            schemaCreatorImpl = new OJAnnotatedClass(TinkerGenerationUtil.UmlgSchemaCreatorImpl.getLast());
            schemaCreatorImpl.addToImplementedInterfaces(TinkerGenerationUtil.UmlgSchemaCreator);
            OJPackage ojPackage = new OJPackage(TinkerGenerationUtil.UmlgAdaptorPackage.toJavaString());
            schemaCreatorImpl.setMyPackage(ojPackage);
            addToSource(schemaCreatorImpl);

            addINSTANCE(schemaCreatorImpl);
            addQualifiedNameVertexSet(schemaCreatorImpl);
            addQualifiedNameEdgeSet(schemaCreatorImpl);
            createVertexAdderMethod(schemaCreatorImpl);
            createEdgeAdderMethod(schemaCreatorImpl);
            OJConstructor constructor = addPrivateConstructor(schemaCreatorImpl);
            constructor.getBody().addToStatements("addAllVertexEntries()");
            constructor.getBody().addToStatements("addAllEdgeEntries()");
            addCreateVertexSchemas(schemaCreatorImpl);
            addCreateEdgeSchemas(schemaCreatorImpl);
            schemaCreatorImpl.addToImports(TinkerGenerationUtil.UMLG_NODE);
            addGetInstance(schemaCreatorImpl);
        }
        addVertexEntries(schemaCreatorImpl, clazz);
        addEdgeEntries(schemaCreatorImpl, clazz);
    }

    private void addGetInstance(OJAnnotatedClass schemaMapImpl) {
        OJAnnotatedOperation getInstance = new OJAnnotatedOperation("getInstance", TinkerGenerationUtil.UmlgSchemaCreator);
        getInstance.setStatic(true);
        getInstance.getBody().addToStatements("return INSTANCE");
        schemaMapImpl.addToOperations(getInstance);
    }

    private void addINSTANCE(OJAnnotatedClass globalMap) {
        OJField instance = new OJField("INSTANCE", globalMap.getPathName());
        instance.setVisibility(OJVisibilityKind.PUBLIC);
        instance.setStatic(true);
        instance.setInitExp("new " + globalMap.getPathName().getLast() + "()");
        globalMap.addToFields(instance);
    }

    private void addQualifiedNameVertexSet(OJAnnotatedClass globalMap) {
        OJField set = new OJField(TinkerGenerationUtil.QualifiedNameVertexSchemaSet,
                new OJPathName("java.util.Set").addToGenerics(new OJPathName("List").addToGenerics("String"))
        );
        set.setVisibility(OJVisibilityKind.PRIVATE);
        set.setInitExp("new HashSet<List<String>>()");
        globalMap.addToImports(new OJPathName("java.util.HashSet"));
        globalMap.addToImports(new OJPathName("java.util.Set"));
        globalMap.addToImports(new OJPathName("java.util.List"));
        globalMap.addToFields(set);
    }

    private void addQualifiedNameEdgeSet(OJAnnotatedClass globalMap) {
        OJField set = new OJField(TinkerGenerationUtil.QualifiedNameEdgeSchemaSet,
                new OJPathName("java.util.Set").addToGenerics("String")
        );
        set.setVisibility(OJVisibilityKind.PRIVATE);
        set.setInitExp("new HashSet<String>()");
        globalMap.addToImports(new OJPathName("java.util.HashSet"));
        globalMap.addToImports(new OJPathName("java.util.Set"));
        globalMap.addToFields(set);
    }

    private OJAnnotatedOperation createVertexAdderMethod(OJAnnotatedClass globalMap) {
        OJAnnotatedOperation addAllEntries = new OJAnnotatedOperation("addAllVertexEntries");
        addAllEntries.setVisibility(OJVisibilityKind.PRIVATE);
        globalMap.addToOperations(addAllEntries);
        return addAllEntries;
    }

    private OJAnnotatedOperation createEdgeAdderMethod(OJAnnotatedClass globalMap) {
        OJAnnotatedOperation addAllEntries = new OJAnnotatedOperation("addAllEdgeEntries");
        addAllEntries.setVisibility(OJVisibilityKind.PRIVATE);
        globalMap.addToOperations(addAllEntries);
        return addAllEntries;
    }

    private OJConstructor addPrivateConstructor(OJAnnotatedClass globalMap) {
        OJConstructor constructor = new OJConstructor();
        constructor.setVisibility(OJVisibilityKind.PRIVATE);
        globalMap.addToConstructors(constructor);
        return constructor;
    }

    private void addCreateVertexSchemas(OJAnnotatedClass globalMap) {
        OJAnnotatedOperation createVertexSchemas = new OJAnnotatedOperation("createVertexSchemas");
        TinkerGenerationUtil.addOverrideAnnotation(createVertexSchemas);
        createVertexSchemas.addParam("vertexSchemaCreator", TinkerGenerationUtil.VertexSchemaCreator);
        OJForStatement ojForStatement = new OJForStatement("hierarchy", new OJPathName("List").addToGenerics("String"), TinkerGenerationUtil.QualifiedNameVertexSchemaSet);
        ojForStatement.getBody().addToStatements("vertexSchemaCreator.create(hierarchy)");
        createVertexSchemas.getBody().addToStatements(ojForStatement);
        globalMap.addToOperations(createVertexSchemas);
    }

    private void addCreateEdgeSchemas(OJAnnotatedClass globalMap) {
        OJAnnotatedOperation createEdgeSchemas = new OJAnnotatedOperation("createEdgeSchemas");
        TinkerGenerationUtil.addOverrideAnnotation(createEdgeSchemas);
        createEdgeSchemas.addParam("edgeSchemaCreator", TinkerGenerationUtil.EdgeSchemaCreator);
        OJForStatement ojForStatement = new OJForStatement("label", new OJPathName("String"), TinkerGenerationUtil.QualifiedNameEdgeSchemaSet);
        ojForStatement.getBody().addToStatements("edgeSchemaCreator.create(label)");
        createEdgeSchemas.getBody().addToStatements(ojForStatement);
        globalMap.addToOperations(createEdgeSchemas);
    }

    private void addVertexEntries(OJAnnotatedClass globalMap, Class clazz) {
        OJAnnotatedOperation addAllEntries = globalMap.findOperation("addAllVertexEntries");
        List<Classifier> generals = TumlClassOperations.getGeneralizationHierarchy(clazz);
        List<String> generalsQualifiedNames = convertClassifierHierarchy(generals);
        if (TumlBehavioredClassifierOperations.hasBehavior(clazz)) {
            generalsQualifiedNames.add(TinkerGenerationUtil.BASE_BEHAVIORED_CLASSIFIER_QUALIFIEDNAME);
        } else {
            generalsQualifiedNames.add(TinkerGenerationUtil.BaseUmlgCompositionNodeQualifiedName);
        }
        Collections.reverse(generalsQualifiedNames);
        addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameVertexSchemaSet + ".add(Arrays.asList(" + convertToCommaSeparatedList(generalsQualifiedNames) + "))");

        //Add in the meta classes
        if (!clazz.isAbstract()) {
            OJAnnotatedClass metaClass = new OJAnnotatedClass(TumlClassOperations.getMetaClassName(clazz));
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".meta");
            metaClass.setMyPackage(ojPackage);
            String qualifiedName = metaClass.getQualifiedName();
            addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameVertexSchemaSet + ".add(Arrays.asList(\"" + qualifiedName + "\"))");
        }

        //Special entry for the root vertex and deletion vertex
        addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameVertexSchemaSet + ".add(Arrays.asList(\"rootVertex\"))");
        addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameVertexSchemaSet + ".add(Arrays.asList(\"deletionVertex\"))");

        globalMap.addToImports(TumlClassOperations.getPathName(clazz));
        globalMap.addToImports("java.util.Arrays");
    }

    private List<String> convertClassifierHierarchy(List<Classifier> generals) {
        List<String> result = new ArrayList<String>();
        for (Classifier c : generals) {
            String qualifiedName = c.getQualifiedName();
            int indexOfModel = qualifiedName.indexOf("::");
            qualifiedName = qualifiedName.substring(indexOfModel + 2);
            result.add(qualifiedName);
        }
        return result;
    }

    private String convertToCommaSeparatedList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String s : list) {
            sb.append("\"");
            sb.append(s);
            sb.append("\"");
            if (++count < list.size()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private void addEdgeEntries(OJAnnotatedClass globalMap, Class clazz) {
        OJAnnotatedOperation addAllEntries = globalMap.findOperation("addAllEdgeEntries");
        Set<Property> properties = TumlClassOperations.getPropertiesThatHaveAndEdge(clazz);
        for (Property p : properties) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            String edgeName;
            if (!(clazz instanceof AssociationClass)) {
                edgeName = TinkerGenerationUtil.getEdgeName(pWrap.getProperty());
            } else {
                edgeName = TinkerGenerationUtil.getEdgeName(pWrap.getProperty()) + "_" + pWrap.getName() + "_AC";
            }
            addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameEdgeSchemaSet + ".add(\"" + edgeName + "\")");
        }

        //Add in the class and meta class's edge to root
        if (!clazz.isAbstract()) {
            addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameEdgeSchemaSet + ".add(\"" + TinkerGenerationUtil.getEdgeToRootLabelStrategyMeta(clazz) + "\")");
            addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameEdgeSchemaSet + ".add(\"" + TinkerGenerationUtil.getEdgeToRootLabelStrategy(clazz) + "\")");
        }

        //Special edges for deletion and all instances node
        addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameEdgeSchemaSet + ".add(\"deletedVertexEdgeToRoot\")");
        addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameEdgeSchemaSet + ".add(\"allinstances\")");

        globalMap.addToImports(TinkerGenerationUtil.UmlgLabelConverterFactoryPathName);
    }

    @Override
    public void visitAfter(Class element) {
    }
}

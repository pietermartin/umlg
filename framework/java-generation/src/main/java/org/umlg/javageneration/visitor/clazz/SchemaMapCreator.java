package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

/**
 * Date: 2013/03/29
 * Time: 9:31 AM
 */
public class SchemaMapCreator extends BaseVisitor implements Visitor<Class> {

    public SchemaMapCreator(Workspace workspace) {
        super(workspace);
    }


    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(org.eclipse.uml2.uml.Class clazz) {
        OJAnnotatedClass schemaMapImpl = this.workspace.findOJClass(TinkerGenerationUtil.TumlSchemaMapImpl.toJavaString());
        if (schemaMapImpl == null) {
            schemaMapImpl = new OJAnnotatedClass(TinkerGenerationUtil.TumlSchemaMapImpl.getLast());
            schemaMapImpl.addToImplementedInterfaces(TinkerGenerationUtil.TumlSchemaMap);
            OJPackage ojPackage = new OJPackage(TinkerGenerationUtil.TumlAdaptorPackage.toJavaString());
            schemaMapImpl.setMyPackage(ojPackage);
            addToSource(schemaMapImpl);

            addINSTANCE(schemaMapImpl);
            addMap(schemaMapImpl);
            createPropertyClassAdderMethod(schemaMapImpl);
            OJConstructor constructor = addPrivateConstructor(schemaMapImpl);
            constructor.getBody().addToStatements("addAllEntries()");
            addGetForQualifiedName(schemaMapImpl);
            schemaMapImpl.addToImports(TinkerGenerationUtil.TUML_NODE);
            addGetInstance(schemaMapImpl);
        }
        addEntries(schemaMapImpl, clazz);
    }

    private void addGetInstance(OJAnnotatedClass schemaMapImpl) {
        OJAnnotatedOperation getInstance = new OJAnnotatedOperation("getInstance", TinkerGenerationUtil.TumlSchemaMap);
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

    private void addMap(OJAnnotatedClass globalMap) {
        OJField map = new OJField(TinkerGenerationUtil.QualifiedNameClassMapName, new OJPathName("java.util.Map").addToGenerics("String").addToGenerics(
                "Class<? extends " + TinkerGenerationUtil.TUML_NODE.getLast() + ">"));
        map.setVisibility(OJVisibilityKind.PRIVATE);
        map.setInitExp("new HashMap<String, Class<? extends " + TinkerGenerationUtil.TUML_NODE.getLast() + ">>()");
        globalMap.addToImports(new OJPathName("java.util.HashMap"));
        globalMap.addToImports(new OJPathName("java.util.Map"));
        globalMap.addToFields(map);
    }

    private OJAnnotatedOperation createPropertyClassAdderMethod(OJAnnotatedClass globalMap) {
        OJAnnotatedOperation addAllEntries = new OJAnnotatedOperation("addAllEntries");
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

    private void addGetForQualifiedName(OJAnnotatedClass globalMap) {
        OJAnnotatedOperation get = new OJAnnotatedOperation("get", new OJPathName("<T extends " + TinkerGenerationUtil.TUML_NODE.getLast() + "> Class<T>"));
        get.addParam("qualifiedName", "String");
        get.getBody().addToStatements("return (Class<T>)this." + TinkerGenerationUtil.QualifiedNameClassMapName + ".get(qualifiedName)");
        globalMap.addToOperations(get);
    }

    private void addEntries(OJAnnotatedClass globalMap, org.eclipse.uml2.uml.Class clazz) {
        OJAnnotatedOperation addAllEntries = globalMap.findOperation("addAllEntries");
        addAllEntries.getBody().addToStatements("this." + TinkerGenerationUtil.QualifiedNameClassMapName + ".put(\"" + clazz.getQualifiedName() +
                "\", " + TumlClassOperations.className(clazz) + ".class)");
        globalMap.addToImports(TumlClassOperations.getPathName(clazz));
    }

    @Override
    public void visitAfter(org.eclipse.uml2.uml.Class element) {
    }
}

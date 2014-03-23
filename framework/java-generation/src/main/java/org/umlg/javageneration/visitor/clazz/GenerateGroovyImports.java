package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;

/**
 * Date: 2014/03/23
 * Time: 8:43 AM
 */
public class GenerateGroovyImports extends BaseVisitor implements Visitor<Class> {

    public GenerateGroovyImports(Workspace workspace) {
        super(workspace);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJAnnotatedClass groovyImporter = this.workspace.findOJClass(UmlgGenerationUtil.UmlgGroovyImporter.toJavaString());
        if (groovyImporter == null) {
            groovyImporter = new OJAnnotatedClass(UmlgGenerationUtil.UmlgGroovyImporter.getLast());
            OJPackage ojPackage = new OJPackage(UmlgGenerationUtil.UmlgAdaptorPackage.toJavaString());
            groovyImporter.setMyPackage(ojPackage);
            addToSource(groovyImporter);
            OJField imports = new OJField(groovyImporter, "imports", new OJPathName("java.util.Set").addToGenerics("String"));
            imports.setInitExp("new HashSet<String>()");
            imports.setStatic(true);

            OJField importStatic = new OJField(groovyImporter, "importStatic", new OJPathName("java.util.Set").addToGenerics("String"));
            importStatic.setInitExp("new HashSet<String>()");
            importStatic.setStatic(true);

            groovyImporter.addToImports("java.util.HashSet");
        }
        addEntry(groovyImporter, clazz);
        addUMLG(groovyImporter);

    }

    private void addUMLG(OJAnnotatedClass groovyImporter) {
        groovyImporter.addToStaticBlock(
                new OJSimpleStatement(
                        "imports.add(\"" + UmlgGenerationUtil.UmlgAdaptorPackage.toJavaString() + ".*\")"
                )
        );
    }

    private void addEntry(OJAnnotatedClass groovyImporter, Class clazz) {
        groovyImporter.addToStaticBlock(
                new OJSimpleStatement(
                        "imports.add(\"" + Namer.name(clazz.getNearestPackage()) + ".*\")"
                )
        );
        groovyImporter.addToStaticBlock(
                new OJSimpleStatement(
                        "importStatic.add(\"" + Namer.name(clazz.getNearestPackage()) + "." + UmlgClassOperations.className(clazz) + "." +
                                UmlgClassOperations.propertyEnumName(clazz) + "\")"
                )
        );

    }

    @Override
    public void visitAfter(Class clazz) {
    }

}

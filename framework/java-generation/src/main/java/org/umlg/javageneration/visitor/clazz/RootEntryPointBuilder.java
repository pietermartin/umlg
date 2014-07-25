package org.umlg.javageneration.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJWhileStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class RootEntryPointBuilder extends BaseVisitor implements Visitor<Class> {

	public RootEntryPointBuilder(Workspace workspace) {
		super(workspace);
	}

	@Override
    @VisitSubclasses({Class.class, AssociationClass.class})
	public void visitBefore(Class clazz) {
		if (!UmlgClassOperations.hasCompositeOwner(clazz) && !clazz.isAbstract()) {
			OJAnnotatedClass root = this.workspace.findOJClass(UmlgGenerationUtil.UmlgRootPackage.toJavaString() + "." + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()));
			addGetterToAppRootForRootEntity(clazz, root);
		}
        if (!clazz.isAbstract()) {
            addGetterToMetaClassForRootEntity(clazz);
        }
	}

    private void addGetterToMetaClassForRootEntity(Class clazz) {
        OJAnnotatedClass annotatedClass = this.workspace.findOJClass(UmlgGenerationUtil.UmlgRootPackage.toJavaString() + "." + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()));
        OJAnnotatedOperation getter = new OJAnnotatedOperation("get" + UmlgClassOperations.getMetaClassPathName(clazz).getLast(),
                UmlgClassOperations.getMetaClassPathName(clazz));
        annotatedClass.addToOperations(getter);
        annotatedClass.addToImports(UmlgGenerationUtil.umlgMemorySequence);
        annotatedClass.addToImports(new OJPathName("java.util.ArrayList"));
        annotatedClass.addToImports(UmlgClassOperations.getMetaClassPathName(clazz));
        getter.getBody().addToStatements("return " + UmlgClassOperations.getMetaClassPathName(clazz) + ".getInstance()");
    }

    private void addGetterToAppRootForRootEntity(Class clazz, OJAnnotatedClass root) {
		OJAnnotatedOperation getter = new OJAnnotatedOperation("get" + UmlgClassOperations.className(clazz),
				UmlgGenerationUtil.umlgSet.getCopy().addToGenerics("? extends " + UmlgClassOperations.getPathName(clazz).getLast()));
		root.addToOperations(getter);
		getter.getBody().addToStatements("return " + UmlgClassOperations.getPathName(clazz) + ".allInstances()");
        root.addToImports(UmlgClassOperations.getPathName(clazz));
		root.addToImports(UmlgGenerationUtil.tinkerDirection);
        root.addToImports(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

}

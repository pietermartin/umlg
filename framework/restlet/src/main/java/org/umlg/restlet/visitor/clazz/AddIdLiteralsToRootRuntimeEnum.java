package org.umlg.restlet.visitor.clazz;

import java.util.Collections;

import org.eclipse.uml2.uml.Model;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.validation.Validation;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.javageneration.visitor.clazz.RuntimePropertyImplementor;

public class AddIdLiteralsToRootRuntimeEnum extends BaseVisitor implements Visitor<Model> {

    public AddIdLiteralsToRootRuntimeEnum(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Model model) {
        OJAnnotatedClass annotatedClass = this.workspace.findOJClass("org.umlg.root.Root");
        OJEnum ojEnum = annotatedClass.findEnum("RootRuntimePropertyEnum");
        addField(ojEnum, "id");
    }

    @Override
    public void visitAfter(Model element) {
    }

    private void addField(OJEnum ojEnum, String fieldName) {
        OJAnnotatedOperation fromLabel = ojEnum.findOperation("fromLabel", new OJPathName("String"));
        OJAnnotatedOperation fromQualifiedName = ojEnum.findOperation("fromQualifiedName", new OJPathName("String"));
        OJAnnotatedOperation fromInverseQualifiedName = ojEnum.findOperation("fromInverseQualifiedName", new OJPathName("String"));
        RuntimePropertyImplementor
                .addEnumLiteral(false, false, null, null, false, ojEnum, fromLabel, fromQualifiedName, fromInverseQualifiedName, fieldName, "not_applicable", "inverseO::not_applicable", "inverseO::not_applicable", true, true, null,
                        Collections.<Validation>emptyList(), false, false, false, false, false, false, false, false, false, 1, 1, 1, false, false, false, false, false,
                        true, "");
    }

}

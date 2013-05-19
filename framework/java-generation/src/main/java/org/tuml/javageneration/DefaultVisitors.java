package org.tuml.javageneration;

import java.util.ArrayList;
import java.util.List;

import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.visitor.clazz.*;
import org.tuml.javageneration.visitor.enumeration.EnumerationVisitor;
import org.tuml.javageneration.visitor.enumeration.TofromJsonForEnumCreator;
import org.tuml.javageneration.visitor.interfaze.InterfaceVisitor;
import org.tuml.javageneration.visitor.model.MetaNodeCreator;
import org.tuml.javageneration.visitor.model.RootEntryPointCreator;
import org.tuml.javageneration.visitor.operation.OperationImplementorSimple;
import org.tuml.javageneration.visitor.property.*;

public class DefaultVisitors {

    private static final String META_SOURCE_FOLDER = "src/main/generated-java-meta";

    public static List<Visitor<?>> getDefaultJavaVisitors() {
        List<Visitor<?>> result = new ArrayList<Visitor<?>>();
        result.add(new InterfaceVisitor(Workspace.INSTANCE));
        result.add(new ClassCreator(Workspace.INSTANCE));
        result.add(new ClassBuilder(Workspace.INSTANCE));
        result.add(new RootEntryPointCreator(Workspace.INSTANCE));
        result.add(new RootEntryPointBuilder(Workspace.INSTANCE));
        result.add(new QualifiedNameClassNameMapBuilder(Workspace.INSTANCE));
        result.add(new SchemaMapCreator(Workspace.INSTANCE));
        result.add(new ClassRuntimePropertyImplementorVisitor(Workspace.INSTANCE));
        result.add(new InterfaceRuntimePropertyImplementorVisitor(Workspace.INSTANCE));
        result.add(new EnumerationVisitor(Workspace.INSTANCE));
        result.add(new CompositionVisitor(Workspace.INSTANCE));
//		result.add(new ComponentProperyVisitor(Workspace.INSTANCE));
        result.add(new PropertyVisitor(Workspace.INSTANCE));
        result.add(new ManyPropertyVisitor(Workspace.INSTANCE));
        result.add(new OnePropertyVisitor(Workspace.INSTANCE));
        result.add(new ClassImplementedInterfacePropertyVisitor(Workspace.INSTANCE));
        result.add(new DerivedPropertyVisitor(Workspace.INSTANCE));
        result.add(new QualifierValidator(Workspace.INSTANCE));
        result.add(new QualifierVisitor(Workspace.INSTANCE));
        result.add(new OperationImplementorSimple(Workspace.INSTANCE));
        result.add(new ToFromJsonCreator(Workspace.INSTANCE));
        result.add(new TofromJsonForEnumCreator(Workspace.INSTANCE));
        result.add(new TmpIdAdder(Workspace.INSTANCE));
//        result.add(new LookupGenerator(Workspace.INSTANCE));
        result.add(new LookupGenerator2(Workspace.INSTANCE));
        result.add(new ClassInterfacePropertyLookupGenerator(Workspace.INSTANCE));
        result.add(new PropertyValidatorBuilder(Workspace.INSTANCE));
        result.add(new PropertyConstraintBuilder(Workspace.INSTANCE));

        result.add(new ClassValidateMultiplicitiesBuilder(Workspace.INSTANCE));
        result.add(new ClassCheckConstraintsBuilder(Workspace.INSTANCE));
        result.add(new MetaClassBuilder(Workspace.INSTANCE, META_SOURCE_FOLDER));
        result.add(new MetaNodeCreator(Workspace.INSTANCE, META_SOURCE_FOLDER));
        return result;

//		if (this.audit) {
//			ModelVisitor.visitModel(this.model, new ClassAuditTransformation(this));
//			ModelVisitor.visitModel(this.model, new AuditClassCreator(this));
//			ModelVisitor.visitModel(this.model, new AuditInterfaceCreator(this));
//			ModelVisitor.visitModel(this.model, new AuditPropertyVisitor(this));
//		}
//		ModelVisitor.visitModel(this.model, new SingleServerResourceBuilder(this));
    }

}

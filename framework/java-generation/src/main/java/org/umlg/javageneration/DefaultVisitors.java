package org.umlg.javageneration;

import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.visitor._package.PackageVisitor;
import org.umlg.javageneration.visitor.clazz.*;
import org.umlg.javageneration.visitor.enumeration.EnumerationVisitor;
import org.umlg.javageneration.visitor.enumeration.TofromJsonForEnumCreator;
import org.umlg.javageneration.visitor.interfaze.InterfaceVisitor;
import org.umlg.javageneration.visitor.model.IndexCreator;
import org.umlg.javageneration.visitor.model.MetaNodeCreator;
import org.umlg.javageneration.visitor.model.RootEntryPointCreatorForModel;
import org.umlg.javageneration.visitor.operation.OperationImplementorSimple;
import org.umlg.javageneration.visitor.property.*;

import java.util.ArrayList;
import java.util.List;

public class DefaultVisitors {

    public static List<Visitor<?>> getDefaultJavaVisitors() {
        List<Visitor<?>> result = new ArrayList<Visitor<?>>();
        result.add(new QualifierValidator(Workspace.INSTANCE));
        result.add(new IndexValidator(Workspace.INSTANCE));
        result.add(new InterfaceVisitor(Workspace.INSTANCE));
        result.add(new ClassCreator(Workspace.INSTANCE));
        result.add(new ClassBuilder(Workspace.INSTANCE));
        result.add(new RootEntryPointCreatorForModel(Workspace.INSTANCE));
        result.add(new RootEntryPointBuilder(Workspace.INSTANCE));
        result.add(new QualifiedNameClassNameMapBuilder(Workspace.INSTANCE));
        result.add(new QualifiedNameClassMapCreator(Workspace.INSTANCE));
        result.add(new SchemaCreator(Workspace.INSTANCE));
        result.add(new InterfaceRuntimePropertyImplementorVisitor(Workspace.INSTANCE));
        result.add(new EnumerationVisitor(Workspace.INSTANCE));
        result.add(new ClassRuntimePropertyImplementorVisitor(Workspace.INSTANCE));
        result.add(new CompositionVisitor(Workspace.INSTANCE));
//		result.add(new ComponentProperyVisitor(Workspace.INSTANCE));
        result.add(new PropertyVisitor(Workspace.INSTANCE));
        result.add(new ManyPropertyVisitor(Workspace.INSTANCE));
        result.add(new OnePropertyVisitor(Workspace.INSTANCE));
        result.add(new ClassImplementedInterfacePropertyVisitor(Workspace.INSTANCE));
        result.add(new DerivedPropertyVisitor(Workspace.INSTANCE));
        result.add(new RedefinitionPropertyVisitor(Workspace.INSTANCE));
        result.add(new DerivedUnionPropertyVisitor(Workspace.INSTANCE));
        result.add(new QualifierVisitor(Workspace.INSTANCE));
        result.add(new OperationImplementorSimple(Workspace.INSTANCE));
        result.add(new ToFromJsonCreator(Workspace.INSTANCE));
        result.add(new TofromJsonForEnumCreator(Workspace.INSTANCE));
        result.add(new TmpIdAdder(Workspace.INSTANCE));
//        result.add(new LookupGenerator(Workspace.INSTANCE));
        result.add(new LookupGenerator(Workspace.INSTANCE));
        result.add(new ClassInterfacePropertyLookupGenerator(Workspace.INSTANCE));
        result.add(new PropertyValidatorBuilder(Workspace.INSTANCE));
        result.add(new PropertyConstraintBuilder(Workspace.INSTANCE));

        result.add(new ClassValidateMultiplicitiesBuilder(Workspace.INSTANCE));
        result.add(new ClassCheckConstraintsBuilder(Workspace.INSTANCE));
        result.add(new MetaClassBuilder(Workspace.INSTANCE, Workspace.META_SOURCE_FOLDER));
        result.add(new MetaInterfaceBuilder(Workspace.INSTANCE, Workspace.META_SOURCE_FOLDER));
        result.add(new MetaNodeCreator(Workspace.INSTANCE, Workspace.META_SOURCE_FOLDER));
        result.add(new IndexCreator(Workspace.INSTANCE));
        result.add(new IndexSetValidator(Workspace.INSTANCE));
        result.add(new PropertyIndexFinderCreator(Workspace.INSTANCE));

        result.add(new PackageVisitor(Workspace.INSTANCE));
//        result.add(new GremlinGroovyModelVisitor(Workspace.INSTANCE, Workspace.GROOVY_SOURCE_FOLDER));
        result.add( new GenerateGroovyImports(Workspace.INSTANCE));

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

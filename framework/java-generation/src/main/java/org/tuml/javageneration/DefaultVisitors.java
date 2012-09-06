package org.tuml.javageneration;

import java.util.ArrayList;
import java.util.List;

import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.visitor.clazz.ClassBuilder;
import org.tuml.javageneration.visitor.clazz.ClassCreator;
import org.tuml.javageneration.visitor.clazz.ClassImplementedInterfacePropertyVisitor;
import org.tuml.javageneration.visitor.clazz.ClassInterfacePropertyLookupGenerator;
import org.tuml.javageneration.visitor.clazz.ClassRuntimePropertyImplementorVisitor;
import org.tuml.javageneration.visitor.clazz.CompositionVisitor;
import org.tuml.javageneration.visitor.clazz.InterfaceRuntimePropertyImplementorVisitor;
import org.tuml.javageneration.visitor.clazz.RootEntryPointBuilder;
import org.tuml.javageneration.visitor.clazz.ToFromJsonCreator;
import org.tuml.javageneration.visitor.enumeration.EnumerationVisitor;
import org.tuml.javageneration.visitor.enumeration.TofromJsonForEnumCreator;
import org.tuml.javageneration.visitor.interfaze.InterfaceVisitor;
import org.tuml.javageneration.visitor.model.RootEntryPointCreator;
import org.tuml.javageneration.visitor.operation.OperationImplementorSimple;
import org.tuml.javageneration.visitor.property.ComponentProperyVisitor;
import org.tuml.javageneration.visitor.property.DerivedPropertyVisitor;
import org.tuml.javageneration.visitor.property.LookupGenerator;
import org.tuml.javageneration.visitor.property.ManyPropertyVisitor;
import org.tuml.javageneration.visitor.property.LookupGenerator;
import org.tuml.javageneration.visitor.property.OnePropertyVisitor;
import org.tuml.javageneration.visitor.property.PropertyVisitor;
import org.tuml.javageneration.visitor.property.QualifierValidator;
import org.tuml.javageneration.visitor.property.QualifierVisitor;

public class DefaultVisitors {

	public static List<Visitor<?>> getDefaultJavaVisitors() {
		List<Visitor<?>> result = new ArrayList<Visitor<?>>();
		result.add(new InterfaceVisitor(Workspace.INSTANCE));
		result.add(new ClassCreator(Workspace.INSTANCE));
		result.add(new ClassBuilder(Workspace.INSTANCE));
		result.add(new RootEntryPointCreator(Workspace.INSTANCE));
		result.add(new RootEntryPointBuilder(Workspace.INSTANCE));
		result.add(new ClassRuntimePropertyImplementorVisitor(Workspace.INSTANCE));
		result.add(new InterfaceRuntimePropertyImplementorVisitor(Workspace.INSTANCE));
		result.add(new EnumerationVisitor(Workspace.INSTANCE));
		result.add(new CompositionVisitor(Workspace.INSTANCE));
		result.add(new ComponentProperyVisitor(Workspace.INSTANCE));
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
		result.add(new LookupGenerator(Workspace.INSTANCE));
<<<<<<< HEAD
		result.add(new ClassInterfacePropertyLookupGenerator(Workspace.INSTANCE));
=======
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
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

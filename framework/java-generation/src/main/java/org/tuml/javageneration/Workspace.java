package org.tuml.javageneration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.Model;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.JavaModelPrinter;
import org.tuml.framework.ModelVisitor;
import org.tuml.javageneration.ocl.visitor.DerivedPropertyVisitor;
import org.tuml.javageneration.visitor.clazz.ClassCreator;
import org.tuml.javageneration.visitor.clazz.ClassImplementedInterfacePropertyVisitor;
import org.tuml.javageneration.visitor.clazz.ClassRuntimePropertyImplementorVisitor;
import org.tuml.javageneration.visitor.clazz.ClassVisitor;
import org.tuml.javageneration.visitor.clazz.CompositionVisitor;
import org.tuml.javageneration.visitor.enumeration.EnumerationVisitor;
import org.tuml.javageneration.visitor.interfaze.InterfaceVisitor;
import org.tuml.javageneration.visitor.property.CompositionProperyVisitor;
import org.tuml.javageneration.visitor.property.ManyPropertyVisitor;
import org.tuml.javageneration.visitor.property.OnePropertyVisitor;
import org.tuml.javageneration.visitor.property.PropertyVisitor;
import org.tuml.javageneration.visitor.property.QualifierVisitor;

public class Workspace {

	private final static Map<String, OJAnnotatedClass> javaClassMap = new HashMap<String, OJAnnotatedClass>(); 
	
	public static void addToClassMap(OJAnnotatedClass ojClass) {
		javaClassMap.put(ojClass.getQualifiedName(), ojClass);
	}

	public static void toText(File project) {
		for (Map.Entry<String, OJAnnotatedClass> entry : javaClassMap.entrySet()) {
			JavaModelPrinter.addToSource(entry.getKey(), entry.getValue().toJavaString());
		}
		JavaModelPrinter.toText(project);
	}

	public static OJAnnotatedClass findOJClass(String name) {
		return javaClassMap.get(name);
	}
	
	public static void visitModel(Model model) {
		ModelVisitor.visitModel(model, new InterfaceVisitor());
		ModelVisitor.visitModel(model, new ClassCreator());
		ModelVisitor.visitModel(model, new ClassVisitor());
		ModelVisitor.visitModel(model, new ClassRuntimePropertyImplementorVisitor());
		ModelVisitor.visitModel(model, new EnumerationVisitor());
		ModelVisitor.visitModel(model, new CompositionVisitor());
		ModelVisitor.visitModel(model, new CompositionProperyVisitor());
		ModelVisitor.visitModel(model, new PropertyVisitor());
		ModelVisitor.visitModel(model, new ManyPropertyVisitor());
		ModelVisitor.visitModel(model, new OnePropertyVisitor());
		ModelVisitor.visitModel(model, new ClassImplementedInterfacePropertyVisitor());
		ModelVisitor.visitModel(model, new DerivedPropertyVisitor());
		ModelVisitor.visitModel(model, new QualifierVisitor());
	}

	
}

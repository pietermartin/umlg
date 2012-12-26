package org.tuml.javageneration.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.property.LookupGenerator;

public class ClassInterfacePropertyLookupGenerator extends BaseVisitor implements Visitor<Class> {

	public ClassInterfacePropertyLookupGenerator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		addLookupPropertiesFromInterfaces(annotatedClass, clazz);
	}

	@Override
	public void visitAfter(Class element) {
	}

	private void addLookupPropertiesFromInterfaces(OJAnnotatedClass annotatedClass, Class clazz) {
		Set<Property> properties = TumlClassOperations.getPropertiesOnRealizedInterfaces(clazz);
		for (Property p : properties) {
			PropertyWrapper propertyWrapper = new PropertyWrapper(p);
			if (!propertyWrapper.isComposite() && !(propertyWrapper.getType() instanceof Enumeration) && !propertyWrapper.isDerived()
					&& !propertyWrapper.isQualifier() && propertyWrapper.getOtherEnd() != null && !(propertyWrapper.getOtherEnd().getType() instanceof Enumeration)
					&& !propertyWrapper.getOtherEnd().isComposite()) {

				Type compositeParent = LookupGenerator.findCompositeParent(propertyWrapper);
				LookupGenerator.generateLookupForNonCompositeProperty(compositeParent, findOJClass(clazz), findOJClass(propertyWrapper.getOwningType()), new PropertyWrapper(propertyWrapper.getOtherEnd()), propertyWrapper);
			}
		}
	}
	

}

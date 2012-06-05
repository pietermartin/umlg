package org.tuml.javageneration.visitor.clazz;

import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedInterface;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.property.ManyPropertyVisitor;
import org.tuml.javageneration.visitor.property.OnePropertyVisitor;
import org.tuml.javageneration.visitor.property.PropertyWrapper;

public class ClassImplementedInterfacePropertyVisitor extends BaseVisitor implements Visitor<Class> {

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		addImplementedInterfaces(annotatedClass, clazz);
		addPropertiesFromInterfaces(annotatedClass, clazz);
	}

	@Override
	public void visitAfter(Class element) {
	}

	private void addImplementedInterfaces(OJAnnotatedClass annotatedClass, Class clazz) {
		List<Interface> interfaces = clazz.getImplementedInterfaces();
		for (Interface interface1 : interfaces) {
			OJAnnotatedInterface ojInterface = (OJAnnotatedInterface) findOJClass(interface1);
			annotatedClass.addToImplementedInterfaces(ojInterface.getPathName());
		}
	}

	private void addPropertiesFromInterfaces(OJAnnotatedClass owner, Class clazz) {
		Set<Property> properties = TumlClassOperations.getPropertiesOnRealizedInterfaces(clazz);
		for (Property p : properties) {
			PropertyWrapper propertyWrapper = new PropertyWrapper(p);
			if (!propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
				buildField(owner, propertyWrapper);
				buildRemover(owner, propertyWrapper);
				buildClearer(owner, propertyWrapper);

				if (propertyWrapper.isMany()) {
					ManyPropertyVisitor.buildGetter(owner, propertyWrapper);
					ManyPropertyVisitor.buildManyAdder(owner, propertyWrapper);
					ManyPropertyVisitor.buildSetter(owner, propertyWrapper);
				} else {
					OnePropertyVisitor.buildGetter(owner, propertyWrapper);
					OnePropertyVisitor.buildOneAdder(owner, propertyWrapper);
					OnePropertyVisitor.buildSetter(owner, propertyWrapper);
				}
			}
		}
	}

}

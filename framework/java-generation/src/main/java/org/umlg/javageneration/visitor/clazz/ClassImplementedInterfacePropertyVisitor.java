package org.umlg.javageneration.visitor.clazz;

import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.javageneration.visitor.property.ManyPropertyVisitor;
import org.umlg.javageneration.visitor.property.OnePropertyVisitor;
import org.umlg.javageneration.visitor.property.PropertyConstraintBuilder;
import org.umlg.javageneration.visitor.property.PropertyValidatorBuilder;

public class ClassImplementedInterfacePropertyVisitor extends BaseVisitor implements Visitor<Class> {

	public ClassImplementedInterfacePropertyVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
    @VisitSubclasses({Class.class, AssociationClass.class})
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

	//TODO move this property visitor and find concrete class via interface dependency supplier association
	private void addPropertiesFromInterfaces(OJAnnotatedClass owner, Class clazz) {
		Set<Property> properties = UmlgClassOperations.getPropertiesOnRealizedInterfaces(clazz);
		for (Property p : properties) {
			PropertyWrapper propertyWrapper = new PropertyWrapper(p);
			if (!propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
				buildField(owner, propertyWrapper);
				buildRemover(owner, propertyWrapper);
				buildClearer(owner, propertyWrapper);

				if (propertyWrapper.isMany()) {
					ManyPropertyVisitor.buildGetter(owner, propertyWrapper);
					ManyPropertyVisitor.buildManyAdder(owner, propertyWrapper, false);
					ManyPropertyVisitor.buildSetter(owner, propertyWrapper);
				} else {
					OnePropertyVisitor.buildGetter(owner, propertyWrapper);
					OnePropertyVisitor.buildOneAdder(owner, propertyWrapper, false);
					OnePropertyVisitor.buildSetter(owner, propertyWrapper);
					PropertyValidatorBuilder.buildValidator(owner, propertyWrapper);
                    PropertyConstraintBuilder.buildCheckConstaint(owner, propertyWrapper);
				}
			}
		}
	}

}

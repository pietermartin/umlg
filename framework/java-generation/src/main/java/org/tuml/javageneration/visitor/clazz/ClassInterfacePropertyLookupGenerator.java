package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

import java.util.Set;

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
            if (propertyWrapper.needsLookup()) {

                OJAnnotatedOperation lookupOnParent = new OJAnnotatedOperation(propertyWrapper.lookup());
                String pathName = TumlClassOperations.getPathName(propertyWrapper.getType()).getLast();
                lookupOnParent.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(pathName));

                OJField result = new OJField("result", TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(pathName));
                result.setInitExp("new " + TinkerGenerationUtil.tumlMemorySet.getCopy().addToGenerics(TumlClassOperations.getPathName(propertyWrapper.getType()).getLast()).getLast() + "()");
                lookupOnParent.getBody().addToLocals(result);
                Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteRealization((Interface) propertyWrapper.getType());
                for (Classifier c : concreteImplementations) {
                    annotatedClass.addToImports(TumlClassOperations.getPathName(c));
                    lookupOnParent.getBody().addToStatements("result.addAll(" + TumlClassOperations.getMetaClassName(c) + ".getInstance().getAllInstances())");
                    annotatedClass.addToImports(TumlClassOperations.getMetaClassPathName(c));
                }
                lookupOnParent.getBody().addToStatements("return result");
                annotatedClass.addToOperations(lookupOnParent);
            }
        }
    }


}

package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Property;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

/**
 * Date: 2013/03/09
 * Time: 11:40 AM
 */
public class LookupGenerator2 extends BaseVisitor implements Visitor<Property> {

    public LookupGenerator2(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper propertyWrapper = new PropertyWrapper(p);
        if (propertyWrapper.needsLookup()) {
            OJAnnotatedClass ojClass = findOJClass(propertyWrapper);
            OJAnnotatedOperation lookupOnParent = new OJAnnotatedOperation(propertyWrapper.lookup());

            String pathName = "? extends " + TumlClassOperations.getPathName(propertyWrapper.getType()).getLast();
            lookupOnParent.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(pathName));

            lookupOnParent.getBody().addToStatements("return " + TumlClassOperations.getPathName(propertyWrapper.getType()).getLast() + ".allInstances()");
            ojClass.addToOperations(lookupOnParent);
        }
    }

    @Override
    public void visitAfter(Property p) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

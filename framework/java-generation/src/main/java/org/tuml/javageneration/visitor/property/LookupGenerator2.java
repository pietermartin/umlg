package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Property;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.javageneration.util.*;
import org.tuml.javageneration.visitor.BaseVisitor;

import java.util.List;

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
            OJAnnotatedOperation lookUp = new OJAnnotatedOperation(propertyWrapper.lookup());

            OJBlock ojBlock1 = new OJBlock();
            lookUp.getBody().addToStatements(ojBlock1);
            OJBlock ojBlock2 = new OJBlock();
            lookUp.getBody().addToStatements(ojBlock2);

            lookUp.setReturnType("Set<" + propertyWrapper.javaBaseTypePath().getLast() + ">");
            OJField result = new OJField("result", "Set<" + propertyWrapper.javaBaseTypePath().getLast() + ">");
            result.setInitExp("new HashSet<" + propertyWrapper.javaBaseTypePath().getLast() + ">()");
            ojClass.addToImports("java.util.HashSet");
            ojClass.addToImports("java.util.Set");
            ojBlock1.addToLocals(result);

            if (propertyWrapper.isOneToOne()) {

                PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());
                ojBlock1.addToStatements("Filter<"+propertyWrapper.javaBaseTypePath().getLast()+"> filter = new Filter<"+propertyWrapper.javaBaseTypePath().getLast()+">() {\n    @Override\n    public boolean filter("+propertyWrapper.javaBaseTypePath().getLast()+" entity){\n        return entity."+otherEnd.getter()+"() == null;\n    }\n}");

                ojBlock1.addToStatements("result.addAll(" + TumlClassOperations.getPathName(propertyWrapper.getType()).getLast() + ".allInstances(filter))");

            } else {
                ojBlock1.addToStatements("result.addAll(" + TumlClassOperations.getPathName(propertyWrapper.getType()).getLast() + ".allInstances())");
            }


            List<Constraint> constraints = TumlPropertyOperations.getConstraints(propertyWrapper.getProperty());
            if (!constraints.isEmpty()) {
                //Filter out constrained element
                OJField iter = new OJField("iter", "java.util.Iterator<" + propertyWrapper.javaBaseTypePath().getLast() + ">");
                ojClass.addToImports("java.util.Iterator");
                iter.setInitExp("result.iterator()");
                ojBlock2.addToLocals(iter);

                OJWhileStatement ojWhileStatement = new OJWhileStatement();
                ojWhileStatement.setCondition("iter.hasNext()");
                OJField next = new OJField(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
                next.setInitExp("iter.next()");
                ojWhileStatement.getBody().addToLocals(next);
                OJTryStatement tryTumlConstraintException = new OJTryStatement();
                tryTumlConstraintException.getTryPart().addToStatements(propertyWrapper.adder() + "(" + propertyWrapper.fieldname() + ")");
                tryTumlConstraintException.setCatchParam(new OJParameter("e", TinkerGenerationUtil.TumlConstraintViolationException));
                tryTumlConstraintException.getCatchPart().addToStatements("iter.remove()");
                ojWhileStatement.getBody().addToStatements(tryTumlConstraintException);
                ojWhileStatement.getBody().addToStatements(propertyWrapper.remover() + "(" + propertyWrapper.fieldname() + ")");
                ojBlock2.addToStatements(ojWhileStatement);
            }

            ojBlock2.addToStatements("return result");
            ojClass.addToOperations(lookUp);
        }
    }

    @Override
    public void visitAfter(Property p) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

package org.umlg.javageneration.visitor.property;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJBlock;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJWhileStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgPropertyOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.List;

/**
 * Date: 2013/03/09
 * Time: 11:40 AM
 */
public class LookupGenerator extends BaseVisitor implements Visitor<Property> {

    public LookupGenerator(Workspace workspace) {
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

            lookUp.setReturnType(UmlgGenerationUtil.umlgSet.getLast() + "<" + propertyWrapper.javaBaseTypePath().getLast() + ">");
            OJField result = new OJField("result", UmlgGenerationUtil.umlgSet.getLast() + "<" + propertyWrapper.javaBaseTypePath().getLast() + ">");
            result.setInitExp("new " + UmlgGenerationUtil.umlgMemorySet.getLast() + "<" + propertyWrapper.javaBaseTypePath().getLast() + ">()");
            ojClass.addToImports("java.util.HashSet");
            ojClass.addToImports("java.util.Set");
            ojBlock1.addToLocals(result);

            if (propertyWrapper.isUnique()) {

                PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());

                if (propertyWrapper.isOneToOne() || otherEnd.isOne()) {
                    ojBlock1.addToStatements("Filter<" + propertyWrapper.javaBaseTypePath().getLast() + "> filter = new Filter<" +
                            propertyWrapper.javaBaseTypePath().getLast() + ">() {\n    @Override\n    public boolean filter(" +
                            propertyWrapper.javaBaseTypePath().getLast() + " entity){\n        return entity." +
                            otherEnd.getter() + "() == null;\n    }\n}");
                } else {
                    ojBlock1.addToStatements("Filter<" + propertyWrapper.javaBaseTypePath().getLast() + "> filter = new Filter<" +
                            propertyWrapper.javaBaseTypePath().getLast() + ">() {\n    @Override\n    public boolean filter(" +
                            propertyWrapper.javaBaseTypePath().getLast() + " entity){\n        return !entity." +
                            otherEnd.getter() + "().contains(" + otherEnd.javaBaseTypePath().getLast() + ".this);\n    }\n}");
                }

                ojBlock1.addToStatements("result.addAll(" + UmlgClassOperations.getPathName(propertyWrapper.getType()) + ".allInstances(filter))");

            } else {
                ojBlock1.addToStatements("result.addAll(" + UmlgClassOperations.getPathName(propertyWrapper.getType()) + ".allInstances())");
            }

            ojClass.addToImports(UmlgClassOperations.getPathName(propertyWrapper.getType()));

            List<Constraint> constraints = UmlgPropertyOperations.getConstraints(propertyWrapper.getProperty());
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

                ojWhileStatement.getBody().addToStatements(propertyWrapper.adder() + "(" + propertyWrapper.fieldname() + ")");

                for (Constraint c : constraints) {
                    ojWhileStatement.getBody().addToStatements("List<UmlgConstraintViolation> violation" + c.getName() + " = " + UmlgClassOperations.checkClassConstraintName(c) + "()");
                    OJIfStatement ifConstraintFails = new OJIfStatement();
                    ifConstraintFails.setCondition("!violation" + c.getName() + ".isEmpty()");
                    ifConstraintFails.getThenPart().addToStatements("iter.remove()");
                    ojWhileStatement.getBody().addToStatements(ifConstraintFails);
                }

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

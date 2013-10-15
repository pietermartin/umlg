package org.umlg.javageneration.visitor.property;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.*;
import org.umlg.framework.VisitFilter;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.ocl.TumlOcl2Java;
import org.umlg.javageneration.util.*;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.ocl.UmlgOcl2Parser;

import java.util.List;

/**
 * Date: 2013/03/09
 * Time: 1:41 PM
 */
public class PropertyConstraintBuilder extends BaseVisitor implements Visitor<Property> {

    public PropertyConstraintBuilder(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper propertyWrapper = new PropertyWrapper(p);
        OJAnnotatedClass owner = findOJClass(p);
        buildCheckConstaint(owner, propertyWrapper);
    }

    @Override
    public void visitAfter(Property element) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void buildCheckConstaint(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        List<Constraint> constraints = TumlPropertyOperations.getConstraints(propertyWrapper.getProperty());
        for (Constraint constraint : constraints) {
            ConstraintWrapper contraintWrapper = new ConstraintWrapper(constraint);
            OJAnnotatedOperation checkConstraint = new OJAnnotatedOperation(propertyWrapper.checkConstraint(constraint));
            checkConstraint.setReturnType(new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
            owner.addToOperations(checkConstraint);
            OJField result = new OJField("result", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
            result.setInitExp("new ArrayList<" + TinkerGenerationUtil.TumlConstraintViolation.getLast() + ">()");
            owner.addToImports(new OJPathName("java.util.ArrayList"));

            OJIfStatement ifConstraintFails = new OJIfStatement();
            String ocl = contraintWrapper.getConstraintOclAsString();
            checkConstraint.setComment(String.format("Implements the ocl statement for constraint '%s'\n<pre>\n%s\n</pre>", contraintWrapper.getName(), ocl));
            OCLExpression<Classifier> oclExp = UmlgOcl2Parser.INSTANCE.parseOcl(ocl);

            ifConstraintFails.setCondition("(" + TumlOcl2Java.oclToJava(owner, oclExp) + ") == false");
            ifConstraintFails.addToThenPart("result.add(new " + TinkerGenerationUtil.TumlConstraintViolation.getLast() + "(\"" + contraintWrapper.getName() + "\", \""
                    + propertyWrapper.getQualifiedName() + "\", \"ocl\\n" + ocl.replace("\n", "\\n") + "\\nfails!\"))");

            checkConstraint.getBody().addToStatements(ifConstraintFails);
            checkConstraint.getBody().addToLocals(result);
            checkConstraint.getBody().addToStatements("return result");

        }
    }

}

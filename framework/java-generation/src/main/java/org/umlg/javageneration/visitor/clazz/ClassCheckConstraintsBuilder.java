package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.List;

/**
 * Date: 2013/03/10
 * Time: 1:55 PM
 */
public class ClassCheckConstraintsBuilder extends BaseVisitor implements Visitor<Class> {

    public ClassCheckConstraintsBuilder(Workspace workspace) {
        super(workspace);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        addCheckConstraints(annotatedClass, clazz);

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void visitAfter(Class element) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void addCheckConstraints(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation checkConstraints = new OJAnnotatedOperation("checkClassConstraints", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
        TinkerGenerationUtil.addOverrideAnnotation(checkConstraints);
        OJField result = new OJField("result", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
        result.setInitExp("new ArrayList<" + TinkerGenerationUtil.TumlConstraintViolation.getLast() + ">()");
        checkConstraints.getBody().addToLocals(result);
        List<Constraint> constraints = ModelLoader.INSTANCE.getConstraints(clazz);
        for (Constraint constraint : constraints) {
            checkConstraints.getBody().addToStatements("result.addAll(" + TumlClassOperations.checkClassConstraintName(constraint) + "())");
        }
        checkConstraints.getBody().addToStatements("return result");
        annotatedClass.addToOperations(checkConstraints);
    }

}

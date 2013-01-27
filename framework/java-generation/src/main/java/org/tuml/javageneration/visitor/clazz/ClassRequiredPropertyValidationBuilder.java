package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJIfStatement;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ClassRequiredPropertyValidationBuilder extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Class> {

    public ClassRequiredPropertyValidationBuilder(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJAnnotatedOperation validateMultiplicities = new OJAnnotatedOperation("validateMultiplicities", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
        annotatedClass.addToOperations(validateMultiplicities);
        OJField result = new OJField("result", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
        result.setInitExp("new ArrayList<" + TinkerGenerationUtil.TumlConstraintViolation.getLast() + ">()");
        annotatedClass.addToImports(new OJPathName("java.util.ArrayList"));
        validateMultiplicities.getBody().addToLocals(result);
        for (Property p : TumlClassOperations.getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            //Qualified properties raw multiplicity is assumed to be *
            if (!pWrap.isDerived() && !pWrap.isQualified() && (pWrap.getLower() > 0 || (pWrap.isMany() && pWrap.getUpper() != -1))) {
                if (pWrap.getLower() > 0) {
                    OJIfStatement ifNotNull;
                    if (pWrap.isMany()) {
                        ifNotNull = new OJIfStatement(pWrap.getter() + "().size() < " + pWrap.getLower());
                    } else {
                        ifNotNull = new OJIfStatement(pWrap.getter() + "() == null");
                    }
                    ifNotNull.addToThenPart("result.add(new " + TinkerGenerationUtil.TumlConstraintViolation.getLast() + "(\"multiplicity\", \"" + pWrap.getQualifiedName() + "\", \"lower multiplicity is " + pWrap.getLower() + "\"))");
                    validateMultiplicities.getBody().addToStatements(ifNotNull);
                }
                if (pWrap.getUpper() != -1) {
                    if (pWrap.isMany()) {
                        OJIfStatement ifNotNull = new OJIfStatement(pWrap.getter() + "().size() > " + pWrap.getUpper());
                        ifNotNull.addToThenPart("result.add(new " + TinkerGenerationUtil.TumlConstraintViolation.getLast() + "(\"multiplicity\", \"" + pWrap.getQualifiedName() + "\", \"upper multiplicity is " + pWrap.getUpper() + "\"))");
                        validateMultiplicities.getBody().addToStatements(ifNotNull);
                    }
                }
            }
        }
        validateMultiplicities.getBody().addToStatements("return result");
    }

    @Override
    public void visitAfter(Class element) {

    }

}

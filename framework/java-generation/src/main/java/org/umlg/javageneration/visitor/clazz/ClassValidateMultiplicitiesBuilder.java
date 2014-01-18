package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class ClassValidateMultiplicitiesBuilder extends BaseVisitor implements Visitor<Class> {

    public ClassValidateMultiplicitiesBuilder(Workspace workspace) {
        super(workspace);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJAnnotatedOperation validateMultiplicities = new OJAnnotatedOperation("validateMultiplicities", new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UmlgConstraintViolation));
        UmlgGenerationUtil.addOverrideAnnotation(validateMultiplicities);
        annotatedClass.addToOperations(validateMultiplicities);
        OJField result = new OJField("result", new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UmlgConstraintViolation));
        result.setInitExp("new ArrayList<" + UmlgGenerationUtil.UmlgConstraintViolation.getLast() + ">()");
        annotatedClass.addToImports(new OJPathName("java.util.ArrayList"));
        validateMultiplicities.getBody().addToLocals(result);
        for (Property p : UmlgClassOperations.getAllOwnedProperties(clazz)) {
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
                    ifNotNull.addToThenPart("result.add(new " + UmlgGenerationUtil.UmlgConstraintViolation.getLast() + "(\"multiplicity\", \"" + pWrap.getQualifiedName() + "\", \"lower multiplicity is " + pWrap.getLower() + "\"))");
                    validateMultiplicities.getBody().addToStatements(ifNotNull);
                }
                if (pWrap.getUpper() != -1) {
                    if (pWrap.isMany()) {
                        OJIfStatement ifNotNull = new OJIfStatement(pWrap.getter() + "().size() > " + pWrap.getUpper());
                        ifNotNull.addToThenPart("result.add(new " + UmlgGenerationUtil.UmlgConstraintViolation.getLast() + "(\"multiplicity\", \"" + pWrap.getQualifiedName() + "\", \"upper multiplicity is " + pWrap.getUpper() + "\"))");
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

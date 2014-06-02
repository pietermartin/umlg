package org.umlg.javageneration.visitor.property;

import org.eclipse.uml2.uml.*;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;

/**
 * Date: 2014/03/18
 * Time: 5:00 PM
 */
public class IndexSetValidator extends BaseVisitor implements Visitor<Property> {

    private Stereotype stereotype;

    public IndexSetValidator(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property element) {
        //Can not get find the stereotype in the constructor as the model is not loaded yet.
        if (this.stereotype == null) {
            this.stereotype = ModelLoader.INSTANCE.findStereotype(UmlgGenerationUtil.Index);
        }
        PropertyWrapper pWrap = new PropertyWrapper(element);
        if (element.isStereotypeApplied(this.stereotype) && (pWrap.isMany() || !(element.getType() instanceof DataType))) {
            throw new IllegalStateException(String.format("Only PrimitiveType may be indexed currently! Current element %s is a %s", new String[]{element.getQualifiedName(), element.getType().getQualifiedName()}));
        }
        if (element.isStereotypeApplied(this.stereotype) && pWrap.isOne() && (element.getType() instanceof PrimitiveType)) {
            EnumerationLiteral enumerationLiteral = (EnumerationLiteral) element.getValue(stereotype, "type");
            if (enumerationLiteral.getName().equals(UmlgGenerationUtil.Index_UNIQUE)) {
                OJAnnotatedClass owner = findOJClass(element);
                OJAnnotatedOperation setter = owner.findOperation(pWrap.setter(), pWrap.javaBaseTypePath());
                OJIfStatement ifIndexNotNull = new OJIfStatement();
                ifIndexNotNull.setCondition(
                        pWrap.fieldname() + " != null && !" + pWrap.fieldname() + ".equals("+pWrap.getter()+"()) && " +
                        UmlgGenerationUtil.UMLGAccess + "." + UmlgGenerationUtil.getFromUniqueIndex + "(" +
                        UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() +
                        ".getUmlgLabelConverter().convert(\"" +
                        pWrap.getQualifiedName() + "\"), " +
                        pWrap.fieldname() +
                        ") != null"
                );
                ifIndexNotNull.addToThenPart("throw new IllegalStateException(\"Unique indexed property " + pWrap.getQualifiedName() + " already has a value of '\" + " + pWrap.fieldname() + " + \"'\")");
                setter.getBody().addToStatements(0, ifIndexNotNull);
                owner.addToImports(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName);
            }
        }

    }

    @Override
    public void visitAfter(Property element) {

    }

}

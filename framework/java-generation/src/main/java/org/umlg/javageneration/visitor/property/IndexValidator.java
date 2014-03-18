package org.umlg.javageneration.visitor.property;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.visitor.BaseVisitor;

/**
 * Date: 2014/03/18
 * Time: 8:12 AM
 */
public class IndexValidator extends BaseVisitor implements Visitor<Property> {

    private Stereotype stereotype;

    public IndexValidator(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property element) {
        //Can not get find the stereotype in the constructor as the model is not loaded yet.
        if (this.stereotype == null) {
            this.stereotype = ModelLoader.INSTANCE.findStereotype("Index");
        }
        PropertyWrapper pWrap = new PropertyWrapper(element);
        if (element.isStereotypeApplied(this.stereotype) && (pWrap.isMany() || !(element.getType() instanceof PrimitiveType))) {
            throw new IllegalStateException(String.format("Only PrimitiveType may be indexed currently! Current element %s is a %s", new String[]{element.getQualifiedName(), element.getType().getQualifiedName()}));
        }
    }

    @Override
    public void visitAfter(Property element) {

    }
}

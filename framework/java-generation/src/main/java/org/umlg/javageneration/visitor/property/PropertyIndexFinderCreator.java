package org.umlg.javageneration.visitor.property;

import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJParameter;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.logging.Logger;

/**
 * Date: 2014/04/08
 * Time: 7:20 PM
 */
public class PropertyIndexFinderCreator extends BaseVisitor implements Visitor<Property> {

    private static Logger logger = Logger.getLogger(PropertyVisitor.class.getPackage().getName());

    public PropertyIndexFinderCreator(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper propertyWrapper = new PropertyWrapper(p);
        if (propertyWrapper.isIndexed()) {
            OJAnnotatedClass owner = findOJClass(p);
            createFinder(owner, propertyWrapper);
        }
    }

    @Override
    public void visitAfter(Property element) {
    }

    private void createFinder(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedOperation finder = new OJAnnotatedOperation(propertyWrapper.finder());
        finder.setStatic(true);
        OJParameter finderParam = new OJParameter(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
        finder.addToParameters(finderParam);
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype("Index");
        EnumerationLiteral enumerationLiteral = (EnumerationLiteral) propertyWrapper.getValue(stereotype, "type");
        if (enumerationLiteral.getName().equals(UmlgGenerationUtil.Index_UNIQUE)) {
            finder.setReturnType(owner.getPathName());
            finder.getBody().addToStatements(
                    "return " +
                    UmlgGenerationUtil.UMLGAccess + "." + UmlgGenerationUtil.getFromUniqueIndex + "(" +
                    owner.getPathName().getLast() + ".class.getSimpleName(), " +
                    UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() +
                    ".getUmlgLabelConverter().convert(\"" +
                    propertyWrapper.getPersistentName() + "\"), " +
                    buildFormatter(propertyWrapper)
            );

        } else if (enumerationLiteral.getName().equals(UmlgGenerationUtil.Index_NON_UNIQUE)) {
            finder.setReturnType(new OJPathName("java.util.List").addToGenerics(owner.getPathName()));
            finder.getBody().addToStatements(
                    "return " +
                            UmlgGenerationUtil.UMLGAccess + "." + UmlgGenerationUtil.getFromNonUniqueIndex + "(" +
                            owner.getPathName().getLast() + ".class.getSimpleName(), " +
                            UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() +
                            ".getUmlgLabelConverter().convert(\"" +
                            propertyWrapper.getPersistentName() + "\"), " +
                            buildFormatter(propertyWrapper)
                    );
        } else {
            throw new IllegalStateException("Unknown Index literal " + enumerationLiteral.getName());
        }
        owner.addToOperations(finder);
    }

    private String buildFormatter(PropertyWrapper propertyWrapper) {
        if (propertyWrapper.isEnumeration()) {
            return propertyWrapper.fieldname() + ".name())";
        } else if (propertyWrapper.isDataType() && !propertyWrapper.isPrimitive()) {
            return UmlgGenerationUtil.umlgFormatter.getLast() + ".format(" +
                    propertyWrapper.getDataTypeEnum().getInitExpression() +
                    ", " +
                    propertyWrapper.fieldname() +
                    "))";
        } else {
            return propertyWrapper.fieldname() + ")";
        }
    }

}

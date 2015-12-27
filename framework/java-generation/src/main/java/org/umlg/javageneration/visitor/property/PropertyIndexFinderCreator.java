package org.umlg.javageneration.visitor.property;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJParameter;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.Set;
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

            //Create a static finder in subclass.
            //Every class returns the union of its subclasses
            Type owningType = propertyWrapper.getOwningType();
            Set<Classifier> specializations = UmlgClassOperations.getSpecializations((Classifier) owningType);
            specializations.add((Classifier) owningType);

            for (Classifier c : specializations) {
                createFinder(c, propertyWrapper);
            }

        }
    }

    @Override
    public void visitAfter(Property element) {
    }

    private void createFinder(Classifier c, PropertyWrapper propertyWrapper) {
        OJAnnotatedClass owner = findOJClass(c);
        OJAnnotatedOperation finder = new OJAnnotatedOperation(StringUtils.uncapitalize(c.getName()) + "_" + propertyWrapper.finder());
        finder.setStatic(true);
        OJParameter finderParam = new OJParameter(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
        finder.addToParameters(finderParam);
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype("Index");
        EnumerationLiteral enumerationLiteral = (EnumerationLiteral) propertyWrapper.getValue(stereotype, "type");

//        if (enumerationLiteral.getName().equals(UmlgGenerationUtil.Index_UNIQUE)) {
//            OJField result = new OJField(finder.getBody(), "result", owner.getPathName());
////            result.setInitExp("null");
//            finder.getBody().addToLocals(result);
//        } else {
//            OJField result = new OJField(finder.getBody(), "result", new OJPathName("java.util.List").addToGenerics(owner.getPathName()));
//            result.setInitExp("null");
//            finder.getBody().addToLocals(result);
//        }
        Set<Classifier> concreteClassifiers = UmlgClassOperations.getConcreteImplementations(c);
        boolean first = true;
        for (Classifier classifier : concreteClassifiers) {

            StringBuilder sb = new StringBuilder();
            if (enumerationLiteral.getName().equals(UmlgGenerationUtil.Index_UNIQUE)) {
                finder.setReturnType(owner.getPathName());
                if (first) {
                    sb.append(owner.getPathName().getLast());
                    sb.append(" ");
                }
                first = false;
                sb.append("result =  " +
                        UmlgGenerationUtil.UMLGAccess + "." + UmlgGenerationUtil.getFromUniqueIndex + "(" +
                        "\"" + classifier.getName() + "\", \"" +
                        propertyWrapper.getPersistentName() + "\", " +
                        buildFormatter(propertyWrapper));
                finder.getBody().addToStatements(sb.toString());

                OJIfStatement ojIfStatement = new OJIfStatement();
                ojIfStatement.setCondition("result != null");
                ojIfStatement.addToThenPart("return result");
                finder.getBody().addToStatements(ojIfStatement);

            } else if (enumerationLiteral.getName().equals(UmlgGenerationUtil.Index_NON_UNIQUE)) {
                finder.setReturnType(new OJPathName("java.util.List").addToGenerics(owner.getPathName()));
                if (first) {
                    sb.append(new OJPathName("java.util.List").addToGenerics(owner.getPathName()).getLast());
                    sb.append(" result = ");
                } else {
                    sb.append("result.addAll(");
                }
                sb.append(UmlgGenerationUtil.UMLGAccess + "." + UmlgGenerationUtil.getFromNonUniqueIndex + "(" +
                        "\"" + classifier.getName() + "\", " +
                        UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() +
                        ".getUmlgLabelConverter().convert(\"" +
                        propertyWrapper.getPersistentName() + "\"), " +
                        buildFormatter(propertyWrapper));

                if (!first) {
                    sb.append(")");
                }
                first = false;
                finder.getBody().addToStatements(sb.toString());

            } else {
                throw new IllegalStateException("Unknown Index literal " + enumerationLiteral.getName());
            }
        }
        owner.addToImports(UmlgGenerationUtil.umlgFormatter);
        finder.getBody().addToStatements("return result");
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

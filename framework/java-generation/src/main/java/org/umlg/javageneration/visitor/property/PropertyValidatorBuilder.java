package org.umlg.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJParameter;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgValidationEnum;
import org.umlg.javageneration.visitor.BaseVisitor;

public class PropertyValidatorBuilder extends BaseVisitor implements Visitor<Property> {

	public PropertyValidatorBuilder(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (propertyWrapper.isDataType() && !propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
			OJAnnotatedClass owner = findOJClass(p);
			buildValidator(owner, propertyWrapper);
		}
	}

	@Override
	public void visitAfter(Property element) {

	}

	public static void buildValidator(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation validateProperty = new OJAnnotatedOperation(propertyWrapper.validator());
		validateProperty.setReturnType(new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UmlgConstraintViolation));
		validateProperty.addToParameters(new OJParameter(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath()));
		owner.addToOperations(validateProperty);
		OJField result = new OJField("result", new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UmlgConstraintViolation));
		result.setInitExp("new ArrayList<" + UmlgGenerationUtil.UmlgConstraintViolation.getLast() + ">()");
		owner.addToImports(new OJPathName("java.util.ArrayList"));
		validateProperty.getBody().addToLocals(result);

        //Validate UnlimitedNatural bigger or equal to zero
        if (propertyWrapper.isUnlimitedNatural()) {
            OJIfStatement ifSmallerThanZero = new OJIfStatement("!" + UmlgGenerationUtil.UmlgValidator.getLast() + ".validateValidUnlimitedNatural"  + "("
                    + propertyWrapper.fieldname() + ")");
            ifSmallerThanZero.addToThenPart("result.add(new " + UmlgGenerationUtil.UmlgConstraintViolation.getLast() + "(\"" + "UnlimitedNatural" + "\", \""
                    + propertyWrapper.getQualifiedName() + "\", \" must be greater or equal to zero!\"))");
            validateProperty.getBody().addToStatements(ifSmallerThanZero);
            owner.addToImports(UmlgGenerationUtil.UmlgValidator);
        }

		for (UmlgValidationEnum e : UmlgValidationEnum.values()) {
			if (propertyWrapper.hasValidation(e)) {
				OJIfStatement ifValidate;
				if (e.getAttributes().length > 0) {
					ifValidate = new OJIfStatement("!" + UmlgGenerationUtil.UmlgValidator.getLast() + "."
                            + e.getMethodName() + "("
							+ propertyWrapper.fieldname() + ", "
                            + propertyWrapper.getValidation(e).toStringForMethod() + ")");
				} else {
					ifValidate = new OJIfStatement("!" + UmlgGenerationUtil.UmlgValidator.getLast() + "."
                            + e.getMethodName() + "("
							+ propertyWrapper.fieldname()
                            + propertyWrapper.getValidation(e).toStringForMethod() + ")");
				}
				ifValidate.addToThenPart("result.add(new " + UmlgGenerationUtil.UmlgConstraintViolation.getLast() + "(\"" + e.name() + "\", \""
						+ propertyWrapper.getQualifiedName() + "\", \"" + e.name() + " does not pass validation!\"))");
				validateProperty.getBody().addToStatements(ifValidate);
				owner.addToImports(UmlgGenerationUtil.UmlgValidator);
				validateProperty.getBody().addToStatements(ifValidate);
			}
		}

		validateProperty.getBody().addToStatements("return result");
	}
}

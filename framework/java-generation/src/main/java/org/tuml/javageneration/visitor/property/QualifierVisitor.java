package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.visitor.BaseVisitor;

public class QualifierVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper pWrap = new PropertyWrapper(p);
		if (pWrap.isQualifier()) {
			validateHasCorrespondingDerivedProperty(pWrap);
		} else {
			if (pWrap.hasQualifiers()) {
				generateQualifierGetter(findOJClass(pWrap), pWrap);
			}
		}
	}

	@Override
	public void visitAfter(Property p) {
	}

	private void validateHasCorrespondingDerivedProperty(PropertyWrapper pWrap) {
		if (!pWrap.qualifierHasCorrespondingDerivedProperty()) {
			throw new IllegalStateException(String.format("Qualified %s does not have a corresponding derived property on %s",
					new Object[] { pWrap.getName(), pWrap.getQualifierContext() }));
		}
	}

	private void generateQualifierGetter(OJAnnotatedClass ojClass, PropertyWrapper pWrap) {
		OJAnnotatedOperation qualifierGetter = new OJAnnotatedOperation(pWrap.getQualifiedGetterName());
		qualifierGetter.addParam("context", pWrap.getQualifierContextPathName());
		ojClass.addToOperations(qualifierGetter);
		OJField result = new OJField();
		result.setName("result");
		result.setType(new OJPathName("java.util.List"));
		result.getType().addToElementTypes(TinkerGenerationUtil.TINKER_QUALIFIER_PATHNAME);
		result.setInitExp("new ArrayList<" + TinkerGenerationUtil.TINKER_QUALIFIER_PATHNAME.getLast() + ">()");
		ojClass.addToImports("java.util.ArrayList");
		qualifierGetter.setReturnType(result.getType());
		qualifierGetter.getBody().addToLocals(result);
		for (Property p : pWrap.getQualifiers()) {
			PropertyWrapper qWrap = new PropertyWrapper(p);
			StringBuilder sb = new StringBuilder();
			sb.append("result.add(");
			sb.append("new ");
			sb.append(TinkerGenerationUtil.TINKER_QUALIFIER_PATHNAME.getLast());
			sb.append("(\"");
			sb.append(qWrap.getName());
			sb.append("\", ");
			sb.append("context.");
			sb.append(qWrap.getter());
			sb.append("(), ");
			sb.append(TinkerGenerationUtil.calculateMultiplcity(qWrap));
			sb.append("))");
			qualifierGetter.getBody().addToStatements(sb.toString());	
		}
		qualifierGetter.getBody().addToStatements("return result");
		ojClass.addToImports(TinkerGenerationUtil.TINKER_QUALIFIER_PATHNAME);
		ojClass.addToImports(TinkerGenerationUtil.tinkerMultiplicityPathName);
	}

}

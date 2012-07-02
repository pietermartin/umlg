package org.tuml.javageneration.visitor.property;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJSimpleStatement;
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
			generateQualifiedGetter(pWrap);
		} else {
			if (pWrap.hasQualifiers()) {
				//TODO findOJClass does not work here as the pWrap is on an association.
				//Need to get the side where the type is different to pWrap's type
				generateQualifierGetter(findOJClass(pWrap), pWrap);
			}
		}
	}

	@Override
	public void visitAfter(Property p) {
	}

	private void validateHasCorrespondingDerivedProperty(PropertyWrapper pWrap) {
		if (!pWrap.qualifierHasCorrespondingDerivedProperty()) {
			throw new IllegalStateException(String.format("Qualified %s on %s does not have a corresponding derived property on %s",
					new Object[] { pWrap.getName(), pWrap.getOwner(), pWrap.getQualifierContext().getName() }));
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

	private void generateQualifiedGetter(PropertyWrapper qualifier) {
		Property ownerElement = (Property) qualifier.getOwner();
		PropertyWrapper ownerElementPWrap = new PropertyWrapper(ownerElement);
		Type qualifiedClassifier = ownerElementPWrap.getOwningType();
		OJAnnotatedClass ojClass = findOJClass(qualifiedClassifier);

		OJAnnotatedOperation qualifierValue = new OJAnnotatedOperation(ownerElementPWrap.getter() + "For" + StringUtils.capitalize(qualifier.fieldname()));
		if (qualifier.isOne()) {
			qualifierValue.setReturnType(ownerElementPWrap.javaBaseTypePath());
		} else {
			// This needs to only return a Set for now, not sorting the result by index as yet
			qualifierValue.setReturnType(ownerElementPWrap.javaTypePath());
		}
		qualifierValue.addParam(qualifier.fieldname(), (qualifier.isOne() ? qualifier.javaBaseTypePath() : qualifier.javaTypePath()));

		ojClass.addToImports(TinkerGenerationUtil.tinkerIndexPathName);
		ojClass.addToImports(TinkerGenerationUtil.tinkerCloseableIterablePathName);
		ojClass.addToImports(TinkerGenerationUtil.tinkerDirection);
		ojClass.addToImports(TinkerGenerationUtil.edgePathName);
		qualifierValue.getBody().addToStatements(
				"Index<Edge> index = GraphDb.getDb().getIndex(getUid() + \":::\" + \"" + TinkerGenerationUtil.getEdgeName(ownerElementPWrap) + "\", Edge.class)");
		OJIfStatement ifIndexNull = new OJIfStatement("index==null", "return null");
		ifIndexNull.addToElsePart(TinkerGenerationUtil.tinkerCloseableIterablePathName.getCopy().addToGenerics(TinkerGenerationUtil.edgePathName).getLast()
				+ " closeableIterable = index.get(\"" + qualifier.fieldname() + "\", " + qualifier.fieldname() + "==null?\"___NULL___\":" + qualifier.fieldname() + ")");
		OJIfStatement ifHasNext = new OJIfStatement("closeableIterable.iterator().hasNext()");
		if (qualifier.isOne()) {
			ifHasNext.addToThenPart("return new " + ownerElementPWrap.javaBaseTypePath().getLast() + "(closeableIterable.iterator().next().getVertex("
					+ TinkerGenerationUtil.tinkerDirection.getLast() + ".IN))");
			ifHasNext.addToElsePart("return null");
		} else {
			OJSimpleStatement ojSimpleStatement = new OJSimpleStatement("return new " + (ownerElementPWrap.isUnique()?TinkerGenerationUtil.tumlTinkerSetClosableIterableImpl.getCopy().addToGenerics(ownerElementPWrap.javaBaseTypePath()).getLast():TinkerGenerationUtil.tumlTinkerSequenceClosableIterableImpl.getCopy().addToGenerics(ownerElementPWrap.javaBaseTypePath()).getLast()) + "(closeableIterable)");
			if (ownerElementPWrap.isUnique()) {
				ojClass.addToImports(TinkerGenerationUtil.tumlTinkerSetClosableIterableImpl);
			} else {
				ojClass.addToImports(TinkerGenerationUtil.tumlTinkerSequenceClosableIterableImpl);
			}
			ifHasNext.addToThenPart(ojSimpleStatement);
			ifHasNext.addToElsePart("return " + ownerElementPWrap.emptyCollection());
			ojClass.addToImports(new OJPathName("java.util.Collections"));
		}

		ifIndexNull.addToElsePart(ifHasNext);
		qualifierValue.getBody().addToStatements(ifIndexNull);
		ojClass.addToOperations(qualifierValue);
	}
}

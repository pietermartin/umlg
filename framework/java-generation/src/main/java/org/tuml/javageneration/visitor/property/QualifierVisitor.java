package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.visitor.BaseVisitor;

public class QualifierVisitor extends BaseVisitor implements Visitor<Property> {

	public QualifierVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper pWrap = new PropertyWrapper(p);
		if (pWrap.isQualifier()) {
			validateHasCorrespondingDerivedProperty(pWrap);
			generateQualifiedGetter(pWrap);
		} else {
			if (pWrap.hasQualifiers()) {
				generateQualifierGetter(findOJClass(pWrap), pWrap);
			}
		}
	}

	@Override
	public void visitAfter(Property p) {
	}

	private void validateHasCorrespondingDerivedProperty(PropertyWrapper qualifier) {
		if (!qualifier.haveQualifierCorrespondingDerivedProperty()) {
			throw new IllegalStateException(String.format("Qualifier %s on %s does not have a corresponding derived property on %s",
					new Object[] { qualifier.getName(), qualifier.getOwner(), qualifier.getQualifierContext().getName() }));
		}
	}

	private void generateQualifierGetter(OJAnnotatedClass ojClass, PropertyWrapper qualified) {
		OJAnnotatedOperation qualifierGetter = new OJAnnotatedOperation(qualified.getQualifiedGetterName());
		qualifierGetter.addParam("context", qualified.getQualifierContextPathName());
		ojClass.addToOperations(qualifierGetter);
		OJField result = new OJField();
		result.setName("result");
		result.setType(new OJPathName("java.util.List"));
		result.getType().addToElementTypes(TinkerGenerationUtil.TINKER_QUALIFIER_PATHNAME);
		result.setInitExp("new ArrayList<" + TinkerGenerationUtil.TINKER_QUALIFIER_PATHNAME.getLast() + ">()");
		ojClass.addToImports("java.util.ArrayList");
		qualifierGetter.setReturnType(result.getType());
		qualifierGetter.getBody().addToLocals(result);
		for (Property p : qualified.getQualifiers()) {
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

//		OJAnnotatedOperation qualifierValue = new OJAnnotatedOperation(ownerElementPWrap.getter() + "For" + StringUtils.capitalize(qualifier.fieldname()));
		OJAnnotatedOperation qualifierValue = new OJAnnotatedOperation(ownerElementPWrap.getQualifiedNameFor(qualifier));
		if (qualifier.isOne()) {
			qualifierValue.setReturnType(ownerElementPWrap.javaBaseTypePath());
		} else {
			// This needs to only return a Set or Bag for now, not sorting the result
			// by index as yet
			qualifierValue.setReturnType(ownerElementPWrap.javaTypePath());
		}
		qualifierValue.addParam(qualifier.fieldname(), qualifier.javaBaseTypePath());

		ojClass.addToImports(TinkerGenerationUtil.tinkerIndexPathName);
		ojClass.addToImports(TinkerGenerationUtil.tinkerCloseableIterablePathName);
		ojClass.addToImports(TinkerGenerationUtil.tinkerDirection);
		ojClass.addToImports(TinkerGenerationUtil.edgePathName);
		qualifierValue.getBody().addToStatements(
				"Index<Edge> index = GraphDb.getDb().getIndex(getUid() + \":::\" + " + ownerElementPWrap.getTumlRuntimePropertyEnum() + ".getLabel(), Edge.class)");
		OJIfStatement ifIndexNull = new OJIfStatement("index==null", "return null");
		ifIndexNull.addToElsePart(TinkerGenerationUtil.tinkerCloseableIterablePathName.getCopy().addToGenerics(TinkerGenerationUtil.edgePathName).getLast()
				+ " closeableIterable = index.get(\"" + qualifier.fieldname() + "\", " + qualifier.fieldname() + "==null?\"___NULL___\":" + qualifier.fieldname() + ")");
		ifIndexNull.addToElsePart("Iterator<Edge> iterator = closeableIterable.iterator()");
		ojClass.addToImports("java.util.Iterator");
		OJIfStatement ifHasNext = new OJIfStatement("iterator.hasNext()");
		if (qualifier.isOne()) {
			ifHasNext.addToThenPart("return new " + ownerElementPWrap.javaBaseTypePath().getLast() + "(iterator.next().getVertex("
					+ TinkerGenerationUtil.tinkerDirection.getLast() + ".IN))");
			ifHasNext.addToElsePart("return null");
		} else {
			OJSimpleStatement ojSimpleStatement;
			if (ownerElementPWrap.isUnique()) {
				ojSimpleStatement = new OJSimpleStatement("return new " + TinkerGenerationUtil.tumlTinkerSetClosableIterableImpl.getCopy().addToGenerics(ownerElementPWrap.javaBaseTypePath())
						.getLast());
			} else {
				ojSimpleStatement = new OJSimpleStatement("return new " + TinkerGenerationUtil.tumlTinkerSequenceClosableIterableImpl.getCopy().addToGenerics(ownerElementPWrap.javaBaseTypePath()).getLast());
			}
			ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + "(iterator, " + ownerElementPWrap.getTumlRuntimePropertyEnum() + ")");
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

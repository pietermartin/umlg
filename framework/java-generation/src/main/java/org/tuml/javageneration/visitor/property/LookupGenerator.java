package org.tuml.javageneration.visitor.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlInterfaceOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class LookupGenerator extends BaseVisitor implements Visitor<Property> {

	public LookupGenerator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (!propertyWrapper.isComposite() && !(propertyWrapper.getType() instanceof Enumeration) && !propertyWrapper.isDerived()
				&& !propertyWrapper.isQualifier() && propertyWrapper.getOtherEnd() != null && !(propertyWrapper.getOtherEnd().getType() instanceof Enumeration)
				&& !propertyWrapper.getOtherEnd().isComposite()) {
			Type compositeParent = findCompositeParent(propertyWrapper);
			generateLookupForOneProperty(compositeParent, propertyWrapper, new PropertyWrapper(propertyWrapper.getOtherEnd()));
		}
	}

	@Override
	public void visitAfter(Property p) {
	}

	private void generateLookupForOneProperty(Type compositeParent, PropertyWrapper propertyWrapper, PropertyWrapper otherEndPropertyWrapper) {
		OJAnnotatedOperation lookUp = new OJAnnotatedOperation("lookup" + StringUtils.capitalize(propertyWrapper.getName()));
		lookUp.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(TumlClassOperations.getPathName(propertyWrapper.getType())));
		OJClass ojClass = findOJClass(propertyWrapper);
		ojClass.addToOperations(lookUp);
		buildGetterToCompositeParent(ojClass, lookUp, propertyWrapper, otherEndPropertyWrapper, compositeParent);
	}

	private void buildGetterToCompositeParent(OJClass ojClass, OJAnnotatedOperation lookup, PropertyWrapper propertyWrapper, PropertyWrapper otherEndPropertyWrapper,
			Type compositeParent) {
		StringBuilder sb = new StringBuilder();
		sb.append("return ");
		// build getter for special case where the non composite lookup of the
		// same type as the is in the composite tree
		if (propertyWrapper.getType().equals(compositeParent)) {
			constructGetterAsString(true, sb, otherEndPropertyWrapper, compositeParent);
		} else if (otherEndPropertyWrapper.getType().equals(compositeParent)) {
			List<String> getterStringsInReverse = new ArrayList<String>();
			constructLookupAsString(ojClass, getterStringsInReverse, propertyWrapper, otherEndPropertyWrapper, compositeParent);
			Collections.reverse(getterStringsInReverse);
			for (String string : getterStringsInReverse) {
				sb.append(string);
			}
		} else {
			constructGetterAsString(true, sb, propertyWrapper, compositeParent);
			sb.append(".");
			List<String> getterStringsInReverse = new ArrayList<String>();
			constructLookupAsString(ojClass, getterStringsInReverse, propertyWrapper, otherEndPropertyWrapper, compositeParent);
			Collections.reverse(getterStringsInReverse);
			for (String string : getterStringsInReverse) {
				sb.append(string);
			}
		}
		sb.append(".asSet()");
		lookup.getBody().addToStatements(sb.toString());
	}

	private void constructLookupAsString(OJClass ojClass, List<String> getterStringsInReverse, PropertyWrapper propertyWrapper, PropertyWrapper otherEndPropertyWrapper, Type compositeParent) {
		// This needs to be done in reverse;
		if (!propertyWrapper.getType().equals(compositeParent)) {
			PropertyWrapper otherEndToComposite;
			if (propertyWrapper.getType() instanceof Interface) {
				Interface type = (Interface) propertyWrapper.getType();
				otherEndToComposite = new PropertyWrapper(TumlInterfaceOperations.getOtherEndToComposite(type));
			} else if (propertyWrapper.getType() instanceof Class) {
				Class type = (Class) propertyWrapper.getType();
				otherEndToComposite = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(type));
			} else {
				throw new RuntimeException("TODO " + propertyWrapper.getType());
			}
			if (!propertyWrapper.getType().equals(otherEndToComposite.getOtherEnd().getType())) {
				getterStringsInReverse.add(new PropertyWrapper(otherEndToComposite.getOtherEnd()).getter() + "().collect(new BodyExpressionEvaluator<"
						+ propertyWrapper.getType().getName() + ", " + otherEndToComposite.getOtherEnd().getType().getName() + ">() {@Override	public "
						+ propertyWrapper.getType().getName() + " evaluate(" + otherEndToComposite.getOtherEnd().getType().getName() + " e) {	if (e instanceof "
						+ propertyWrapper.getType().getName() + ") { return (" + propertyWrapper.getType().getName() + ")e; } return null;	}})");
				ojClass.addToImports(TinkerGenerationUtil.BodyExpressionEvaluator);
			} else {
				getterStringsInReverse.add(new PropertyWrapper(otherEndToComposite.getOtherEnd()).getter() + "()");
			}
			constructLookupAsString(ojClass, getterStringsInReverse, otherEndToComposite, propertyWrapper, compositeParent);
		}
	}

	private void constructGetterAsString(boolean first, StringBuilder sb, PropertyWrapper propertyWrapper, Type compositeParent) {
		if (!propertyWrapper.getType().equals(compositeParent)) {
			PropertyWrapper otherEndToComposite;
			if (propertyWrapper.getType() instanceof Interface) {
				Interface type = (Interface) propertyWrapper.getType();
				otherEndToComposite = new PropertyWrapper(TumlInterfaceOperations.getOtherEndToComposite(type));
			} else if (propertyWrapper.getType() instanceof Class) {
				Class type = (Class) propertyWrapper.getType();
				otherEndToComposite = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(type));
			} else {
				throw new RuntimeException("TODO " + propertyWrapper.getType());
			}
			if (!first) {
				first = false;
				sb.append(".");
			}
			sb.append(otherEndToComposite.getter());
			sb.append("()");
			constructGetterAsString(false, sb, otherEndToComposite, compositeParent);
		}

	}

	private Type findCompositeParent(PropertyWrapper propertyWrapper) {
		PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());
		// Need to find the composite parent of the 2 properties
		return findCompositeParent(propertyWrapper, otherEnd);
	}

	private Type findCompositeParent(PropertyWrapper propertyWrapper, PropertyWrapper otherEnd) {
		List<Type> orderedListOfCompositeTypes = new ArrayList<Type>();
		createListOfOrderedTypes(orderedListOfCompositeTypes, propertyWrapper);
		List<Type> otherEndOrderedListOfCompositeTypes = new ArrayList<Type>();
		createListOfOrderedTypes(otherEndOrderedListOfCompositeTypes, otherEnd);

		for (Type type : orderedListOfCompositeTypes) {
			if (otherEndOrderedListOfCompositeTypes.contains(type)) {
				return type;
			}
		}
		return null;
	}

	private void createListOfOrderedTypes(List<Type> orderedListOfCompositeTypes, PropertyWrapper propertyWrapper) {
		orderedListOfCompositeTypes.add(propertyWrapper.getType());
		Property otherEndToComposite;
		if (propertyWrapper.getType() instanceof Interface) {
			Interface type = (Interface) propertyWrapper.getType();
			otherEndToComposite = TumlInterfaceOperations.getOtherEndToComposite(type);
		} else if (propertyWrapper.getType() instanceof Class) {
			Class type = (Class) propertyWrapper.getType();
			otherEndToComposite = TumlClassOperations.getOtherEndToComposite(type);
		} else {
			throw new RuntimeException("TODO " + propertyWrapper.getType());
		}
		if (otherEndToComposite != null) {
			createListOfOrderedTypes(orderedListOfCompositeTypes, new PropertyWrapper(otherEndToComposite));
		}
	}

}

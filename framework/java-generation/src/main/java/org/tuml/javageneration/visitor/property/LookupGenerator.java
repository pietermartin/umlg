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
<<<<<<< HEAD
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
=======
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
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
<<<<<<< HEAD
		if (!propertyWrapper.isComposite() && !(propertyWrapper.getType() instanceof Enumeration) && !propertyWrapper.isDerived() && !propertyWrapper.isQualifier()
				&& propertyWrapper.getOtherEnd() != null && !(propertyWrapper.getOtherEnd().getType() instanceof Enumeration) && !propertyWrapper.getOtherEnd().isComposite()) {
			Type compositeParent = findCompositeParent(propertyWrapper);
			OJAnnotatedClass ojClass = findOJClass(propertyWrapper);
			generateLookupForOneProperty(compositeParent, ojClass, new PropertyWrapper(propertyWrapper.getOtherEnd()), propertyWrapper);
=======
		if (!propertyWrapper.isComposite() && !(propertyWrapper.getType() instanceof Enumeration) && !propertyWrapper.isDerived()
				&& !propertyWrapper.isQualifier() && propertyWrapper.getOtherEnd() != null && !(propertyWrapper.getOtherEnd().getType() instanceof Enumeration)
				&& !propertyWrapper.getOtherEnd().isComposite()) {
			Type compositeParent = findCompositeParent(propertyWrapper);
			generateLookupForOneProperty(compositeParent, propertyWrapper, new PropertyWrapper(propertyWrapper.getOtherEnd()));
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
		}
	}

	@Override
	public void visitAfter(Property p) {
	}

<<<<<<< HEAD
	public static void generateLookupForOneProperty(Type compositeParent, OJAnnotatedClass ojClass, PropertyWrapper propertyWrapper, PropertyWrapper otherEndPropertyWrapper) {
		OJAnnotatedOperation lookUp = new OJAnnotatedOperation("lookup" + StringUtils.capitalize(otherEndPropertyWrapper.getName()));
		lookUp.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(TumlClassOperations.getPathName(otherEndPropertyWrapper.getType())));
=======
	private void generateLookupForOneProperty(Type compositeParent, PropertyWrapper propertyWrapper, PropertyWrapper otherEndPropertyWrapper) {
		OJAnnotatedOperation lookUp = new OJAnnotatedOperation("lookup" + StringUtils.capitalize(propertyWrapper.getName()));
		lookUp.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(TumlClassOperations.getPathName(propertyWrapper.getType())));
		OJClass ojClass = findOJClass(propertyWrapper);
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
		ojClass.addToOperations(lookUp);
		buildGetterToCompositeParent(ojClass, lookUp, propertyWrapper, otherEndPropertyWrapper, compositeParent);
	}

<<<<<<< HEAD
	private static void buildGetterToCompositeParent(OJAnnotatedClass ojClass, OJAnnotatedOperation lookup, PropertyWrapper propertyWrapper,
			PropertyWrapper otherEndPropertyWrapper, Type compositeParent) {
		StringBuilder sb = new StringBuilder();
		sb.append("return ");
		constructGetterAsString(true, sb, propertyWrapper, compositeParent);
		if (!propertyWrapper.getType().equals(compositeParent)) {
			sb.append(".");
		}
		List<String> getterStringsInReverse = new ArrayList<String>();
		constructLookupAsString(ojClass, getterStringsInReverse, otherEndPropertyWrapper, compositeParent);
		Collections.reverse(getterStringsInReverse);
		for (String string : getterStringsInReverse) {
			sb.append(string);
		}
		sb.append("asSet()");
		lookup.getBody().addToStatements(sb.toString());
	}

	private static void constructLookupAsString(OJClass ojClass, List<String> getterStringsInReverse, PropertyWrapper propertyWrapper, Type compositeParent) {
=======
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
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
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
<<<<<<< HEAD

			PropertyWrapper otherEndToComposite2;
			if (otherEndToComposite.getType() instanceof Interface) {
				Interface type = (Interface) otherEndToComposite.getType();
				otherEndToComposite2 = new PropertyWrapper(TumlInterfaceOperations.getOtherEndToComposite(type));
			} else if (otherEndToComposite.getType() instanceof Class) {
				Class type = (Class) otherEndToComposite.getType();
				otherEndToComposite2 = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(type));
			} else {
				throw new RuntimeException("TODO " + propertyWrapper.getType());
			}

			PropertyWrapper otherEndPWrap = new PropertyWrapper(otherEndToComposite.getOtherEnd());

			// Coerce to correct type
			if (!otherEndToComposite.getOtherEnd().getType().equals(propertyWrapper.getType())) {
				if (new PropertyWrapper(otherEndToComposite.getOtherEnd()).isMany()) {
					getterStringsInReverse.add("<" + propertyWrapper.javaBaseTypePath().getLast() + ", " + propertyWrapper.javaBaseTypePath().getCopy().getLast() + ">collect("
							+ constructBodyEvaluateExpressionForType(otherEndPWrap, propertyWrapper) + ").");
					ojClass.addToImports(TinkerGenerationUtil.BodyExpressionEvaluator);
				} else {
					throw new RuntimeException("Not yet implemented!");
				}
			}

			// Use collect on manies
			if (otherEndToComposite2.getProperty() != null && new PropertyWrapper(otherEndToComposite2.getOtherEnd()).isMany()
					&& !otherEndToComposite2.getOtherEnd().getType().equals(compositeParent)) {
				PropertyWrapper propertyWrapper2 = new PropertyWrapper(otherEndToComposite2.getOtherEnd());
				getterStringsInReverse.add("<" + otherEndPWrap.javaBaseTypePath().getLast() + ", " + otherEndPWrap.javaTumlTypePath().getCopy().getLast() + ">collect("
						+ constructBodyEvaluateExpression(otherEndPWrap, propertyWrapper2) + ").");
				ojClass.addToImports(otherEndPWrap.javaTumlTypePath());
				ojClass.addToImports(TinkerGenerationUtil.BodyExpressionEvaluator);
			} else {
				getterStringsInReverse.add(otherEndPWrap.getter() + "().");
			}

			constructLookupAsString(ojClass, getterStringsInReverse, otherEndToComposite, compositeParent);
		}
	}

	private static String constructBodyEvaluateExpressionForType(PropertyWrapper otherEndPWrap, PropertyWrapper propertyWrapper) {
		return "new BodyExpressionEvaluator<" + propertyWrapper.javaBaseTypePath().getCopy().getLast() + ", " + otherEndPWrap.javaBaseTypePath().getCopy().getLast() + ">(){\n"
				+ "    @Override\n    public " + propertyWrapper.javaBaseTypePath().getCopy().getLast() + " evaluate(" + otherEndPWrap.javaBaseTypePath().getLast() + " e)"
				+ "{\n        return (e instanceof " + propertyWrapper.javaBaseTypePath().getCopy().getLast() + " ? (" + propertyWrapper.javaBaseTypePath().getCopy().getLast()
				+ ")e : null);\n    }" + "\n}";
	}

	private static String constructBodyEvaluateExpression(PropertyWrapper otherEndPWrap, PropertyWrapper propertyWrapper) {
		return "new BodyExpressionEvaluator<" + otherEndPWrap.javaTumlTypePath().getCopy().getLast() + ", " + propertyWrapper.javaBaseTypePath().getLast() + ">(){\n"
				+ "    @Override\n    public " + otherEndPWrap.javaTumlTypePath().getCopy().getLast() + " evaluate(" + propertyWrapper.javaBaseTypePath().getLast() + " e)"
				+ "{\n        return e." + otherEndPWrap.getter() + "();\n    }" + "\n}";
	}

	private static void constructGetterAsString(boolean first, StringBuilder sb, PropertyWrapper propertyWrapper, Type compositeParent) {
=======
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
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
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
<<<<<<< HEAD
			sb.append(otherEndToComposite.getter() + "()");
=======
			sb.append(otherEndToComposite.getter());
			sb.append("()");
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
			constructGetterAsString(false, sb, otherEndToComposite, compositeParent);
		}

	}

<<<<<<< HEAD
	public static Type findCompositeParent(PropertyWrapper propertyWrapper) {
=======
	private Type findCompositeParent(PropertyWrapper propertyWrapper) {
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
		PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());
		// Need to find the composite parent of the 2 properties
		return findCompositeParent(propertyWrapper, otherEnd);
	}

<<<<<<< HEAD
	private static Type findCompositeParent(PropertyWrapper propertyWrapper, PropertyWrapper otherEnd) {
=======
	private Type findCompositeParent(PropertyWrapper propertyWrapper, PropertyWrapper otherEnd) {
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
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

<<<<<<< HEAD
	private static void createListOfOrderedTypes(List<Type> orderedListOfCompositeTypes, PropertyWrapper propertyWrapper) {
=======
	private void createListOfOrderedTypes(List<Type> orderedListOfCompositeTypes, PropertyWrapper propertyWrapper) {
>>>>>>> 953d047099e607a112abaaf6120cc6da8def3c35
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

package org.tuml.javageneration.visitor.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
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
		if (!propertyWrapper.isComposite() && !(propertyWrapper.getType() instanceof Enumeration) && !propertyWrapper.isDerived() && !propertyWrapper.isQualifier()
				&& propertyWrapper.getOtherEnd() != null && !(propertyWrapper.getOtherEnd().getType() instanceof Enumeration) && !propertyWrapper.getOtherEnd().isComposite()) {
			Type compositeParent = findCompositeParent(propertyWrapper);
			OJAnnotatedClass ojClass = findOJClass(propertyWrapper);
			generateLookupForNonCompositeProperty(compositeParent, ojClass, new PropertyWrapper(propertyWrapper.getOtherEnd()), propertyWrapper);
		}
	}

	@Override
	public void visitAfter(Property p) {
	}

	public static void generateLookupForNonCompositeProperty(Type compositeParent, OJAnnotatedClass ojClass, PropertyWrapper propertyWrapper,
			PropertyWrapper otherEndPropertyWrapper) {
		OJAnnotatedOperation lookupCompositeParent = new OJAnnotatedOperation(otherEndPropertyWrapper.lookupCompositeParent());
		lookupCompositeParent.setReturnType(TumlClassOperations.getPathName(compositeParent));
		ojClass.addToOperations(lookupCompositeParent);
		OJAnnotatedOperation lookup = new OJAnnotatedOperation(otherEndPropertyWrapper.lookup());
		lookup.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(TumlClassOperations.getPathName(otherEndPropertyWrapper.getType())));
		ojClass.addToOperations(lookup);
		buildGetterToCompositeParent(ojClass, lookupCompositeParent, propertyWrapper, compositeParent);
		buildLookupFromCompositeParent(ojClass, lookup, propertyWrapper, otherEndPropertyWrapper, compositeParent);
	}

	private static void buildGetterToCompositeParent(OJAnnotatedClass ojClass, OJAnnotatedOperation lookupCompositeParent, PropertyWrapper propertyWrapper, Type compositeParent) {
		// build getter for special case where the non composite lookup of the
		// same type as the is in the composite tree
		StringBuilder sb = new StringBuilder();
		sb.append("return ");
		if (propertyWrapper.getType().equals(compositeParent)) {
			sb.append("this");
		} else {
			constructGetterAsString(true, sb, propertyWrapper, compositeParent);
		}
		lookupCompositeParent.getBody().addToStatements(sb.toString());
	}

	private static void buildLookupFromCompositeParent(OJAnnotatedClass ojClass, OJAnnotatedOperation lookup, PropertyWrapper propertyWrapper,
			PropertyWrapper otherEndPropertyWrapper, Type compositeParent) {
		// build getter for special case where the non composite lookup of the
		// same type as the is in the composite tree
		StringBuilder sb = new StringBuilder();
		sb.append("return ");
		if (!propertyWrapper.getType().equals(compositeParent)) {
			OJField compositeParentField = new OJField("compositeParent", TumlClassOperations.getPathName(compositeParent));
			compositeParentField.setInitExp(otherEndPropertyWrapper.lookupCompositeParent() + "()");
			lookup.getBody().addToLocals(compositeParentField);
			sb.append("compositeParent.");
		}
		List<String> getterStringsInReverse = new ArrayList<String>();
		constructLookupAsString(ojClass, getterStringsInReverse, otherEndPropertyWrapper, compositeParent);
		Collections.reverse(getterStringsInReverse);
		for (String string : getterStringsInReverse) {
			sb.append(string);
		}
		sb.append("<" + otherEndPropertyWrapper.javaBaseTypePath().getLast() + ">");
		sb.append("asSet()");
		// Ensure that one to one have a empty other side.
		// Eg., can only display rings in the dropdown that are not already on a
		// finger
		if (propertyWrapper.isOneToOne()) {
			sb.append(".select(" + constructBooleanEvaluateExpressionForType(propertyWrapper, otherEndPropertyWrapper) + ")");
			ojClass.addToImports(TinkerGenerationUtil.BooleanExpressionEvaluator);
		}
		lookup.getBody().addToStatements(sb.toString());
	}

	private static void constructLookupAsString(OJClass ojClass, List<String> getterStringsInReverse, PropertyWrapper propertyWrapper, Type compositeParent) {
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

	private static String constructBooleanEvaluateExpressionForType(PropertyWrapper otherEndPWrap, PropertyWrapper propertyWrapper) {
		return "new BooleanExpressionEvaluator<" + propertyWrapper.javaBaseTypePath().getCopy().getLast() + ">(){\n" + "    @Override\n    public Boolean evaluate("
				+ propertyWrapper.javaBaseTypePath().getLast() + " e)" + "{\n        return e." + otherEndPWrap.getter() + "() == null ;\n    }\n}";
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
			sb.append(otherEndToComposite.getter() + "()");
			constructGetterAsString(false, sb, otherEndToComposite, compositeParent);
		}

	}

	public static Type findCompositeParent(PropertyWrapper propertyWrapper) {
		PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());
		// Need to find the composite parent of the 2 properties
		return findCompositeParent(propertyWrapper, otherEnd);
	}

	private static Type findCompositeParent(PropertyWrapper propertyWrapper, PropertyWrapper otherEnd) {
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

	private static void createListOfOrderedTypes(List<Type> orderedListOfCompositeTypes, PropertyWrapper propertyWrapper) {
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

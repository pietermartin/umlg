package org.tuml.javageneration.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.internal.operations.ClassOperations;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.javageneration.naming.Namer;

public class TumlClassOperations extends ClassOperations {

	/*
	 * These include all properties that are on the other end of an
	 * association
	 */
	public static List<Property> getAllOwnedProperties(org.eclipse.uml2.uml.Class clazz) {
		List<Property> result = clazz.getOwnedAttributes();
		List<Association> associations = clazz.getAssociations();
		for (Association association : associations) {
			List<Property> memberEnds = association.getMemberEnds();
			for (Property property : memberEnds) {
				if (property.getType() != clazz) {
					result.add(property);
				}
			}
		}
		return result;
	}

	public static Property getOtherEndToComposite(Class clazz) {
		Set<Association> associations = getAllAssociations(clazz);
		for (Association association : associations) {
			List<Property> memberEnds = association.getMemberEnds();
			for (Property property : memberEnds) {
				if (!property.isComposite() && property.getType() != clazz && property.getOtherEnd().isComposite() && isSpecializationOf(clazz, property.getOtherEnd().getType())) {
					return property;
				}
			}
		}
		return null;
	}

	/*
	 * Only BehavioredClassifier can realize interfaces
	 */
	public static boolean isSpecializationOf(Classifier classifier, Type type) {
		if (classifier == type) {
			return true;
		}
		if ((classifier instanceof BehavioredClassifier) && ((BehavioredClassifier) classifier).getAllImplementedInterfaces().contains(type)) {
			return true;
		}
		for (Classifier general : classifier.getGenerals()) {
			isSpecializationOf(general, type);
		}
		return false;
	}

	public static Set<Association> getAllAssociations(Class clazz) {
		Set<Association> result = new HashSet<Association>();
		for (Interface implementedInterface : clazz.getAllImplementedInterfaces()) {
			result.addAll(implementedInterface.getAssociations());
		}
		getAllAssociationsFromGenerals(clazz, result);
		return result;
	}

	public static void getAllAssociationsFromGenerals(Classifier classifier, Set<Association> result) {
		result.addAll(classifier.getAssociations());
		for (Classifier general : classifier.getGenerals()) {
			getAllAssociationsFromGenerals(general, result);
		}
	}

	public static OJPathName getOtherEndToCompositePathName(Class clazz) {
		Property endToComposite = getOtherEndToComposite(clazz);
		if (endToComposite != null) {
			return getPathName(endToComposite.getType());
		} else {
			return null;
		}
	}

	public static OJPathName getPathName(Type type) {
		String className = Namer.name(type);
		String fullPackageName = Namer.name(type.getNearestPackage());
		OJPathName ojPathName = new OJPathName(fullPackageName + "." + className);
		return ojPathName;
	}

	public static boolean hasSupertype(Class clazz) {
		return !getConcreteGenerals(clazz).isEmpty();
	}

	public static List<Class> getConcreteGenerals(Class clazz) {
		List<Class> result = new ArrayList<Class>();
		List<Classifier> generals = clazz.getGenerals();
		for (Classifier classifier : generals) {
			if (classifier instanceof Class) {
				result.add((Class) classifier);
			}
		}
		return result;
	}

	public static boolean hasCompositeOwner(Class clazz) {
		return getOtherEndToComposite(clazz) != null;
	}

	public static String className(Class clazz) {
		return Namer.name(clazz);
	}

	public static String propertyEnumName(Type type) {
		return Namer.name(type) + "RuntimePropertyEnum";
	}

}

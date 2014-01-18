package org.umlg.javageneration.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.operations.InterfaceOperations;

public class UmlgInterfaceOperations extends InterfaceOperations {

	public static boolean hasCompositeOwner(Interface inf) {
		return getOtherEndToComposite(inf) != null;
	}

	public static Property getOtherEndToComposite(Interface inf) {
		Set<Association> associations = getAllAssociations(inf);
		for (Association association : associations) {
			List<Property> memberEnds = association.getMemberEnds();
			for (Property property : memberEnds) {
				if (!property.isComposite() && property.getType() != inf && property.getOtherEnd().isComposite()
						&& UmlgClassOperations.isSpecializationOf(inf, property.getOtherEnd().getType())) {
					return property;
				}
			}
		}
		return null;
	}

	public static Set<Association> getAllAssociations(Interface inf) {
		Set<Association> result = new HashSet<Association>();
		UmlgClassOperations.getAllAssociationsFromGenerals(inf, result);
		return result;
	}

	public static Set<Property> getAllProperties(Interface inf) {
		Set<Property> result = new HashSet<Property>();
		result.addAll(inf.getAllAttributes());
		Set<Association> associations = getAllAssociations(inf);
		for (Association association : associations) {
			List<Property> memberEnds = association.getMemberEnds();
			for (Property property : memberEnds) {
				if (!UmlgClassOperations.isSpecializationOf(inf, property.getType())) {
					result.add(property);
				}
			}
		}
		return result;
	}

}

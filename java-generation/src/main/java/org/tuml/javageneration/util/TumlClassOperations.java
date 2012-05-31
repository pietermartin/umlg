package org.tuml.javageneration.util;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.operations.ClassOperations;

public class TumlClassOperations extends ClassOperations {

	/*
	 * These include all owned properties and redefinitions
	 */
	public List<Property> getDirectoryImplementedProperties(org.eclipse.uml2.uml.Class clazz) {
		return clazz.getOwnedAttributes();
	}

	public static Property getEndToComposite(Class clazz) {
		List<Association> associations = clazz.getAssociations();
		for (Association association : associations) {
			List<Property> memberEnds = association.getMemberEnds();
			for (Property property : memberEnds) {
				if (!property.isComposite() && property.getType() != clazz && property.getOtherEnd().isComposite() && property.getOtherEnd().getType() == clazz) {
					return property;
				}
			}
		}
		return null;
	}

}

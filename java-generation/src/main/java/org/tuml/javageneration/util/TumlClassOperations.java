package org.tuml.javageneration.util;

import java.util.List;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.operations.ClassOperations;

public class TumlClassOperations extends ClassOperations {

	/*
	 * These include all owned properties and redefinitions
	 */
	public List<Property> getDirectoryImplementedProperties(org.eclipse.uml2.uml.Class clazz) {
		return clazz.getOwnedAttributes();
	}
	
}

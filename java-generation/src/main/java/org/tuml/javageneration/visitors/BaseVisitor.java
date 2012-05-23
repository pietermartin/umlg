package org.tuml.javageneration.visitors;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.naming.Namer;

public class BaseVisitor {
	
	protected void addToSource(OJAnnotatedClass source) {
		Workspace.addToClassMap(source);
	}
	
	protected OJAnnotatedClass findOJClass(NamedElement owner) {
		return Workspace.findOJClass(Namer.qualifiedName(owner));
	}
	
	protected OJAnnotatedClass findOJClass(Property p) {
		Element owner = p.getOwner();
		//Association must come first in this if statement as Association is also a Classifier
		if (owner instanceof Association) {
			Association a = (Association) owner;
			List<Property> members = a.getMemberEnds();
			Property otherEnd = null;
			for (Property member : members) {
				if (member!=p) {
					otherEnd = member;
					break;
				}
			}
			if (otherEnd==null) {
				throw new IllegalStateException("Oy, where is the other end gone to!!!");
			}
			return Workspace.findOJClass(Namer.qualifiedName((NamedElement)otherEnd.getType()));
		} else if (owner instanceof Classifier) {
				return Workspace.findOJClass(Namer.qualifiedName((NamedElement)owner));
		} else {
			throw new IllegalStateException("Not catered for, think about ne.");
		}
	}
	
}

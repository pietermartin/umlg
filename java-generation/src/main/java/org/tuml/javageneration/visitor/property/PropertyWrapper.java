package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.javageneration.util.TumlPropertyOperations;

public class PropertyWrapper {

	private Property property;

	public PropertyWrapper(Property property) {
		super();
		this.property = property;
	}
	
	public String getter() {
		return TumlPropertyOperations.getter(this.property);
	}

	public String setter() {
		return TumlPropertyOperations.setter(this.property);
	}

	public OJPathName javaBaseTypePath() {
		return TumlPropertyOperations.getTypePath(this.property);
	}

}

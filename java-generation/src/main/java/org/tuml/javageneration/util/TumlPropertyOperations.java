package org.tuml.javageneration.util;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.operations.PropertyOperations;

public final class TumlPropertyOperations extends PropertyOperations {

	private Property property;

	public TumlPropertyOperations(Property property) {
		super();
		this.property = property;
	}

	public boolean isMany() {
		int qualifierCount =this. property.getQualifiers().size();
		return this.property.getUpper() > 1 || qualifierCount > 0;
	}

	public boolean isOne() {
		return !isMany();
	}

//	public boolean isOneToMany() {
//		return otherEndIsOne() && isMany();
//	}
//
//	public boolean isManyToMany() {
//		return !otherEndIsOne() && isMany();
//	}
//
//	public boolean isManyToOne() {
//		return !otherEndIsOne() && isOne();
//	}
//
//	public boolean isOneToOne() {
//		return otherEndIsOne() && isOne();
//	}
//
//	protected boolean otherEndIsOne(){
//		if(this.property.getOtherEnd() != null){
//			Property otherEnd = this.property.getOtherEnd();
//			return otherEnd.isNavigable() && otherEnd.getNakedMultiplicity().isOne() && otherEnd.getQualifiers().size() == 0;
//		}else{
//			return false;
//		}
//	}

}

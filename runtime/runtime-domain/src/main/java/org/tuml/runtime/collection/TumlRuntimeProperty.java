package org.tuml.runtime.collection;

import org.tuml.runtime.domain.DataTypeEnum;

public interface TumlRuntimeProperty {
	boolean isOnePrimitive();
	DataTypeEnum getDataTypeEnum();
	boolean isManyPrimitive();
	boolean isOneEnumeration();
	boolean isManyEnumeration();
	boolean isControllingSide();
	boolean isComposite();
	boolean isInverseComposite();
	boolean isOneToOne(); 
	boolean isOneToMany(); 
	boolean isManyToOne(); 
	boolean isManyToMany();
	int getUpper();
	int getLower();
	String getLabel();
	String getQualifiedName();
	String getInverseQualifiedName();
	boolean isValid(int elementCount);
	boolean isQualified();
	boolean isInverseQualified();
	boolean isOrdered();
	boolean isInverseOrdered();
	boolean isUnique();
	String toJson();
}

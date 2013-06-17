package org.umlg.runtime.collection;

import org.umlg.runtime.domain.DataTypeEnum;

public interface TumlRuntimeProperty {
    boolean isAssociationClass();
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
    int getInverseUpper();
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
    boolean isInverseUnique();
	String toJson();
}

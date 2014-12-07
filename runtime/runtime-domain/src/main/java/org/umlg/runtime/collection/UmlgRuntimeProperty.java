package org.umlg.runtime.collection;

import org.umlg.runtime.domain.DataTypeEnum;

public interface UmlgRuntimeProperty {
    /**
     * This returns true for the fake property on the association class to its member ends.
     */
    boolean isAssociationClassProperty();
    boolean isOnePrimitivePropertyOfAssociationClass();
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
    String getPersistentName();
	String getInverseQualifiedName();
	boolean isValid(int elementCount);
	boolean isQualified();
	boolean isInverseQualified();
	boolean isOrdered();
	boolean isInverseOrdered();
	boolean isUnique();
    boolean isInverseUnique();
	String toJson();
	boolean isChangeListener();
    Class getPropertyType();
}

package org.umlg.javageneration.util;

import org.eclipse.ocl.expressions.CollectionKind;
import org.eclipse.ocl.uml.BagType;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.ocl.uml.OrderedSetType;
import org.eclipse.ocl.uml.SequenceType;
import org.eclipse.ocl.uml.SetType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.umlg.java.metamodel.OJPathName;

public enum UmlgCollectionKindEnum {

	COLLECTION(UmlgGenerationUtil.tinkerCollection.getCopy(), null, UmlgGenerationUtil.umlgMemoryCollection.getCopy(), null),

	SET(UmlgGenerationUtil.tinkerSet.getCopy(), UmlgGenerationUtil.tinkerSetImpl.getCopy(), UmlgGenerationUtil.umlgMemorySet.getCopy(), UmlgGenerationUtil.umlgSetCloseableIterablePathName.getCopy()),
	QUALIFIED_SET(UmlgGenerationUtil.tinkerQualifiedSet.getCopy(), UmlgGenerationUtil.tinkerQualifiedSetImpl.getCopy(), UmlgGenerationUtil.umlgMemorySet.getCopy(), UmlgGenerationUtil.umlgSetCloseableIterablePathName.getCopy()),
    ASSOCIATION_CLASS_SET(UmlgGenerationUtil.umlgPropertyAssociationClassSet.getCopy(), UmlgGenerationUtil.umlgPropertyAssociationClassSetImpl.getCopy(), UmlgGenerationUtil.umlgMemorySet.getCopy(), UmlgGenerationUtil.umlgSetCloseableIterablePathName.getCopy()),
    AC_SET(UmlgGenerationUtil.tinkerSet.getCopy(), UmlgGenerationUtil.umlgAssociationClassSetImpl.getCopy(), UmlgGenerationUtil.umlgMemorySet.getCopy(), UmlgGenerationUtil.umlgSetCloseableIterablePathName.getCopy()),

    SEQUENCE(UmlgGenerationUtil.tinkerSequence.getCopy(), UmlgGenerationUtil.tinkerSequenceImpl.getCopy(), UmlgGenerationUtil.umlgMemorySequence.getCopy(), UmlgGenerationUtil.umlgSequenceCloseableIterablePathName.getCopy()),
	QUALIFIED_SEQUENCE(UmlgGenerationUtil.tinkerQualifiedSequence.getCopy(), UmlgGenerationUtil.tinkerQualifiedSequenceImpl.getCopy(), UmlgGenerationUtil.umlgMemorySequence.getCopy(), UmlgGenerationUtil.umlgSequenceCloseableIterablePathName.getCopy()),
    ASSOCIATION_CLASS_SEQUENCE(UmlgGenerationUtil.umlgPropertyAssociationClassSequence.getCopy(), UmlgGenerationUtil.umlgPropertyAssociationClassSequenceImpl.getCopy(), UmlgGenerationUtil.umlgMemorySequence.getCopy(), UmlgGenerationUtil.umlgSequenceCloseableIterablePathName.getCopy()),
    AC_SEQUENCE(UmlgGenerationUtil.tinkerSequence.getCopy(), UmlgGenerationUtil.umlgAssociationClassSequenceImpl.getCopy(), UmlgGenerationUtil.umlgMemorySequence.getCopy(), UmlgGenerationUtil.umlgSequenceCloseableIterablePathName.getCopy()),


    BAG(UmlgGenerationUtil.tinkerBag.getCopy(), UmlgGenerationUtil.tinkerBagImpl.getCopy(), UmlgGenerationUtil.umlgMemoryBag.getCopy(), UmlgGenerationUtil.umlgBagCloseableIterablePathName.getCopy()),
	QUALIFIED_BAG(UmlgGenerationUtil.tinkerQualifiedBag.getCopy(), UmlgGenerationUtil.tinkerQualifiedBagImpl.getCopy(), UmlgGenerationUtil.umlgMemoryBag.getCopy(), UmlgGenerationUtil.umlgBagCloseableIterablePathName.getCopy()),
    ASSOCIATION_CLASS_BAG(UmlgGenerationUtil.umlgPropertyAssociationClassBag.getCopy(), UmlgGenerationUtil.umlgPropertyAssociationClassBagImpl.getCopy(), UmlgGenerationUtil.umlgMemoryBag.getCopy(), UmlgGenerationUtil.umlgBagCloseableIterablePathName.getCopy()),
    AC_BAG(UmlgGenerationUtil.tinkerBag.getCopy(), UmlgGenerationUtil.umlgAssociationClassBagImpl.getCopy(), UmlgGenerationUtil.umlgMemoryBag.getCopy(), UmlgGenerationUtil.umlgBagCloseableIterablePathName.getCopy()),

	ORDERED_SET(UmlgGenerationUtil.tinkerOrderedSet.getCopy(), UmlgGenerationUtil.tinkerOrderedSetImpl.getCopy(), UmlgGenerationUtil.umlgMemoryOrderedSet.getCopy(), UmlgGenerationUtil.umlgOrderedSetCloseableIterablePathName.getCopy()),
	QUALIFIED_ORDERED_SET(UmlgGenerationUtil.tinkerQualifiedOrderedSet.getCopy(), UmlgGenerationUtil.tinkerQualifiedOrderedSetImpl.getCopy(), UmlgGenerationUtil.umlgMemoryOrderedSet.getCopy(), UmlgGenerationUtil.umlgOrderedSetCloseableIterablePathName.getCopy()),
    ASSOCIATION_CLASS_ORDERED_SET(UmlgGenerationUtil.umlgPropertyAssociationClassOrderedSet.getCopy(), UmlgGenerationUtil.umlgPropertyAssociationClassOrderedSetImpl.getCopy(), UmlgGenerationUtil.umlgMemoryOrderedSet.getCopy(), UmlgGenerationUtil.umlgOrderedSetCloseableIterablePathName.getCopy()),
    AC_ORDERED_SET(UmlgGenerationUtil.tinkerOrderedSet.getCopy(), UmlgGenerationUtil.umlgAssociationClassOrderedSetImpl.getCopy(), UmlgGenerationUtil.umlgMemoryOrderedSet.getCopy(), UmlgGenerationUtil.umlgOrderedSetCloseableIterablePathName.getCopy());

	private OJPathName interfacePathName;
	private OJPathName implPathName;
	private OJPathName memoryCollection;
	private OJPathName closableIteratorPathName;

	private UmlgCollectionKindEnum(OJPathName interfacePathName, OJPathName implPathName, OJPathName memoryCollection, OJPathName closableIteratorPathName) {
		this.interfacePathName = interfacePathName;
		this.implPathName = implPathName;
		this.memoryCollection = memoryCollection;
		this.closableIteratorPathName = closableIteratorPathName;
	}

	public OJPathName getOjPathName() {
		return interfacePathName.getCopy();
	}

	public static UmlgCollectionKindEnum from(CollectionKind collectionKind) {
		switch (collectionKind) {
		case BAG_LITERAL:
			return BAG;
		case COLLECTION_LITERAL:
			return COLLECTION;
		case ORDERED_SET_LITERAL:
			return ORDERED_SET;
		case SEQUENCE_LITERAL:
			return SEQUENCE;
		case SET_LITERAL:
			return SET;
		default:
			throw new IllegalStateException("Unknown collection literal");
		}
	}

	public static UmlgCollectionKindEnum from(Type type) {
		if (type instanceof SequenceType) {
			return SEQUENCE;
		} else if (type instanceof BagType) {
			return BAG;
		} else if (type instanceof SetType) {
			return SET;
		} else if (type instanceof OrderedSetType) {
			return ORDERED_SET;
		} else if (type instanceof CollectionType) {
			return COLLECTION;
		} else {
			throw new IllegalStateException("Unknown collection literal");
		}
	}

	public static Type getElementType(Type type) {
		if (type instanceof SequenceType) {
			return ((SequenceType)type).getElementType();
		} else if (type instanceof BagType) {
			return ((BagType)type).getElementType();
		} else if (type instanceof SetType) {
			return ((SetType)type).getElementType();
		} else if (type instanceof OrderedSetType) {
			return ((OrderedSetType)type).getElementType();
		} else if (type instanceof CollectionType) {
			return ((CollectionType)type).getElementType();
		} else {
			throw new IllegalStateException("Unknown collection literal");
		}
	}

	public static UmlgCollectionKindEnum from(Property p) {
		if (p.isOrdered() && p.isUnique()) {
			return ORDERED_SET;
		} else if (p.isOrdered() && !p.isUnique()) {
			return SEQUENCE;
		} else if (!p.isOrdered() && !p.isUnique()) {
			return BAG;
		} else if (!p.isOrdered() && p.isUnique()) {
			return SET;
		} else {
			throw new RuntimeException("wtf");
		}
	}

	public OJPathName getInterfacePathName() {
		return this.interfacePathName.getCopy();
	}

	public OJPathName getMemoryCollection() {
		return this.memoryCollection.getCopy();
	}

	public OJPathName getClosableIteratorPathName() {
		return this.closableIteratorPathName.getCopy();
	}

	public OJPathName getImplementationPathName() {
		return this.implPathName.getCopy();
	}

}

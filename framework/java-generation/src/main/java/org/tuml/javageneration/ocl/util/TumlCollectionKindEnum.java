package org.tuml.javageneration.ocl.util;

import org.eclipse.ocl.expressions.CollectionKind;
import org.eclipse.ocl.uml.BagType;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.ocl.uml.OrderedSetType;
import org.eclipse.ocl.uml.SequenceType;
import org.eclipse.ocl.uml.SetType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.javageneration.util.TinkerGenerationUtil;

public enum TumlCollectionKindEnum {

	COLLECTION(TinkerGenerationUtil.tinkerCollection.getCopy(), null, TinkerGenerationUtil.tumlMemoryCollection.getCopy(), null), 
	SET(TinkerGenerationUtil.tinkerSet.getCopy(), TinkerGenerationUtil.tinkerSetImpl.getCopy(), TinkerGenerationUtil.tumlMemorySet.getCopy(), TinkerGenerationUtil.tumlSetCloseableIterablePathName.getCopy()),
	QUALIFIED_SET(TinkerGenerationUtil.tinkerQualifiedSet.getCopy(), TinkerGenerationUtil.tinkerQualifiedSetImpl.getCopy(), TinkerGenerationUtil.tumlMemorySet.getCopy(), TinkerGenerationUtil.tumlSetCloseableIterablePathName.getCopy()), 
	SEQUENCE(TinkerGenerationUtil.tinkerSequence.getCopy(), TinkerGenerationUtil.tinkerSequenceImpl.getCopy(), TinkerGenerationUtil.tumlMemorySequence.getCopy(), TinkerGenerationUtil.tumlSequenceCloseableIterablePathName.getCopy()), 
	QUALIFIED_SEQUENCE(TinkerGenerationUtil.tinkerQualifiedSequence.getCopy(), TinkerGenerationUtil.tinkerQualifiedSequenceImpl.getCopy(), TinkerGenerationUtil.tumlMemorySequence.getCopy(), TinkerGenerationUtil.tumlSequenceCloseableIterablePathName.getCopy()),
	BAG(TinkerGenerationUtil.tinkerBag.getCopy(), TinkerGenerationUtil.tinkerBagImpl.getCopy(), TinkerGenerationUtil.tumlMemoryBag.getCopy(), TinkerGenerationUtil.tumlBagCloseableIterablePathName.getCopy()), 
	QUALIFIED_BAG(TinkerGenerationUtil.tinkerQualifiedBag.getCopy(), TinkerGenerationUtil.tinkerQualifiedBagImpl.getCopy(), TinkerGenerationUtil.tumlMemoryBag.getCopy(), TinkerGenerationUtil.tumlBagCloseableIterablePathName.getCopy()), 
	ORDERED_SET(TinkerGenerationUtil.tinkerOrderedSet.getCopy(), TinkerGenerationUtil.tinkerOrderedSetImpl.getCopy(), TinkerGenerationUtil.tumlMemoryOrderedSet.getCopy(), TinkerGenerationUtil.tumlOrderedSetCloseableIterablePathName.getCopy()),
	QUALIFIED_ORDERED_SET(TinkerGenerationUtil.tinkerQualifiedOrderedSet.getCopy(), TinkerGenerationUtil.tinkerQualifiedOrderedSetImpl.getCopy(), TinkerGenerationUtil.tumlMemoryOrderedSet.getCopy(), TinkerGenerationUtil.tumlOrderedSetCloseableIterablePathName.getCopy());

	private OJPathName interfacePathName;
	private OJPathName implPathName;
	private OJPathName memoryCollection;
	private OJPathName closableIteratorPathName;

	private TumlCollectionKindEnum(OJPathName interfacePathName, OJPathName implPathName, OJPathName memoryCollection, OJPathName closableIteratorPathName) {
		this.interfacePathName = interfacePathName;
		this.implPathName = implPathName;
		this.memoryCollection = memoryCollection;
		this.closableIteratorPathName = closableIteratorPathName;
	}

	public OJPathName getOjPathName() {
		return interfacePathName;
	}

	public static TumlCollectionKindEnum from(CollectionKind collectionKind) {
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

	public static TumlCollectionKindEnum from(Type type) {
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

	public static Type getElemenTtype(Type type) {
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

	public static TumlCollectionKindEnum from(Property p) {
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

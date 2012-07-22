package org.tuml.javageneration.ocl.util;

import org.eclipse.ocl.expressions.CollectionKind;
import org.eclipse.ocl.uml.BagType;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.ocl.uml.OrderedSetType;
import org.eclipse.ocl.uml.SequenceType;
import org.eclipse.ocl.uml.SetType;
import org.eclipse.uml2.uml.Type;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.javageneration.util.TinkerGenerationUtil;

public enum TumlCollectionKindEnum {

	COLLECTION(TinkerGenerationUtil.tinkerCollection.getCopy(), TinkerGenerationUtil.tumlMemoryCollection.getCopy()), SET(TinkerGenerationUtil.tinkerSet.getCopy(),
			TinkerGenerationUtil.tumlMemorySet.getCopy()), SEQUENCE(TinkerGenerationUtil.tinkerSequence.getCopy(), TinkerGenerationUtil.tumlMemorySequence.getCopy()), BAG(
			TinkerGenerationUtil.tinkerBag.getCopy(), TinkerGenerationUtil.tumlMemoryBag.getCopy()), ORDERED_SET(TinkerGenerationUtil.tinkerOrderedSet.getCopy(),
			TinkerGenerationUtil.tumlMemoryOrderedSet.getCopy());

	private OJPathName ojPathName;
	private OJPathName memoryCollection;

	private TumlCollectionKindEnum(OJPathName ojPathName, OJPathName memoryCollection) {
		this.ojPathName = ojPathName;
		this.memoryCollection = memoryCollection;
	}

	public OJPathName getOjPathName() {
		return ojPathName;
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

	public OJPathName getMemoryCollection() {
		return memoryCollection;
	}

}

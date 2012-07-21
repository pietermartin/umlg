package org.tuml.javageneration.ocl.util;

import org.eclipse.ocl.expressions.CollectionKind;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.javageneration.util.TinkerGenerationUtil;

public enum TumlCollectionKindEnum {

	COLLECTION(TinkerGenerationUtil.tinkerCollection.getCopy(), "//TODO"), SET(TinkerGenerationUtil.tinkerSet.getCopy(), "new TumlMemorySet()"), SEQUENCE(TinkerGenerationUtil.tinkerSequence.getCopy(), "new TumlMemorySequence()"), BAG(
			TinkerGenerationUtil.tinkerBag.getCopy(), "new TumlMemoryBag()"), ORDERED_SET(TinkerGenerationUtil.tinkerOrderedSet.getCopy(), "new TumlMemoryOrderedSet()");

	private OJPathName ojPathName;
	private String emptyCollection;

	private TumlCollectionKindEnum(OJPathName ojPathName, String emptyCollection) {
		this.ojPathName = ojPathName;
		this.emptyCollection = emptyCollection;
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

	public String getEmptyCollection() {
		return emptyCollection;
	}

}

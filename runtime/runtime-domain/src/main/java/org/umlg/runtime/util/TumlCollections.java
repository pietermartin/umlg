package org.umlg.runtime.util;

import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.TinkerOrderedSet;
import org.umlg.runtime.collection.TinkerSequence;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.collection.memory.TumlMemoryBag;
import org.umlg.runtime.collection.memory.TumlMemoryOrderedSet;
import org.umlg.runtime.collection.memory.TumlMemorySequence;
import org.umlg.runtime.collection.memory.TumlMemorySet;

public class TumlCollections {

	public static final <E> TinkerSet<E> emptySet() {
		return new TumlMemorySet<E>();
	}

	public static final <E> TinkerSequence<E> emptySequence() {
		return new TumlMemorySequence<E>();
	}

	public static final <E> TinkerBag<E> emptyBag() {
		return new TumlMemoryBag<E>();
	}

	public static final <E> TinkerOrderedSet<E> emptyOrderedSet() {
		return new TumlMemoryOrderedSet<E>();
	}

}

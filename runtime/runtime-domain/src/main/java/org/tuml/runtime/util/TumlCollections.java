package org.tuml.runtime.util;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.memory.TumlMemoryBag;
import org.tuml.runtime.collection.memory.TumlMemoryOrderedSet;
import org.tuml.runtime.collection.memory.TumlMemorySequence;
import org.tuml.runtime.collection.memory.TumlMemorySet;

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

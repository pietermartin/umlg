package org.umlg.runtime.util;

import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.TinkerOrderedSet;
import org.umlg.runtime.collection.TinkerSequence;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.collection.memory.UmlgMemoryBag;
import org.umlg.runtime.collection.memory.UmlgMemoryOrderedSet;
import org.umlg.runtime.collection.memory.UmlgMemorySequence;
import org.umlg.runtime.collection.memory.UmlgMemorySet;

public class UmlgCollections {

	public static final <E> TinkerSet<E> emptySet() {
		return new UmlgMemorySet<E>();
	}

	public static final <E> TinkerSequence<E> emptySequence() {
		return new UmlgMemorySequence<E>();
	}

	public static final <E> TinkerBag<E> emptyBag() {
		return new UmlgMemoryBag<E>();
	}

	public static final <E> TinkerOrderedSet<E> emptyOrderedSet() {
		return new UmlgMemoryOrderedSet<E>();
	}

}

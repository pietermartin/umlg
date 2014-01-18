package org.umlg.runtime.util;

import org.umlg.runtime.collection.*;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.memory.UmlgMemoryBag;
import org.umlg.runtime.collection.memory.UmlgMemoryOrderedSet;
import org.umlg.runtime.collection.memory.UmlgMemorySequence;
import org.umlg.runtime.collection.memory.UmlgMemorySet;

public class UmlgCollections {

	public static final <E> UmlgSet<E> emptySet() {
		return new UmlgMemorySet<E>();
	}

	public static final <E> UmlgSequence<E> emptySequence() {
		return new UmlgMemorySequence<E>();
	}

	public static final <E> UmlgBag<E> emptyBag() {
		return new UmlgMemoryBag<E>();
	}

	public static final <E> UmlgOrderedSet<E> emptyOrderedSet() {
		return new UmlgMemoryOrderedSet<E>();
	}

}

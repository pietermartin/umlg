package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.OclStdLibCollection;

import java.util.Collection;

public interface UmlgCollection<E> extends Collection<E>, OclStdLibCollection<E> {
    public static final String ASSOCIATION_CLASS_VERTEX_ID = "associationClassVertexId";
	String toJson();
    boolean inverseAdder(E e);
}

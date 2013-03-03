package org.tuml.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.TinkerQualifiedSequence;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TumlNode;

import java.util.Collection;

public class TinkerQualifiedSequenceImpl<E> extends BaseSequence<E> implements TinkerQualifiedSequence<E> {

	public TinkerQualifiedSequenceImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
        this.index = GraphDb.getDb().getIndex(owner.getUid() + INDEX_SEPARATOR + getQualifiedName(), Edge.class);
        if (this.index == null) {
            this.index = GraphDb.getDb().createIndex(owner.getUid() + INDEX_SEPARATOR + getQualifiedName(), Edge.class);
        }
    }

	@Override
	public void add(int indexOf, E e) {
        //Do not load, it needs to be traversed every time
        //It needs to be traversed because it is not possible to find the hyper vertex via the index because of duplicates
        Edge edge = addToListAtIndex(indexOf, e);
		// Can only qualify TinkerNode's
		if (!(e instanceof TumlNode)) {
			throw new IllegalStateException("Primitive properties can not be qualified!");
		}
		addQualifierToIndex(edge, (TumlNode)e);
        if (this.loaded) {
            getInternalList().add(indexOf, e);
        }
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public E set(int index, E element) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

}

package org.tuml.runtime.collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class BaseSet<E> extends BaseCollection<E> implements TinkerSet<E> {

	protected Map<Object, Vertex> internalVertexMap = new HashMap<Object, Vertex>();

	protected Set<E> getInternalSet() {
		return (Set<E>) this.internalCollection;
	}
	
	@Override
	public void clear() {
		maybeLoad();
		for (E e : new HashSet<E>(this.getInternalSet())) {
			this.remove(e);
		}
	}	

}

package org.umlg.runtime.collection.persistent;

import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.domain.UmlgMetaNode;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

public class UmlgSequenceClosableIterableImpl<E> extends BaseSequence<E> implements UmlgSequence<E> {

	private Iterator<Vertex> closeableIterator;

	public UmlgSequenceClosableIterableImpl(Iterator<Vertex> closeableIterator, UmlgRuntimeProperty umlgRuntimeProperty) {
		super(umlgRuntimeProperty);
		this.closeableIterator = closeableIterator;
	}

	@Override
	protected Iterator<Edge> getEdges() {
		return null;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	public E set(int index, E element) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	public void add(int index, E element) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void loadFromVertex() {
		for (Iterator<Vertex> iter = this.closeableIterator; iter.hasNext(); ) {
			Vertex vertex = iter.next();
			E node;
			try {
                Class<?> c = Class.forName((String) vertex.value("className"));
				if (c.isEnum()) {
					Object value = vertex.value(getPersistentName());
					node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                    putToInternalMap(node, vertex);
                } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
                    Method m = c.getDeclaredMethod("getInstance", new Class[0]);
                    node = (E) m.invoke(null);
				} else if (UmlgNode.class.isAssignableFrom(c)) {
					node = (E) c.getConstructor(Vertex.class).newInstance(vertex);
				} else {
					Object value = vertex.value(getPersistentName());
					node = (E) value;
                    putToInternalMap(value, vertex);
				}
				this.internalCollection.add(node);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

}

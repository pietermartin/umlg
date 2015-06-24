package org.umlg.runtime.collection.persistent;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.memory.UmlgMemoryOrderedSet;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibOrderedSet;
import org.umlg.runtime.domain.UmlgMetaNode;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Method;
import java.util.*;

public class UmlgOrderedSetClosableIterableImpl<E> extends BaseCollection<E> implements UmlgOrderedSet<E> {

	private Iterator<Vertex> iterator;
	protected OclStdLibOrderedSet<E> oclStdLibOrderedSet;

	@SuppressWarnings("unchecked")
	public UmlgOrderedSetClosableIterableImpl(Iterator<Vertex> iterator, UmlgRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.internalCollection = new ListOrderedSet();
		this.iterator = iterator;
	}
	
	protected ListOrderedSet getInternalListOrderedSet() {
		return (ListOrderedSet) this.internalCollection;
	}

    @Override
    protected void addToLinkedList(Edge edge) {
    }

	@Override
	protected Iterator<Edge> getEdges() {
		return null;
	}

	@Override
	public boolean add(E e) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	public boolean remove(Object o) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	public void clear() {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if (!this.loaded) {
			loadFromVertex();
		}
		return (E) this.getInternalListOrderedSet().get(index);
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
	public E remove(int index) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	public int indexOf(Object o) {
		maybeLoad();
		return this.getInternalListOrderedSet().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new RuntimeException("Not supported");
	}
	
	@Override
	public <R> UmlgSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibOrderedSet.collectNested(v);
	}

	@Override
	public <T, R> UmlgSequence<T> collect(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibOrderedSet.collect(v);
	}
	
	@Override
	public <T2> UmlgSequence<T2> flatten() {
		maybeLoad();
		return this.oclStdLibOrderedSet.flatten();
	}

	@Override
	public UmlgOrderedSet<E> select(BooleanExpressionEvaluator<E> v) {
		maybeLoad();
		return this.oclStdLibOrderedSet.select(v);
	}


	@Override
	public UmlgOrderedSet<E> append(E e) {
		maybeLoad();
		return this.oclStdLibOrderedSet.append(e);
	}

	@Override
	public UmlgOrderedSet<E> prepend(E e) {
		maybeLoad();
		return this.oclStdLibOrderedSet.prepend(e);
	}

	@Override
	public UmlgOrderedSet<E> insertAt(Integer index, E e) {
		maybeLoad();
		return this.oclStdLibOrderedSet.insertAt(index, e);
	}

	@Override
	public UmlgOrderedSet<E> subOrderedSet(Integer lower, Integer upper) {
		maybeLoad();
		return this.oclStdLibOrderedSet.subOrderedSet(lower, upper);
	}

	@Override
	public E at(Integer i) {
		maybeLoad();
		return this.oclStdLibOrderedSet.at(i);
	}

	@Override
	public E first() {
		maybeLoad();
		return this.oclStdLibOrderedSet.first();
	}

	@Override
	public E last() {
		maybeLoad();
		return this.oclStdLibOrderedSet.last();
	}

	@Override
	public UmlgOrderedSet<E> reverse() {
		maybeLoad();
		return this.oclStdLibOrderedSet.reverse();
	}

	@Override
	public UmlgOrderedSet<E> including(E e) {
		maybeLoad();
		return this.oclStdLibOrderedSet.including(e);
	}
	
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void loadFromVertex() {
        for (Iterator<Vertex> iter = this.iterator; iter.hasNext(); ) {
            Vertex vertex = iter.next();
            E node;
            try {
                Class<?> c = Class.forName((String) vertex.value("className"));
                if (c.isEnum()) {
					throw new RuntimeException();
//                    Object value = vertex.value(getPersistentName());
//                    node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
//                    putToInternalMap(node, vertex);
                } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
                    Method m = c.getDeclaredMethod("getInstance", new Class[0]);
                    node = (E) m.invoke(null);
                } else if (UmlgNode.class.isAssignableFrom(c)) {
                    node = (E) c.getConstructor(Vertex.class).newInstance(vertex);
                } else {
					throw new RuntimeException();
//                    Object value = vertex.value(getPersistentName());
//                    node = (E) value;
//                    putToInternalMap(value, vertex);
                }
                this.internalCollection.add(node);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

	@Override
	public UmlgOrderedSet<E> sortedBy(Comparator comparator) {
		maybeLoad();
		ArrayList<E> list = new ArrayList<>(this.internalCollection);
		Collections.sort(list, comparator);
		UmlgOrderedSet<E> result = new UmlgMemoryOrderedSet<>(list);
		return result;
	}
}

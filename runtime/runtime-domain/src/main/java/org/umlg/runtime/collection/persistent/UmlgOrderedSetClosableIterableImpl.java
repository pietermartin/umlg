package org.umlg.runtime.collection.persistent;

import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Vertex;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibOrderedSet;
import org.umlg.runtime.domain.UmlgMetaNode;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class UmlgOrderedSetClosableIterableImpl<E> extends BaseCollection<E> implements UmlgOrderedSet<E> {

	private Iterator<Edge> iterator;
	protected OclStdLibOrderedSet<E> oclStdLibOrderedSet;

	@SuppressWarnings("unchecked")
	public UmlgOrderedSetClosableIterableImpl(Iterator<Edge> iterator, UmlgRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.internalCollection = new ListOrderedSet();
		this.iterator = iterator;
	}
	
	protected ListOrderedSet getInternalListOrderedSet() {
		return (ListOrderedSet) this.internalCollection;
	}

    @Override
    protected void addToLinkedList(Edge edge) {
        //Get the new vertex for the element
        Vertex newElementVertex = getVertexForDirection(edge);
        if (this.vertex.outE(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel()).hasNext()) {
            Edge edgeToLastVertex = this.vertex.outE(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel()).next();
            Vertex lastVertex = edgeToLastVertex.inV().next();

            //move the edge to the last vertex
            edgeToLastVertex.remove();
            this.vertex.addEdge(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel(), newElementVertex);

            //add the element to the linked list
            lastVertex.addEdge(LABEL_TO_NEXT_IN_SEQUENCE, newElementVertex);
        } else {
            //its the first element in the list
            this.vertex.addEdge(LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel(), newElementVertex);
            this.vertex.addEdge(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel(), newElementVertex);
        }
    }

	@Override
	protected Iterator<Edge> getEdges() {
		return this.iterator;
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
        for (Iterator<Edge> iter = getEdges(); iter.hasNext(); ) {
            Edge edge = iter.next();
            E node;
            try {
                Class<?> c = this.getClassToInstantiate(edge);
                if (c.isEnum()) {
                    Object value = this.getVertexForDirection(edge).value(getPersistentName());
                    node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                    putToInternalMap(node, this.getVertexForDirection(edge));
                } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
                    Method m = c.getDeclaredMethod("getInstance", new Class[0]);
                    node = (E) m.invoke(null);
                } else if (UmlgNode.class.isAssignableFrom(c)) {
                    node = (E) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
                } else {
                    Object value = this.getVertexForDirection(edge).value(getPersistentName());
                    node = (E) value;
                    putToInternalMap(value, this.getVertexForDirection(edge));
                }
                this.internalCollection.add(node);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}

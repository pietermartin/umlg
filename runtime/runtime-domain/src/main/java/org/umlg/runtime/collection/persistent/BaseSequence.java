package org.umlg.runtime.collection.persistent;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibSequence;
import org.umlg.runtime.collection.ocl.OclStdLibSequenceImpl;
import org.umlg.runtime.domain.UmlgNode;

import java.util.*;

public abstract class BaseSequence<E> extends BaseCollection<E> implements UmlgSequence<E> {

    protected OclStdLibSequence<E> oclStdLibSequence;

    public BaseSequence(UmlgRuntimeProperty runtimeProperty) {
        super(runtimeProperty);
        this.internalCollection = new ArrayList<E>();
        this.oclStdLibSequence = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
        this.oclStdLibCollection = this.oclStdLibSequence;
    }

    public BaseSequence(UmlgNode owner, PropertyTree propertyTree) {
        super(owner, propertyTree);
        this.internalCollection = new ArrayList<E>();
        this.oclStdLibSequence = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
        this.oclStdLibCollection = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
    }

    public BaseSequence(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
        this.internalCollection = new ArrayList<E>();
        this.oclStdLibSequence = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
        this.oclStdLibCollection = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
    }

    protected List<E> getInternalList() {
        return (List<E>) this.internalCollection;
    }

    //By now the element is already added to the internal list
    @Override
    protected void addToLinkedList(Edge edge) {
        //set the sequence number on the edge.
        //as the vertex is added to the end of the list it equals the size
        edge.property(this.isControllingSide() ? IN_EDGE_SEQUENCE_ID : OUT_EDGE_SEQUENCE_ID, (double) this.oclStdLibSequence.size());
    }

    @Override
    protected void addToInverseLinkedList(Edge edge) {
        if (!this.isControllingSide()) {
            edge.property(BaseCollection.IN_EDGE_SEQUENCE_ID, (double) this.inverseCollectionSize);
        } else {
            edge.property(BaseCollection.OUT_EDGE_SEQUENCE_ID, (double) this.inverseCollectionSize);
        }
    }

    //The list is loaded by the time this is called
    @SuppressWarnings("unchecked")
    protected Edge addToListAtIndex(int indexOf, E e) {
        //Create the edge to the new element
        Edge edgeFromParentToElementVertex = addInternal(e);
        manageLinkedList(indexOf, edgeFromParentToElementVertex);
        ((UmlgNode) e).setEdge(this.umlgRuntimeProperty, edgeFromParentToElementVertex);
        return edgeFromParentToElementVertex;
    }

    //The element is not yet in the internal list
    private void manageLinkedList(int indexOfNewElement, Edge edgeFromParentToElementVertex) {
        E current = this.getInternalList().get(indexOfNewElement);
        Double currentValue;
        //Take anyone, with bags there may be more than one
        Edge currentEdge = ((UmlgNode) current).getEdge(this.umlgRuntimeProperty);
        //currentEdge can be null when the element is added via the indexed adders, i.e. x.addToY(0, y);
        if (currentEdge == null) {
            E next = this.getInternalList().get(indexOfNewElement + 1);
            currentEdge = ((UmlgNode) next).getEdge(this.umlgRuntimeProperty);
            currentValue = currentEdge.<Double>value(this.isControllingSide() ? IN_EDGE_SEQUENCE_ID : OUT_EDGE_SEQUENCE_ID);
        } else {
            currentValue = currentEdge.<Double>value(this.isControllingSide() ? IN_EDGE_SEQUENCE_ID : OUT_EDGE_SEQUENCE_ID);
        }

        if (indexOfNewElement > 0) {
            E previous = this.getInternalList().get(indexOfNewElement - 1);
            Set<Edge> previousEdges = UMLG.get().getEdgesBetween(this.vertex, ((UmlgNode) previous).getVertex(), getLabel());
            if (previousEdges.isEmpty()) {
                throw new IllegalStateException();
            }
            Edge previousEdge = previousEdges.stream().findFirst().get();
            Double previousValue = previousEdge.<Double>value(this.isControllingSide() ? IN_EDGE_SEQUENCE_ID : OUT_EDGE_SEQUENCE_ID);
            edgeFromParentToElementVertex.property(this.isControllingSide() ? IN_EDGE_SEQUENCE_ID : OUT_EDGE_SEQUENCE_ID, (currentValue + previousValue) / 2);
        } else {
            edgeFromParentToElementVertex.property(this.isControllingSide() ? IN_EDGE_SEQUENCE_ID : OUT_EDGE_SEQUENCE_ID, currentValue - 0.1);
        }
    }

    @Override
    public E get(int index) {
        //TODO this does not need to load the sequence, use the index instead, probably need to create another index to store the index to tinkerIndex relationship
        if (!this.loaded) {
            loadFromVertex();
        }
        return this.getInternalList().get(index);
    }

    @Override
    public E remove(int index) {
        E e = this.get(index);
        this.remove(e);
        return e;
    }

    @Override
    public void clear() {
        maybeLoad();
        for (E e : new ArrayList<E>(this.getInternalList())) {
            this.remove(e);
        }
    }

    @Override
    public int indexOf(Object o) {
        maybeLoad();
        return this.getInternalList().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        maybeLoad();
        return this.getInternalList().lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        maybeLoad();
        return this.getInternalList().listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        maybeLoad();
        return this.getInternalList().listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        maybeLoad();
        return this.getInternalList().subList(fromIndex, toIndex);
    }

    @Override
    public <R> UmlgSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
        maybeLoad();
        return this.oclStdLibSequence.collectNested(v);
    }

    @Override
    public <T, R> UmlgSequence<T> collect(BodyExpressionEvaluator<R, E> v) {
        maybeLoad();
        return this.oclStdLibSequence.collect(v);
    }

    @Override
    public <R> UmlgSequence<R> flatten() {
        maybeLoad();
        return this.oclStdLibSequence.flatten();
    }

    @Override
    public UmlgSequence<E> select(BooleanExpressionEvaluator<E> v) {
        maybeLoad();
        return this.oclStdLibSequence.select(v);
    }

    @Override
    public E first() {
        maybeLoad();
        return oclStdLibSequence.first();
    }

    @Override
    public Boolean equals(UmlgSequence<E> s) {
        maybeLoad();
        return this.oclStdLibSequence.equals(s);
    }

    @Override
    public UmlgSequence<E> union(UmlgSequence<? extends E> s) {
        maybeLoad();
        return this.oclStdLibSequence.union(s);
    }

    @Override
    public UmlgSequence<E> append(E object) {
        maybeLoad();
        return this.oclStdLibSequence.append(object);
    }

    @Override
    public UmlgSequence<E> prepend(E object) {
        maybeLoad();
        return this.oclStdLibSequence.prepend(object);
    }

    @Override
    public UmlgSequence<E> insertAt(Integer index, E object) {
        maybeLoad();
        return this.oclStdLibSequence.insertAt(index, object);
    }

    @Override
    public UmlgSequence<E> subSequence(Integer lower, Integer upper) {
        maybeLoad();
        return this.oclStdLibSequence.subSequence(lower, upper);
    }

    @Override
    public E at(Integer i) {
        maybeLoad();
        return this.oclStdLibSequence.at(i);
    }

    @Override
    public E last() {
        maybeLoad();
        return this.oclStdLibSequence.last();
    }

    @Override
    public UmlgSequence<E> including(E object) {
        maybeLoad();
        return this.oclStdLibSequence.including(object);
    }

    @Override
    public UmlgSequence<E> excluding(E object) {
        maybeLoad();
        return this.oclStdLibSequence.excluding(object);
    }

    @Override
    public UmlgSequence<E> reverse() {
        maybeLoad();
        return this.oclStdLibSequence.reverse();
    }

    @Override
    public UmlgSequence<E> sortedBy(Comparator<E> comparator) {
        maybeLoad();
        return this.oclStdLibSequence.sortedBy(comparator);
    }
}

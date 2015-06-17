package org.umlg.runtime.collection.persistent;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibSequence;
import org.umlg.runtime.collection.ocl.OclStdLibSequenceImpl;
import org.umlg.runtime.domain.UmlgMetaNode;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseSequence<E> extends BaseCollection<E> implements UmlgSequence<E> {

    protected OclStdLibSequence<E> oclStdLibSequence;

    public BaseSequence(UmlgRuntimeProperty runtimeProperty) {
        super(runtimeProperty);
        this.internalCollection = new ArrayList<E>();
        this.oclStdLibSequence = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
        this.oclStdLibCollection = this.oclStdLibSequence;
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
            edge.property(BaseCollection.IN_EDGE_SEQUENCE_ID, this.inverseCollectionSize);
        } else {
            edge.property(BaseCollection.OUT_EDGE_SEQUENCE_ID, this.inverseCollectionSize);
        }
    }

    @Override
    protected void loadManyNotPrimitiveNotDataType() {
        GraphTraversal<Vertex, Map<String, Element>> traversal = getVerticesXX();
        while (traversal.hasNext()) {
            final Map<String, Element> bindings = traversal.next();
            Edge edge = (Edge) bindings.get("edge");
            Vertex vertex = (Vertex) bindings.get("vertex");
            E node;
            try {
                Class<?> c = getClassToInstantiate(vertex);
                if (c.isEnum()) {
                    Object value = vertex.value(getPersistentName());
                    node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                    putToInternalMap(node, vertex);
                } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
                    Method m = c.getDeclaredMethod("getInstance", new Class[0]);
                    node = (E) m.invoke(null);
                } else if (UmlgNode.class.isAssignableFrom(c)) {
                    node = (E) c.getConstructor(Vertex.class).newInstance(vertex);
                    ((UmlgNode)node).setEdge(edge);
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
//        for (Iterator<Vertex> iter = getVerticesXX(); iter.hasNext(); ) {
//            Vertex vertex = iter.next();
//            E node;
//            try {
//                Class<?> c = getClassToInstantiate(vertex);
//                if (c.isEnum()) {
//                    Object value = vertex.value(getPersistentName());
//                    node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
//                    putToInternalMap(node, vertex);
//                } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
//                    Method m = c.getDeclaredMethod("getInstance", new Class[0]);
//                    node = (E) m.invoke(null);
//                } else if (UmlgNode.class.isAssignableFrom(c)) {
//                    node = (E) c.getConstructor(Vertex.class).newInstance(vertex);
//                } else {
//                    Object value = vertex.value(getPersistentName());
//                    node = (E) value;
//                    putToInternalMap(value, vertex);
//                }
//                this.internalCollection.add(node);
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        }
    }

    //    @Override
    protected GraphTraversal<Vertex, Map<String, Element>> getVerticesXX() {
        if (this.isControllingSide()) {
            //TODO gremlin/sqlg optimization needed, this is super inefficient now
            return UMLG.get().getUnderlyingGraph().traversal().V(this.vertex)
                    .outE(this.getLabel())
                    .as("edge")
                    .order().by(BaseCollection.IN_EDGE_SEQUENCE_ID, Order.incr)
                    .inV()
                    .as("vertex")
                    .select();
        } else {
            return UMLG.get().getUnderlyingGraph().traversal().V(this.vertex)
                    .inE(this.getLabel())
                    .as("edge")
                    .order().by(BaseCollection.OUT_EDGE_SEQUENCE_ID, Order.incr)
                    .outV()
                    .as("vertex")
                    .select();
        }
    }

    //The list is loaded by the time this is called
    @SuppressWarnings("unchecked")
    protected Edge addToListAtIndex(int indexOf, E e) {
        //Create the edge to the new element
        Edge edgeFromParentToElementVertex = addInternal(e);
        manageLinkedList(indexOf, edgeFromParentToElementVertex);
        return edgeFromParentToElementVertex;
    }

    //The element is not yet in the internal list
    private void manageLinkedList(int indexOfNewElement, Edge edgeFromParentToElementVertex) {
        E current = this.getInternalList().get(indexOfNewElement);
        //Take anyone, with bags there may be more than one
        Edge currentEdge = ((UmlgNode)current).getEdge();
        Double currentValue = currentEdge.<Double>value(this.isControllingSide() ? IN_EDGE_SEQUENCE_ID : OUT_EDGE_SEQUENCE_ID);

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

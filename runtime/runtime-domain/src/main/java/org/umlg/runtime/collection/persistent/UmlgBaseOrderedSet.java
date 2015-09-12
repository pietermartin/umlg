package org.umlg.runtime.collection.persistent;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.memory.UmlgMemoryOrderedSet;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibOrderedSet;
import org.umlg.runtime.collection.ocl.OclStdLibOrderedSetImpl;
import org.umlg.runtime.domain.UmlgNode;

import java.util.*;

/**
 * Date: 2013/03/02
 * Time: 9:18 PM
 */
public abstract class UmlgBaseOrderedSet<E> extends BaseCollection<E> implements UmlgOrderedSet<E> {

    protected OclStdLibOrderedSet<E> oclStdLibOrderedSet;

    @SuppressWarnings("unchecked")
    public UmlgBaseOrderedSet(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
        this.internalCollection = new ListOrderedSet<>();
        this.oclStdLibOrderedSet = new OclStdLibOrderedSetImpl<>((ListOrderedSet) this.internalCollection);
        this.oclStdLibCollection = this.oclStdLibOrderedSet;
    }

    protected ListOrderedSet getInternalListOrderedSet() {
        return (ListOrderedSet) this.internalCollection;
    }

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
        Set<Edge> currentEdges = UMLG.get().getEdgesBetween(this.vertex, ((UmlgNode) current).getVertex(), getLabel());
        if (currentEdges.isEmpty()) {
            throw new IllegalStateException();
        }
        Edge currentEdge = currentEdges.stream().findFirst().get();
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

    protected ListOrderedSet<E> getInternalList() {
        return (ListOrderedSet<E>) this.internalCollection;
    }

    //By now the element is already added to the internal list
    @Override
    protected void addToLinkedList(Edge edge) {
        //set the sequence number on the edge.
        //as the vertex is added to the end of the list it equals the size
        edge.property(this.isControllingSide() ? IN_EDGE_SEQUENCE_ID : OUT_EDGE_SEQUENCE_ID, (double) this.oclStdLibOrderedSet.size());
    }

    @Override
    protected void addToInverseLinkedList(Edge edge) {
        if (!this.isControllingSide()) {
            edge.property(BaseCollection.IN_EDGE_SEQUENCE_ID, (double) this.inverseCollectionSize);
        } else {
            edge.property(BaseCollection.OUT_EDGE_SEQUENCE_ID, (double) this.inverseCollectionSize);
        }
    }

    @Override
    protected Iterator<Vertex> getVertices() {
        if (this.isControllingSide()) {
            return UMLG.get().getUnderlyingGraph().traversal().V(this.vertex)
                    .outE(this.getLabel()).as("e")
                    .inV().as("v")
                    .select("e", "v")
                    .order().by(__.select("e").by(BaseCollection.IN_EDGE_SEQUENCE_ID), Order.incr)
                    .map(m -> (Vertex)m.get().get("v"));
        } else {
            return UMLG.get().getUnderlyingGraph().traversal().V(this.vertex)
                    .inE(this.getLabel()).as("e")
                    .outV().as("v")
                    .select("e", "v")
                    .order().by(__.select("e").by(BaseCollection.OUT_EDGE_SEQUENCE_ID), Order.incr)
                    .map(m -> (Vertex)m.get().get("v"));
        }
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
    public E remove(int index) {
        E e = this.get(index);
        this.remove(e);
        return e;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        maybeLoad();
        for (E e : new HashSet<E>(this.getInternalListOrderedSet())) {
            this.remove(e);
        }
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

//    private Vertex getVertexFromElement(E previous, E e) {
//        Vertex previousVertex;
//        if (previous instanceof UmlgNode) {
//            UmlgNode node = (UmlgNode) previous;
//            previousVertex = node.getVertex();
//        } else if (e.getClass().isEnum()) {
//            List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + previous.toString());
//            Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
//            previousVertex = vertexes.get(0);
//        } else if (getDataTypeEnum() != null) {
//            List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + e.toString());
//            Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
//            previousVertex = vertexes.get(0);
//        } else {
//            List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + previous.toString());
//            Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
//            previousVertex = vertexes.get(0);
//        }
//        return previousVertex;
//    }

//    protected void loadNode(Edge edgeToElement, Vertex vertex) {
//        E node;
//        try {
//            Class<?> c = Class.forName(vertex.value("className"));
//            if (c.isEnum()) {
//                Object value = vertex.value(getPersistentName());
//                node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
//                putToInternalMap(node, vertex);
//                this.getInternalListOrderedSet().add(node);
//            } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
//                Method m = c.getDeclaredMethod("getInstance", new Class[0]);
//                node = (E) m.invoke(null);
//                this.getInternalListOrderedSet().add(node);
//            } else if (UmlgNode.class.isAssignableFrom(c)) {
//                node = (E) c.getConstructor(Vertex.class).newInstance(vertex);
//                this.getInternalListOrderedSet().add(node);
//            } else if (getDataTypeEnum() != null) {
//                loadDataTypeFromVertex(vertex);
//            } else {
//                Object value = vertex.value(getPersistentName());
//                node = (E) value;
//                putToInternalMap(node, vertex);
//                this.getInternalListOrderedSet().add(node);
//            }
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//    }

    @Override
    public UmlgOrderedSet<E> sortedBy(Comparator comparator) {
        maybeLoad();
        ArrayList<E> list = new ArrayList<>(this.internalCollection);
        Collections.sort(list, comparator);
        UmlgOrderedSet<E> result = new UmlgMemoryOrderedSet<>(list);
        return result;
    }
}

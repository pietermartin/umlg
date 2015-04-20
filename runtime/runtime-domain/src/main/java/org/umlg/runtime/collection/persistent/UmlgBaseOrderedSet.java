package org.umlg.runtime.collection.persistent;

import com.google.common.base.Preconditions;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.tinkerpop.gremlin.structure.Direction;
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
import org.umlg.runtime.domain.UmlgMetaNode;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Method;
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
        Direction inverseDirection;
        Direction direction;
        if (isControllingSide()) {
            inverseDirection = Direction.IN;
            direction = Direction.OUT;
        } else {
            inverseDirection = Direction.OUT;
            direction = Direction.IN;
        }

        //If it is the last element then the edge (LABEL_TO_LAST_ELEMENT_IN_SEQUENCE) needs to be moved
        this.getInternalListOrderedSet().add(indexOf, e);
        //Add the regular edge for the label
        Edge edge = addInternal(e);
        //If it is the first element then the edge (LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE) needs to be moved
        if (indexOf == 0) {
            if (this.vertex.edges(Direction.OUT, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex)).hasNext()) {
                Edge edgeToFirstElement = this.vertex.edges(Direction.OUT, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex)).next();
                edgeToFirstElement.remove();
            }
            this.vertex.addEdge(LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex), vertexForDirection(edge, inverseDirection));
        }
        if (indexOf == size() - 1) {
            if (this.vertex.edges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex)).hasNext()) {
                Edge edgeToLastElement = this.vertex.edges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex)).next();
                edgeToLastElement.remove();
            }
            this.vertex.addEdge(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex), vertexForDirection(edge, inverseDirection));
        }
        //Shift the linked list
        //Find the element at the index
        if (size() > 1) {
            if (indexOf == 0) {
                //add a edge to the previous first element
                E previous = (E) this.getInternalListOrderedSet().get(1);
                Vertex previousVertex = getVertexFromElement(previous, e);
                vertexForDirection(edge, inverseDirection).addEdge(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex), previousVertex);
            } else {
                E previous = (E) this.getInternalListOrderedSet().get(indexOf - 1);
                Vertex previousVertex = getVertexFromElement(previous, e);
                //size already includes the current added element, so if the size is 2 it means that there was only one in the list, i.e. no edge (LABEL_TO_NEXT_IN_SEQUENCE)
                if (size() > 2 && indexOf + 1 < size()) {
                    Edge edgeToNextElement = previousVertex.edges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex)).next();
                    Vertex shiftedVertex = vertexForDirection(edgeToNextElement, inverseDirection);
                    edgeToNextElement.remove();
                    vertexForDirection(edge, inverseDirection).addEdge(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex), shiftedVertex);
                }
                previousVertex.addEdge(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex), vertexForDirection(edge, inverseDirection));
            }
        }
        return edge;
    }

    @Override
    protected void addToLinkedList(Edge edge) {
        Direction direction;
        if (isControllingSide()) {
            direction = Direction.OUT;
        } else {
            direction = Direction.IN;
        }
        //Get the new vertex for the element
        Vertex newElementVertex = getVertexForDirection(edge);
        if (this.vertex.edges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex)).hasNext()) {
            Edge edgeToLastVertex = this.vertex.edges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex)).next();
            Vertex lastVertex = edgeToLastVertex.vertices(Direction.IN).next();

            //move the edge to the last vertex
            edgeToLastVertex.remove();
            Edge edgeToLast = this.vertex.addEdge(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex), newElementVertex);
            //add the element to the linked list
            //last here is the previous last
            Edge edgeToNext = lastVertex.addEdge(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex), newElementVertex);
            System.out.println("");
        } else {
            //its the first element in the list
            Edge edgeToFirst = this.vertex.addEdge(LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex), newElementVertex);
            Edge edgeToLast = this.vertex.addEdge(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex), newElementVertex);
            System.out.println("");
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void loadFromVertex() {
        Direction direction;
        Direction inverseDirection;
        if (isControllingSide()) {
            direction = Direction.OUT;
            inverseDirection = Direction.IN;
        } else {
            direction = Direction.IN;
            inverseDirection = Direction.OUT;
        }
        if (getUpper() == 1) {
            Iterator<Edge> edgeIterator = edgesForDirection(this.vertex, direction, getLabel());
            if (!edgeIterator.hasNext()) {
                this.loaded = true;
            } else {
                Edge edgeToElement = edgeIterator.next();
                if (!UMLG.get().hasEdgeBeenDeleted(edgeToElement)) {
                    Vertex element = vertexForDirection(edgeToElement, inverseDirection);
                    loadNode(edgeToElement, element);
                    this.loaded = true;
                } else {
                    this.loaded = true;
                }
            }
        } else {
            Iterator<Edge> edgeIterator = this.vertex.edges(Direction.OUT, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex));
            if (!edgeIterator.hasNext()) {
                this.loaded = true;
            } else {
                Edge edgeToFirstElement = edgeIterator.next();
                if (!UMLG.get().hasEdgeBeenDeleted(edgeToFirstElement)) {
                    Vertex firstVertexInSequence =  edgeToFirstElement.vertices(Direction.IN).next();
                    loadNode(edgeToFirstElement, firstVertexInSequence);
                    Vertex elementVertex = firstVertexInSequence;
                    while (elementVertex.edges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex)).hasNext()) {
                        Edge edgeToNext = elementVertex.edges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.getIdForLabel(this.vertex)).next();
                        if (!UMLG.get().hasEdgeBeenDeleted(edgeToNext)) {
                            elementVertex = edgeToNext.vertices(Direction.IN).next();
                            loadNode(edgeToNext, elementVertex);
                        }
                    }
                    this.loaded = true;
                } else {
                    this.loaded = true;
                }
            }
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

    private Vertex getVertexFromElement(E previous, E e) {
        Vertex previousVertex;
        if (previous instanceof UmlgNode) {
            UmlgNode node = (UmlgNode) previous;
            previousVertex = node.getVertex();
        } else if (e.getClass().isEnum()) {
            List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + previous.toString());
            Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
            previousVertex = vertexes.get(0);
        } else if (getDataTypeEnum() != null) {
            List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + e.toString());
            Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
            previousVertex = vertexes.get(0);
        } else {
            List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + previous.toString());
            Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
            previousVertex = vertexes.get(0);
        }
        return previousVertex;
    }

    protected void loadNode(Edge edgeToElement, Vertex vertex) {
        E node;
        try {
            Class<?> c = Class.forName(vertex.value("className"));
            if (c.isEnum()) {
                Object value = vertex.value(getPersistentName());
                node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                putToInternalMap(node, vertex);
                this.getInternalListOrderedSet().add(node);
            } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
                Method m = c.getDeclaredMethod("getInstance", new Class[0]);
                node = (E) m.invoke(null);
                this.getInternalListOrderedSet().add(node);
            } else if (UmlgNode.class.isAssignableFrom(c)) {
                node = (E) c.getConstructor(Vertex.class).newInstance(vertex);
                this.getInternalListOrderedSet().add(node);
            } else if (getDataTypeEnum() != null) {
                loadDataTypeFromVertex(vertex);
            } else {
                Object value = vertex.value(getPersistentName());
                node = (E) value;
                putToInternalMap(node, vertex);
                this.getInternalListOrderedSet().add(node);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
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

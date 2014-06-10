package org.umlg.runtime.collection.persistent;

import com.google.common.base.Preconditions;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
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
        Direction direction;
        Direction inverseDirection;
        if (isControllingSide()) {
            direction = Direction.OUT;
            inverseDirection = Direction.IN;
        } else {
            direction = Direction.IN;
            inverseDirection = Direction.OUT;
        }

        //Get the new vertex for the element
        Vertex newElementVertex = getVertexForDirection(edge);
        //Get the last hyperVertex
        //This is size - 2 as the hyperVertexIndex does not yet contain the entry for the new element and it is zero based
        Vertex lastHyperVertex;
        if (size() > 1) {
            Edge edgeToLastHyperVertex = this.vertex.getEdges(Direction.OUT, LABEL_TO_LAST_HYPER_VERTEX + getLabel() + direction).iterator().next();
            lastHyperVertex = edgeToLastHyperVertex.getVertex(Direction.IN);
            Vertex previousVertex = getVertexFromElement(this.getInternalList().get(size() - 2));
            //Check is duplicate
            if (previousVertex.equals(newElementVertex)) {
                //Add to the hyper vertex
                UMLG.get().addEdge(null, lastHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
            } else {
                //Create a new hyper vertex
                Vertex newHyperVertex = UMLG.get().addVertex("hyperVertex");
                UMLG.get().addEdge(null, newHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
                //Put the new hyper vertex in the linked list
                UMLG.get().addEdge(null, lastHyperVertex, newHyperVertex, LABEL_TO_NEXT_HYPER_VERTEX + direction);
                //remove the lastHyperVertex edge to parent, add it to new last hyper vertex
                UMLG.get().removeEdge(edgeToLastHyperVertex);
                UMLG.get().addEdge(null, this.vertex, newHyperVertex, LABEL_TO_LAST_HYPER_VERTEX + getLabel() + direction);
            }
        } else {
            //Is the first element being added, create the hyper vertex
            Vertex newHyperVertex = UMLG.get().addVertex("hyperVertex");
            UMLG.get().addEdge(null, newHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
            //Put the new hyper vertex in the linked list
            UMLG.get().addEdge(null, this.vertex, newHyperVertex, LABEL_TO_FIRST_HYPER_VERTEX + getLabel() + direction);
            //remove the lastHyperVertex edge to parent, add it to new last hyper vertex
            UMLG.get().addEdge(null, this.vertex, newHyperVertex, LABEL_TO_LAST_HYPER_VERTEX + getLabel() + direction);
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
        //If the multiplicity is a one don't bother with the linked list jol
        if (getUpper() == 1) {
            Iterable<Edge> edgeIterable = this.vertex.getEdges(direction, getLabel());
            if (!edgeIterable.iterator().hasNext()) {
                this.loaded = true;
            } else {
                Edge edgeToElement = edgeIterable.iterator().next();
                if (!UMLG.get().hasEdgeBeenDeleted(edgeToElement)) {
                    Vertex elementVertex = edgeToElement.getVertex(inverseDirection);
                    loadNode(edgeToElement, elementVertex, false);
                    this.loaded = true;
                } else {
                    this.loaded = true;
                }
            }
        } else {
            //Load via hyper vertex
            if (this.vertex.getEdges(Direction.OUT, LABEL_TO_FIRST_HYPER_VERTEX + getLabel() + direction).iterator().hasNext()) {
                Edge edgeToFirstHyperVertex = this.vertex.getEdges(Direction.OUT, LABEL_TO_FIRST_HYPER_VERTEX + getLabel() + direction).iterator().next();
                Vertex hyperVertex = edgeToFirstHyperVertex.getVertex(Direction.IN);
                int toIndex = loadFromHyperVertex(hyperVertex, 0);
                while (hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX + direction).iterator().hasNext()) {
                    Edge edgeToNextHyperVertex = hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX + direction).iterator().next();
                    hyperVertex = edgeToNextHyperVertex.getVertex(Direction.IN);
                    toIndex = loadFromHyperVertex(hyperVertex, toIndex);
                }
                this.loaded = true;
            } else {
                this.loaded = true;
            }
        }
    }

    private int loadFromHyperVertex(Vertex hyperVertex, int fromIndex) {
        int toIndex = fromIndex;
        for (Edge edgeToElement : hyperVertex.getEdges(Direction.OUT, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX)) {
            Vertex vertexToLoad = edgeToElement.getVertex(Direction.IN);
            loadNode(edgeToElement, vertexToLoad, true);
        }
        return toIndex;
    }

    protected void loadNode(Edge edgeToElement, Vertex vertexToLoad, boolean hyperVertexEdge) {
        E node;
        try {
            Class<?> c;
            if (hyperVertexEdge) {
                c = this.getClassToInstantiateFromHyperVertexEdge(edgeToElement);
                //This is a debug check
                //TODO optimize
                Vertex debugVertex = edgeToElement.getVertex(Direction.IN);
                if (!debugVertex.equals(vertexToLoad)) {
                    throw new IllegalStateException("Vertexes should be the same, what is going on?");
                }
            } else {
                c = this.getClassToInstantiate(edgeToElement);
            }
            if (c.isEnum()) {
                Object value = vertexToLoad.getProperty(getPersistentName());
                node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                putToInternalMap(node, vertexToLoad);
                this.getInternalList().add(node);
            } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
                Method m = c.getDeclaredMethod("getInstance", new Class[0]);
                node = (E) m.invoke(null);
                this.getInternalList().add(node);
            } else if (UmlgNode.class.isAssignableFrom(c)) {
                node = (E) c.getConstructor(Vertex.class).newInstance(vertexToLoad);
                this.getInternalList().add(node);
            } else if (getDataTypeEnum() != null) {
                loadDataTypeFromVertex(vertexToLoad);
            } else {
                Object value = vertexToLoad.getProperty(getPersistentName());
                node = (E) value;
                putToInternalMap(value, vertexToLoad);
                this.getInternalList().add(node);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
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

    private class Triad {
        Vertex hyperVertex;
        Vertex elementVertex;
        Integer count;

        public Triad(Vertex hyperVertex, Vertex elementVertex, Integer count) {
            this.hyperVertex = hyperVertex;
            this.elementVertex = elementVertex;
            this.count = count;
        }
    }

    //The element is not yet in the internal list
    private void manageLinkedList(int indexOfNewElement, Edge edgeFromParentToElementVertex) {

        Direction direction;
        Direction inverseDirection;
        if (isControllingSide()) {
            direction = Direction.OUT;
            inverseDirection = Direction.IN;
        } else {
            direction = Direction.IN;
            inverseDirection = Direction.OUT;
        }

        //Get the new vertex for the element
        Vertex newElementVertex = getVertexForDirection(edgeFromParentToElementVertex);

        if (this.vertex.getEdges(Direction.OUT, LABEL_TO_FIRST_HYPER_VERTEX + getLabel() + direction).iterator().hasNext()) {
            Edge edgeToFirstHyperVertex = this.vertex.getEdges(Direction.OUT, LABEL_TO_FIRST_HYPER_VERTEX + getLabel() + direction).iterator().next();
            Vertex firstHyperVertex = edgeToFirstHyperVertex.getVertex(Direction.IN);
            Triad hyperVertexAtIndexPair = getHyperVertexForIndex(indexOfNewElement, firstHyperVertex, 0,  direction);
            if (hyperVertexAtIndexPair.hyperVertex == null) {
                //Element needs to be added to the end
                if (hyperVertexAtIndexPair.count < indexOfNewElement) {
                    throw new IndexOutOfBoundsException(String.format("List size = %d can not add element at index %d", new Object[]{hyperVertexAtIndexPair.count, indexOfNewElement}));
                }
                //Find the last hyper vertex
                Edge edgeToLastHyperVertex = this.vertex.getEdges(Direction.OUT, LABEL_TO_LAST_HYPER_VERTEX + getLabel() + direction).iterator().next();
                Vertex lastHyperVertex = edgeToLastHyperVertex.getVertex(Direction.IN);
                //Check if the last hyper vertex hold element equal to the one being added

                Edge edgeToElementFromLastHyperVertex = lastHyperVertex.getEdges(Direction.OUT, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).iterator().next();
                Vertex elementAtLastHyperVertex = edgeToElementFromLastHyperVertex.getVertex(Direction.IN);
                if (elementAtLastHyperVertex.equals(newElementVertex)) {
                    //Its a duplicate
                    UMLG.get().addEdge(null, lastHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
                } else {
                    //Create a new last hyper vertex
                    Vertex newLastHyperVertex = UMLG.get().addVertex("hyperVertex");
                    //Remove the current edge to last hyper vertex
                    UMLG.get().removeEdge(edgeToLastHyperVertex);
                    //Create a new edge to last hyper vertex
                    UMLG.get().addEdge(null, this.vertex, newLastHyperVertex, LABEL_TO_LAST_HYPER_VERTEX + getLabel()+ direction);
                    //Link the previous last hyper vertex to the new on
                    UMLG.get().addEdge(null, lastHyperVertex, newLastHyperVertex, LABEL_TO_NEXT_HYPER_VERTEX+ direction);
                    //link the last hyper vertex to the new element
                    UMLG.get().addEdge(null, newLastHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
                }
            } else {
                Vertex hyperVertexAtIndex = hyperVertexAtIndexPair.hyperVertex;
                Vertex currentElementAtIndex = hyperVertexAtIndexPair.elementVertex;

                //Check if the hyperVertexAtIndex holds elements that are equal to the element being added
                if (currentElementAtIndex != null && currentElementAtIndex.equals(newElementVertex)) {
                    //Need to attach the newVertex to the current hyper vertex as its a duplicate
                    UMLG.get().addEdge(null, hyperVertexAtIndex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
                } else {
                    //Element is not a duplicate.
                    //Create a new hyper vertex to attach it to
                    Vertex newHyperVertex = UMLG.get().addVertex("hyperVertex");
                    UMLG.get().addEdge(null, newHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
                    //place the hyper vertex in the linked list
                    if (indexOfNewElement == 0) {
                        //Need to move the edge (LABEL_TO_FIRST_HYPER_VERTEX) to the parent
                        //Remove the label to first
                        Edge edgeFromParent = hyperVertexAtIndex.getEdges(Direction.IN, LABEL_TO_FIRST_HYPER_VERTEX + getLabel()+ direction).iterator().next();
                        UMLG.get().removeEdge(edgeFromParent);
                        //Add the edge (LABEL_TO_FIRST_HYPER_VERTEX) to the new hyper vertex in position 0
                        UMLG.get().addEdge(null, this.vertex, newHyperVertex, LABEL_TO_FIRST_HYPER_VERTEX + getLabel()+ direction);
                        //Link the new hyper vertex to the next one
                        UMLG.get().addEdge(null, newHyperVertex, hyperVertexAtIndex, LABEL_TO_NEXT_HYPER_VERTEX+ direction);
                    } else {
                        //Place the new hyper vertex in the list
                        //Break the link to the previous hyper edge
                        Edge edgeToPreviousHyperVertex = hyperVertexAtIndex.getEdges(Direction.IN, LABEL_TO_NEXT_HYPER_VERTEX+ direction).iterator().next();
                        Vertex previousHyperVertex = edgeToPreviousHyperVertex.getVertex(Direction.OUT);
                        UMLG.get().removeEdge(edgeToPreviousHyperVertex);
                        //Recreate the link from the previous hyper vertex to the new one
                        UMLG.get().addEdge(null, previousHyperVertex, newHyperVertex, LABEL_TO_NEXT_HYPER_VERTEX+ direction);
                        //Add a edge to the next hyper edge from the new one.
                        UMLG.get().addEdge(null, newHyperVertex, hyperVertexAtIndex, LABEL_TO_NEXT_HYPER_VERTEX+ direction);
                    }
                }
            }
        } else {
            //List is empty
            if (indexOfNewElement > 0) {
                throw new IndexOutOfBoundsException("List is empty, can only add an element at index 0");
            }
            addToLinkedList(edgeFromParentToElementVertex);
        }

    }

    private Triad getHyperVertexForIndex(int indexOfNewElement, Vertex hyperVertex, int count, Direction direction) {
        for (Edge edgeToElementVertex : hyperVertex.getEdges(Direction.OUT, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX)) {
            Vertex elementVertex = edgeToElementVertex.getVertex(Direction.IN);
            if (indexOfNewElement == count++) {
                return new Triad(hyperVertex, elementVertex, count);
            }
        }
        if (hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX + direction).iterator().hasNext()) {
            Edge edgeToNextHyperVertex = hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX + direction).iterator().next();
            hyperVertex = edgeToNextHyperVertex.getVertex(Direction.IN);
            return getHyperVertexForIndex(indexOfNewElement, hyperVertex, count, direction);
        } else {
            return new Triad(null, null, count);
        }
    }

    private Vertex getVertexFromElement(E e) {
        Vertex previousVertex;
        if (e instanceof UmlgNode) {
            UmlgNode node = (UmlgNode) e;
            previousVertex = node.getVertex();
        } else if (e.getClass().isEnum()) {
            List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + e.toString());
            Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
            previousVertex = vertexes.get(0);
        } else if (getDataTypeEnum() != null) {
            List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + e.toString());
            Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
            previousVertex = vertexes.get(0);
        } else {
            List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + e.toString());
            Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
            previousVertex = vertexes.get(0);
        }
        return previousVertex;
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

}

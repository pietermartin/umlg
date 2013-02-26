package org.tuml.runtime.collection.persistent;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibSequence;
import org.tuml.runtime.collection.ocl.OclStdLibSequenceImpl;
import org.tuml.runtime.domain.TumlNode;

import java.util.*;

public abstract class BaseSequence<E> extends BaseCollection<E> implements TinkerSequence<E> {

    protected OclStdLibSequence<E> oclStdLibSequence;
    //This is used in the logic for adding a node at a index
    protected Map<Integer, Vertex> hyperVertexIndex;

    public BaseSequence(TumlRuntimeProperty runtimeProperty) {
        super(runtimeProperty);
        this.internalCollection = new ArrayList<E>();
        this.oclStdLibSequence = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
        this.oclStdLibCollection = this.oclStdLibSequence;
        this.hyperVertexIndex = new HashMap<Integer, Vertex>();
    }


    public BaseSequence(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
        this.internalCollection = new ArrayList<E>();
        this.oclStdLibSequence = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
        this.oclStdLibCollection = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
        this.hyperVertexIndex = new HashMap<Integer, Vertex>();
    }

    protected List<E> getInternalList() {
        return (List<E>) this.internalCollection;
    }

    //By now the element is already added to the internal list
    @Override
    protected void addToLinkedList(Edge edge, TumlNode e) {
        //Get the new vertex for the element
        Vertex newElementVertex = getVertexForDirection(edge);
        //Get the last hyperVertex
        //This is size - 2 as the hyperVertexIndex does not yet contain the entry for the new element and it is zero based
        Vertex lastHyperVertex;
        if (size() > 1) {
            lastHyperVertex = this.hyperVertexIndex.get(size() - 2);
            Vertex previousVertex = getVertexFromElement(this.getInternalList().get(size() - 2));
            //Check is duplicate
            if (previousVertex.equals(newElementVertex)) {
                //Add to the hyper vertex
                GraphDb.getDb().addEdge(null, lastHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
                this.hyperVertexIndex.put(size() - 1, lastHyperVertex);
            } else {
                //Create a new hyper vertex
                Vertex newHyperVertex = GraphDb.getDb().addVertex("hyperVertex");
                GraphDb.getDb().addEdge(null, newHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
                //Put the new hyper vertex in the linked list
                GraphDb.getDb().addEdge(null, lastHyperVertex, newHyperVertex, LABEL_TO_NEXT_HYPER_VERTEX);
                this.hyperVertexIndex.put(size() - 1, newHyperVertex);
                //remove the lastHyperVertex edge to parent, add it to new last hyper vertex
                Edge edgeToLastHyperVertex = this.vertex.getEdges(Direction.OUT, LABEL_TO_LAST_HYPER_VERTEX).iterator().next();
                GraphDb.getDb().removeEdge(edgeToLastHyperVertex);
                GraphDb.getDb().addEdge(null, this.vertex, newHyperVertex, LABEL_TO_LAST_HYPER_VERTEX);
            }
        } else {
            //Is the first element being added, create the hyper vertex
            Vertex newHyperVertex = GraphDb.getDb().addVertex("hyperVertex");
            GraphDb.getDb().addEdge(null, newHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
            //Put the new hyper vertex in the linked list
            GraphDb.getDb().addEdge(null, this.vertex, newHyperVertex, LABEL_TO_FIRST_HYPER_VERTEX);
            this.hyperVertexIndex.put(size() - 1, newHyperVertex);
            //remove the lastHyperVertex edge to parent, add it to new last hyper vertex
            GraphDb.getDb().addEdge(null, this.vertex, newHyperVertex, LABEL_TO_LAST_HYPER_VERTEX);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void loadFromVertex() {
        Direction direction;
        if (isControllingSide()) {
            direction = Direction.OUT;
        } else {
            direction = Direction.IN;
        }
        //If the multiplicity is a one don't bother with the linked list jol
        if (getUpper() == 1) {
            Iterable<Edge> edgeIterable = this.vertex.getEdges(direction, getLabel());
            if (!edgeIterable.iterator().hasNext()) {
                this.loaded = true;
            } else {
                Edge edgeToElement = edgeIterable.iterator().next();
                if (!GraphDb.getDb().hasEdgeBeenDeleted(edgeToElement)) {
                    Vertex elementVertex = edgeToElement.getVertex(Direction.IN);
                    loadNode(edgeToElement, elementVertex);
                    this.loaded = true;
                } else {
                    this.loaded = true;
                }
            }
        } else {
            //Load via hyper vertex
            if (this.vertex.getEdges(direction, LABEL_TO_FIRST_HYPER_VERTEX).iterator().hasNext()) {
                Edge edgeToFirstHyperVertex = this.vertex.getEdges(direction, LABEL_TO_FIRST_HYPER_VERTEX).iterator().next();
                Vertex hyperVertex = edgeToFirstHyperVertex.getVertex(Direction.IN);
                int toIndex = loadFromHyperVertex(hyperVertex, 0);
                while (hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().hasNext()) {
                    Edge edgeToNextHyperVertex = hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().next();
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
            loadNode(edgeToElement, vertexToLoad);
            this.hyperVertexIndex.put(toIndex++, hyperVertex);
        }
        return toIndex;
    }

    private void loadNode(Edge edgeToElement, Vertex vertexToLoad) {
        E node;
        try {
            Class<?> c = this.getClassToInstantiate(edgeToElement);
            if (c.isEnum()) {
                Object value = vertexToLoad.getProperty("value");
                node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                this.internalVertexMap.put(value, vertexToLoad);
            } else if (TumlNode.class.isAssignableFrom(c)) {
                node = (E) c.getConstructor(Vertex.class).newInstance(vertexToLoad);
            } else {
                Object value = vertexToLoad.getProperty("value");
                node = (E) value;
                this.internalVertexMap.put(value, vertexToLoad);
            }
            this.getInternalList().add(node);
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
        this.getInternalList().add(indexOf, e);
        return edgeFromParentToElementVertex;
    }

    private void manageLinkedList(int indexOfNewElement, Edge edgeFromParentToElementVertex) {
        //Get the new vertex for the element
        Vertex newElementVertex = getVertexForDirection(edgeFromParentToElementVertex);
        Vertex hyperVertexAtIndex = this.hyperVertexIndex.get(indexOfNewElement);
        //Check if the hyperVertexAtIndex holds elements that are equal to the element being added

        Vertex currentElementAtIndex = getVertexFromElement(this.getInternalList().get(indexOfNewElement));
        if (currentElementAtIndex != null && currentElementAtIndex.equals(newElementVertex)) {
            //Need to attach the newVertex to the current hyper vertex as its a duplicate
            GraphDb.getDb().addEdge(null, hyperVertexAtIndex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
        } else {
            //Element is not a duplicate.
            //Create a new hyper vertex to attach it to
            Vertex newHyperVertex = GraphDb.getDb().addVertex("hyperVertex");
            GraphDb.getDb().addEdge(null, newHyperVertex, newElementVertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
            //place the hyper vertex in the linked list
            if (indexOfNewElement == 0) {
                if (!isEmpty()) {
                    //Need to move the edge (LABEL_TO_FIRST_HYPER_VERTEX) to the parent
                    //Remove the label to first
                    Edge edgeFromParent = hyperVertexAtIndex.getEdges(Direction.IN, LABEL_TO_FIRST_HYPER_VERTEX).iterator().next();
                    GraphDb.getDb().removeEdge(edgeFromParent);
                }
                //Add the edge (LABEL_TO_FIRST_HYPER_VERTEX) to the new hyper vertex in position 0
                GraphDb.getDb().addEdge(null, this.vertex, newHyperVertex, LABEL_TO_FIRST_HYPER_VERTEX);
                //Link the new hyper vertex to the next one
                GraphDb.getDb().addEdge(null, newHyperVertex, hyperVertexAtIndex, LABEL_TO_NEXT_HYPER_VERTEX);
            } else {
                //Place the new hyper vertex in the list
                //Break the link to the previous hyper edge
                Edge edgeToPreviousHyperVertex = hyperVertexAtIndex.getEdges(Direction.IN, LABEL_TO_NEXT_HYPER_VERTEX).iterator().next();
                Vertex previousHyperVertex = edgeToPreviousHyperVertex.getVertex(Direction.OUT);
                GraphDb.getDb().removeEdge(edgeToPreviousHyperVertex);
                //Recreate the link from the previous hyper vertex to the new one
                GraphDb.getDb().addEdge(null, previousHyperVertex, newHyperVertex, LABEL_TO_NEXT_HYPER_VERTEX);
                //Add a edge to the next hyper edge from the new one.
                GraphDb.getDb().addEdge(null, newHyperVertex, hyperVertexAtIndex, LABEL_TO_NEXT_HYPER_VERTEX);
            }
        }
    }

    private Vertex getVertexFromElement(E e) {
        Vertex previousVertex;
        if (e instanceof TumlNode) {
            TumlNode node = (TumlNode) e;
            previousVertex = node.getVertex();
        } else if (e.getClass().isEnum()) {
            previousVertex = this.internalVertexMap.get(((Enum<?>) e).name());
        } else {
            previousVertex = this.internalVertexMap.get(((Enum<?>) e).name());
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

    protected abstract void removeEdgefromIndex(Vertex v, Edge edge, int indexOf);

    @Override
    public <R> TinkerSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
        maybeLoad();
        return this.oclStdLibSequence.collectNested(v);
    }

    @Override
    public <T, R> TinkerSequence<T> collect(BodyExpressionEvaluator<R, E> v) {
        maybeLoad();
        return this.oclStdLibSequence.collect(v);
    }

    @Override
    public <R> TinkerSequence<R> flatten() {
        maybeLoad();
        return this.oclStdLibSequence.flatten();
    }

    @Override
    public TinkerSequence<E> select(BooleanExpressionEvaluator<E> v) {
        maybeLoad();
        return this.oclStdLibSequence.select(v);
    }

    @Override
    public E first() {
        maybeLoad();
        return oclStdLibSequence.first();
    }

    @Override
    public Boolean equals(TinkerSequence<E> s) {
        maybeLoad();
        return this.oclStdLibSequence.equals(s);
    }

    @Override
    public TinkerSequence<E> union(TinkerSequence<? extends E> s) {
        maybeLoad();
        return this.oclStdLibSequence.union(s);
    }

    @Override
    public TinkerSequence<E> append(E object) {
        maybeLoad();
        return this.oclStdLibSequence.append(object);
    }

    @Override
    public TinkerSequence<E> prepend(E object) {
        maybeLoad();
        return this.oclStdLibSequence.prepend(object);
    }

    @Override
    public TinkerSequence<E> insertAt(Integer index, E object) {
        maybeLoad();
        return this.oclStdLibSequence.insertAt(index, object);
    }

    @Override
    public TinkerSequence<E> subSequence(Integer lower, Integer upper) {
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
    public TinkerSequence<E> including(E object) {
        maybeLoad();
        return this.oclStdLibSequence.including(object);
    }

    @Override
    public TinkerSequence<E> excluding(E object) {
        maybeLoad();
        return this.oclStdLibSequence.excluding(object);
    }

    @Override
    public TinkerSequence<E> reverse() {
        maybeLoad();
        return this.oclStdLibSequence.reverse();
    }

}

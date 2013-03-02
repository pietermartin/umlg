package org.tuml.runtime.collection.persistent;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.TinkerQualifiedOrderedSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TumlNode;

import java.util.Collection;

public class TinkerQualifiedOrderedSetImpl<E> extends TumlBaseOrderedSet<E> implements TinkerQualifiedOrderedSet<E> {

    @SuppressWarnings("unchecked")
    public TinkerQualifiedOrderedSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
        this.index = GraphDb.getDb().getIndex(owner.getUid() + INDEX_SEPARATOR + getQualifiedName(), Edge.class);
        if (this.index == null) {
            this.index = GraphDb.getDb().createIndex(owner.getUid() + INDEX_SEPARATOR + getQualifiedName(), Edge.class);
        }
    }

    @Override
    public void add(int indexOf, E e) {
        maybeLoad();
        if (!this.getInternalListOrderedSet().contains(e)) {
            Edge edge = addToListAtIndex(indexOf, e);
            // Can only qualify TinkerNode's
            if (!(e instanceof TumlNode)) {
                throw new IllegalStateException("Primitive properties can not be qualified!");
            }
            addQualifierToIndex(edge, (TumlNode) e);
        }
    }

    @Override
    protected void addToLinkedList(Edge edge) {
        //Get the new vertex for the element
        Vertex newElementVertex = getVertexForDirection(edge);
        if (this.vertex.getEdges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel()).iterator().hasNext()) {
            Edge edgeToLastVertex = this.vertex.getEdges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel()).iterator().next();
            Vertex lastVertex = edgeToLastVertex.getVertex(Direction.IN);

            //move the edge to the last vertex
            GraphDb.getDb().removeEdge(edgeToLastVertex);
            GraphDb.getDb().addEdge(null, this.vertex, newElementVertex, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel());

            //add the element to the linked list
            GraphDb.getDb().addEdge(null, lastVertex, newElementVertex, LABEL_TO_NEXT_IN_SEQUENCE);

        } else {
            //its the first element in the list
            GraphDb.getDb().addEdge(null, this.vertex, newElementVertex, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel());
            GraphDb.getDb().addEdge(null, this.vertex, newElementVertex, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel());
        }
    }

    @SuppressWarnings("unchecked")
    protected Edge addToListAtIndex(int indexOf, E e) {
        //If it is the last element then the edge (LABEL_TO_LAST_ELEMENT_IN_SEQUENCE) needs to be moved
        this.getInternalListOrderedSet().add(indexOf, e);
        //Add the regular edge for the label
        Edge edge = addInternal(e);
        //If it is the first element then the edge (LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE) needs to be moved
        if (indexOf == 0) {
            if (this.vertex.getEdges(Direction.OUT, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel()).iterator().hasNext()) {
                Edge edgeToFirstElement = this.vertex.getEdges(Direction.OUT, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel()).iterator().next();
                GraphDb.getDb().removeEdge(edgeToFirstElement);
            }
            GraphDb.getDb().addEdge(null, this.vertex, edge.getVertex(Direction.IN), LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel());
        }
        if (indexOf == size() - 1) {
            if (this.vertex.getEdges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel()).iterator().hasNext()) {
                Edge edgeToLastElement = this.vertex.getEdges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel()).iterator().next();
                GraphDb.getDb().removeEdge(edgeToLastElement);
            }
            GraphDb.getDb().addEdge(null, this.vertex, edge.getVertex(Direction.IN), LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel());
        }
        //Shift the linked list
        //Find the element at the index
        if (size() > 1) {
            if (indexOf == 0) {
                //add a edge to the previous first element
                E previous = (E) this.getInternalListOrderedSet().get(1);
                Vertex previousVertex = getVertexFromElement(previous, e);
                GraphDb.getDb().addEdge(null, edge.getVertex(Direction.IN), previousVertex, LABEL_TO_NEXT_IN_SEQUENCE);
            } else {
                E previous = (E) this.getInternalListOrderedSet().get(indexOf - 1);
                Vertex previousVertex = getVertexFromElement(previous, e);
                //size already includes the current added element, so if the size is 2 it means that there was only one in the list, i.e. no edge (LABEL_TO_NEXT_IN_SEQUENCE)
                if (size() > 2 && indexOf + 1 < size()) {
                    Edge edgeToNextElement = previousVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                    Vertex shiftedVertex = edgeToNextElement.getVertex(Direction.IN);
                    GraphDb.getDb().removeEdge(edgeToNextElement);
                    GraphDb.getDb().addEdge(null, edge.getVertex(Direction.IN), shiftedVertex, LABEL_TO_NEXT_IN_SEQUENCE);
                }
                GraphDb.getDb().addEdge(null, previousVertex, edge.getVertex(Direction.IN), LABEL_TO_NEXT_IN_SEQUENCE);
            }
        }
        return edge;
    }

    private Vertex getVertexFromElement(E previous, E e) {
        Vertex previousVertex;
        if (previous instanceof TumlNode) {
            TumlNode node = (TumlNode) previous;
            previousVertex = node.getVertex();
        } else if (e.getClass().isEnum()) {
            previousVertex = this.internalVertexMap.get(((Enum<?>) previous).name());
        } else {
            previousVertex = this.internalVertexMap.get(previous);
        }
        return previousVertex;
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
            Iterable<Edge> edgeIterable = this.vertex.getEdges(direction, getLabel());
            if (!edgeIterable.iterator().hasNext()) {
                this.loaded = true;
            } else {
                Edge edgeToElement = edgeIterable.iterator().next();
                if (!GraphDb.getDb().hasEdgeBeenDeleted(edgeToElement)) {
                    Vertex element = edgeToElement.getVertex(inverseDirection);
                    loadNode(edgeToElement, element);
                    this.loaded = true;
                } else {
                    this.loaded = true;
                }
            }
        } else {
            Iterable<Edge> edgeIterable = this.vertex.getEdges(direction, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel());
            if (!edgeIterable.iterator().hasNext()) {
                this.loaded = true;
            } else {
                Edge edgeToFirstElement = edgeIterable.iterator().next();
                if (!GraphDb.getDb().hasEdgeBeenDeleted(edgeToFirstElement)) {
                    Vertex firstVertexInSequence = edgeToFirstElement.getVertex(inverseDirection);
                    loadNode(edgeToFirstElement, firstVertexInSequence);
                    Vertex elementVertex = firstVertexInSequence;
                    while (elementVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().hasNext()) {
                        Edge edgeToNext = elementVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                        if (!GraphDb.getDb().hasEdgeBeenDeleted(edgeToNext)) {
                            elementVertex = edgeToNext.getVertex(Direction.IN);
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

    private void loadNode(Edge edgeToFirstElement, Vertex firstVertexInSequence) {
        E node;
        try {
            Class<?> c = this.getClassToInstantiate(edgeToFirstElement);
            if (c.isEnum()) {
                Object value = firstVertexInSequence.getProperty("value");
                node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                this.internalVertexMap.put(value, firstVertexInSequence);
            } else if (TumlNode.class.isAssignableFrom(c)) {
                node = (E) c.getConstructor(Vertex.class).newInstance(firstVertexInSequence);
            } else {
                Object value = firstVertexInSequence.getProperty("value");
                node = (E) value;
                this.internalVertexMap.put(value, firstVertexInSequence);
            }
            this.getInternalListOrderedSet().add(node);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean remove(Object o) {
        maybeLoad();
        int indexOf = this.getInternalListOrderedSet().indexOf(o);
        boolean result = this.getInternalListOrderedSet().remove(o);
        if (result) {
            Vertex vertexToDelete;
            if (o instanceof TumlNode) {
                TumlNode node = (TumlNode) o;
                vertexToDelete = node.getVertex();
            } else if (o.getClass().isEnum()) {
                vertexToDelete = this.internalVertexMap.get(((Enum<?>) o).name());
            } else {
                vertexToDelete = this.internalVertexMap.get(o);
            }
            //if first then move the edge LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE
            if (indexOf == 0) {
                Edge edgeToFirstElementInSequence = this.vertex.getEdges(Direction.OUT, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel()).iterator().next();
                GraphDb.getDb().removeEdge(edgeToFirstElementInSequence);
                //If there are more than one element in the list the add the edge LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE
                if (size() > 0) {
                    Edge edgeToNext = vertexToDelete.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                    GraphDb.getDb().addEdge(null, this.vertex, edgeToNext.getVertex(Direction.IN), LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel());
                }
            }
            //if last then move the edge LABEL_TO_LAST_ELEMENT_IN_SEQUENCE
            if (indexOf == size()) {
                Edge edgeToLastElementInSequence = this.vertex.getEdges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel()).iterator().next();
                GraphDb.getDb().removeEdge(edgeToLastElementInSequence);
                //If there are more than one element in the list add the edge LABEL_TO_LAST_ELEMENT_IN_SEQUENCE
                if (size() > 0) {
                    Edge edgeToPrevious = vertexToDelete.getEdges(Direction.IN, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                    GraphDb.getDb().addEdge(null, this.vertex, edgeToPrevious.getVertex(Direction.OUT), LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel());
                }
            }

            //reorder the edges LABEL_TO_NEXT_IN_SEQUENCE
            //If first or last then nothing to do
            if (indexOf != 0 && indexOf != size()) {
                Edge edgeToPrevious = vertexToDelete.getEdges(Direction.IN, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                Edge edgeToNext = vertexToDelete.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                GraphDb.getDb().addEdge(null, edgeToPrevious.getVertex(Direction.OUT), edgeToNext.getVertex(Direction.IN), LABEL_TO_NEXT_IN_SEQUENCE);
            }

            GraphDb.getDb().removeVertex(vertexToDelete);

        }
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
    }

    @Override
    public E set(int index, E element) {
        throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
    }

}

package org.tuml.runtime.collection.persistent;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.adaptor.TransactionThreadVar;
import org.tuml.runtime.adaptor.TumlTinkerIndex;
import org.tuml.runtime.collection.*;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.IterateExpressionAccumulator;
import org.tuml.runtime.collection.ocl.OclStdLibCollection;
import org.tuml.runtime.domain.*;
import org.tuml.runtime.domain.ocl.OclState;
import org.tuml.runtime.util.TumlFormatter;

import java.util.*;

public abstract class BaseCollection<E> implements Collection<E>, TumlRuntimeProperty, OclStdLibCollection<E> {

    protected Collection<E> internalCollection;
    protected OclStdLibCollection<E> oclStdLibCollection;
    protected TumlTinkerIndex<Edge> index;
    protected boolean loaded = false;
    // This is the owner of the collection
    protected TumlNode owner;
    // This is the vertex of the owner of the collection
    protected Vertex vertex;
    protected Class<?> parentClass;
    protected Map<Object, Vertex> internalVertexMap = new HashMap<Object, Vertex>();
    protected TumlRuntimeProperty tumlRuntimeProperty;
    protected final static String INDEX_SEPARATOR = ":::";
    protected static final String LABEL_TO_FIRST_HYPER_VERTEX = "labelToFirstHyperVertex";
    protected static final String LABEL_TO_LAST_HYPER_VERTEX = "labelToLastHyperVertex";
    protected static final String LABEL_TO_NEXT_HYPER_VERTEX = "labelToNextHyperVertex";
    protected static final String LABEL_TO_ELEMENT_FROM_HYPER_VERTEX = "labelToElementFromHyperVertex";

    protected static final String LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE = "labelToFirstElementInSequence";
    protected static final String LABEL_TO_NEXT_IN_SEQUENCE = "labelToNextElementInSequence";
    protected static final String LABEL_TO_LAST_ELEMENT_IN_SEQUENCE = "labelToLastElementInSequence";

    public BaseCollection(TumlRuntimeProperty runtimeProperty) {
        this.tumlRuntimeProperty = runtimeProperty;
    }

    public BaseCollection(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super();
        this.owner = owner;
        this.vertex = owner.getVertex();
        this.parentClass = owner.getClass();
        this.tumlRuntimeProperty = runtimeProperty;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void loadFromVertex() {
        if (!isOnePrimitive() && getDataTypeEnum() == null) {
            for (Iterator<Edge> iter = getEdges(); iter.hasNext(); ) {
                Edge edge = iter.next();
                E node;
                try {
                    Class<?> c = this.getClassToInstantiate(edge);
                    if (c.isEnum()) {
                        Object value = this.getVertexForDirection(edge).getProperty("value");
                        node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                        putToInternalMap(value, this.getVertexForDirection(edge));
                    } else if (TumlNode.class.isAssignableFrom(c)) {
                        node = (E) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
                    } else {
                        Object value = this.getVertexForDirection(edge).getProperty("value");
                        node = (E) value;
                        putToInternalMap(value, this.getVertexForDirection(edge));
                    }
                    this.internalCollection.add(node);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isDateTime()) {
            String s = (String) this.vertex.getProperty(getLabel());
            if (s != null) {
                E property = (E) new DateTime(s);
                this.internalCollection.add(property);
            }
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isDate()) {
            String s = (String) this.vertex.getProperty(getLabel());
            if (s != null) {
                E property = (E) new LocalDate(s);
                this.internalCollection.add(property);
            }
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isTime()) {
            String s = (String) this.vertex.getProperty(getLabel());
            if (s != null) {
                E property = (E) new LocalTime(s);
                this.internalCollection.add(property);
            }
        } else {
            E property = (E) this.vertex.getProperty(getLabel());
            if (property != null) {
                this.internalCollection.add(property);
            }
        }
        this.loaded = true;
    }

    protected Vertex removeFromInternalMap(Object key) {
        return this.internalVertexMap.remove(key);
    }

    protected void putToInternalMap(Object key, Vertex vertex) {
        this.internalVertexMap.put(key, vertex);
    }

    protected Iterator<Edge> getEdges() {
        if (this.isControllingSide()) {
            return this.vertex.getEdges(Direction.OUT, this.getLabel()).iterator();
        } else {
            return this.vertex.getEdges(Direction.IN, this.getLabel()).iterator();
        }
    }

    protected Iterable<Edge> getEdges(Vertex v) {
        if (!this.isControllingSide()) {
            return v.getEdges(Direction.OUT, this.getLabel());
        } else {
            return v.getEdges(Direction.IN, this.getLabel());
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        maybeLoad();
        boolean result = true;
        for (E e : c) {
            if (!this.add(e)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean add(E e) {
        maybeLoad();
        if (isQualified() || isInverseQualified()) {
            validateQualifiedAssociation(e);
        }
        boolean result = this.internalCollection.add(e);
        if (result) {
            TransactionThreadEntityVar.setNewEntity((CompositionNode) this.owner);
            Edge edge = addInternal(e);

            // Edge can only be null on a one primitive
            if (edge == null && !isOnePrimitive() && getDataTypeEnum() == null) {
                throw new IllegalStateException("Edge can only be null on isOne which is a String, Integer, Boolean or primitive");
            }

            if (isQualified() || isInverseQualified()) {
                // Can only qualify TinkerNode's
                if (!(e instanceof TumlNode)) {
                    throw new IllegalStateException("Primitive properties can not be qualified!");
                }
                addQualifierToIndex(edge, (TumlNode) e);
            }

            if (isOrdered()) {
                addToLinkedList(edge);
            }
            if (isInverseOrdered()) {
                // Can only qualify TinkerNode's
                if (!(e instanceof TumlNode)) {
                    throw new IllegalStateException("Primitive properties can not be qualified!");
                }
                addToInverseLinkedList(edge);
            }

        }
        return result;
    }


    private void addToInverseLinkedList(Edge edge) {
        if (!isInverseUnique()) {
            //Handle duplicates with hyper vertexes
            //Get the new vertex for the element
            Vertex newElementVertex = getVertexForDirection(edge);
            if (newElementVertex.getEdges(Direction.OUT, LABEL_TO_LAST_HYPER_VERTEX).iterator().hasNext()) {
                Edge edgeToLastHyperVertex = newElementVertex.getEdges(Direction.OUT, LABEL_TO_LAST_HYPER_VERTEX).iterator().next();
                Vertex lastHyperVertex = edgeToLastHyperVertex.getVertex(Direction.IN);

                //Check if it is a duplicate
                Edge edgeToLastElement = lastHyperVertex.getEdges(Direction.OUT, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).iterator().next();
                Vertex lastVertex = edgeToLastElement.getVertex(Direction.IN);
                if (lastVertex.equals(this.vertex)) {
                    GraphDb.getDb().addEdge(null, lastHyperVertex, this.vertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
                } else {
                    //Create a new hyper vertex
                    Vertex newHyperVertex = GraphDb.getDb().addVertex("hyperVertex");
                    GraphDb.getDb().removeEdge(edgeToLastHyperVertex);
                    GraphDb.getDb().addEdge(null, newElementVertex, newHyperVertex, LABEL_TO_LAST_HYPER_VERTEX);
                    GraphDb.getDb().addEdge(null, newHyperVertex, this.vertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
                    //add to the linked list
                    GraphDb.getDb().addEdge(null, lastHyperVertex, newHyperVertex, LABEL_TO_NEXT_HYPER_VERTEX);
                }
            } else {
                //its the first element in the list
                //Create a new hyper vertex
                Vertex newHyperVertex = GraphDb.getDb().addVertex("hyperVertex");
                GraphDb.getDb().addEdge(null, newElementVertex, newHyperVertex, LABEL_TO_FIRST_HYPER_VERTEX);
                GraphDb.getDb().addEdge(null, newElementVertex, newHyperVertex, LABEL_TO_LAST_HYPER_VERTEX);
                //Add the new element to the hyper vertex
                GraphDb.getDb().addEdge(null, newHyperVertex, this.vertex, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX);
            }
        } else {
            //Get the new vertex for the element
            Vertex newElementVertex = getVertexForDirection(edge);
            if (newElementVertex.getEdges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE).iterator().hasNext()) {
                Edge edgeToLastVertex = newElementVertex.getEdges(Direction.OUT, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE).iterator().next();
                Vertex lastVertex = edgeToLastVertex.getVertex(Direction.IN);

                //move the edge to the last vertex
                GraphDb.getDb().removeEdge(edgeToLastVertex);
                GraphDb.getDb().addEdge(null, newElementVertex, this.vertex, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE);

                //add the element to the linked list
                GraphDb.getDb().addEdge(null, lastVertex, this.vertex, LABEL_TO_NEXT_IN_SEQUENCE);

            } else {
                //its the first element in the list
                GraphDb.getDb().addEdge(null, newElementVertex, this.vertex, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE);
                GraphDb.getDb().addEdge(null, newElementVertex, this.vertex, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE);
            }
        }
    }

    private void removeFromLinkedList(TumlNode o) {
        if (!isUnique()) {
            //Handle duplicates
            //Find hyper vertex
            Vertex vertexToRemove = o.getVertex();
            Vertex hyperVertex = getFirstHyperVertexInListForVertex(vertexToRemove);

            Edge edgeToVertexToRemove = hyperVertex.getEdges(Direction.OUT, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).iterator().next();

            //Remove the edge to the hyper vertex
            GraphDb.getDb().removeEdge(edgeToVertexToRemove);
            //Check if the are duplicates, i.e. the hyper vertex has remaining edges.
            //If there are duplicates then there is nothing more to do
            if (!hyperVertex.getEdges(Direction.OUT, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).iterator().hasNext()) {
                //No duplicates so remove the hyper vertex and fix the linked list

                //Check if it is the first in the linked list
                if (hyperVertex.getEdges(Direction.IN, LABEL_TO_NEXT_HYPER_VERTEX).iterator().hasNext()) {
                    //Not the first in the linked list
                    Edge edgeToPreviousHyperVertex = hyperVertex.getEdges(Direction.IN, LABEL_TO_NEXT_HYPER_VERTEX).iterator().next();
                    Vertex previousHyperVertex = edgeToPreviousHyperVertex.getVertex(Direction.OUT);

                    if (hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().hasNext()) {
                        //Not the last
                        Edge edgeToNextHyperVertex = hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().next();
                        Vertex nextHyperVertex = edgeToNextHyperVertex.getVertex(Direction.IN);
                        //Add in a link between previous and next
                        GraphDb.getDb().removeVertex(hyperVertex);
                        GraphDb.getDb().addEdge(null, previousHyperVertex, nextHyperVertex, LABEL_TO_NEXT_HYPER_VERTEX);

                    } else {
                        //Its the last, i.e. make the previous point to the parent
                        GraphDb.getDb().removeVertex(hyperVertex);
                        GraphDb.getDb().addEdge(null, this.vertex, previousHyperVertex, LABEL_TO_LAST_HYPER_VERTEX);
                    }
                } else {
                    //The first in the linked list, make the next the first
                    //Check if it is last
                    if (hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().hasNext()) {
                        //Not last
                        Edge edgeToNextHyperVertex = hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().next();
                        Vertex nextHyperVertex = edgeToNextHyperVertex.getVertex(Direction.IN);
                        GraphDb.getDb().addEdge(null, this.vertex, nextHyperVertex, LABEL_TO_FIRST_HYPER_VERTEX);
                        GraphDb.getDb().removeVertex(hyperVertex);
                    } else {
                        //Last
                        //By this time it must be the only element in the list
                        GraphDb.getDb().removeVertex(hyperVertex);
                    }
                }
            }
        } else {
            //No duplicates to handle
            Vertex vertexToRemove = o.getVertex();
            //this.vertex has the next and previous links to manage in a inverse situation
            //Check if it is first
            if (vertexToRemove.getEdges(Direction.IN, LABEL_TO_NEXT_IN_SEQUENCE).iterator().hasNext()) {
                //It is not first
                Edge edgeToPrevious = vertexToRemove.getEdges(Direction.IN, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                Vertex previousVertex = edgeToPrevious.getVertex(Direction.OUT);
                GraphDb.getDb().removeEdge(edgeToPrevious);
                //Check if it is last
                if (vertexToRemove.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().hasNext()) {
                    //Not last
                    Edge edgeToNext = vertexToRemove.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                    Vertex nextVertex = edgeToPrevious.getVertex(Direction.IN);
                    GraphDb.getDb().removeEdge(edgeToNext);
                    GraphDb.getDb().addEdge(null, previousVertex, nextVertex, LABEL_TO_NEXT_IN_SEQUENCE);
                } else {
                    //Last,
                    //previous becomes to last
                    Edge edgeToLast = vertexToRemove.getEdges(Direction.IN, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE).iterator().next();
                    GraphDb.getDb().removeEdge(edgeToLast);
                    GraphDb.getDb().addEdge(null, this.vertex, previousVertex, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE);
                }
            } else {
                //It is first
                Edge edgeToFirst = vertexToRemove.getEdges(Direction.IN, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE).iterator().next();
                GraphDb.getDb().removeEdge(edgeToFirst);
                //Check is it is last
                if (vertexToRemove.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().hasNext()) {
                    //Not last
                    //Move the edge to first
                    Edge edgeToNext = vertexToRemove.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                    Vertex nextVertex = edgeToNext.getVertex(Direction.IN);
                    GraphDb.getDb().removeEdge(edgeToNext);
                    GraphDb.getDb().addEdge(null, this.vertex, nextVertex, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE);
                } else {
                    //Last
                    //Only one element in the list
                    Edge edgeToLast = vertexToRemove.getEdges(Direction.IN, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE).iterator().next();
                    GraphDb.getDb().removeEdge(edgeToLast);
                }
            }
        }
    }

    /**
     * Find all hyper vertexes from vertexToRemove. There can be many as the list could have duplicates.
     * Out of all the hyper vertexes find the earliest one in the list. To do that iterate to the beginning of the list looking to see which hyper vertex is before another.
     *
     * @param vertex
     * @return
     */
    private Vertex getFirstHyperVertexInListForVertex(Vertex vertex) {
        Set<Vertex> hyperVertexes = new HashSet<Vertex>();
        for (Edge edge : vertex.getEdges(Direction.IN, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX)) {
            hyperVertexes.add(edge.getVertex(Direction.OUT));
        }

        //Take any hyper vertex and start iterating to the beginning.
        Vertex firstHyperVertex = hyperVertexes.iterator().next();
        hyperVertexes.remove(firstHyperVertex);
        Iterator<Edge> iterator = firstHyperVertex.getEdges(Direction.IN, LABEL_TO_NEXT_HYPER_VERTEX).iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            Vertex previousHyperVertex = edge.getVertex(Direction.OUT);
            if (hyperVertexes.contains(previousHyperVertex)) {
                firstHyperVertex = previousHyperVertex;
                iterator = firstHyperVertex.getEdges(Direction.IN, LABEL_TO_NEXT_HYPER_VERTEX).iterator();
                hyperVertexes.remove(previousHyperVertex);
            }
        }

        return firstHyperVertex;
    }

    private void removeFromInverseLinkedList(TumlNode o) {
        if (!isInverseUnique()) {
            //Handle duplicates
            //Find hyper vertex
            Vertex vertexToRemove = o.getVertex();
            //Vertex to remove is the parent in a inverse situation
            Edge edgeToHyperVertex = this.vertex.getEdges(Direction.IN, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).iterator().next();
            Vertex hyperVertex = edgeToHyperVertex.getVertex(Direction.OUT);
            //Remove the edge to the hyper vertex
            GraphDb.getDb().removeEdge(edgeToHyperVertex);
            //Check if the are duplicates, i.e. the hyper vertex has remaining edges.
            //If there are duplicates then there is nothing more to do
            if (!hyperVertex.getEdges(Direction.OUT, LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).iterator().hasNext()) {
                //No duplicates so remove the hyper vertex and fix the linked list

                //Check if it is the first in the linked list
                if (hyperVertex.getEdges(Direction.IN, LABEL_TO_NEXT_HYPER_VERTEX).iterator().hasNext()) {
                    //Not the first in the linked list
                    Edge edgeToPreviousHyperVertex = hyperVertex.getEdges(Direction.IN, LABEL_TO_NEXT_HYPER_VERTEX).iterator().next();
                    Vertex previousHyperVertex = edgeToPreviousHyperVertex.getVertex(Direction.OUT);

                    if (hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().hasNext()) {
                        //Not the last
                        Edge edgeToNextHyperVertex = hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().next();
                        Vertex nextHyperVertex = edgeToNextHyperVertex.getVertex(Direction.IN);
                        //Add in a link between previous and next
                        GraphDb.getDb().removeVertex(hyperVertex);
                        GraphDb.getDb().addEdge(null, previousHyperVertex, nextHyperVertex, LABEL_TO_NEXT_HYPER_VERTEX);

                    } else {
                        //Its the last, i.e. make the previous point to the parent
                        GraphDb.getDb().removeVertex(hyperVertex);
                        GraphDb.getDb().addEdge(null, vertexToRemove, previousHyperVertex, LABEL_TO_LAST_HYPER_VERTEX);
                    }
                } else {
                    //The first in the linked list, make the next the first
                    //Check if it is last
                    if (hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().hasNext()) {
                        //Not last
                        Edge edgeToNextHyperVertex = hyperVertex.getEdges(Direction.OUT, LABEL_TO_NEXT_HYPER_VERTEX).iterator().next();
                        Vertex nextHyperVertex = edgeToNextHyperVertex.getVertex(Direction.IN);
                        GraphDb.getDb().addEdge(null, vertexToRemove, nextHyperVertex, LABEL_TO_FIRST_HYPER_VERTEX);
                        GraphDb.getDb().removeVertex(hyperVertex);
                    } else {
                        //Last
                        //By this time it must be the only element in the list
                        GraphDb.getDb().removeVertex(hyperVertex);
                    }
                }
            }
        } else {
            //No duplicates to handle
            Vertex vertexToRemove = o.getVertex();
            //this.vertex has the next and previous links to manage in a inverse situation
            //Check if it is first
            if (this.vertex.getEdges(Direction.IN, LABEL_TO_NEXT_IN_SEQUENCE).iterator().hasNext()) {
                //It is not first
                Edge edgeToPrevious = this.vertex.getEdges(Direction.IN, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                Vertex previousVertex = edgeToPrevious.getVertex(Direction.OUT);
                GraphDb.getDb().removeEdge(edgeToPrevious);
                //Check if it is last
                if (this.vertex.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().hasNext()) {
                    //Not last
                    Edge edgeToNext = this.vertex.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                    Vertex nextVertex = edgeToPrevious.getVertex(Direction.IN);
                    GraphDb.getDb().removeEdge(edgeToNext);
                    GraphDb.getDb().addEdge(null, previousVertex, nextVertex, LABEL_TO_NEXT_IN_SEQUENCE);
                } else {
                    //Last,
                    //previous becomes to last
                    Edge edgeToLast = this.vertex.getEdges(Direction.IN, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE).iterator().next();
                    GraphDb.getDb().removeEdge(edgeToLast);
                    GraphDb.getDb().addEdge(null, vertexToRemove, previousVertex, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE);
                }
            } else {
                //It is first
                Edge edgeToFirst = this.vertex.getEdges(Direction.IN, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE).iterator().next();
                GraphDb.getDb().removeEdge(edgeToFirst);
                //Check is it is last
                if (this.vertex.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().hasNext()) {
                    //Not last
                    //Move the edge to first
                    Edge edgeToNext = this.vertex.getEdges(Direction.OUT, LABEL_TO_NEXT_IN_SEQUENCE).iterator().next();
                    Vertex nextVertex = edgeToNext.getVertex(Direction.IN);
                    GraphDb.getDb().removeEdge(edgeToNext);
                    GraphDb.getDb().addEdge(null, vertexToRemove, nextVertex, LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE);
                } else {
                    //Last
                    //Only one element in the list
                    Edge edgeToLast = this.vertex.getEdges(Direction.IN, LABEL_TO_LAST_ELEMENT_IN_SEQUENCE).iterator().next();
                    GraphDb.getDb().removeEdge(edgeToLast);
                }
            }
        }
    }

    /**
     * This must be implemented by the appropriate collection type
     *
     * @param edge
     */
    protected abstract void addToLinkedList(Edge edge);

    private void validateQualifiedAssociation(E e) {
        if (!(e instanceof TumlNode)) {
            throw new IllegalStateException("Primitive properties can not be qualified!");
        }
        TumlNode node = (TumlNode) e;
        if (isQualified()) {
            for (Qualifier qualifier : this.owner.getQualifiers(this.tumlRuntimeProperty, node, false)) {
                validateQualifiedMultiplicity(index, qualifier);
            }
        }
        if (isInverseQualified()) {
            Index<Edge> tmpIndex;
            tmpIndex = GraphDb.getDb().getIndex(((TumlNode) e).getUid() + INDEX_SEPARATOR + getInverseQualifiedName(), Edge.class);
            if (tmpIndex == null) {
                tmpIndex = GraphDb.getDb().createIndex(((TumlNode) e).getUid() + INDEX_SEPARATOR + getInverseQualifiedName(), Edge.class);
            }
            for (Qualifier qualifier : node.getQualifiers(this.tumlRuntimeProperty, this.owner, true)) {
                validateQualifiedMultiplicity(tmpIndex, qualifier);
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        maybeLoad();
        boolean result = this.internalCollection.remove(o);
        if (result) {
            TransactionThreadEntityVar.setNewEntity((CompositionNode) this.owner);
            @SuppressWarnings("unchecked")
            E e = (E) o;
            Vertex v;
            if (o instanceof TumlNode) {
                TumlNode node = (TumlNode) o;
                v = node.getVertex();

                if (node instanceof CompositionNode) {
                    TransactionThreadEntityVar.setNewEntity((CompositionNode) node);
                }

                Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.getLabel());
                for (Edge edge : edges) {
                    if (o instanceof TinkerAuditableNode) {
                        createAudit(e, true);
                    }
                    if (isOrdered()) {
                        removeFromLinkedList((TumlNode) o);
                    } 
                    if (isInverseOrdered()) {
                        removeFromInverseLinkedList((TumlNode) o);
                    }
                    GraphDb.getDb().removeEdge(edge);
                    break;
                }
            } else if (o.getClass().isEnum()) {
                v = removeFromInternalMap(((Enum<?>) o).name());
                GraphDb.getDb().removeVertex(v);
            } else if (isOnePrimitive() || getDataTypeEnum() != null) {
                this.vertex.removeProperty(getLabel());
            } else {
                v = removeFromInternalMap(o);
                if (this.owner instanceof TinkerAuditableNode) {
                    createAudit(e, true);
                }
            }
        }
        return result;
    }

    protected Edge addInternal(E e) {
        Vertex v = null;
        validateElementType(e);
        if (e instanceof TumlNode) {
            TumlNode node = (TumlNode) e;
            if (e instanceof CompositionNode) {
                TransactionThreadEntityVar.setNewEntity((CompositionNode) node);
            }
            v = node.getVertex();
            if (this.isUnique() && (this.isOneToMany() || this.isOneToOne())) {
                // Check that the existing one from the element does not exist,
                // the user must first remove it
                Iterator<Edge> iteratorToOne = getEdges(v).iterator();
                if (iteratorToOne.hasNext()) {
                    throw new IllegalStateException("Its a 1");
                }

                // Even if the user cleared the one, a reference on the other
                // side may remain in memory.
                // Clearing this property is not performance issue as it is a
                // one
                node.initialiseProperty(tumlRuntimeProperty, true);

            }
        } else if (e.getClass().isEnum()) {
            v = GraphDb.getDb().addVertex(null);
            v.setProperty("value", ((Enum<?>) e).name());
            v.setProperty("className", e.getClass().getName());
            putToInternalMap(((Enum<?>) e).name(), v);
        } else if (isOnePrimitive()) {
            this.vertex.setProperty(getLabel(), e);
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isDateTime()) {
            this.vertex.setProperty(getLabel(), e.toString());
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isDate()) {
            this.vertex.setProperty(getLabel(), e.toString());
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isTime()) {
            this.vertex.setProperty(getLabel(), e.toString());
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isInternationalPhoneNumber()) {
            this.vertex.setProperty(getLabel(), e);
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isLocalPhoneNumber()) {
            this.vertex.setProperty(getLabel(), e);
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isEmail()) {
            this.vertex.setProperty(getLabel(), e);
        } else {
            v = GraphDb.getDb().addVertex(null);
            v.setProperty("value", e);
            v.setProperty("className", e.getClass().getName());
            putToInternalMap(e, v);
        }
        if (v != null) {
            Edge edge = createEdge(e, v);
            if (this.owner instanceof TinkerAuditableNode) {
                createAudit(e, false);
            }
            return edge;
        } else {
            if (owner instanceof TinkerAuditableNode) {
                createPrimitiveAudit(e);
            }
            return null;
        }

    }

    protected Edge createEdge(E e, Vertex v) {
        Edge edge;
        if (this.isControllingSide()) {
            edge = GraphDb.getDb().addEdge(null, this.vertex, v, this.getLabel());
            if (this.isManyToMany()) {
                edge.setProperty("manyToManyCorrelationInverseTRUE", "SETTED");
            }
        } else {
            edge = GraphDb.getDb().addEdge(null, v, this.vertex, this.getLabel());
            if (this.isManyToMany()) {
                edge.setProperty("manyToManyCorrelationInverseFALSE", "SETTED");
            }
        }
        return edge;
    }

    private void createPrimitiveAudit(E e) {
        TinkerAuditableNode auditOwner = (TinkerAuditableNode) owner;
        if (TransactionThreadVar.hasNoAuditEntry(owner.getClass().getName() + owner.getUid())) {
            auditOwner.createAuditVertex(false);
        }
        auditOwner.getAuditVertex().setProperty(getLabel(), e);
    }

    protected void createAudit(E e, boolean deletion) {
        if (!(owner instanceof TinkerAuditableNode)) {
            throw new IllegalStateException("if the collection member is an TinkerAuditableNode, then the owner must be a TinkerAuditableNode!");
        }
        TinkerAuditableNode auditOwner = (TinkerAuditableNode) owner;
        if (TransactionThreadVar.hasNoAuditEntry(owner.getClass().getName() + owner.getUid())) {
            auditOwner.createAuditVertex(false);
        }
        if (e instanceof TinkerAuditableNode) {
            TinkerAuditableNode node = (TinkerAuditableNode) e;
            if (TransactionThreadVar.hasNoAuditEntry(node.getClass().getName() + node.getUid())) {
                node.createAuditVertex(false);
            }
            Edge auditEdge;
            if (isControllingSide()) {
                auditEdge = GraphDb.getDb().addEdge(null, auditOwner.getAuditVertex(), node.getAuditVertex(), this.getLabel());
                auditEdge.setProperty("outClass", auditOwner.getClass().getName() + "Audit");
                auditEdge.setProperty("inClass", node.getClass().getName() + "Audit");
            } else {
                auditEdge = GraphDb.getDb().addEdge(null, node.getAuditVertex(), auditOwner.getAuditVertex(), this.getLabel());
                auditEdge.setProperty("inClass", auditOwner.getClass().getName() + "Audit");
                auditEdge.setProperty("outClass", node.getClass().getName() + "Audit");
            }
            if (deletion) {
                auditEdge.setProperty("deletedOn", TumlFormatter.format(new DateTime()));
            }
        } else if (e.getClass().isEnum()) {
            Vertex v = GraphDb.getDb().addVertex(null);
            v.setProperty("value", ((Enum<?>) e).name());
            Edge auditEdge = GraphDb.getDb().addEdge(null, auditOwner.getAuditVertex(), v, this.getLabel());
            auditEdge.setProperty("outClass", auditOwner.getClass().getName() + "Audit");
            auditEdge.setProperty("inClass", e.getClass().getName());
        } else {
            if (TransactionThreadVar.hasNoAuditEntry(owner.getClass().getName() + e.getClass().getName() + e.toString())) {
                Vertex auditVertex = GraphDb.getDb().addVertex(null);
                auditVertex.setProperty("value", e);
                TransactionThreadVar.putAuditVertexFalse(owner.getClass().getName() + e.getClass().getName() + e.toString(), auditVertex);
                auditVertex.setProperty("transactionNo", GraphDb.getDb().getTransactionCount());
                Edge auditEdge;
                if (isControllingSide()) {
                    auditEdge = GraphDb.getDb().addEdge(null, auditOwner.getAuditVertex(), auditVertex, this.getLabel());
                    auditEdge.setProperty("outClass", this.parentClass.getName());
                    auditEdge.setProperty("inClass", e.getClass().getName() + "Audit");
                } else {
                    auditEdge = GraphDb.getDb().addEdge(null, auditVertex, auditOwner.getAuditVertex(), this.getLabel());
                    auditEdge.setProperty("inClass", this.parentClass.getName());
                    auditEdge.setProperty("outClass", e.getClass().getName() + "Audit");
                }
                if (deletion) {
                    auditEdge.setProperty("transactionNo", GraphDb.getDb().getTransactionCount());
                    auditEdge.setProperty("deletedOn", TumlFormatter.format(new DateTime()));
                }
            }
        }
    }

    protected void maybeLoad() {
        if (!this.loaded) {
            loadFromVertex();
        }
    }

    protected Vertex getVertexForDirection(Edge edge) {
        if (this.isControllingSide()) {
            return edge.getVertex(Direction.IN);
        } else {
            return edge.getVertex(Direction.OUT);
        }
    }

    protected Vertex getOppositeVertexForDirection(Edge edge) {
        if (this.isControllingSide()) {
            return edge.getVertex(Direction.OUT);
        } else {
            return edge.getVertex(Direction.IN);
        }
    }

    protected Class<?> getClassToInstantiate(Edge edge) {
        try {
            if (this.isControllingSide()) {
                return Class.forName((String) edge.getVertex(Direction.IN).getProperty("className"));
//                return Class.forName((String) edge.getProperty("inClass"));
            } else {
                return Class.forName((String) edge.getVertex(Direction.OUT).getProperty("className"));
//                return Class.forName((String) edge.getProperty("outClass"));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        maybeLoad();
        return this.internalCollection.size();
    }

    @Override
    public boolean isEmpty() {
        maybeLoad();
        return this.internalCollection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        maybeLoad();
        return this.internalCollection.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        maybeLoad();
        return this.internalCollection.iterator();
    }

    @Override
    public Object[] toArray() {
        maybeLoad();
        return this.internalCollection.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        maybeLoad();
        return this.internalCollection.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        maybeLoad();
        return this.internalCollection.containsAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (!this.loaded) {
            loadFromVertex();
        }
        boolean result = true;
        for (E e : this.internalCollection) {
            if (!c.contains(e)) {
                if (!this.remove(e)) {
                    result = false;
                }
            }
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        maybeLoad();
        boolean result = true;
        for (Object object : c) {
            if (!this.remove(object)) {
                result = false;
            }
        }
        return result;
    }

    private void validateQualifiedMultiplicity(Index<Edge> index, Qualifier qualifier) {
        if (qualifier.isOne()) {
            long count = index.count(qualifier.getKey(), qualifier.getValue());
            if (count > 0) {
                // Add info to exception
                throw new IllegalStateException(String.format("Qualifier fails, qualifier multiplicity is one and an entry for key '%s' and value '%s' already exist",
                        qualifier.getKey(), qualifier.getValue()));
            }
        }
    }

    protected void addQualifierToIndex(Edge edge, TumlNode node) {
        // if is qualified update index
        if (isQualified()) {
            addQualifierToIndex(this.index, edge, this.owner, node, false);
        }

        // if is qualified update index
        if (isInverseQualified()) {
            Index<Edge> index = GraphDb.getDb().getIndex(node.getUid() + INDEX_SEPARATOR + getInverseQualifiedName(), Edge.class);
            if (index == null) {
                index = GraphDb.getDb().createIndex(node.getUid() + INDEX_SEPARATOR + getInverseQualifiedName(), Edge.class);
            }
            addQualifierToIndex(index, edge, node, this.owner, true);
        }
    }

    /**
     * element is the context for the ocl expression representing the qualifier
     * value
     *
     * @param index
     * @param qualifiedNode
     * @param qualifierNode
     */
    private void addQualifierToIndex(Index<Edge> index, Edge edge, TumlNode qualifiedNode, TumlNode qualifierNode, boolean inverse) {
        for (Qualifier qualifier : qualifiedNode.getQualifiers(this.tumlRuntimeProperty, qualifierNode, inverse)) {
            index.put(qualifier.getKey(), qualifier.getValue(), edge);
            edge.setProperty("index" + qualifier.getKey(), qualifier.getValue());
        }
    }

    @Override
    public String toJson() {
        maybeLoad();
        StringBuilder sb = new StringBuilder();
        if (isManyPrimitive() || isManyEnumeration()) {
            sb.append("[");
            int count = 0;
            for (E e : this.internalCollection) {
                count++;
                if (e instanceof String) {
                    sb.append("\"");
                    sb.append(e);
                    sb.append("\"");
                } else if (e instanceof TumlEnum) {
                    sb.append("\"");
                    sb.append(e);
                    sb.append("\"");
                } else {
                    sb.append(e);
                }
                if (count < size()) {
                    sb.append(",");
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public boolean isOnePrimitive() {
        return this.tumlRuntimeProperty.isOnePrimitive();
    }

    @Override
    public DataTypeEnum getDataTypeEnum() {
        return this.tumlRuntimeProperty.getDataTypeEnum();
    }

    @Override
    public boolean isOneEnumeration() {
        return this.tumlRuntimeProperty.isOneEnumeration();
    }

    @Override
    public boolean isManyPrimitive() {
        return this.tumlRuntimeProperty.isManyPrimitive();
    }

    @Override
    public boolean isManyEnumeration() {
        return this.tumlRuntimeProperty.isManyEnumeration();
    }

    @Override
    public String getLabel() {
        return this.tumlRuntimeProperty.getLabel();
    }

    @Override
    public String getQualifiedName() {
        return this.tumlRuntimeProperty.getQualifiedName();
    }

    @Override
    public String getInverseQualifiedName() {
        return this.tumlRuntimeProperty.getInverseQualifiedName();
    }

    @Override
    public boolean isOneToOne() {
        return this.tumlRuntimeProperty.isOneToOne();
    }

    @Override
    public boolean isOneToMany() {
        return this.tumlRuntimeProperty.isOneToMany();
    }

    @Override
    public boolean isManyToOne() {
        return this.tumlRuntimeProperty.isManyToOne();
    }

    @Override
    public boolean isManyToMany() {
        return this.tumlRuntimeProperty.isManyToMany();
    }

    @Override
    public int getUpper() {
        return this.tumlRuntimeProperty.getUpper();
    }

    @Override
    public int getLower() {
        return this.tumlRuntimeProperty.getLower();
    }

    @Override
    public boolean isControllingSide() {
        return this.tumlRuntimeProperty.isControllingSide();
    }

    @Override
    public boolean isComposite() {
        return this.tumlRuntimeProperty.isComposite();
    }

    @Override
    public boolean isInverseComposite() {
        return this.tumlRuntimeProperty.isInverseComposite();
    }

    @Override
    public boolean isValid(int elementCount) {
        return this.tumlRuntimeProperty.isValid(elementCount);
    }

    @Override
    public boolean isQualified() {
        return this.tumlRuntimeProperty.isQualified();
    }

    @Override
    public boolean isInverseQualified() {
        return this.tumlRuntimeProperty.isInverseQualified();
    }

    @Override
    public boolean isOrdered() {
        return this.tumlRuntimeProperty.isOrdered();
    }

    @Override
    public boolean isInverseOrdered() {
        return this.tumlRuntimeProperty.isInverseOrdered();
    }

    @Override
    public boolean isUnique() {
        return this.tumlRuntimeProperty.isUnique();
    }

    @Override
    public boolean isInverseUnique() {
        return this.tumlRuntimeProperty.isInverseUnique();
    }

    @Override
    public boolean equals(TinkerCollection<E> c) {
        maybeLoad();
        return this.oclStdLibCollection.equals(c);
    }

    @Override
    public boolean notEquals(TinkerCollection<E> c) {
        maybeLoad();
        return this.oclStdLibCollection.notEquals(c);
    }

    @Override
    public E any(BooleanExpressionEvaluator<E> v) {
        maybeLoad();
        return this.oclStdLibCollection.any(v);
    }

    @Override
    public TinkerSet<E> asSet() {
        maybeLoad();
        return this.oclStdLibCollection.asSet();
    }

    @Override
    public TinkerOrderedSet<E> asOrderedSet() {
        maybeLoad();
        return this.oclStdLibCollection.asOrderedSet();
    }

    @Override
    public TinkerSequence<E> asSequence() {
        maybeLoad();
        return this.oclStdLibCollection.asSequence();
    }

    @Override
    public TinkerBag<E> asBag() {
        maybeLoad();
        return this.oclStdLibCollection.asBag();
    }

    @Override
    public <R> R iterate(IterateExpressionAccumulator<R, E> v) {
        maybeLoad();
        return this.oclStdLibCollection.iterate(v);
    }

    @Override
    public boolean equals(Object object) {
        maybeLoad();
        return this.oclStdLibCollection.equals(object);
    }

    @Override
    public boolean notEquals(Object object) {
        maybeLoad();
        return this.oclStdLibCollection.notEquals(object);
    }

    @Override
    public Boolean oclIsNew() {
        maybeLoad();
        return this.oclStdLibCollection.oclIsNew();
    }

    @Override
    public Boolean oclIsUndefined() {
        maybeLoad();
        return this.oclStdLibCollection.oclIsUndefined();
    }

    @Override
    public Boolean oclIsInvalid() {
        maybeLoad();
        return this.oclStdLibCollection.oclIsInvalid();
    }

    @Override
    public <T> T oclAsType(T classifier) {
        maybeLoad();
        return this.oclStdLibCollection.oclAsType(classifier);
    }

    @Override
    public Boolean oclIsTypeOf(Object object) {
        maybeLoad();
        return this.oclStdLibCollection.oclIsTypeOf(object);
    }

    @Override
    public Boolean oclIsKindOf(Object object) {
        maybeLoad();
        return this.oclStdLibCollection.oclIsKindOf(object);
    }

    @Override
    public Boolean oclIsInState(OclState state) {
        maybeLoad();
        return this.oclStdLibCollection.oclIsInState(state);
    }

    @Override
    public <T extends Object> Class<T> oclType() {
        maybeLoad();
        return this.oclStdLibCollection.oclType();
    }

    @Override
    public String oclLocale() {
        maybeLoad();
        return this.oclStdLibCollection.oclLocale();
    }

    @Override
    public boolean includes(E t) {
        maybeLoad();
        return this.oclStdLibCollection.includes(t);
    }

    @Override
    public boolean excludes(E t) {
        maybeLoad();
        return this.oclStdLibCollection.excludes(t);
    }

    @Override
    public int count(E e) {
        maybeLoad();
        return this.oclStdLibCollection.count(e);
    }

    @Override
    public Boolean includesAll(TinkerCollection<E> c) {
        maybeLoad();
        return this.oclStdLibCollection.includesAll(c);
    }

    @Override
    public Boolean excludesAll(TinkerCollection<E> c) {
        maybeLoad();
        return this.oclStdLibCollection.excludesAll(c);
    }

    @Override
    public Boolean notEmpty() {
        maybeLoad();
        return this.oclStdLibCollection.notEmpty();
    }

    @Override
    public E max() {
        maybeLoad();
        return this.oclStdLibCollection.max();
    }

    @Override
    public E min() {
        maybeLoad();
        return this.oclStdLibCollection.min();
    }

    @Override
    public E sum() {
        maybeLoad();
        return this.oclStdLibCollection.sum();
    }

    @Override
    public TinkerSet<?> product(TinkerCollection<E> c) {
        maybeLoad();
        return this.oclStdLibCollection.product(c);
    }

    @Override
    public <T2> TinkerCollection<T2> flatten() {
        maybeLoad();
        return this.oclStdLibCollection.flatten();
    }

    @Override
    public TinkerCollection<E> select(BooleanExpressionEvaluator<E> e) {
        maybeLoad();
        return this.oclStdLibCollection.select(e);
    }

    @Override
    public <T, R> TinkerCollection<T> collect(BodyExpressionEvaluator<R, E> e) {
        maybeLoad();
        return this.oclStdLibCollection.collect(e);
    }

    @Override
    public <R> TinkerCollection<R> collectNested(BodyExpressionEvaluator<R, E> e) {
        maybeLoad();
        return this.oclStdLibCollection.collectNested(e);
    }

    protected boolean validateElementType(E e) {
        if (this.tumlRuntimeProperty.isManyPrimitive() || this.tumlRuntimeProperty.isOnePrimitive()) {
            if (!(e instanceof String) && !(e instanceof Boolean) && !(e instanceof Integer) && !(e instanceof Long) && !(e instanceof Double)) {
                throw new IllegalStateException(String.format("Expected primitive got %s", e.getClass().getName()));
            }
        } else if (this.tumlRuntimeProperty.isManyEnumeration() || this.tumlRuntimeProperty.isOneEnumeration()) {
            if (!(e instanceof TumlEnum)) {
                throw new IllegalStateException(String.format("Expected %s got %s", TumlEnum.class.getName(), e.getClass().getName()));
            }
        } else if (this.tumlRuntimeProperty.getDataTypeEnum() != null) {
            if (!(e.getClass().equals(this.tumlRuntimeProperty.getDataTypeEnum().getType()))) {
                throw new IllegalStateException(String.format("Expected %s got %s", this.tumlRuntimeProperty.getDataTypeEnum().getType().getName(), e.getClass().getName()));
            }
        }
        return true;
    }
}

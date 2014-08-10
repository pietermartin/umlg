package org.umlg.runtime.collection.persistent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.tinkerpop.gremlin.structure.Direction;
import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Property;
import com.tinkerpop.gremlin.structure.Vertex;
import org.joda.time.DateTime;
import org.umlg.runtime.adaptor.TransactionThreadEntityVar;
import org.umlg.runtime.adaptor.TransactionThreadVar;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgAdminGraph;
import org.umlg.runtime.collection.*;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.IterateExpressionAccumulator;
import org.umlg.runtime.collection.ocl.OclStdLibCollection;
import org.umlg.runtime.domain.*;
import org.umlg.runtime.domain.ocl.OclState;
import org.umlg.runtime.types.Password;
import org.umlg.runtime.types.UmlgType;
import org.umlg.runtime.util.UmlgFormatter;

import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseCollection<E> implements UmlgCollection<E>, UmlgRuntimeProperty, OclStdLibCollection<E> {

    protected Collection<E> internalCollection;
    protected OclStdLibCollection<E> oclStdLibCollection;
    protected boolean loaded = false;
    // This is the owner of the collection
    protected UmlgNode owner;
    // This is the vertex of the owner of the collection
    protected Vertex vertex;
    //Created when an element is added to the collection.
    //Used to store the id of an association class instance.
    protected Edge edge;
    protected Class<?> parentClass;
    //The internal map is used to store the vertex representing a primitive or an enumeration
    protected ListMultimap<Object, Vertex> internalVertexMap = ArrayListMultimap.create();
    protected UmlgRuntimeProperty umlgRuntimeProperty;
    protected static final String LABEL_TO_FIRST_HYPER_VERTEX = "LTFHV";
    protected static final String LABEL_TO_LAST_HYPER_VERTEX = "LTLHV";
    protected static final String LABEL_TO_NEXT_HYPER_VERTEX = "LTNHV";
    protected static final String LABEL_TO_ELEMENT_FROM_HYPER_VERTEX = "LTEFHV";

    protected static final String LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE = "LTFEIS";
    protected static final String LABEL_TO_NEXT_IN_SEQUENCE = "LTNEIS";
    protected static final String LABEL_TO_LAST_ELEMENT_IN_SEQUENCE = "LTLEIS";

    public BaseCollection(UmlgRuntimeProperty runtimeProperty) {
        this.umlgRuntimeProperty = runtimeProperty;
    }

    public BaseCollection(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
        super();
        this.owner = owner;
        this.vertex = owner.getVertex();
        this.parentClass = owner.getClass();
        this.umlgRuntimeProperty = runtimeProperty;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void loadFromVertex() {
        if (!isOnePrimitive() && !isOneEnumeration() && getDataTypeEnum() == null) {
            loadManyNotPrimitiveNotDataType();
        } else if (getDataTypeEnum() != null && (isManyToMany() || isOneToMany())) {
            loadManyDataType();
        } else if (getDataTypeEnum() != null) {
            loadOneDataType();
        } else {
            this.vertex.<E>property(getPersistentName()).ifPresent(
                    value -> {
                        if (isOneEnumeration()) {
                            Class<?> c = this.getPropertyType();
                            this.internalCollection.add((E) Enum.valueOf((Class<? extends Enum>) c, (String) value));
                        } else {
                            this.internalCollection.add(value);
                        }
                    }
            );
        }
        this.loaded = true;
    }

    protected Vertex removeFromInternalMap(Object key) {
        List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + key.toString());
        Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
        Vertex vertex = vertexes.get(0);
        this.internalVertexMap.remove(getPersistentName() + key.toString(), vertex);
        return vertex;
    }

    protected void putToInternalMap(Object key, Vertex vertex) {
        this.internalVertexMap.put(getPersistentName() + key.toString(), vertex);
    }

    protected Iterator<Edge> getEdges() {
        if (this.isControllingSide()) {
            return this.vertex.outE(this.getLabel());
        } else {
            return this.vertex.inE(this.getLabel());
        }
    }

    protected Iterator<Edge> getEdges(Vertex v) {
        if (!this.isControllingSide()) {
            return v.outE(this.getLabel());
        } else {
            return v.inE(this.getLabel());
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

    /**
     * This gets invoked from the opposite side in addInternal.
     * It is called before the edge is created so the new element will not be loaded by loadFromVertex
     *
     * @param e the element to add
     * @return true if the element was succesfully added
     */
    @Override
    public boolean inverseAdder(E e) {
        if (!this.loaded) {
            loadFromVertex();
        }
        return this.internalCollection.add(e);
    }

    @Override
    public boolean add(E e) {
        maybeLoad();
        if (isQualified() || isInverseQualified()) {
            validateQualifiedAssociation(e);
        }
        boolean result = this.internalCollection.add(e);
        if (result) {
            if (!(this.owner instanceof UmlgMetaNode)) {
                TransactionThreadEntityVar.setNewEntity(this.owner);
            }

            //This is saved on the collection as the edge is needed on UmlgPropertyAssociationClassSet to set the id of the association class vertex
            this.edge = addInternal(e);

            // Edge can only be null on a one primitive
            if (this.edge == null && !isOnePrimitive() && !isOneEnumeration() && getDataTypeEnum() == null) {
                throw new IllegalStateException("Edge can only be null on isOne which is a String, Integer, Boolean or primitive");
            }

            if (isQualified() || isInverseQualified()) {
                // Can only qualify TinkerNode's
                if (!(e instanceof UmlgNode)) {
                    throw new IllegalStateException("Primitive properties can not be qualified!");
                }
//                addQualifierToIndex(this.edge, (UmlgNode) e);
            }

            if (isOrdered()) {
                addToLinkedList(this.edge);
            }
            if (isInverseOrdered()) {
                // Can only qualify TinkerNode's
                if (!(e instanceof UmlgNode)) {
                    throw new IllegalStateException("Primitive properties can not be qualified!");
                }
                addToInverseLinkedList(this.edge);
            }
        }
        return result;
    }


    protected void addToInverseLinkedList(Edge edge) {
        Direction direction;
        Direction inverseDirection;
        if (isControllingSide()) {
            direction = Direction.OUT;
            inverseDirection = Direction.IN;
        } else {
            direction = Direction.IN;
            inverseDirection = Direction.OUT;
        }
        if (!isInverseUnique()) {
            //Handle duplicates with hyper vertexes
            //Get the new vertex for the element
            Vertex newElementVertex = getVertexForDirection(edge);
            if (newElementVertex.outE(LABEL_TO_LAST_HYPER_VERTEX + getLabel() + inverseDirection).hasNext()) {
                Edge edgeToLastHyperVertex = newElementVertex.outE(LABEL_TO_LAST_HYPER_VERTEX + getLabel() + inverseDirection).next();
                Vertex lastHyperVertex = edgeToLastHyperVertex.inV().next();

                //Check if it is a duplicate
                Edge edgeToLastElement = lastHyperVertex.outE(LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).next();
                Vertex lastVertex = edgeToLastElement.inV().next();
                if (lastVertex.equals(this.vertex)) {
                    lastHyperVertex.addEdge(LABEL_TO_ELEMENT_FROM_HYPER_VERTEX, this.vertex);
                } else {
                    //Create a new hyper vertex
                    Vertex newHyperVertex = UMLG.get().addVertex("hyperVertex");
                    edgeToLastHyperVertex.remove();
                    newElementVertex.addEdge(LABEL_TO_LAST_HYPER_VERTEX + getLabel() + inverseDirection, newHyperVertex);
                    newHyperVertex.addEdge(LABEL_TO_ELEMENT_FROM_HYPER_VERTEX, this.vertex);
                    //add to the linked list
                    lastHyperVertex.addEdge(LABEL_TO_NEXT_HYPER_VERTEX + inverseDirection, newHyperVertex);
                }
            } else {
                //its the first element in the list
                //Create a new hyper vertex
                Vertex newHyperVertex = UMLG.get().addVertex("hyperVertex");
                newElementVertex.addEdge(LABEL_TO_FIRST_HYPER_VERTEX + getLabel() + inverseDirection, newHyperVertex);
                newElementVertex.addEdge(LABEL_TO_LAST_HYPER_VERTEX + getLabel() + inverseDirection, newHyperVertex);
                //Add the new element to the hyper vertex
                newHyperVertex.addEdge(LABEL_TO_ELEMENT_FROM_HYPER_VERTEX, this.vertex);
            }
        } else {
            //Get the new vertex for the element
            Vertex newElementVertex = getVertexForDirection(edge);
            System.out.println("About to call outE " + LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + newElementVertex.id());
            if (newElementVertex.outE(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + newElementVertex.id()).hasNext()) {
                Edge edgeToLastVertex = newElementVertex.outE(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + newElementVertex.id()).next();
                Vertex lastVertex = edgeToLastVertex.inV().next();

                //move the edge to the last vertex
                edgeToLastVertex.remove();

                newElementVertex.addEdge(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + newElementVertex.id(), this.vertex);
                //add the element to the linked list
                //lastVertex here is the previous last vertex. its last edge has been removed, this.vertex is the new last vertex
                lastVertex.addEdge(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + inverseDirection + newElementVertex.id(), this.vertex);
            } else {
                //its the first element in the list
                Edge edgeToFirst = newElementVertex.addEdge(LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + newElementVertex.id(), this.vertex);
                Edge edgeToLast = newElementVertex.addEdge(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + newElementVertex.id(), this.vertex);
                System.out.println("");
            }
        }
    }

    private void removeFromLinkedList(Vertex v) {
        Direction direction;
        Direction inverseDirection;
        if (isControllingSide()) {
            direction = Direction.OUT;
            inverseDirection = Direction.IN;
        } else {
            direction = Direction.IN;
            inverseDirection = Direction.OUT;
        }
        if (!isUnique()) {

            //Handle duplicates
            //Find hyper vertex
            Vertex vertexToRemove = v;
            Vertex hyperVertex = getFirstHyperVertexInListForVertex(vertexToRemove, direction);

            Edge edgeToVertexToRemove = hyperVertex.outE(LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).next();

            //Remove the edge to the hyper vertex
            edgeToVertexToRemove.remove();

            //Check if the are duplicates, i.e. the hyper vertex has remaining edges.
            //If there are duplicates then there is nothing more to do
            if (!hyperVertex.outE(LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).hasNext()) {
                //No duplicates so remove the hyper vertex and fix the linked list

                //Check if it is the first in the linked list
                if (hyperVertex.inE(LABEL_TO_NEXT_HYPER_VERTEX + direction).hasNext()) {
                    //Not the first in the linked list
                    Edge edgeToPreviousHyperVertex = hyperVertex.inE(LABEL_TO_NEXT_HYPER_VERTEX + direction).next();
                    Vertex previousHyperVertex = edgeToPreviousHyperVertex.outV().next();

                    if (hyperVertex.outE(LABEL_TO_NEXT_HYPER_VERTEX + direction).hasNext()) {
                        //Not the last
                        Edge edgeToNextHyperVertex = hyperVertex.outE(LABEL_TO_NEXT_HYPER_VERTEX + direction).next();
                        Vertex nextHyperVertex = edgeToNextHyperVertex.inV().next();
                        //Add in a link between previous and next
                        hyperVertex.remove();
                        previousHyperVertex.addEdge(LABEL_TO_NEXT_HYPER_VERTEX + direction, nextHyperVertex);
                    } else {
                        //Its the last, i.e. make the previous point to the parent
                        hyperVertex.remove();
                        this.vertex.addEdge(LABEL_TO_LAST_HYPER_VERTEX + getLabel() + direction, previousHyperVertex);
                    }
                } else {
                    //The first in the linked list, make the next the first
                    //Check if it is last
                    if (hyperVertex.outE(LABEL_TO_NEXT_HYPER_VERTEX + direction).hasNext()) {
                        //Not last
                        Edge edgeToNextHyperVertex = hyperVertex.outE(LABEL_TO_NEXT_HYPER_VERTEX + direction).next();
                        Vertex nextHyperVertex = edgeToNextHyperVertex.inV().next();
                        this.vertex.addEdge(LABEL_TO_FIRST_HYPER_VERTEX + getLabel() + direction, nextHyperVertex);
                        hyperVertex.remove();
                    } else {
                        //Last
                        //By this time it must be the only element in the list
                        hyperVertex.remove();
                    }
                }
            }
        } else {
            //No duplicates to handle, i.e the collection is an ordered set
            Vertex vertexToRemove = v;
            //Check if it is first, i.e. see if it has a previous vertex
            if (vertexToRemove.inE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.vertex.id()).hasNext()) {
                //It is not first
                Edge edgeToPrevious = vertexToRemove.inE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.vertex.id()).next();
                Vertex previousVertex = edgeToPrevious.outV().next();
                edgeToPrevious.remove();
                //Check if it is last
                if (vertexToRemove.outE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.vertex.id()).hasNext()) {
                    //Not last
                    Edge edgeToNext = vertexToRemove.outE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.vertex.id()).next();
                    Vertex nextVertex = edgeToNext.inV().next();
                    edgeToNext.remove();
                    previousVertex.addEdge(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.vertex.id(), nextVertex);
                } else {
                    //Last,
                    //previous becomes to last
                    Edge edgeToLast = this.vertex.outE(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.vertex.id()).next();
                    edgeToLast.remove();
                    this.vertex.addEdge(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.vertex.id(), previousVertex);
                }
            } else {
                //It is first
                Edge edgeToFirst = this.vertex.outE(LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.vertex.id()).next();
                edgeToFirst.remove();
                //Check is it is last
                if (vertexToRemove.outE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.vertex.id()).hasNext()) {
                    //Not last
                    //Move the edge to first
                    Edge edgeToNext = vertexToRemove.outE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + direction + this.vertex.id()).next();
                    Vertex nextVertex = edgeToNext.inV().next();
                    edgeToNext.remove();
                    this.vertex.addEdge(LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.vertex.id(), nextVertex);
                } else {
                    //Last
                    //Only one element in the list
                    Edge edgeToLast = this.vertex.outE(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + direction + this.vertex.id()).next();
                    edgeToLast.remove();
                }
            }
        }
    }

    /**
     * Find all hyper vertexes from vertexToRemove. There can be many as the list could have duplicates.
     * Out of all the hyper vertexes find the earliest one in the list. To do that iterate to the beginning of the list looking to see which hyper vertex is before another.
     *
     * @param vertex
     * @param direction
     * @return
     */
    private Vertex getFirstHyperVertexInListForVertex(Vertex vertex, Direction direction) {
        Set<Vertex> hyperVertexes = new HashSet<>();
        vertex.inE(LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).forEach(
                e -> hyperVertexes.add(e.outV().next())
        );
        //Take any hyper vertex and start iterating to the beginning.
        Vertex firstHyperVertex = hyperVertexes.iterator().next();
        hyperVertexes.remove(firstHyperVertex);
        Iterator<Edge> iterator = firstHyperVertex.inE(LABEL_TO_NEXT_HYPER_VERTEX + direction);
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            Vertex previousHyperVertex = edge.outV().next();
            if (hyperVertexes.contains(previousHyperVertex)) {
                firstHyperVertex = previousHyperVertex;
                iterator = firstHyperVertex.inE(LABEL_TO_NEXT_HYPER_VERTEX + direction);
                hyperVertexes.remove(previousHyperVertex);
            } else {
                iterator = previousHyperVertex.inE(LABEL_TO_NEXT_HYPER_VERTEX + direction);
            }
        }
        return firstHyperVertex;
    }

    private void removeFromInverseLinkedList(Vertex v) {
        Direction direction;
        Direction inverseDirection;
        if (isControllingSide()) {
            direction = Direction.OUT;
            inverseDirection = Direction.IN;
        } else {
            direction = Direction.IN;
            inverseDirection = Direction.OUT;
        }
        if (!isInverseUnique()) {
            //Handle duplicates
            //Find hyper vertex
            Vertex vertexToRemove = v;
            //Vertex to remove is the parent in a inverse situation
            Edge edgeToHyperVertex = this.vertex.inE(LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).next();
            Vertex hyperVertex = edgeToHyperVertex.outV().next();
            //Remove the edge to the hyper vertex
            edgeToHyperVertex.remove();
            //Check if the are duplicates, i.e. the hyper vertex has remaining edges.
            //If there are duplicates then there is nothing more to do
            if (!hyperVertex.outE(LABEL_TO_ELEMENT_FROM_HYPER_VERTEX).hasNext()) {
                //No duplicates so remove the hyper vertex and fix the linked list

                //Check if it is the first in the linked list
                if (hyperVertex.inE(LABEL_TO_NEXT_HYPER_VERTEX + inverseDirection).hasNext()) {
                    //Not the first in the linked list
                    Edge edgeToPreviousHyperVertex = hyperVertex.inE(LABEL_TO_NEXT_HYPER_VERTEX + inverseDirection).next();
                    Vertex previousHyperVertex = edgeToPreviousHyperVertex.outV().next();

                    if (hyperVertex.outE(LABEL_TO_NEXT_HYPER_VERTEX + inverseDirection).hasNext()) {
                        //Not the last
                        Edge edgeToNextHyperVertex = hyperVertex.outE(LABEL_TO_NEXT_HYPER_VERTEX + inverseDirection).next();
                        Vertex nextHyperVertex = edgeToNextHyperVertex.inV().next();
                        //Add in a link between previous and next
                        hyperVertex.remove();
                        previousHyperVertex.addEdge(LABEL_TO_NEXT_HYPER_VERTEX + inverseDirection, nextHyperVertex);
                    } else {
                        //Its the last, i.e. make the previous point to the parent
                        hyperVertex.remove();
                        vertexToRemove.addEdge(LABEL_TO_LAST_HYPER_VERTEX + getLabel() + inverseDirection, previousHyperVertex);
                    }
                } else {
                    //The first in the linked list, make the next the first
                    //Check if it is last
                    if (hyperVertex.outE(LABEL_TO_NEXT_HYPER_VERTEX + inverseDirection).hasNext()) {
                        //Not last
                        Edge edgeToNextHyperVertex = hyperVertex.outE(LABEL_TO_NEXT_HYPER_VERTEX + inverseDirection).next();
                        Vertex nextHyperVertex = edgeToNextHyperVertex.inV().next();
                        vertexToRemove.addEdge(LABEL_TO_FIRST_HYPER_VERTEX + getLabel() + inverseDirection, nextHyperVertex);
                        hyperVertex.remove();
                    } else {
                        //Last
                        //By this time it must be the only element in the list
                        hyperVertex.remove();
                    }
                }
            }
        } else {
            //No duplicates to handle
            Vertex vertexToRemove = v;
            //this.vertex has the next and previous links to manage in a inverse situation
            //Check if it is first
            if (this.vertex.inE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id()).hasNext()) {
                //It is not first
                Edge edgeToPrevious = this.vertex.inE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id()).next();
                Vertex previousVertex = edgeToPrevious.outV().next();
                edgeToPrevious.remove();
                //Check if it is last
                if (this.vertex.outE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id()).hasNext()) {
                    //Not last
                    Edge edgeToNext = this.vertex.outE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id()).next();
                    Vertex nextVertex = edgeToNext.inV().next();
                    edgeToNext.remove();
                    previousVertex.addEdge(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id(), nextVertex);
                } else {
                    //Last,
                    //previous becomes to last
                    Edge edgeToLast = vertexToRemove.outE(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id()).next();
                    edgeToLast.remove();
                    vertexToRemove.addEdge(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id(), previousVertex);
                }
            } else {
                //It is first
                Edge edgeToFirst = vertexToRemove.outE(LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id()).next();
                edgeToFirst.remove();
                //Check is it is last
                if (this.vertex.outE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id()).hasNext()) {
                    //Not last
                    //Move the edge to first
                    Edge edgeToNext = this.vertex.outE(LABEL_TO_NEXT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id()).next();
                    Vertex nextVertex = edgeToNext.inV().next();
                    edgeToNext.remove();
                    vertexToRemove.addEdge(LABEL_TO_FIRST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id(), nextVertex);
                } else {
                    //Last
                    //Only one element in the list
                    Edge edgeToLast = vertexToRemove.outE(LABEL_TO_LAST_ELEMENT_IN_SEQUENCE + getLabel() + inverseDirection + vertexToRemove.id()).next();
                    edgeToLast.remove();
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
        if (!(e instanceof UmlgNode)) {
            throw new IllegalStateException("Primitive properties can not be qualified!");
        }
        UmlgNode node = (UmlgNode) e;
        if (isQualified()) {
            for (Qualifier qualifier : this.owner.getQualifiers(this.umlgRuntimeProperty, node, false)) {
                validateQualifiedMultiplicity(false, this.vertex, qualifier);
            }
        }
        if (isInverseQualified()) {
            for (Qualifier qualifier : node.getQualifiers(this.umlgRuntimeProperty, this.owner, true)) {
                validateQualifiedMultiplicity(true, node.getVertex(), qualifier);
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        maybeLoad();
        boolean result = this.internalCollection.remove(o);
        if (result) {
            if (!(this.owner instanceof UmlgMetaNode)) {
                TransactionThreadEntityVar.setNewEntity(this.owner);
            }
            @SuppressWarnings("unchecked")
            E e = (E) o;
            Vertex v;
            if (o instanceof UmlgNode) {
                UmlgNode node = (UmlgNode) o;
                v = node.getVertex();

                if (!(this.owner instanceof UmlgMetaNode)) {
                    TransactionThreadEntityVar.setNewEntity(node);
                }

                Set<Edge> edges = UMLG.get().getEdgesBetween(this.vertex, v, this.getLabel());
                for (Edge edge : edges) {
                    if (o instanceof TinkerAuditableNode) {
                        createAudit(e, true);
                    }
                    if (isOrdered()) {
                        removeFromLinkedList(((UmlgNode) o).getVertex());
                    }
                    if (isInverseOrdered()) {
                        removeFromInverseLinkedList(((UmlgNode) o).getVertex());
                    }
                    edge.remove();

                    //TODO optimize this to remove only the node, not initialize the collection
                    node.initialiseProperty(umlgRuntimeProperty, true);
                    //Need to break here for bag logic. There will only be more than one edge in the case of the collection being a bag.
                    break;
                }


            } else if (e.getClass().isEnum() && (isManyToMany() || isOneToMany())) {
                v = removeFromInternalMap(o);
                if (isOrdered()) {
                    removeFromLinkedList(v);
                }
                if (isInverseOrdered()) {
                    removeFromInverseLinkedList(v);
                }
                v.remove();
            } else if ((isOneEnumeration() || isOnePrimitive()) && getDataTypeEnum() == null) {
                this.vertex.property(getPersistentName()).remove();
                if (isOnePrimitivePropertyOfAssociationClass()) {
                    //edgeId can be null when the property is set on association class that is not yet been added to its member ends.
                    Property edgeIdProperty = this.vertex.property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID);
                    if (edgeIdProperty.isPresent()) {
                        Edge edge1 = UMLG.get().e(edgeIdProperty.value());
                        edge1.property(getPersistentName()).remove();
                        //This is here because Titan has the nasty habit of recreating edges and changing the id.
                        this.vertex.property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID, edge1.id().toString());
                    }
                }
            } else if (getDataTypeEnum() != null && (isManyToMany() || isOneToMany())) {
                v = removeFromInternalMap(o.toString());
                if (isOrdered()) {
                    removeFromLinkedList(v);
                }
                if (isInverseOrdered()) {
                    removeFromInverseLinkedList(v);
                }
                v.remove();
            } else if (getDataTypeEnum() != null) {
                this.vertex.property(getPersistentName()).remove();
            } else {
                v = removeFromInternalMap(o);
                if (this.owner instanceof TinkerAuditableNode) {
                    createAudit(e, true);
                }
                if (isOrdered()) {
                    removeFromLinkedList(v);
                }
                if (isInverseOrdered()) {
                    removeFromInverseLinkedList(v);
                }
                v.remove();
            }
        }
        return result;
    }

    protected Edge addInternal(E e) {
        Vertex v = null;
        validateElementType(e);
        if (e instanceof UmlgNode) {
            UmlgNode node = (UmlgNode) e;
            if (!(this.owner instanceof UmlgMetaNode)) {
                TransactionThreadEntityVar.setNewEntity(node);
            }
            v = node.getVertex();
            if (this.isUnique() && (this.isOneToMany() || this.isOneToOne())) {
                // Check that the existing one from the element does not exist,
                // the user must first remove it
                Iterator<Edge> iteratorToOne = getEdges(v);
                if (iteratorToOne.hasNext()) {
                    throw new IllegalStateException(String.format("Property %s has a multiplicity of 1 and is already set. First remove the value before setting it.", new String[]{getInverseQualifiedName()}));
                }
                this.handleInverseSide(node, umlgRuntimeProperty, true, this.owner);
            }
            this.handleInverseSide(node, umlgRuntimeProperty, true, this.owner);
        } else if (e.getClass().isEnum() && (isManyToMany() || isOneToMany())) {
            v = UMLG.get().addVertex();
            v.property(getPersistentName(), ((Enum<?>) e).name());
            v.property("className", e.getClass().getName());
            putToInternalMap(e, v);
        } else if (isOnePrimitive()) {
            this.vertex.property(getPersistentName(), e);
            if (isOnePrimitivePropertyOfAssociationClass()) {
                Property edgeIdProperty = this.vertex.property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID);
                //edgeId can be null when the property is set on association class that is not yet been added to its member ends.
                if (edgeIdProperty.isPresent()) {
                    UMLG.get().e(edgeIdProperty.value()).property(getPersistentName(), e);
                }
            }
        } else if (isOneEnumeration()) {
            this.vertex.property(getPersistentName(), ((Enum<?>) e).name());
            if (isOnePrimitivePropertyOfAssociationClass()) {
                //edgeId can be null when the property is set on association class that is not yet been added to its member ends.
                Property edgeIdProperty = this.vertex.property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID);
                if (edgeIdProperty.isPresent()) {
                    UMLG.get().e(edgeIdProperty.value()).property(getPersistentName(), ((Enum<?>) e).name());
                }
            }
        } else if (getDataTypeEnum() != null && (isManyToMany() || isOneToMany())) {
            v = UMLG.get().addVertex();
            v.property("className", e.getClass().getName());
            setDataTypeOnVertex(v, e);
            putToInternalMap(e, v);
        } else if (getDataTypeEnum() != null) {
            setDataTypeOnVertex(this.vertex, e);
        } else {
            v = UMLG.get().addVertex();
            v.property(getPersistentName(), e);
            v.property("className", e.getClass().getName());
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

    protected void handleInverseSide(UmlgNode node, UmlgRuntimeProperty umlgRuntimeProperty, boolean b, UmlgNode owner) {
        node.inverseAdder(umlgRuntimeProperty, true, this.owner);
    }

    protected Edge createEdge(E e, Vertex v) {
        Edge edge;
        if (this.isControllingSide()) {
            edge = this.vertex.addEdge(this.getLabel(), v);
            if (this.isManyToMany()) {
                edge.property("manyToManyCorrelationInverseTRUE", "SETTED");
            }
        } else {
            edge = v.addEdge(this.getLabel(), this.vertex);
            if (this.isManyToMany()) {
                edge.property("manyToManyCorrelationInverseFALSE", "SETTED");
            }
        }
        return edge;
    }

    private void createPrimitiveAudit(E e) {
        TinkerAuditableNode auditOwner = (TinkerAuditableNode) owner;
        if (TransactionThreadVar.hasNoAuditEntry(owner.getClass().getName() + owner.getUid())) {
            auditOwner.createAuditVertex(false);
        }
        auditOwner.getAuditVertex().property(getLabel(), e);
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
                auditEdge = auditOwner.getAuditVertex().addEdge(this.getLabel(), node.getAuditVertex());
                auditEdge.property("outClass", auditOwner.getClass().getName() + "Audit");
                auditEdge.property("inClass", node.getClass().getName() + "Audit");
            } else {
                auditEdge = node.getAuditVertex().addEdge(this.getLabel(), auditOwner.getAuditVertex());
                auditEdge.property("inClass", auditOwner.getClass().getName() + "Audit");
                auditEdge.property("outClass", node.getClass().getName() + "Audit");
            }
            if (deletion) {
                auditEdge.property("deletedOn", UmlgFormatter.format(new DateTime()));
            }
        } else if (e.getClass().isEnum()) {
            Vertex v = UMLG.get().addVertex();
            v.property(getPersistentName(), ((Enum<?>) e).name());
            Edge auditEdge = auditOwner.getAuditVertex().addEdge(this.getLabel(), v);
            auditEdge.property("outClass", auditOwner.getClass().getName() + "Audit");
            auditEdge.property("inClass", e.getClass().getName());
        } else {
            if (TransactionThreadVar.hasNoAuditEntry(owner.getClass().getName() + e.getClass().getName() + e.toString())) {
                Vertex auditVertex = UMLG.get().addVertex();
                auditVertex.property(getPersistentName(), e);
                TransactionThreadVar.putAuditVertexFalse(owner.getClass().getName() + e.getClass().getName() + e.toString(), auditVertex);
                auditVertex.property("transactionNo", ((UmlgAdminGraph) UMLG.get()).getTransactionCount());
                Edge auditEdge;
                if (isControllingSide()) {
                    auditEdge = auditOwner.getAuditVertex().addEdge(this.getLabel(), auditVertex);
                    auditEdge.property("outClass", this.parentClass.getName());
                    auditEdge.property("inClass", e.getClass().getName() + "Audit");
                } else {
                    auditEdge = auditVertex.addEdge(this.getLabel(), auditOwner.getAuditVertex());
                    auditEdge.property("inClass", this.parentClass.getName());
                    auditEdge.property("outClass", e.getClass().getName() + "Audit");
                }
                if (deletion) {
                    auditEdge.property("transactionNo", ((UmlgAdminGraph) UMLG.get()).getTransactionCount());
                    auditEdge.property("deletedOn", UmlgFormatter.format(new DateTime()));
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
            return edge.inV().next();
        } else {
            return edge.outV().next();
        }
    }

    protected Class<?> getClassToInstantiate(Edge edge) {
        try {
            if (this.isControllingSide()) {
                return Class.forName((String) edge.inV().next().value("className"));
            } else {
                return Class.forName((String) edge.outV().next().value("className"));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected Class<?> getClassToInstantiateFromHyperVertexEdge(Edge hyperVertexEdge) {
        try {
            final Vertex v = hyperVertexEdge.inV().next();
            return Class.forName((String) v.value("className"));
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

    private void validateQualifiedMultiplicity(boolean inverse, Vertex vertex, Qualifier qualifier) {
        if (qualifier.isOne()) {
            long count;
            if (inverse) {
                if (!isControllingSide()) {
                    count = vertex.out(this.getLabel()).has(qualifier.getKey(), qualifier.getValue()).count().next();
                } else {
                    count = vertex.in(this.getLabel()).has(qualifier.getKey(), qualifier.getValue()).count().next();
                }
            } else {
                if (!isControllingSide()) {
                    count = vertex.in(this.getLabel()).has(qualifier.getKey(), qualifier.getValue()).count().next();
                } else {
                    count = vertex.out(this.getLabel()).has(qualifier.getKey(), qualifier.getValue()).count().next();
                }
            }
            if (count > 0) {
                // Add info to exception
                throw new IllegalStateException(String.format("Qualifier fails, qualifier multiplicity is one and an entry for key '%s' and value '%s' already exist",
                        qualifier.getKey(), qualifier.getValue()));
            }
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
                } else if (e instanceof UmlgEnum) {
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
    public boolean isAssociationClassProperty() {
        return this.umlgRuntimeProperty.isAssociationClassProperty();
    }

    @Override
    public boolean isOnePrimitivePropertyOfAssociationClass() {
        return this.umlgRuntimeProperty.isOnePrimitivePropertyOfAssociationClass();
    }

    @Override
    public boolean isOnePrimitive() {
        return this.umlgRuntimeProperty.isOnePrimitive();
    }

    @Override
    public DataTypeEnum getDataTypeEnum() {
        return this.umlgRuntimeProperty.getDataTypeEnum();
    }

    @Override
    public boolean isOneEnumeration() {
        return this.umlgRuntimeProperty.isOneEnumeration();
    }

    @Override
    public boolean isManyPrimitive() {
        return this.umlgRuntimeProperty.isManyPrimitive();
    }

    @Override
    public boolean isManyEnumeration() {
        return this.umlgRuntimeProperty.isManyEnumeration();
    }

    @Override
    public String getLabel() {
        return this.umlgRuntimeProperty.getLabel();
    }

    @Override
    public String getQualifiedName() {
        return this.umlgRuntimeProperty.getQualifiedName();
    }

    @Override
    public String getPersistentName() {
        return this.umlgRuntimeProperty.getPersistentName();
    }

    @Override
    public String getInverseQualifiedName() {
        return this.umlgRuntimeProperty.getInverseQualifiedName();
    }

    @Override
    public boolean isOneToOne() {
        return this.umlgRuntimeProperty.isOneToOne();
    }

    @Override
    public boolean isOneToMany() {
        return this.umlgRuntimeProperty.isOneToMany();
    }

    @Override
    public boolean isManyToOne() {
        return this.umlgRuntimeProperty.isManyToOne();
    }

    @Override
    public boolean isManyToMany() {
        return this.umlgRuntimeProperty.isManyToMany();
    }

    @Override
    public int getUpper() {
        return this.umlgRuntimeProperty.getUpper();
    }

    @Override
    public int getLower() {
        return this.umlgRuntimeProperty.getLower();
    }

    @Override
    public int getInverseUpper() {
        return this.umlgRuntimeProperty.getInverseUpper();
    }

    @Override
    public boolean isControllingSide() {
        return this.umlgRuntimeProperty.isControllingSide();
    }

    @Override
    public boolean isComposite() {
        return this.umlgRuntimeProperty.isComposite();
    }

    @Override
    public boolean isInverseComposite() {
        return this.umlgRuntimeProperty.isInverseComposite();
    }

    @Override
    public boolean isValid(int elementCount) {
        return this.umlgRuntimeProperty.isValid(elementCount);
    }

    @Override
    public boolean isQualified() {
        return this.umlgRuntimeProperty.isQualified();
    }

    @Override
    public boolean isInverseQualified() {
        return this.umlgRuntimeProperty.isInverseQualified();
    }

    @Override
    public boolean isOrdered() {
        if (this.umlgRuntimeProperty.isOrdered() && this.umlgRuntimeProperty.isQualified() && getUpper() == 1) {
            return true;
        } else {
            return this.umlgRuntimeProperty.isOrdered() && (getUpper() == -1 || getUpper() > 1);
        }
    }

    @Override
    public boolean isInverseOrdered() {
        if (this.umlgRuntimeProperty.isInverseOrdered() && this.umlgRuntimeProperty.isInverseQualified() && getUpper() == 1) {
            return true;
        } else {
            return this.umlgRuntimeProperty.isInverseOrdered() && (getInverseUpper() == -1 || getInverseUpper() > 1);
        }
    }

    @Override
    public boolean isUnique() {
        return this.umlgRuntimeProperty.isUnique();
    }

    @Override
    public boolean isInverseUnique() {
        return this.umlgRuntimeProperty.isInverseUnique();
    }

    @Override
    public Class getPropertyType() {
        return this.umlgRuntimeProperty.getPropertyType();
    }


    @Override
    public boolean equals(UmlgCollection<E> c) {
        maybeLoad();
        return this.oclStdLibCollection.equals(c);
    }

    @Override
    public boolean notEquals(UmlgCollection<E> c) {
        maybeLoad();
        return this.oclStdLibCollection.notEquals(c);
    }

    @Override
    public E any(BooleanExpressionEvaluator<E> v) {
        maybeLoad();
        return this.oclStdLibCollection.any(v);
    }

    @Override
    public UmlgSet<E> asSet() {
        maybeLoad();
        return this.oclStdLibCollection.asSet();
    }

    @Override
    public UmlgOrderedSet<E> asOrderedSet() {
        maybeLoad();
        return this.oclStdLibCollection.asOrderedSet();
    }

    @Override
    public UmlgSequence<E> asSequence() {
        maybeLoad();
        return this.oclStdLibCollection.asSequence();
    }

    @Override
    public UmlgBag<E> asBag() {
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
    public Boolean includesAll(UmlgCollection<E> c) {
        maybeLoad();
        return this.oclStdLibCollection.includesAll(c);
    }

    @Override
    public Boolean excludesAll(UmlgCollection<E> c) {
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
    public UmlgSet<?> product(UmlgCollection<E> c) {
        maybeLoad();
        return this.oclStdLibCollection.product(c);
    }

    @Override
    public <T2> UmlgCollection<T2> flatten() {
        maybeLoad();
        return this.oclStdLibCollection.flatten();
    }

    @Override
    public UmlgCollection<E> select(BooleanExpressionEvaluator<E> e) {
        maybeLoad();
        return this.oclStdLibCollection.select(e);
    }

    @Override
    public <T, R> UmlgCollection<T> collect(BodyExpressionEvaluator<R, E> e) {
        maybeLoad();
        return this.oclStdLibCollection.collect(e);
    }

    @Override
    public <R> UmlgCollection<R> collectNested(BodyExpressionEvaluator<R, E> e) {
        maybeLoad();
        return this.oclStdLibCollection.collectNested(e);
    }

    protected boolean validateElementType(E e) {
        if (this.umlgRuntimeProperty.isManyPrimitive() || this.umlgRuntimeProperty.isOnePrimitive()) {
            if (!(e instanceof String) && !(e instanceof Boolean) && !(e instanceof Integer) &&
                    !(e instanceof Long) && !(e instanceof Float) && !(e instanceof Double) &&
                    !(e instanceof Byte) && !(e instanceof Short)) {
                throw new IllegalStateException(String.format("Expected primitive got %s", e.getClass().getName()));
            }
        } else if (this.umlgRuntimeProperty.isManyEnumeration() || this.umlgRuntimeProperty.isOneEnumeration()) {
            if (!(e instanceof UmlgEnum)) {
                throw new IllegalStateException(String.format("Expected %s got %s", UmlgEnum.class.getName(), e.getClass().getName()));
            }
        } else if (this.umlgRuntimeProperty.getDataTypeEnum() != null) {
            if (!(e.getClass().equals(this.umlgRuntimeProperty.getDataTypeEnum().getType()))) {
                throw new IllegalStateException(String.format("Expected %s got %s", this.umlgRuntimeProperty.getDataTypeEnum().getType().getName(), e.getClass().getName()));
            }
        }
        return true;
    }

    private void setDataTypeOnVertex(Vertex v, E e) {
        if (e instanceof UmlgType) {
            ((UmlgType)e).setOnVertex(v, getPersistentName());
        } else {
            v.property(getPersistentName(), UmlgFormatter.format(getDataTypeEnum(), e));
        }
    }

    private void loadOneDataType() {
        this.vertex.property(getPersistentName()).ifPresent(
                s -> {
                    E result;
                    DataTypeEnum dte = getDataTypeEnum();
                    switch (dte) {
                        case Password:
                            Password password = new Password();
                            password.loadFromVertex(this.vertex, getPersistentName());
                            result = (E)password;
                            break;
                        default:
                            result = UmlgFormatter.parse(getDataTypeEnum(), s);
                    }
                    this.internalCollection.add(result);
                }
        );
        this.loaded = true;
    }

    private void loadManyDataType() {
        for (Iterator<Edge> iter = getEdges(); iter.hasNext(); ) {
            Edge edge = iter.next();
            try {
                Vertex v = this.getVertexForDirection(edge);
                loadDataTypeFromVertex(v);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    protected E loadDataTypeFromVertex(Vertex v) {
        E result = null;
        Object s = v.value(getPersistentName());
        if (s != null) {
            result = UmlgFormatter.parse(getDataTypeEnum(), s);
            putToInternalMap(result, v);
            this.internalCollection.add(result);
        }
        return result;
    }

    private void loadManyNotPrimitiveNotDataType() {
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

    protected Vertex vertexForDirection(Edge edge, Direction direction) {
        return (direction == Direction.IN ? edge.inV().next() : edge.outV().next());
    }

    protected Iterator<Edge> edgesForDirection(Vertex vertex, Direction direction, String label) {
        switch (direction) {
            case IN:
                return vertex.inE(getLabel());
            case OUT:
                return vertex.outE(getLabel());
            case BOTH:
                return vertex.bothE(getLabel());
            default:
                throw new IllegalStateException("Unkown direction " + direction.name());

        }

    }
}

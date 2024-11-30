package org.umlg.runtime.collection.persistent;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.*;
import org.umlg.runtime.adaptor.TransactionThreadEntityVar;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.*;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.IterateExpressionAccumulator;
import org.umlg.runtime.collection.ocl.OclStdLibCollection;
import org.umlg.runtime.domain.DataTypeEnum;
import org.umlg.runtime.domain.UmlgMetaNode;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.domain.ocl.OclState;
import org.umlg.runtime.notification.ChangeHolder;
import org.umlg.runtime.notification.UmlgNotificationManager;
import org.umlg.runtime.types.Password;
import org.umlg.runtime.types.UmlgType;
import org.umlg.runtime.util.PathTree;
import org.umlg.runtime.util.UmlgFormatter;

import java.util.*;
import java.util.logging.Logger;

public abstract class BaseCollection<E> implements UmlgCollection<E>, UmlgRuntimeProperty, OclStdLibCollection<E> {

    private static final Logger LOGGER = Logger.getLogger(BaseCollection.class.getPackage().getName());
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
    protected UmlgRuntimeProperty umlgRuntimeProperty;

    protected boolean ignoreInverse = false;

    protected static final String IN_EDGE_SEQUENCE_ID = "inEdgeSequenceId";
    protected static final String OUT_EDGE_SEQUENCE_ID = "outEdgeSequenceId";


    //This is used to set the IN_EDGE_SEQUENCE_ID, OUT_EDGE_SEQUENCE_ID on the edge
    protected int inverseCollectionSize;

    protected PropertyTree propertyTree;

    public BaseCollection(UmlgRuntimeProperty runtimeProperty) {
        this.umlgRuntimeProperty = runtimeProperty;
    }

    public BaseCollection(UmlgNode owner, PropertyTree propertyTree) {
        super();
        this.owner = owner;
        this.vertex = owner.getVertex();
        this.parentClass = owner.getClass();
        this.propertyTree = propertyTree;
        this.umlgRuntimeProperty = propertyTree.getUmlgRuntimeProperty();
    }

    /**
     * This constructor is for for new objects where the collections are empty this loaded should be true as there is nothing to load.
     *
     * @param owner
     * @param propertyTree
     * @param loaded
     */
    public BaseCollection(UmlgNode owner, PropertyTree propertyTree, boolean loaded) {
        super();
        this.owner = owner;
        this.vertex = owner.getVertex();
        this.parentClass = owner.getClass();
        this.propertyTree = propertyTree;
        this.umlgRuntimeProperty = propertyTree.getUmlgRuntimeProperty();
        this.loaded = loaded;
    }

    public BaseCollection(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
        super();
        this.owner = owner;
        this.vertex = owner.getVertex();
        this.parentClass = owner.getClass();
        this.umlgRuntimeProperty = runtimeProperty;
    }

    public BaseCollection(Enum owner, UmlgRuntimeProperty runtimeProperty) {
        super();
        this.parentClass = owner.getClass();
        this.umlgRuntimeProperty = runtimeProperty;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    protected void loadFromVertex() {
        if (UMLG.get().supportsBatchMode() && UMLG.get().isInBatchMode()) {
            LOGGER.warning("In batch mode but collection is not loaded. Collection: " + this.umlgRuntimeProperty.getQualifiedName());
        }
        if (isManyPrimitive()) {
            loadManyPrimitive();
        } else if (isManyEnumeration()) {
            loadManyEnumeration();
        } else if (getDataTypeEnum() != null && (isManyToMany() || isOneToMany())) {
            loadManyDataType();
        } else if (isOnePrimitive()) {
            loadOnePrimitive();
        } else if (isOneEnumeration()) {
            loadOneEnumeration();
        } else if (getDataTypeEnum() != null && (isOneToOne() || isManyToOne())) {
            loadOneDataType();
        } else {
            loadUmlgNodes();
        }
        this.loaded = true;
    }

    private void loadOnePrimitive() {
        VertexProperty<E> property = this.vertex.property(getPersistentName());
        if (property.isPresent() && property.value() != null) {
            this.internalCollection.add(property.value());
        }
    }

    private void loadOneEnumeration() {
        VertexProperty<E> property = this.vertex.<E>property(getPersistentName());
        if (property.isPresent() && property.value() != null) {
            Class<?> c = this.getPropertyType();
            this.internalCollection.add((E) Enum.valueOf((Class<? extends Enum>) c, (String) property.value()));
        }
    }

    protected Iterator<Edge> getEdges() {
        if (this.isControllingSide()) {
            return this.vertex.edges(Direction.OUT, this.getLabel());
        } else {
            return this.vertex.edges(Direction.IN, this.getLabel());
        }
    }

    protected Iterator<Edge> getEdges(Vertex v) {
        if (!this.isControllingSide()) {
            return v.edges(Direction.OUT, this.getLabel());
        } else {
            return v.edges(Direction.IN, this.getLabel());
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
     * @return true if the element was successfully added
     */
    @Override
    public boolean inverseAdder(E e) {
        if (!this.loaded) {
            loadFromVertex();
        }
        return this.internalCollection.add(e);
    }

    @Override
    public boolean addIgnoreInverse(E e) {
        this.ignoreInverse = true;
        boolean result = add(e);
        this.ignoreInverse = false;
        return result;
    }

    @Override
    public boolean add(E e) {
        maybeLoad();
        if (!this.ignoreInverse && (isQualified() || isInverseQualified())) {
            validateQualifiedAssociation(e);
        }
        boolean result = this.internalCollection.add(e);
        if (result) {
            if (this.umlgRuntimeProperty.isChangeListener()) {
                UmlgNotificationManager.INSTANCE.notify(
                        this.umlgRuntimeProperty,
                        ChangeHolder.of(
                                this.owner,
                                this.umlgRuntimeProperty,
                                ChangeHolder.ChangeType.ADD,
                                e
                        )
                );
            }
            if (!(this.owner instanceof UmlgMetaNode)) {
                TransactionThreadEntityVar.setNewEntity(this.owner);
            }

            //This is saved on the collection as the edge is needed on UmlgPropertyAssociationClassSet to set the id of the association class vertex
            this.edge = addInternal(e);

            if (e instanceof UmlgNode && this.edge == null) {
                throw new IllegalStateException("Edge can not be null for a UmlgNode");
            }

            if (isQualified() || isInverseQualified()) {
                // Can only qualify TinkerNode's
                if (!(e instanceof UmlgNode)) {
                    throw new IllegalStateException("Primitive properties can not be qualified!");
                }
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

    private void removeFromLinkedList(Vertex v) {
    }


    private void removeFromInverseLinkedList(Vertex v) {
    }

    /**
     * This must be implemented by the appropriate collection type
     *
     * @param edge
     */
    protected abstract void addToLinkedList(Edge edge);

    protected void addToInverseLinkedList(Edge edge) {
    }

    private void validateQualifiedAssociation(E e) {
        if (!(e instanceof UmlgNode)) {
            throw new IllegalStateException("Primitive properties can not be qualified!");
        }
        UmlgNode node = (UmlgNode) e;
        if (isQualified()) {
            List<Qualifier> qualifiers = this.owner.getQualifiers(this.umlgRuntimeProperty, node, false);
            validateQualifiedMultiplicity(false, this.vertex, qualifiers);
        }
        if (isInverseQualified()) {
            List<Qualifier> qualifiers = node.getQualifiers(this.umlgRuntimeProperty, this.owner, true);
            validateQualifiedMultiplicity(true, node.getVertex(), qualifiers);
        }
    }

    @Override
    public void z_internalClear() {
        Preconditions.checkState(
                this.umlgRuntimeProperty.isOnePrimitive() ||
                        this.umlgRuntimeProperty.isOneEnumeration() ||
                        this.umlgRuntimeProperty.getDataTypeEnum() != null, "BaseCollection.z_internalClear() failed for " + this.umlgRuntimeProperty.getQualifiedName());
        this.internalCollection.clear();
    }

    @Override
    public boolean remove(Object o) {
        maybeLoad();
        boolean result = this.internalCollection.remove(o);
        if (result) {
            if (this.umlgRuntimeProperty.isChangeListener()) {
                UmlgNotificationManager.INSTANCE.notify(
                        this.umlgRuntimeProperty,
                        ChangeHolder.of(
                                this.owner,
                                this.umlgRuntimeProperty,
                                ChangeHolder.ChangeType.REMOVE,
                                o
                        )
                );
            }
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
                    if (isOrdered()) {
                        removeFromLinkedList(((UmlgNode) o).getVertex());
                    }
                    if (isInverseOrdered()) {
                        removeFromInverseLinkedList(((UmlgNode) o).getVertex());
                    }
                    edge.remove();

                    //TODO optimize this to remove only the node, not initialize the collection
                    node.initialiseProperty(umlgRuntimeProperty, true, false);
                    //Need to break here for bag logic. There will only be more than one edge in the case of the collection being a bag.
                    break;
                }


            } else if ((isOneEnumeration() || isOnePrimitive()) && getDataTypeEnum() == null) {
                this.vertex.property(getPersistentName()).remove();
                if (isOnePrimitivePropertyOfAssociationClass()) {
                    //edgeId can be null when the property is set on association class that is not yet been added to its member ends.
                    Property edgeIdProperty = this.vertex.property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID);
                    if (edgeIdProperty.isPresent() && edgeIdProperty.value() != null) {
                        Edge edge1 = UMLG.get().traversal().E(edgeIdProperty.value()).next();
                        edge1.property(getPersistentName()).remove();
                        //This is here because Titan has the nasty habit of recreating edges and changing the id.
                        this.vertex.property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID, edge1.id().toString());
                    }
                }
            } else if (getDataTypeEnum() != null && (isManyToMany() || isOneToMany())) {
                this.vertex.property(getPersistentName(), convertToArrayForRemoval());
            } else if (getDataTypeEnum() != null) {
                this.vertex.property(getPersistentName()).remove();
            } else if (e.getClass().isEnum() && (isManyToMany() || isOneToMany())) {
                this.vertex.property(getPersistentName(), convertToArrayForRemoval());
            } else {
                this.vertex.property(getPersistentName(), convertToArrayForRemoval());
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
                UmlgCollection<?> umlgCollection = node.z_internalGetCollectionFor(this.umlgRuntimeProperty, true);
                if (umlgCollection.isLoaded()) {
                    if (!umlgCollection.isEmpty()) {
                        throw new IllegalStateException(String.format("Property %s has a multiplicity of 1 and is already set. First remove the value before setting it.", new String[]{getInverseQualifiedName()}));
                    }
                } else {
                    Iterator<Edge> iteratorToOne = getEdges(v);
                    if (iteratorToOne.hasNext()) {
                        throw new IllegalStateException(String.format("Property %s has a multiplicity of 1 and is already set. First remove the value before setting it.", new String[]{getInverseQualifiedName()}));
                    }
                }
            }
            if (!this.ignoreInverse) {
                this.handleInverseSide(node, umlgRuntimeProperty, true, this.owner);
                //This can be null on non datatype (classifier) properties that are not a association member end.
                //i.e. just a direct property of a class
                UmlgCollection<?> inverseCollection = node.z_internalGetCollectionFor(this.umlgRuntimeProperty, true);
                this.inverseCollectionSize = (inverseCollection != null ? inverseCollection.size() : 0);
            } else {
                node.initialiseProperty(this.umlgRuntimeProperty, true, false);
            }
        } else if (isOnePrimitive()) {
            this.vertex.property(getPersistentName(), e);
            if (isOnePrimitivePropertyOfAssociationClass()) {
                Property edgeIdProperty = this.vertex.property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID);
                //edgeId can be null when the property is set on association class that is not yet been added to its member ends.
                if (edgeIdProperty.isPresent() && edgeIdProperty.value() != null) {
                    UMLG.get().traversal().E(edgeIdProperty.value()).next().property(getPersistentName(), e);
                }
            }
        } else if (isOneEnumeration()) {
            this.vertex.property(getPersistentName(), ((Enum<?>) e).name());
            if (isOnePrimitivePropertyOfAssociationClass()) {
                //edgeId can be null when the property is set on association class that is not yet been added to its member ends.
                Property edgeIdProperty = this.vertex.property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID);
                if (edgeIdProperty.isPresent() && edgeIdProperty.value() != null) {
                    UMLG.get().traversal().E(edgeIdProperty.value()).next().property(getPersistentName(), ((Enum<?>) e).name());
                }
            }
        } else if (getDataTypeEnum() != null && (isOneToOne() || isManyToOne())) {
            setDataTypeOnVertex(this.vertex, e);
        } else if (e.getClass().isEnum() && (isManyToMany() || isOneToMany())) {
            this.vertex.property(getPersistentName(), convertToArray(e));
        } else {
            this.vertex.property(getPersistentName(), convertToArray(e));
        }
        if (v != null) {
            Edge edge = createEdge(e, v);
            if (e instanceof UmlgNode) {
                ((UmlgNode) e).setEdge(this.umlgRuntimeProperty, edge);
            }
            return edge;
        } else {
            return null;
        }
    }

    protected UmlgRuntimeProperty handleInverseSide(UmlgNode node, UmlgRuntimeProperty umlgRuntimeProperty, boolean b, UmlgNode owner) {
        return node.inverseAdder(umlgRuntimeProperty, true, this.owner);
    }

    protected Edge createEdge(E e, Vertex v) {
        Edge edge;
        if (this.isControllingSide()) {
            edge = this.vertex.addEdge(this.getLabel(), v);
        } else {
            edge = v.addEdge(this.getLabel(), this.vertex);
        }
        return edge;
    }

    protected void maybeLoad() {
        if ((!this.loaded && (this.isOnePrimitive() || isOneEnumeration()))) {
            loadFromVertex();
        } else if (!this.loaded) {
            loadFromVertex();
        }
    }

    protected Vertex getVertexForDirection(Edge edge) {
        if (this.isControllingSide()) {
            return edge.vertices(Direction.IN).next();
        } else {
            return edge.vertices(Direction.OUT).next();
        }
    }

    protected Class<?> getClassToInstantiate(Edge edge) {
        try {
            if (this.isControllingSide()) {
                return Class.forName(edge.vertices(Direction.IN).next().value("className"));
            } else {
                return Class.forName(edge.vertices(Direction.OUT).next().value("className"));
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

    private void validateQualifiedMultiplicity(boolean inverse, Vertex vertex, List<Qualifier> qualifiers) {
        if (qualifiers.get(0).isOne()) {
            StringBuilder qualifierNames = new StringBuilder();
            StringBuilder keys = new StringBuilder();
            StringBuilder values = new StringBuilder();
            long count = 1;
            GraphTraversal traversal;

            if (inverse) {
                if (!isControllingSide()) {
                    traversal = UMLG.get().getUnderlyingGraph().traversal().V(vertex).out(this.getLabel());
                } else {
                    traversal = UMLG.get().getUnderlyingGraph().traversal().V(vertex).in(this.getLabel());
                }
            } else {
                if (!isControllingSide()) {
                    traversal = UMLG.get().getUnderlyingGraph().traversal().V(vertex).in(this.getLabel());
                } else {
                    traversal = UMLG.get().getUnderlyingGraph().traversal().V(vertex).out(this.getLabel());
                }
            }

            for (Qualifier qualifier : qualifiers) {
                qualifierNames.append(qualifier.getUmlgRuntimeProperty().getQualifiedName());
                qualifierNames.append(" ");
                if (qualifier.getValue() == null) {
                    break;
                }
                keys.append(qualifier.getKey());
                values.append(qualifier.getValue() == null ? "null" : qualifier.getValue().toString());
                if (count++ < qualifiers.size()) {
                    keys.append(", ");
                    values.append(", ");
                }
                traversal.has(qualifier.getKey(), qualifier.getValue());
            }
            count = (Long) traversal.count().next();
            if (count > 0) {
                // Add info to exception
                throw new IllegalStateException(
                        String.format(
                                "Qualifier %s fails, qualifier multiplicity is one and an entry for key '%s' and value '%s' already exist",
                                qualifierNames.toString(),
                                keys.toString(),
                                values.toString()));
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
                } else if (e instanceof Enum) {
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
    public String getAssociationClassPropertyName() {
        return this.umlgRuntimeProperty.getAssociationClassPropertyName();
    }

    @Override
    public boolean isOrdered() {
        if (isManyPrimitive() || isManyEnumeration() || (getDataTypeEnum() != null && (isOneToMany() || isManyToMany()))) {
            return false;
        } else {
            return this.umlgRuntimeProperty.isOrdered();
//            if (this.umlgRuntimeProperty.isOrdered() && this.umlgRuntimeProperty.isQualified() && getUpper() == 1) {
//                return true;
//            } else {
//                return this.umlgRuntimeProperty.isOrdered() && (getUpper() == -1 || getUpper() > 1);
//            }
        }
    }

    @Override
    public boolean isInverseOrdered() {
        //primitives, enumerations and data types can not have a ordered inverse
        if (isManyPrimitive() || isManyEnumeration() || (getDataTypeEnum() != null && (isOneToMany() || isManyToMany()))) {
            return false;
        } else {
            return this.umlgRuntimeProperty.isInverseOrdered();
//            if (this.umlgRuntimeProperty.isInverseOrdered() && this.umlgRuntimeProperty.isInverseQualified() && getUpper() == 1) {
//                return true;
//            } else {
//                return this.umlgRuntimeProperty.isInverseOrdered() && (getInverseUpper() == -1 || getInverseUpper() > 1);
//            }
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
    public boolean isChangeListener() {
        return this.umlgRuntimeProperty.isChangeListener();
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

    @Override
    public <R> Boolean isUnique(BodyExpressionEvaluator<R, E> e) {
        maybeLoad();
        return this.oclStdLibCollection.isUnique(e);
    }

    @Override
    public Boolean exists(BooleanExpressionEvaluator<E> v) {
        maybeLoad();
        return this.oclStdLibCollection.exists(v);
    }

    @Override
    public Boolean forAll(BooleanExpressionEvaluator<E> v) {
        maybeLoad();
        return this.oclStdLibCollection.forAll(v);
    }

    protected boolean validateElementType(E e) {
        if (this.umlgRuntimeProperty.isManyPrimitive() || this.umlgRuntimeProperty.isOnePrimitive()) {
            if (!(e instanceof String) && !(e instanceof Boolean) && !(e instanceof Integer) &&
                    !(e instanceof Long) && !(e instanceof Float) && !(e instanceof Double) &&
                    !(e instanceof Byte) && !(e instanceof Short)) {
                throw new IllegalStateException(String.format("Expected primitive got %s", e.getClass().getName()));
            }
        } else if (this.umlgRuntimeProperty.isManyEnumeration() || this.umlgRuntimeProperty.isOneEnumeration()) {
            if (!(e instanceof Enum)) {
                throw new IllegalStateException(String.format("Expected %s got %s", Enum.class.getName(), e.getClass().getName()));
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
            ((UmlgType) e).setOnVertex(v, getPersistentName());
        } else {
            v.property(getPersistentName(), UmlgFormatter.format(getDataTypeEnum(), e));
        }
    }

    private void loadOneDataType() {
        VertexProperty<Object> property = this.vertex.property(getPersistentName());
        if (property.isPresent() && property.value() != null) {
            E result;
            DataTypeEnum dte = getDataTypeEnum();
            if (Objects.requireNonNull(dte) == DataTypeEnum.Password) {
                Password password = new Password();
                password.loadFromVertex(this.vertex, getPersistentName());
                result = (E) password;
            } else {
                result = UmlgFormatter.parse(getDataTypeEnum(), property.value());
            }
            this.internalCollection.add(result);
        }
        this.loaded = true;
    }

    private void loadManyDataType() {
        Property<Object> manyProperty = this.vertex.property(getPersistentName());
        if (manyProperty.isPresent() && manyProperty.value() != null) {
            this.internalCollection.clear();
            Collection<E> coll = UmlgFormatter.convertFromArray(this.umlgRuntimeProperty.getDataTypeEnum(), manyProperty.value());
            this.internalCollection.addAll(coll);
        }
    }

    protected void loadUmlgNodes() {
        List<PathTree> pathTrees = this.propertyTree.traversal(UMLG.get().getUnderlyingGraph(), this.vertex);
        for (PathTree pathTree : pathTrees) {
            try {
                pathTree.loadUmlgNodes(owner, this.propertyTree);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void z_internalAdder(E e) {
        //nulls indicate that there is nothing to load but that the load attempt has been made.
        if (e != null) {
            this.internalCollection.add(e);
        }
        this.loaded = true;
    }

//    protected GraphTraversal<Vertex, Map<String, Element>> getVerticesWithEdge() {
//        if (this.isControllingSide()) {
//            return UMLG.get().getUnderlyingGraph().traversal().V(this.vertex)
//                    .outE(this.getLabel()).as("edge")
//                    .inV().as("vertex")
//                    .<Element>select("edge", "vertex")
//                    .order().by(__.select("edge").by(BaseCollection.IN_EDGE_SEQUENCE_ID), Order.incr);
//        } else {
//            return UMLG.get().getUnderlyingGraph().traversal().V(this.vertex)
//                    .inE(this.getLabel()).as("edge")
//                    .outV().as("vertex")
//                    .<Element>select("edge", "vertex")
//                    .order().by(__.select("edge").by(BaseCollection.OUT_EDGE_SEQUENCE_ID), Order.incr);
//        }
//    }
//
//
//    protected void loadUmlgNodes() {
//        for (Iterator<Vertex> iter = getVertices(); iter.hasNext(); ) {
//            Vertex vertex = iter.next();
//            E node;
//            try {
//                Class<?> c = getClassToInstantiate(vertex);
//                if (c.isEnum()) {
//                    throw new RuntimeException();
//                } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
//                    Method m = c.getDeclaredMethod("getInstance", new Class[0]);
//                    node = (E) m.invoke(null);
//                } else if (UmlgNode.class.isAssignableFrom(c)) {
//                    node = (E) c.getConstructor(Vertex.class).newInstance(vertex);
//                } else {
//                    throw new RuntimeException();
//                }
//                this.internalCollection.add(node);
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }

    protected void loadManyEnumeration() {
        Property<String[]> manyProperty = this.vertex.property(getPersistentName());
        if (manyProperty.isPresent() && manyProperty.value() != null) {
            this.internalCollection.clear();
            String[] value = vertex.value(getPersistentName());
            for (String enumerationAsString : value) {
                E node = (E) Enum.valueOf((Class<? extends Enum>) this.umlgRuntimeProperty.getPropertyType(), enumerationAsString);
                this.internalCollection.add(node);
            }
        }
    }

    protected void loadManyPrimitive() {
        Property<Object> manyProperty = this.vertex.property(getPersistentName());
        if (manyProperty.isPresent() && manyProperty.value() != null) {
            this.internalCollection.clear();
            Class propertyType = this.umlgRuntimeProperty.getPropertyType();
            List<E> list;
            if (propertyType.isAssignableFrom(String.class)) {
                list = Arrays.asList((E[]) (manyProperty.value()));
            } else if (propertyType.isAssignableFrom(Integer.class)) {
                list = Arrays.asList((E[]) ArrayUtils.toObject((int[]) manyProperty.value()));
            } else if (propertyType.isAssignableFrom(Long.class)) {
                list = Arrays.asList((E[]) ArrayUtils.toObject((long[]) manyProperty.value()));
            } else if (propertyType.isAssignableFrom(Double.class)) {
                list = Arrays.asList((E[]) ArrayUtils.toObject((double[]) manyProperty.value()));
            } else if (propertyType.isAssignableFrom(Float.class)) {
                list = Arrays.asList((E[]) ArrayUtils.toObject((float[]) manyProperty.value()));
            } else if (propertyType.isAssignableFrom(Boolean.class)) {
                list = Arrays.asList((E[]) ArrayUtils.toObject((boolean[]) manyProperty.value()));
            } else if (propertyType.isAssignableFrom(Byte.class)) {
                list = Arrays.asList((E[]) ArrayUtils.toObject((byte[]) manyProperty.value()));
            } else {
                throw new IllegalStateException("Unknown propertyType " + propertyType.toString());
            }
            this.internalCollection.addAll(list);
        }
    }

    protected Collection convertToArrayCollection() {
        return new ArrayList<>(this.internalCollection.size() + 1);
    }

    protected Object convertToArray(E e) {
        //the element has already been added to the internalCollection
        if (this.umlgRuntimeProperty.getDataTypeEnum() != null) {
            if (e instanceof UmlgType) {
                throw new RuntimeException();
            } else {
                return UmlgFormatter.convertToArray(getDataTypeEnum(), this.internalCollection);
            }
        } else if (isManyEnumeration()) {
            Collection<String> enumerationAsList = convertToArrayCollection();
            for (E enumeration : this.internalCollection) {
                enumerationAsList.add(((Enum<?>) enumeration).name());
            }
            return enumerationAsList.toArray(new String[enumerationAsList.size()]);
        } else {
            Class propertyType = this.umlgRuntimeProperty.getPropertyType();
            if (propertyType.isAssignableFrom(String.class)) {
                return this.internalCollection.toArray(new String[this.internalCollection.size()]);
            } else if (propertyType.isAssignableFrom(Integer.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Integer[this.internalCollection.size()]));
            } else if (propertyType.isAssignableFrom(Long.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Long[this.internalCollection.size()]));
            } else if (propertyType.isAssignableFrom(Double.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Double[this.internalCollection.size()]));
            } else if (propertyType.isAssignableFrom(Float.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Float[this.internalCollection.size()]));
            } else if (propertyType.isAssignableFrom(Boolean.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Boolean[this.internalCollection.size()]));
            } else if (propertyType.isAssignableFrom(Byte.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Byte[this.internalCollection.size()]));
            } else {
                throw new IllegalStateException("Unknown propertyType " + propertyType.toString());
            }
        }
    }

    protected Object convertToArrayForRemoval() {
        if (this.umlgRuntimeProperty.getDataTypeEnum() != null) {
            return UmlgFormatter.convertToArray(getDataTypeEnum(), this.internalCollection);
        } else if (isManyEnumeration()) {
            Collection<String> enumerationAsList = convertToArrayCollection();
            for (E enumeration : this.internalCollection) {
                enumerationAsList.add(((Enum<?>) enumeration).name());
            }
            return enumerationAsList.toArray(new String[enumerationAsList.size()]);
        } else {
            Class propertyType = this.umlgRuntimeProperty.getPropertyType();
            if (propertyType.isAssignableFrom(String.class)) {
                return this.internalCollection.toArray(new String[this.internalCollection.size()]);
            } else if (propertyType.isAssignableFrom(Integer.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Integer[this.internalCollection.size()]));
            } else if (propertyType.isAssignableFrom(Long.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Long[this.internalCollection.size()]));
            } else if (propertyType.isAssignableFrom(Double.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Double[this.internalCollection.size()]));
            } else if (propertyType.isAssignableFrom(Float.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Float[this.internalCollection.size()]));
            } else if (propertyType.isAssignableFrom(Boolean.class)) {
                return ArrayUtils.toPrimitive(this.internalCollection.toArray(new Boolean[this.internalCollection.size()]));
            } else {
                throw new IllegalStateException("Unknown propertyType " + propertyType.toString());
            }
        }
    }

    protected boolean isEmbedded() {
        return isOnePrimitive() || isManyPrimitive() || isOneEnumeration() || isManyEnumeration() ||
                (getDataTypeEnum() != null && (isOneToMany() || isManyToMany()));
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

}

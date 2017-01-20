package org.umlg.runtime.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.adaptor.TransactionThreadEntityVar;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgExceptionUtilFactory;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.memory.UmlgMemorySet;
import org.umlg.runtime.domain.ocl.OclState;
import org.umlg.runtime.util.UmlgFormatter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseUmlg implements UmlgNode, Serializable {

    private static final long serialVersionUID = 3751023772087546585L;
    protected Vertex vertex;
    protected boolean hasInitBeenCalled = false;
    private Map<UmlgRuntimeProperty, Edge> edgeMap = new ConcurrentHashMap<>();

    public BaseUmlg() {
        super();
    }

    public BaseUmlg(Vertex vertex) {
        super();
        this.vertex = vertex;
        TransactionThreadEntityVar.setNewEntity(this);
        initialiseProperties(false);
    }

    public BaseUmlg(Object id) {
        super();
        //check if it has been deleted
        this.vertex = UMLG.get().traversal().V(id).next();
        TransactionThreadEntityVar.setNewEntity(this);
        initialiseProperties(false);
    }

    public BaseUmlg(Boolean persistent) {
        super();
        Set<UmlgRuntimeProperty> booleanProperties = z_internalBooleanProperties();
        Map<String, Object> properties = new HashMap<>();
        properties.put("className", getClass().getName());
        properties.put("uid", UUID.randomUUID().toString());
        for (UmlgRuntimeProperty booleanProperty : booleanProperties) {
            properties.put(booleanProperty.getLabel(), Boolean.FALSE);
        }
        Map<UmlgRuntimeProperty, Object> primitiveDefaultValueProperties = z_internalDataTypePropertiesWithDefaultValues();
        for (Map.Entry<UmlgRuntimeProperty, Object> umlgRuntimePropertyObjectEntry : primitiveDefaultValueProperties.entrySet()) {
            if (umlgRuntimePropertyObjectEntry.getKey().isOneEnumeration()) {
                properties.put(umlgRuntimePropertyObjectEntry.getKey().getPersistentName(), ((Enum)umlgRuntimePropertyObjectEntry.getValue()).name());
            } else if (umlgRuntimePropertyObjectEntry.getKey().getDataTypeEnum() != null) {
                properties.put(umlgRuntimePropertyObjectEntry.getKey().getPersistentName(), UmlgFormatter.format(umlgRuntimePropertyObjectEntry.getKey().getDataTypeEnum(), umlgRuntimePropertyObjectEntry.getValue()));
            } else {
                properties.put(umlgRuntimePropertyObjectEntry.getKey().getPersistentName(), umlgRuntimePropertyObjectEntry.getValue());
            }
        }
        this.vertex = UMLG.get().addVertex(this.getClass().getName(), properties);
        addToThreadEntityVar();
        initialiseProperties(true);
        for (UmlgRuntimeProperty booleanProperty : booleanProperties) {
            this.z_internalAddToCollection(booleanProperty, false);
        }
        for (Map.Entry<UmlgRuntimeProperty, Object> umlgRuntimePropertyObjectEntry : primitiveDefaultValueProperties.entrySet()) {
            this.z_internalAddToCollection(umlgRuntimePropertyObjectEntry.getKey(), umlgRuntimePropertyObjectEntry.getValue());
        }
        initVariables();
        initDataTypeVariablesWithDefaultValues();
    }

    @Override
    public void doBeforeCommit() {
        //Override to do something exciting!
    }

    public BaseUmlg reload() {
        this.vertex = UMLG.get().traversal().V(this.vertex.id()).next();
        initialiseProperties(false);
        return this;
    }

    public void addToThreadEntityVar() {
        TransactionThreadEntityVar.setNewEntity(this);
    }

    @Override
    public final Object getId() {
        return this.vertex.id();
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public String getName() {
        return getClass().getName() + "[" + getId() + "]";
    }

    public boolean hasInitBeenCalled() {
        return hasInitBeenCalled;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getUid()).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        BaseUmlg rhs = (BaseUmlg) obj;
        try {
            return new EqualsBuilder().append(getUid(), rhs.getUid()).isEquals();
        } catch (RuntimeException e) {
            //this is in case the node has been deleted
            if (UmlgExceptionUtilFactory.getTumlExceptionUtil().isNodeNotFoundException(e)) {
                return false;
            } else {
                throw e;
            }
        }
    }

    @Override
    public boolean notEquals(Object object) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Boolean oclIsNew() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Boolean oclIsUndefined() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Boolean oclIsInvalid() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public <T> T oclAsType(T classifier) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Boolean oclIsTypeOf(Object object) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Boolean oclIsKindOf(Object object) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Boolean oclIsInState(OclState state) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public <T extends Object> Class<T> oclType() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String oclLocale() {
        throw new RuntimeException("Not implemented");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> UmlgSet<E> asSet() {
        UmlgSet<E> result = new UmlgMemorySet<E>();
        result.add((E) this);
        return result;
    }

    protected boolean changed(Object o1, Object o2) {
        if (o1 == null && o2 != null) {
            return false;
        }
        if (o1 != null && o2 == null) {
            return false;
        }
        if (o1 == null && o2 == null) {
            return true;
        }
        return o1.equals(o2);
    }

    @Override
    public void setEdge(UmlgRuntimeProperty umlgRuntimeProperty, Edge edge) {
        this.edgeMap.put(umlgRuntimeProperty, edge);
    }

    @Override
    public Edge getEdge(UmlgRuntimeProperty umlgRuntimeProperty) {
        return this.edgeMap.get(umlgRuntimeProperty);
    }
}

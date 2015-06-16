package org.umlg.runtime.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.adaptor.TransactionThreadEntityVar;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgExceptionUtilFactory;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.memory.UmlgMemorySet;
import org.umlg.runtime.domain.ocl.OclState;

import java.io.Serializable;

public abstract class BaseUmlg implements UmlgNode, Serializable {

    private static final long serialVersionUID = 3751023772087546585L;
    protected Vertex vertex;
    protected boolean hasInitBeenCalled = false;
    private Edge edge;

    public BaseUmlg() {
        super();
    }

    public BaseUmlg(Vertex vertex) {
        super();
        //check if it has been deleted
        vertex.<Boolean>property("_deleted").ifPresent(
                deleted -> {
                    if (deleted) {
                        throw new IllegalStateException("Vertex has been deleted!");
                    }
                }
        );
        this.vertex = vertex;
        TransactionThreadEntityVar.setNewEntity(this);
        initialiseProperties();
    }

    public BaseUmlg(Object id) {
        super();
        //check if it has been deleted
        this.vertex = UMLG.get().traversal().V(id).next();
        Property<Boolean> deletedProperty = this.vertex.property("deleted");
        if (deletedProperty.isPresent() && deletedProperty.value()) {
            throw new IllegalStateException("Vertex has been deleted!");
        }
        TransactionThreadEntityVar.setNewEntity(this);
        initialiseProperties();
    }

    public BaseUmlg(Boolean persistent) {
        super();
        this.vertex = UMLG.get().addVertex(this.getClass().getName());
        this.vertex.property("className", getClass().getName());
        addToThreadEntityVar();
        defaultCreate();
        initialiseProperties();
        initVariables();
    }

//    protected GraphTraversal v(Vertex v) {
//        UMLG.get()
//    }

    @Override
    public void doBeforeCommit() {
        //Override to do something exciting!
    }

    public BaseUmlg reload() {
        this.vertex = UMLG.get().traversal().V(this.vertex.id()).next();
        initialiseProperties();
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

    public void defaultCreate() {
        getUid();
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
    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    @Override
    public Edge getEdge() {
        return this.edge;
    }
}

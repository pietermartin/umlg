package org.umlg.runtime.domain;

import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.adaptor.TransactionThreadEntityVar;
import org.umlg.runtime.adaptor.UmlgExceptionUtilFactory;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.memory.UmlgMemorySet;
import org.umlg.runtime.domain.ocl.OclState;

import java.io.Serializable;

public abstract class BaseUmlg implements UmlgNode, Serializable {

    private static final long serialVersionUID = 3751023772087546585L;
    protected Vertex vertex;
    protected boolean hasInitBeenCalled = false;

    public BaseUmlg() {
        super();
    }

    public BaseUmlg(Vertex vertex) {
        super();
        //check if it has been deleted
        Boolean deleted = vertex.getProperty("deleted");
        if (deleted != null && deleted) {
            throw new IllegalStateException("Vertex has been deleted!");
        }
        this.vertex = vertex;
        TransactionThreadEntityVar.setNewEntity(this);
        initialiseProperties();
    }

    public BaseUmlg(Object id) {
        super();
        //check if it has been deleted
        this.vertex = GraphDb.getDb().getVertex(id);
        Boolean deleted = this.vertex.getProperty("deleted");
        if (deleted != null && deleted) {
            throw new IllegalStateException("Vertex has been deleted!");
        }
        TransactionThreadEntityVar.setNewEntity(this);
        initialiseProperties();
    }

    public BaseUmlg(Boolean persistent) {
        super();
        this.vertex = GraphDb.getDb().addVertex(this.getClass().getName());
        this.vertex.setProperty("className", getClass().getName());
        addToThreadEntityVar();
        addEdgeToMetaNode();
        defaultCreate();
        initialiseProperties();
        initVariables();
    }

    public BaseUmlg reload() {
        this.vertex = GraphDb.getDb().getVertex(this.vertex.getId());
        initialiseProperties();
        return this;
    }

    public void addToThreadEntityVar() {
        TransactionThreadEntityVar.setNewEntity(this);
    }

    @Override
    public final Object getId() {
        return this.vertex.getId();
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

}

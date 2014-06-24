package org.umlg.runtime.domain;

import com.tinkerpop.gremlin.structure.Vertex;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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

    public BaseUmlg() {
        super();
    }

    public BaseUmlg(Vertex vertex) {
        super();
        //check if it has been deleted
        vertex.<Boolean>property("deleted").ifPresent(
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
        this.vertex = UMLG.get().v(id);
        Boolean deleted = this.vertex.value("deleted");
        if (deleted != null && deleted) {
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
        addEdgeToMetaNode();
        defaultCreate();
        initialiseProperties();
        initVariables();
    }

    @Override
    public void doBeforeCommit() {
        //Override to do something exciting!
    }

    public BaseUmlg reload() {
        this.vertex = UMLG.get().v(this.vertex.id());
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

}

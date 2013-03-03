package org.tuml.runtime.domain;

import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.tuml.runtime.adaptor.TumlExceptionUtilFactory;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.memory.TumlMemorySet;
import org.tuml.runtime.domain.ocl.OclState;

import java.io.Serializable;

public abstract class BaseTuml implements TumlNode, Serializable {

    private static final long serialVersionUID = 3751023772087546585L;
    protected Vertex vertex;
    protected boolean hasInitBeenCalled = false;

    public BaseTuml() {
        super();
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
        BaseTuml rhs = (BaseTuml) obj;
        try {
            return new EqualsBuilder().append(getUid(), rhs.getUid()).isEquals();
        } catch (RuntimeException e) {
            //this is in case the node has been deleted
            if (TumlExceptionUtilFactory.getTumlExceptionUtil().isNodeNotFoundException(e)) {
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
    public <E> TinkerSet<E> asSet() {
        TinkerSet<E> result = new TumlMemorySet<E>();
        result.add((E) this);
        return result;
    }

}

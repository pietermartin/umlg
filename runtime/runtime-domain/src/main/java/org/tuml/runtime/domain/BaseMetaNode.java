package org.tuml.runtime.domain;

import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TumlExceptionUtilFactory;

import java.util.UUID;

/**
 * Date: 2013/03/09
 * Time: 8:42 AM
 */
public abstract class BaseMetaNode implements TumlMetaNode {
    protected Vertex vertex;

    public BaseMetaNode() {
        super();
    }

    @Override
    public Vertex getVertex() {
        return vertex;
    }

    public void defaultCreate() {
        getUid();
    }

    public String getName() {
        return getClass().getName() + "[" + getId() + "]";
    }

//    @Override
//    public Long getId() {
//        return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
//    }

    @Override
    public String getUid() {
        String uid = (String) this.vertex.getProperty("uid");
        if ( uid==null || uid.trim().length()==0 ) {
            uid= UUID.randomUUID().toString();
            this.vertex.setProperty("uid", uid);
        }
        return uid;
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


}

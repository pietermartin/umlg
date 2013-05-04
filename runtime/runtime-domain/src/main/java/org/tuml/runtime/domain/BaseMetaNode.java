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

    @Override
    public final Long getId() {
        return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
    }

    @Override
    public String getUid() {
        String uid = this.vertex.getProperty("uid");
        if ( uid==null || uid.trim().length()==0 ) {
            uid=UUID.randomUUID().toString();
            this.vertex.setProperty("uid", uid);
        }
        return uid;
    }

    public void defaultCreate() {
        getUid();
    }


}

package org.umlg.runtime.domain;


import com.tinkerpop.gremlin.structure.Vertex;

import java.util.UUID;

/**
 * Date: 2013/03/09
 * Time: 8:42 AM
 */
public abstract class BaseMetaNode implements UmlgMetaNode {
    protected Vertex vertex;

    public BaseMetaNode() {
        super();
    }

    @Override
    public Vertex getVertex() {
        return vertex;
    }

    @Override
    public final Object getId() {
        return this.vertex.id();
    }

    @Override
    public String getUid() {
        String uid = this.vertex.value("uid");
        if ( uid==null || uid.trim().length()==0 ) {
            uid=UUID.randomUUID().toString();
            this.vertex.property("uid", uid);
        }
        return uid;
    }

    public void defaultCreate() {
        getUid();
    }


}

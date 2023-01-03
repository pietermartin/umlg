package org.umlg.runtime.domain;


import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;

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
        Property<String> uidProperty = this.vertex.property("uid");
        if ( !uidProperty.isPresent() || (uidProperty.value() == null) || uidProperty.value().trim().length()==0 ) {
            String uid = UUID.randomUUID().toString();
            this.vertex.property("uid", uid);
            return uid;
        } else {
            return uidProperty.value();
        }
    }

    public void defaultCreate() {
        getUid();
    }


}

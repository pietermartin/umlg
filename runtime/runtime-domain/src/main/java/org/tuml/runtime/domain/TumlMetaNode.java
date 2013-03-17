package org.tuml.runtime.domain;

import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.collection.TinkerSet;

/**
 * Date: 2012/12/26
 * Time: 12:16 PM
 */
public interface TumlMetaNode<T> {
    TinkerSet<T> getAllInstances();
    Vertex getVertex();
    Long getId();
    String getUid();
    Long getIdHigh();
}

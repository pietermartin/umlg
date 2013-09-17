package org.umlg.runtime.domain;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.collection.Filter;
import org.umlg.runtime.collection.TinkerSet;

/**
 * Date: 2012/12/26
 * Time: 12:16 PM
 */
public interface TumlMetaNode<T> {
    TinkerSet<T> getAllInstances();
    TinkerSet<T> getAllInstances(Filter<T> filter);
    Vertex getVertex();
    Object getId();
    String getUid();
    Long getIdHigh();
}

package org.umlg.runtime.domain;

import org.apache.tinkerpop.gremlin.structure.Vertex;

/**
 * Date: 2012/12/26
 * Time: 12:16 PM
 */
public interface UmlgMetaNode<T> {
//    UmlgSet<T> getAllInstances();
//    UmlgSet<T> getAllInstances(Filter<T> filter);
    Vertex getVertex();
    Object getId();
    String getUid();
}

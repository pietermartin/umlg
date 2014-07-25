package org.umlg.runtime.domain;

import com.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.Filter;
import org.umlg.runtime.collection.UmlgSet;

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

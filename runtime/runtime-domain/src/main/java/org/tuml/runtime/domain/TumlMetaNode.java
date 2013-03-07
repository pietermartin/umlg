package org.tuml.runtime.domain;

import org.tuml.runtime.collection.TinkerSet;

/**
 * Date: 2012/12/26
 * Time: 12:16 PM
 */
public interface TumlMetaNode<T> extends TumlNode {
    TinkerSet<T> getAllInstances();
}

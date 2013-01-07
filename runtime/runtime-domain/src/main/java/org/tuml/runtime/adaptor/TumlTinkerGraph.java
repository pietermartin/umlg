package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.TransactionalGraph;

/**
 * Date: 2013/01/06
 * Time: 4:43 PM
 */
public interface TumlTinkerGraph extends TransactionalGraph, IndexableGraph, KeyIndexableGraph {

}

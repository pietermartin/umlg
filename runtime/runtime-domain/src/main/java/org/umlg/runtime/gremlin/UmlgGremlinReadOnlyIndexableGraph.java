package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.util.wrappers.WrapperGraph;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyTokens;

/**
 * Date: 2014/03/21
 * Time: 3:12 PM
 */
public class UmlgGremlinReadOnlyIndexableGraph <T extends IndexableGraph> extends UmlgGremlinReadonlyGraph<T> implements IndexableGraph, WrapperGraph<T> {

    public UmlgGremlinReadOnlyIndexableGraph(final T baseIndexableGraph) {
        super(baseIndexableGraph);
    }

    /**
     * @throws UnsupportedOperationException
     */
    public void dropIndex(final String name) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    /**
     * @throws UnsupportedOperationException
     */
    public <T extends Element> Index<T> createIndex(final String indexName, final Class<T> indexClass, final Parameter... indexParameters) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    public <T extends Element> Index<T> getIndex(final String indexName, final Class<T> indexClass) {
        final Index<T> index = this.baseGraph.getIndex(indexName, indexClass);
        return new UmlgGremlinReadOnlyIndex<T>(index);
    }

    public Iterable<Index<? extends Element>> getIndices() {
        return new UmlgGremlinReadOnlyIndexIterable(this.baseGraph.getIndices());
    }

}

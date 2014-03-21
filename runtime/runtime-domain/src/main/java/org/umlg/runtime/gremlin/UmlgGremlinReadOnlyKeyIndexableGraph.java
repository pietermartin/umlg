package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyTokens;

import java.util.Set;

/**
 * Date: 2014/03/21
 * Time: 3:17 PM
 */
public class UmlgGremlinReadOnlyKeyIndexableGraph <T extends KeyIndexableGraph> extends UmlgGremlinReadOnlyIndexableGraph<IndexableGraph> implements KeyIndexableGraph {

    public UmlgGremlinReadOnlyKeyIndexableGraph(final T baseKIGraph) {
        super((IndexableGraph) baseKIGraph);
    }

    /**
     * @throws UnsupportedOperationException
     */
    public <T extends Element> void dropKeyIndex(final String name, Class<T> elementClass) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    /**
     * @throws UnsupportedOperationException
     */
    public <T extends Element> void createKeyIndex(final String name, Class<T> elementClass, final Parameter... indexParameters) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    public <T extends Element> Set<String> getIndexedKeys(Class<T> elementClass) {
        return ((KeyIndexableGraph) this.baseGraph).getIndexedKeys(elementClass);
    }



}

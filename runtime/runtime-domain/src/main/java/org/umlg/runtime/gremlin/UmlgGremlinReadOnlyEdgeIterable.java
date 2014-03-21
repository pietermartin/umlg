package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyEdge;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyTokens;

import java.util.Iterator;

/**
 * Date: 2014/03/21
 * Time: 3:10 PM
 */
public class UmlgGremlinReadOnlyEdgeIterable implements CloseableIterable<Edge> {

    private final Iterable<Edge> iterable;

    protected UmlgGremlinReadOnlyEdgeIterable(final Iterable<Edge> iterable) {
        this.iterable = iterable;
    }

    public Iterator<Edge> iterator() {
        return new Iterator<Edge>() {
            private final Iterator<Edge> itty = iterable.iterator();

            public void remove() {
                throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
            }

            public Edge next() {
                return new UmlgGremlinReadOnlyEdge(this.itty.next());
            }

            public boolean hasNext() {
                return this.itty.hasNext();
            }
        };
    }

    public void close() {
        if (this.iterable instanceof CloseableIterable) {
            ((CloseableIterable) this.iterable).close();
        }
    }

}

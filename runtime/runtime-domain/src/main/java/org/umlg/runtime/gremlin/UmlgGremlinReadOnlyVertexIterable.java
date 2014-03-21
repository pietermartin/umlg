package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyTokens;

import java.util.Iterator;

/**
 * Date: 2014/03/21
 * Time: 3:09 PM
 */
public class UmlgGremlinReadOnlyVertexIterable implements CloseableIterable<Vertex> {

    private final Iterable<Vertex> iterable;

    protected UmlgGremlinReadOnlyVertexIterable(final Iterable<Vertex> iterable) {
        this.iterable = iterable;
    }

    public Iterator<Vertex> iterator() {
        return new Iterator<Vertex>() {
            private Iterator<Vertex> itty = iterable.iterator();

            public void remove() {
                throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
            }


            public Vertex next() {
                return new UmlgGremlinReadOnlyVertex(this.itty.next());
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

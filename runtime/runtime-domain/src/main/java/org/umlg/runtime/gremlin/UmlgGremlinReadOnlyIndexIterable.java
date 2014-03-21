package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyTokens;

import java.util.Iterator;

/**
 * Date: 2014/03/21
 * Time: 3:14 PM
 */
public class UmlgGremlinReadOnlyIndexIterable <T extends Element> implements Iterable<Index<T>> {

    private final Iterable<Index<T>> iterable;

    protected UmlgGremlinReadOnlyIndexIterable(final Iterable<Index<T>> iterable) {
        this.iterable = iterable;
    }

    public Iterator<Index<T>> iterator() {
        return new Iterator<Index<T>>() {
            private final Iterator<Index<T>> itty = iterable.iterator();

            public void remove() {
                throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
            }

            public Index<T> next() {
                return new UmlgGremlinReadOnlyIndex<T>(this.itty.next());
            }

            public boolean hasNext() {
                return this.itty.hasNext();
            }
        };
    }

}

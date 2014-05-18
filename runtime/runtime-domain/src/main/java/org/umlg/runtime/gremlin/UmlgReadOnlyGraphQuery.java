package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.util.DefaultGraphQuery;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Date: 2014/05/18
 * Time: 8:32 AM
 */
public class UmlgReadOnlyGraphQuery extends DefaultGraphQuery implements GraphQuery {

    public UmlgReadOnlyGraphQuery(Graph graph) {
        super(graph);
    }

    @Override
    public Iterable<Edge> edges() {
        return new UmlgDefaultGraphQueryIterable<Edge>(false);
    }

    @Override
    public Iterable<Vertex> vertices() {
        return new UmlgDefaultGraphQueryIterable<Vertex>(true);
    }

    protected class UmlgDefaultGraphQueryIterable<T extends Element> implements Iterable<T> {

        private Iterable<T> iterable = null;

        public UmlgDefaultGraphQueryIterable(final boolean forVertex) {
            this.iterable = (Iterable<T>) getElementIterable(forVertex ? Vertex.class : Edge.class);
        }

        public Iterator<T> iterator() {
            return new Iterator<T>() {
                T nextElement = null;
                final Iterator<T> itty = iterable.iterator();
                long count = 0;

                public boolean hasNext() {
                    if (null != this.nextElement) {
                        return true;
                    } else {
                        return this.loadNext();
                    }
                }

                public T next() {
                    while (true) {
                        if (this.nextElement != null) {
                            final T temp = this.nextElement;
                            this.nextElement = null;
                            return temp;
                        }

                        if (!this.loadNext())
                            throw new NoSuchElementException();
                    }
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                private boolean loadNext() {
                    this.nextElement = null;
                    if (this.count > limit) return false;
                    while (this.itty.hasNext()) {
                        final T element = this.itty.next();
                        boolean filter = false;

                        for (final HasContainer hasContainer : hasContainers) {
                            if (!hasContainer.isLegal(element)) {
                                filter = true;
                                break;
                            }
                        }

                        if (!filter) {
                            if (++this.count <= limit) {
                                this.nextElement = element;
                                return true;
                            }
                        }

                    }
                    return false;
                }
            };
        }

        private Iterable<?> getElementIterable(final Class<? extends Element> elementClass) {
            if (graph instanceof KeyIndexableGraph) {
                final Set<String> keys = getIndexedKeys(elementClass);
                HasContainer container = null;
                for (final HasContainer hasContainer : hasContainers) {
                    if (hasContainer.predicate.equals(com.tinkerpop.blueprints.Compare.EQUAL) && keys.contains(hasContainer.key)) {
                        container = hasContainer;
                        break;
                    }
                }
                if (container != null) {
                    if (Vertex.class.isAssignableFrom(elementClass))
                        return graph.getVertices(container.key, container.value);
                    else
                        return graph.getEdges(container.key, container.value);
                }
            }

            if (Vertex.class.isAssignableFrom(elementClass))
                return new UmlgGremlinReadOnlyVertexIterable(graph.getVertices());
            else
                return new UmlgGremlinReadOnlyEdgeIterable(graph.getEdges());
        }

        protected Set<String> getIndexedKeys(final Class<? extends Element> elementClass) {
            return ((KeyIndexableGraph) graph).getIndexedKeys(elementClass);
        }


    }


}

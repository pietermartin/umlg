package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Predicate;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.pipes.util.PipeHelper;

/**
 * Date: 2014/03/19
 * Time: 7:18 PM
 */
public class AssociationClassPropertyFilter <S extends Element, T> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final String key;
    private final Object value;
    private final Predicate predicate;
    private final Graph graph;

    public AssociationClassPropertyFilter(final Graph graph, final String key, final Predicate predicate, final Object value) {
        this.graph = graph;
        this.key = key;
        this.value = value;
        this.predicate = predicate;
    }

    protected S processNextStart() {
        while (true) {
            final S element = this.starts.next();
            //Find the association class
            Long associationClassVertexId = element.getProperty("associationClassVertexId");
            if (associationClassVertexId != null) {
                Vertex associationClassVertex = this.graph.getVertex(associationClassVertexId);
                if (this.predicate.evaluate(associationClassVertex.getProperty(this.key), this.value)) {
                    return element;
                }
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.key, this.predicate, this.value);
    }

    public String getKey() {
        return this.key;
    }

    public Object getValue() {
        return this.value;
    }

    public Predicate getPredicate() {
        return this.predicate;
    }

}

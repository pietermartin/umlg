package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyVertex;

/**
 * Date: 2014/03/21
 * Time: 3:06 PM
 */
public class UmlgGremlinReadOnlyEdge extends UmlgGremlinReadOnlyElement implements Edge {

    public UmlgGremlinReadOnlyEdge(final Edge baseEdge) {
        super(baseEdge);
    }

    public Vertex getVertex(final Direction direction) throws IllegalArgumentException {
        return new UmlgGremlinReadOnlyVertex(((Edge) baseElement).getVertex(direction));
    }

    public String getLabel() {
        return ((Edge) this.baseElement).getLabel();
    }

}

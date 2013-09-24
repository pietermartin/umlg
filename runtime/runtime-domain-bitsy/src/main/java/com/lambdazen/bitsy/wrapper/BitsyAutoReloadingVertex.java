package com.lambdazen.bitsy.wrapper;

import java.util.Set;

import com.lambdazen.bitsy.BitsyEdge;
import com.lambdazen.bitsy.BitsyGraph;
import com.lambdazen.bitsy.BitsyVertex;
import com.lambdazen.bitsy.tx.BitsyTransaction;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;

public class BitsyAutoReloadingVertex implements Vertex {
    BitsyVertex vertex;
    BitsyGraph graph;

    public BitsyAutoReloadingVertex(BitsyGraph g, BitsyVertex v) {
        this.vertex = v;
        this.graph = g;
    }

    public Vertex getBaseVertex() {
        if (((BitsyTransaction)vertex.getTransaction()).isStopped()) {
            vertex = (BitsyVertex)graph.getVertex(vertex.getId());
        }
        
        return vertex;
    }

    @Override
    public <T> T getProperty(String key) {
        return getBaseVertex().getProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return getBaseVertex().getPropertyKeys();
    }

    @Override
    public void setProperty(String key, Object value) {
        getBaseVertex().setProperty(key, value);
    }

    @Override
    public <T> T removeProperty(String key) {
        return getBaseVertex().removeProperty(key);
    }

    @Override
    public void remove() {
        getBaseVertex().remove();
    }

    @Override
    public Object getId() {
        // Don't reload just for the ID
        return vertex.getId();
    }

    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        return new BitsyAutoReloadingGraph.EdgeIterable(graph, getBaseVertex().getEdges(direction, labels));
    }

    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        return new BitsyAutoReloadingGraph.VertexIterable(graph, getBaseVertex().getVertices(direction, labels));
    }

    @Override
    public VertexQuery query() {
        return new DefaultVertexQuery(this);
    }

    @Override
    public Edge addEdge(String label, Vertex inVertex) {
        return new BitsyAutoReloadingEdge(graph, (BitsyEdge)(getBaseVertex().addEdge(label, inVertex)));
    }

    public int hashCode() {
        return getBaseVertex().hashCode();
    }
    
    public boolean equals(Object o) {
        return (o instanceof BitsyAutoReloadingVertex) && getBaseVertex().equals(((BitsyAutoReloadingVertex)o).getBaseVertex());
    }
}

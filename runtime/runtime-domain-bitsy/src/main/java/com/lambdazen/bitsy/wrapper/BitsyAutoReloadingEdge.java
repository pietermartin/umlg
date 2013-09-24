package com.lambdazen.bitsy.wrapper;

import java.util.Set;

import com.lambdazen.bitsy.BitsyEdge;
import com.lambdazen.bitsy.BitsyGraph;
import com.lambdazen.bitsy.BitsyVertex;
import com.lambdazen.bitsy.tx.BitsyTransaction;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class BitsyAutoReloadingEdge implements Edge {
    BitsyEdge edge;
    BitsyGraph graph;

    public BitsyAutoReloadingEdge(BitsyGraph g, BitsyEdge e) {
        this.edge = e;
        this.graph = g;
    }

    public Edge getBaseEdge() {
        if (((BitsyTransaction)edge.getTransaction()).isStopped()) {
            edge = (BitsyEdge)graph.getEdge(edge.getId());
        }
        
        return edge;
    }

    @Override
    public <T> T getProperty(String key) {
        return getBaseEdge().getProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return getBaseEdge().getPropertyKeys();
    }

    @Override
    public void setProperty(String key, Object value) {
        getBaseEdge().setProperty(key, value);
    }

    @Override
    public <T> T removeProperty(String key) {
        return getBaseEdge().removeProperty(key);
    }

    @Override
    public void remove() {
        getBaseEdge().remove();
    }

    @Override
    public Object getId() {
        // Don't reload just for the ID
        return edge.getId();
    }

    @Override
    public Vertex getVertex(Direction direction) throws IllegalArgumentException {
        return new BitsyAutoReloadingVertex(graph, (BitsyVertex)(getBaseEdge().getVertex(direction)));
    }

    @Override
    public String getLabel() {
        return getBaseEdge().getLabel();
    }

    public int hashCode() {
        return getBaseEdge().hashCode();
    }
    
    public boolean equals(Object o) {
        return (o instanceof BitsyAutoReloadingEdge) && getBaseEdge().equals(((BitsyAutoReloadingEdge)o).getBaseEdge());
    }
}

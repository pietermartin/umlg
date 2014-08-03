package org.umlg.runtime.types;

import com.tinkerpop.gremlin.structure.Vertex;

/**
 * Created by pieter on 2014/08/02.
 */
public interface UmlgType {
    void setOnVertex(Vertex v, String persistentName);
    void loadFromVertex(Vertex v, String persistentName);
}

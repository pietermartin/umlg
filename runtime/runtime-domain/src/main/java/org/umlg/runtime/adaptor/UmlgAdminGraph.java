package org.umlg.runtime.adaptor;

/**
 * Created by pieter on 2014/04/28.
 */
public interface UmlgAdminGraph {

    /**
     * Adds in a singleton super duper node. Represents the application/model.
     * Attached to the root node is each concrete classes meta class.
     */
    void addRoot();

    /**
     * For blueprint's implementation that recycle ids, vertexes are not removed but instead attached to a deletion node.
     * The deletion node is attached to the root node.
     */
    void addDeletionNode();

    //Used for auditing
    void incrementTransactionCount();

    //Used for auditing
    long getTransactionCount();

    long countVertices();

    long countEdges();
}

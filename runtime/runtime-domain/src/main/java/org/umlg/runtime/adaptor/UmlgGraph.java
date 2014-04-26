package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.*;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgApplicationNode;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface UmlgGraph extends TransactionalGraph, KeyIndexableGraph {
    static final String ROOT_VERTEX = "UmlgRootVertex";
    static final String DELETION_VERTEX = "deletionVertex";
    static final String DELETED_VERTEX_EDGE = "deletedVertexEdgeToRoot";
    static final String ROOT_CLASS_NAME = "org.umlg.root.Root";

    /**
     * Adds in a singleton super duper node. Represents the application/model.
     * Attached to the root node is each concrete classes meta class.
     */
    void addRoot();

    /**
     * @return The singleton root node of the application/model
     */
    Vertex getRoot();

    /**
     * returns the singleton Entity that represents the model.
     * @return
     */
    UmlgApplicationNode getUmlgApplicationNode();

    Vertex addVertex(String className);

    Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels);

    long countVertices();

    long countEdges();

    <T> T instantiateClassifier(Object id);

    <T extends PersistentObject> T getFromUniqueIndex(String indexKey, Object indexValue);

    <T extends PersistentObject> List<T> getFromIndex(String indexKey, Object indexValue);

    boolean hasEdgeBeenDeleted(Edge edge);

    boolean isTransactionActive();

    /**
     * To be called at the end of a threads interaction with the graph.
     * This is useful for clearing thread locals.
     * Blueprints implementation have different threading models.
     * For OrientDb this is where the graph will be shutdown.
     */
    void afterThreadContext();

    /**
     * For blueprint's implementation that recycle ids, vertexes are not removed but instead attached to a deletion node.
     * The deletion node is attached to the root node.
     */
    void addDeletionNode();

    String executeQueryToString(UmlgQueryEnum umlgQueryEnum, Object contextId, String query);

    Object executeQuery(UmlgQueryEnum umlgQueryEnum, Object contextId, String query);

    /**
     * Shutdown the db and delete the data files.
     * Remove this instance from the threadvar
     */
    void drop();

    //Used for auditing
    void incrementTransactionCount();

    //Used for auditing
    long getTransactionCount();

}
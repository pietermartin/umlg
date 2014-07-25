package org.umlg.runtime.adaptor;

import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Element;
import com.tinkerpop.gremlin.structure.Graph;
import com.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.Filter;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgApplicationNode;

import java.util.List;
import java.util.Set;

public interface UmlgGraph extends Graph {

    static final String ROOT_VERTEX = "UmlgRootVertex";
    static final String DELETION_VERTEX = "deletionVertex";
    static final String DELETED_VERTEX_EDGE = "deletedVertexEdgeToRoot";
    static final String ROOT_CLASS_NAME = "org.umlg.root.Root";

    void commit();
    void rollback();

    <T extends PersistentObject> UmlgSet<T> allInstances(String className);
    <T extends PersistentObject> UmlgSet<T> allInstances(String className, Filter filter);

    /**
     * returns the singleton Entity that represents the model.
     * @return
     */
    UmlgApplicationNode getUmlgApplicationNode();

    <T extends Element> void createKeyIndex(final String key, final Class<T> elementClass, final Parameter... indexParameters);

    /**
     * Instantiate any concrete classifier with its vertex id.
     * @param id The id of the vertex that represents this object.
     * @param <T> The class of the object to instantiate.
     * @return The entity that is represented by the vertex with id id.
     */
    <T extends PersistentObject> T getEntity(Object id);

    /**
     * uml models that have applied the Umlg::Profile and applied the <<Index>> stereotype to a property of the class
     * will have instances of that class automatically indexed. The key of the index is the qualified name of the property.
     * The value is the value of the property.
     * UMLG <<Index>> stereotype supports UNIQUE and NON_UNIQUE indexes.
     * getFromUniqueIndex returns the single object from the UNIQUE index.
     *
     * @param key The key to search the index on.
     * @param value The value of the indexed property.
     * @param <T> The class of the indexed entity.
     * @return The entity of class T with property with indexedKey that has a value of indexValue.
     */
    <T extends PersistentObject> T getFromUniqueIndex(String key, Object value);

    /**
     * uml models that have applied the Umlg::Profile and applied the <<Index>> stereotype to a property of the class
     * will have instances of that class automatically indexed. The key of the index is the qualified name of the property.
     * The value is the value of the property.
     * UMLG <<Index>> stereotype supports UNIQUE and NON_UNIQUE indexes.
     * getFromIndex returns the all objects from the NON_UNIQUE index.
     *
     * @param key The key to search the index on.
     * @param value The value of the indexed property.
     * @param <T> The class of the indexed entity.
     * @return The entity of class T with property with indexedKey that has a value of indexValue.
     */
    <T extends PersistentObject> List<T> getFromIndex(String key, Object value);

    /**
     * Execute a query.
     * @param umlgQueryEnum UMLG queries can be one of {@link org.umlg.runtime.adaptor.UmlgQueryEnum#OCL},
     *                      {@link org.umlg.runtime.adaptor.UmlgQueryEnum#GROOVY} or {@link org.umlg.runtime.adaptor.UmlgQueryEnum#NATIVE}
     *                      GROOVY includes gremlin queries.
     * @param contextId The contextId of a query is the starting point of the query. If the query is an OCL query then it is the
     *                  id of the Class that will be the context of the OCL query.
     *                  If the query is a Gremlin query then the key word 'self' together with a context id translates into calling g.V(contextId)
     * @param query The text of the query.
     * @return return a String representation of the result of the query.
     */
    String executeQueryToJson(UmlgQueryEnum umlgQueryEnum, Object contextId, String query);

    /**
     * Execute a query.
     * @param umlgQueryEnum UMLG queries can be one of {@link org.umlg.runtime.adaptor.UmlgQueryEnum#OCL},
     *                      {@link org.umlg.runtime.adaptor.UmlgQueryEnum#GROOVY} or {@link org.umlg.runtime.adaptor.UmlgQueryEnum#NATIVE}
     *                      GROOVY includes gremlin queries.
     * @param contextId The contextId of a query is the starting point of the query. If the query is an OCL query then it is the
     *                  id of the Class that will be the context of the OCL query.
     *                  If the query is a Gremlin query then the key word 'self' together with a context id translates into calling g.V(contextId)
     * @param query The text of the query.
     * @return The result of the query.
     */
    <T> T executeQuery(UmlgQueryEnum umlgQueryEnum, Object contextId, String query);

    /**
     * @return The singleton root node of the application/model
     */
    Vertex getRoot();

    Vertex addVertex(String className);

    Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels);

    boolean hasEdgeBeenDeleted(Edge edge);

    boolean isTransactionActive();

    /**
     * To be called at the end of a threads interaction with the graph.
     * This is useful for clearing thread locals.
     * Blueprints implementation have different threading models.
     * For OrientDb this is where the graph will be shutdown.
     */
    void afterThreadContext();

    public static class Exceptions {
        public static IllegalArgumentException classForElementCannotBeNull() {
            return new IllegalArgumentException("elementClass argument cannot be null.");
        }

        public static IllegalArgumentException classIsNotIndexable(final Class clazz) {
            return new IllegalArgumentException("Class is not indexable: " + clazz);
        }
    }


}
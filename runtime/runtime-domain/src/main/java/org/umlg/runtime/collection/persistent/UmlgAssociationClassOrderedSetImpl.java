package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.collection.TinkerCollection;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.TumlMetaNode;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/**
 * This set is used for navigating from a class to its related association classes.
 * Date: 2013/06/22
 * Time: 10:08 AM
 */
public class UmlgAssociationClassOrderedSetImpl<AssociationClassNode> extends TinkerOrderedSetImpl<AssociationClassNode> {

    public UmlgAssociationClassOrderedSetImpl(UmlgNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
    }

    /**
     * This gets invoked from the opposite side in addInternal.
     * It is called before the edge is created so the new element will not be loaded by loadFromVertex
     *
     * @param e
     * @return
     */
    @Override
    public boolean inverseAdder(AssociationClassNode e) {
        if (!this.loaded) {
            //Do not call the regular loadFromVertex
            //Association classes are loaded via the edge to the member end.
            //On inverseAdder that edge exist but the association class vertex has not yet been set as we do not want
            //to load the AssociationClass from the db.
            //The point of the inverseAdder is to keep the current object in memory
            associationClassLoadFromVertex();
        }
        return this.internalCollection.add(e);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void associationClassLoadFromVertex() {
        if (!isOnePrimitive() && getDataTypeEnum() == null) {
            for (Iterator<Edge> iter = getEdges(); iter.hasNext(); ) {
                Edge edge = iter.next();
                if (edge.getPropertyKeys().contains(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID)) {
                    AssociationClassNode node;
                    try {
                        Class<?> c = this.getClassToInstantiate(edge);
                        if (c.isEnum()) {
                            Object value = this.getVertexForDirection(edge).getProperty("value");
                            node = (AssociationClassNode) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                            putToInternalMap(value, this.getVertexForDirection(edge));
                        } else if (UmlgNode.class.isAssignableFrom(c)) {
                            node = (AssociationClassNode) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
                        } else {
                            Object value = this.getVertexForDirection(edge).getProperty("value");
                            node = (AssociationClassNode) value;
                            putToInternalMap(value, this.getVertexForDirection(edge));
                        }
                        this.internalCollection.add(node);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isDateTime()) {
            String s = this.vertex.getProperty(getLabel());
            if (s != null) {
                AssociationClassNode property = (AssociationClassNode) new DateTime(s);
                this.internalCollection.add(property);
            }
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isDate()) {
            String s = this.vertex.getProperty(getLabel());
            if (s != null) {
                AssociationClassNode property = (AssociationClassNode) new LocalDate(s);
                this.internalCollection.add(property);
            }
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isTime()) {
            String s = this.vertex.getProperty(getLabel());
            if (s != null) {
                AssociationClassNode property = (AssociationClassNode) new LocalTime(s);
                this.internalCollection.add(property);
            }
        } else {
            AssociationClassNode property = this.vertex.getProperty(getLabel());
            if (property != null) {
                this.internalCollection.add(property);
            }
        }
        this.loaded = true;
    }

    @Override
    protected void loadNode(Edge edgeToFirstElement, Vertex vertex) {
        AssociationClassNode node;
        try {
            //Get the edges between the vertexToLoad and the owner vertex.
            //Take the first one. If there
            Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.owner.getVertex(), vertex, getLabel());
            //Debug check
            if (edges.size() > 1) {
                throw new IllegalStateException("Only a bag can have multiple edges between vertices!");
            }
            Vertex associationClassVertex = null;
            for (Edge edge : edges) {
                associationClassVertex = GraphDb.getDb().getVertex(edge.getProperty(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID));
            }
            if (associationClassVertex == null) {
                throw new IllegalStateException("Can not find associationClassVertex, this is a bug!");
            }

            Class<?> c = Class.forName((String) associationClassVertex.getProperty("className"));
            if (c.isEnum()) {
                Object value = associationClassVertex.getProperty("value");
                node = (AssociationClassNode) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                this.internalVertexMap.put(value, associationClassVertex);
            } else if (TumlMetaNode.class.isAssignableFrom(c)) {
                Method m = c.getDeclaredMethod("getInstance", new Class[0]);
                node = (AssociationClassNode) m.invoke(null);
            } else if (UmlgNode.class.isAssignableFrom(c)) {
                node = (AssociationClassNode) c.getConstructor(Vertex.class).newInstance(associationClassVertex);
            } else {
                Object value = associationClassVertex.getProperty("value");
                node = (AssociationClassNode) value;
                this.internalVertexMap.put(value, associationClassVertex);
            }
            this.getInternalListOrderedSet().add(node);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean add(AssociationClassNode e) {
        throw new RuntimeException("The collection to an association class is immutable!");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("The collection to an association class is immutable!");
    }

}

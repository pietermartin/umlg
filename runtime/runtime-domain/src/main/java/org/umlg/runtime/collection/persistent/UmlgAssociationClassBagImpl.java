package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Iterator;

/**
 * This set is used for navigating from a class to its related association classes.
 * Date: 2013/06/22
 * Time: 10:08 AM
 */
public class UmlgAssociationClassBagImpl<AssociationClassNode> extends UmlgBagImpl<AssociationClassNode> {

    public UmlgAssociationClassBagImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
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
                if (edge.getPropertyKeys().contains(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID)) {
                    AssociationClassNode node;
                    try {
                        Class<?> c = this.getClassToInstantiate(edge);
                        if (c.isEnum()) {
                            Object value = this.getVertexForDirection(edge).getProperty(getPersistentName());
                            node = (AssociationClassNode) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                            putToInternalMap(node, this.getVertexForDirection(edge));
                        } else if (UmlgNode.class.isAssignableFrom(c)) {
                            node = (AssociationClassNode) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
                        } else {
                            Object value = this.getVertexForDirection(edge).getProperty(getPersistentName());
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
    protected Class<?> getClassToInstantiate(Edge edge) {
        try {
            Vertex associationClassVertex = UMLG.get().getVertex(edge.getProperty(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID));
            return Class.forName((String) associationClassVertex.getProperty("className"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Vertex getVertexForDirection(Edge edge) {
        Vertex associationClassVertex = UMLG.get().getVertex(edge.getProperty(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID));
        return associationClassVertex;
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

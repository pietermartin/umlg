package org.umlg.runtime.collection.persistent;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.util.PathTree;

import java.util.Iterator;
import java.util.List;

/**
 * This set is used for navigating from a class to its related association classes.
 * The association class's vertex id is stored on edge to its member end.
 * Date: 2013/06/22
 * Time: 10:08 AM
 */
public class UmlgAssociationClassSetImpl<AssociationClassNode> extends UmlgSetImpl<AssociationClassNode> implements UmlgAssociationClassSet<AssociationClassNode> {

    private PropertyTree associationClassPropertyTree;

    public UmlgAssociationClassSetImpl(UmlgNode owner, PropertyTree propertyTree, PropertyTree associationClassPropertyTree) {
        super(owner, propertyTree);
        this.associationClassPropertyTree = associationClassPropertyTree;
    }

    public UmlgAssociationClassSetImpl(UmlgNode owner, PropertyTree propertyTree, PropertyTree associationClassPropertyTree, boolean loaded) {
        super(owner, propertyTree, loaded);
        this.associationClassPropertyTree = associationClassPropertyTree;
    }

    public UmlgAssociationClassSetImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    protected void loadUmlgNodes() {
        List<PathTree> pathTrees = this.propertyTree.traversal(UMLG.get().getUnderlyingGraph(), this.vertex);
        for (PathTree pathTree : pathTrees) {
            try {
                pathTree.loadUmlgAssociationClassNodes(owner, this.associationClassPropertyTree);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

//    /**
//     * AssociationClass need to go via the edges as the association class' id is stored there
//     * @return
//     */
//    protected void loadUmlgNodes() {
//        for (Iterator<Edge> iter = getEdges(); iter.hasNext(); ) {
//            Edge edge = iter.next();
//            AssociationClassNode node;
//            try {
//                Class<?> c = this.getClassToInstantiate(edge);
//                if (c.isEnum()) {
//                    throw new RuntimeException();
////                    Object value = this.getVertexForDirection(edge).value(getPersistentName());
////                    node = (AssociationClassNode) Enum.valueOf((Class<? extends Enum>) c, (String) value);
////                    putToInternalMap(node, this.getVertexForDirection(edge));
//                } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
//                    Method m = c.getDeclaredMethod("getInstance", new Class[0]);
//                    node = (AssociationClassNode) m.invoke(null);
//                } else if (UmlgNode.class.isAssignableFrom(c)) {
//                    node = (AssociationClassNode) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
//                } else {
//                    throw new RuntimeException();
////                    Object value = this.getVertexForDirection(edge).value(getPersistentName());
////                    node = (AssociationClassNode) value;
////                    putToInternalMap(value, this.getVertexForDirection(edge));
//                }
//                this.internalCollection.add(node);
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }

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
                if (IteratorUtils.list(edge.properties()).contains(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID)) {
                    AssociationClassNode node;
                    try {
                        Class<?> c = this.getClassToInstantiate(edge);
                        if (c.isEnum()) {
                            throw new RuntimeException();
//                            Object value = this.getVertexForDirection(edge).value(getPersistentName());
//                            node = (AssociationClassNode) Enum.valueOf((Class<? extends Enum>) c, (String) value);
//                            putToInternalMap(node, this.getVertexForDirection(edge));
                        } else if (UmlgNode.class.isAssignableFrom(c)) {
                            node = (AssociationClassNode) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
                        } else {
                            throw new RuntimeException();
//                            Object value = this.getVertexForDirection(edge).value(getPersistentName());
//                            node = (AssociationClassNode) value;
//                            putToInternalMap(value, this.getVertexForDirection(edge));
                        }
                        this.internalCollection.add(node);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isDateTime()) {
            String s = this.vertex.value(getLabel());
            if (s != null) {
                AssociationClassNode property = (AssociationClassNode) new DateTime(s);
                this.internalCollection.add(property);
            }
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isDate()) {
            String s = this.vertex.value(getLabel());
            if (s != null) {
                AssociationClassNode property = (AssociationClassNode) new LocalDate(s);
                this.internalCollection.add(property);
            }
        } else if (getDataTypeEnum() != null && getDataTypeEnum().isTime()) {
            String s = this.vertex.value(getLabel());
            if (s != null) {
                AssociationClassNode property = (AssociationClassNode) new LocalTime(s);
                this.internalCollection.add(property);
            }
        } else {
            AssociationClassNode property = this.vertex.value(getLabel());
            if (property != null) {
                this.internalCollection.add(property);
            }
        }
        this.loaded = true;
    }

    @Override
    protected Class<?> getClassToInstantiate(Edge edge) {
        try {
            String value = edge.value(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID);
            Vertex associationClassVertex = UMLG.get().traversal().V(value).next();
            return Class.forName((String) associationClassVertex.value("className"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Vertex getVertexForDirection(Edge edge) {
        String value = edge.value(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID);
        Vertex associationClassVertex = UMLG.get().traversal().V(value).next();
        return associationClassVertex;
    }

    @Override
    public boolean add(AssociationClassNode node) {
        throw new RuntimeException("The collection to an association class is immutable!");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("The collection to an association class is immutable!");
    }

    /**
     * This add does not created an edge.
     * It is used internally to keep the object in memory.
     *
     * @return
     */
    @Override
    public boolean internalAdd(AssociationClassNode node) {
        return this.internalCollection.add(node);
    }

    /**
     * This does not remove any edges. It only removes the object from memory
     *
     * @param node
     * @return
     */
    @Override
    public boolean internalRemove(AssociationClassNode node) {
        return this.internalCollection.add(node);
    }

}

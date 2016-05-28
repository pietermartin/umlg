package org.umlg.runtime.util;

import com.google.common.base.Preconditions;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.persistent.PropertyTree;
import org.umlg.runtime.domain.UmlgNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Date: 2016/05/25
 * Time: 4:38 PM
 */
public class PathTree {

    private Object element;
    private Set<String> labels;
    private PathTree parent;
    private ListOrderedMap<PathTree, PathTree> children = new ListOrderedMap<>();

    public static List<PathTree> from(GraphTraversal<? extends Element, Path> traversal) {
        ListOrderedMap<PathTree, PathTree> roots = new ListOrderedMap<>();
        PathTree current = null;
        while (traversal.hasNext()) {
            Path path = traversal.next();
            List<Object> objects = path.objects();
            List<Set<String>> labelList = path.labels();
            int count = 0;
            for (Object object : objects) {
                Set<String> labels = labelList.get(count);
                PathTree pathTree = new PathTree();
                pathTree.element = object;
                pathTree.labels = labels;
                if (count == 0) {
                    if (!roots.containsKey(pathTree)) {
                        roots.put(pathTree, pathTree);
                        current = pathTree;
                    } else {
                        current = roots.get(pathTree);
                    }
                } else {
                    if (!current.children.containsKey(pathTree)) {
                        current.children.put(pathTree, pathTree);
                        pathTree.parent = current;
                        current = pathTree;
                    } else {
                        current = current.children.get(pathTree);
                    }
                }
                count++;
            }
        }
        return roots.keyList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof PathTree)) {
            return false;
        }
        return this.element.equals(((PathTree) other).element) && this.labels.equals(((PathTree) other).labels);
    }

    @Override
    public int hashCode() {
        return this.element.hashCode() + this.labels.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        walk(sb, 1);
        return sb.toString();
    }

    private void walk(StringBuilder sb, int count) {
        sb.append(this.element.toString() + " : ");
        sb.append(this.labels.toString());
        for (PathTree pathTree : children.values()) {
            sb.append("\n");
            for (int i = 0; i < count; i++) {
                sb.append("\t");
            }
            pathTree.walk(sb, ++count);
            --count;
        }
    }

    public void loadUmlgNodes(UmlgNode owner, PropertyTree propertyTree) throws Exception {
        loadUmlgNodes(owner, Collections.singletonList(propertyTree));
    }

    public void loadUmlgNodes(UmlgNode owner, List<PropertyTree> propertyTreesToNavigateTo) throws Exception {
        for (PathTree pathTree : this.children.values()) {
            Object object = pathTree.element;
            Set<String> labels = pathTree.labels;
            PropertyTree propertyTreeToNav = null;
            if (object instanceof Vertex) {
                Vertex vertex = (Vertex) object;
                Class<?> c = getClassToInstantiate(vertex);
                UmlgNode umlgNode = (UmlgNode) c.getConstructor(Vertex.class).newInstance(vertex);
                for (PropertyTree propertyTree : propertyTreesToNavigateTo) {
                    if (labels.contains(propertyTree.getUmlgRuntimeProperty().getLabel())) {
                        propertyTreeToNav = propertyTree;
                        break;
                    }
                }
                owner.z_addToInternalCollection(propertyTreeToNav.getUmlgRuntimeProperty(), umlgNode);
                owner = umlgNode;
                Preconditions.checkState(pathTree.parent.element instanceof Edge, "Expected the PathTree.parent to hold an edge!");
                umlgNode.setEdge(propertyTreeToNav.getUmlgRuntimeProperty(), (Edge) pathTree.parent.element);
                pathTree.loadUmlgNodes(owner, new ArrayList<>(propertyTreeToNav.getChildren()));
            } else {
                pathTree.loadUmlgNodes(owner, propertyTreesToNavigateTo);
            }
        }
    }

    public void loadUmlgAssociationClassNodes(UmlgNode owner, PropertyTree propertyTree) throws Exception {
        loadUmlgAssociationClassNodes(owner, Collections.singletonList(propertyTree));
    }

    public void loadUmlgAssociationClassNodes(UmlgNode owner, List<PropertyTree> propertyTreesToNavigateTo) throws Exception {
        for (PathTree pathTree : this.children.values()) {
            Object object = pathTree.element;
            Set<String> labels = pathTree.labels;
            PropertyTree propertyTreeToNav = null;
            if (object instanceof Vertex) {
                Vertex vertex = (Vertex) object;
                for (PropertyTree propertyTree : propertyTreesToNavigateTo) {
                    if (labels.contains(propertyTree.getUmlgRuntimeProperty().getAssociationClassPropertyName())) {
                        propertyTreeToNav = propertyTree;
                        break;
                    }
                }
                Preconditions.checkState(pathTree.parent.element instanceof Edge, "Expected the PathTree.parent to hold an edge!");
                Edge edge = (Edge) pathTree.parent.element;
                Object value = edge.value(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID);
                Vertex associationClassVertex = UMLG.get().traversal().V(value).next();
                Class<?> c = getClassToInstantiate(associationClassVertex);
                UmlgNode umlgNode = (UmlgNode) c.getConstructor(Vertex.class).newInstance(associationClassVertex);
                owner.z_addToInternalCollection(propertyTreeToNav.getUmlgRuntimeProperty(), umlgNode);
                owner = umlgNode;
                umlgNode.setEdge(propertyTreeToNav.getUmlgRuntimeProperty(), edge);
                pathTree.loadUmlgAssociationClassNodes(owner, new ArrayList<>(propertyTreeToNav.getChildren()));
            } else {
                pathTree.loadUmlgAssociationClassNodes(owner, propertyTreesToNavigateTo);
            }
        }
    }

//    @Override
//    protected void loadUmlgNodes() {
//        GraphTraversal<Vertex, Map<String, Element>> traversal = getVerticesWithEdge();
//        while (traversal.hasNext()) {
//            final Map<String, Element> bindings = traversal.next();
//            Edge edge = (Edge) bindings.get("edge");
//            AssociationClassNode node;
//            Object value = edge.value(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID);
//            Vertex associationClassVertex = UMLG.get().traversal().V(value).next();
//            try {
//                Class<?> c = getClassToInstantiate(associationClassVertex);
//                if (UmlgNode.class.isAssignableFrom(c)) {
//                    node = (AssociationClassNode) c.getConstructor(Vertex.class).newInstance(associationClassVertex);
//                    ((UmlgNode) node).setEdge(this.umlgRuntimeProperty, edge);
//                } else {
//                    throw new IllegalStateException("Unexpected class: " + c.getName());
//                }
//                this.internalCollection.add(node);
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }

    private Class<?> getClassToInstantiate(Vertex vertex) {
        try {
            return Class.forName(vertex.value("className"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

package org.umlg.runtime.util;

import com.google.common.base.Preconditions;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.persistent.PropertyTree;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Date: 2016/05/25
 * Time: 4:38 PM
 */
public class PathTree {

    private Object element;
    private PathTree parent;
    private ListOrderedMap<PathTree, PathTree> children = new ListOrderedMap<>();

    private static final ThreadLocal<Map<String, Class<?>>> classMap = new ThreadLocal<Map<String, Class<?>>>() {
        protected Map<String, Class<?>> initialValue() {
            return new HashMap<>();
        }
    };

    private static final ThreadLocal<Map<String, Constructor<?>>> constructorMap = new ThreadLocal<Map<String, Constructor<?>>>() {
        protected Map<String, Constructor<?>> initialValue() {
            return new HashMap<>();
        }
    };

    public static List<PathTree> from(GraphTraversal<? extends Element, Path> traversal) {
        ListOrderedMap<PathTree, PathTree> roots = new ListOrderedMap<>();
        PathTree current = null;
        while (traversal.hasNext()) {
            Path path = traversal.next();
            List<Object> objects = path.objects();
            int count = 0;
            for (Object object : objects) {
                PathTree pathTree = new PathTree();
                pathTree.element = object;
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

    public void loadUmlgNodes(UmlgNode owner, PropertyTree propertyTree) throws Exception {
        loadUmlgNodes(owner, ListOrderedSet.listOrderedSet(Collections.singletonList(propertyTree)), null);
    }

    public UmlgNode loadUmlgNodes(ListOrderedSet<PropertyTree> propertyTreesToNavigateTo) throws Exception {
        Preconditions.checkState(this.element instanceof Vertex);
        Vertex vertex = (Vertex) this.element;
        Class<?> c = getClassToInstantiate(vertex);
        UmlgNode umlgNode = instantiateUmlgNode(vertex, c);
        loadUmlgNodes(umlgNode, propertyTreesToNavigateTo, null);
        return umlgNode;
    }

    private void loadUmlgNodes(UmlgNode owner, ListOrderedSet<PropertyTree> propertyTreesToNavigateTo, String edgeLabel) throws Exception {
        for (PathTree pathTree : this.children.values()) {
            Object object = pathTree.element;
            PropertyTree propertyTreeToNav = null;
            if (object instanceof Vertex) {
                Vertex vertex = (Vertex) object;
                Class<?> c = getClassToInstantiate(vertex);
                UmlgNode umlgNode = instantiateUmlgNode(vertex, c);
                for (PropertyTree propertyTree : propertyTreesToNavigateTo) {
                    if (edgeLabel.equals(propertyTree.getUmlgRuntimeProperty().getLabel())) {
                        propertyTreeToNav = propertyTree;
                        break;
                    }
                }
                owner.z_addToInternalCollection(propertyTreeToNav.getUmlgRuntimeProperty(), umlgNode);
                owner = umlgNode;
                Preconditions.checkState(pathTree.parent.element instanceof Edge, "Expected the PathTree.parent to hold an edge!");
                umlgNode.setEdge(propertyTreeToNav.getUmlgRuntimeProperty(), (Edge) pathTree.parent.element);
                pathTree.loadUmlgNodes(owner, propertyTreeToNav.getChildren(), "");
            } else {
                edgeLabel = ((Edge) object).label();
                pathTree.loadUmlgNodes(owner, propertyTreesToNavigateTo, edgeLabel);
            }
        }
    }

    public void loadUmlgAssociationClassNodes(UmlgNode owner, PropertyTree propertyTree) throws Exception {
        loadUmlgAssociationClassNodes(owner, Collections.singletonList(propertyTree), null);
    }

    private void loadUmlgAssociationClassNodes(UmlgNode owner, List<PropertyTree> propertyTreesToNavigateTo, String edgeLabel) throws Exception {
        for (PathTree pathTree : this.children.values()) {
            Object object = pathTree.element;
            PropertyTree propertyTreeToNav = null;
            if (object instanceof Vertex) {
                Vertex vertex = (Vertex) object;
                for (PropertyTree propertyTree : propertyTreesToNavigateTo) {
                    if (propertyTree.getUmlgRuntimeProperty().getAssociationClassPropertyName().startsWith(edgeLabel + "_")) {
                        propertyTreeToNav = propertyTree;
                        break;
                    }
                }
                Preconditions.checkState(pathTree.parent.element instanceof Edge, "Expected the PathTree.parent to hold an edge!");
                Edge edge = (Edge) pathTree.parent.element;
                Object value = edge.value(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID);
                Vertex associationClassVertex = UMLG.get().traversal().V(value).next();
                Class<?> c = getClassToInstantiate(associationClassVertex);
                UmlgNode umlgNode = instantiateUmlgNode(associationClassVertex, c);
                owner.z_addToInternalCollection(propertyTreeToNav.getUmlgRuntimeProperty(), umlgNode);
                owner = umlgNode;
                umlgNode.setEdge(propertyTreeToNav.getUmlgRuntimeProperty(), edge);
                pathTree.loadUmlgAssociationClassNodes(owner, new ArrayList<>(propertyTreeToNav.getChildren()), edgeLabel);
            } else {
                edgeLabel = ((Edge) object).label();
                pathTree.loadUmlgAssociationClassNodes(owner, propertyTreesToNavigateTo, edgeLabel);
            }
        }
    }

    private Class<?> getClassToInstantiate(Vertex vertex) {
        try {
            String className = vertex.value("className");
            Class<?> clazz = classMap.get().get(className);
            if (clazz == null) {
                clazz = Class.forName(className);
                classMap.get().put(className, clazz);
            }
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private UmlgNode instantiateUmlgNode(Vertex vertex, Class<?> c) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        String className = c.getName();
        Constructor<?> constructor = constructorMap.get().get(className);
        if (constructor == null) {
            constructor = c.getConstructor(Vertex.class);
            constructorMap.get().put(className, constructor);
        }
        return (UmlgNode) constructor.newInstance(vertex);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof PathTree)) {
            return false;
        }
        return this.element.equals(((PathTree) other).element);
    }

    @Override
    public int hashCode() {
        return this.element.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        walkToString(sb, 1);
        return sb.toString();
    }

    private void walkToString(StringBuilder sb, int count) {
        sb.append(this.element.toString() + " : ");
//        sb.append(this.labels.toString());
        for (PathTree pathTree : children.values()) {
            sb.append("\n");
            for (int i = 0; i < count; i++) {
                sb.append("\t");
            }
            pathTree.walkToString(sb, ++count);
            --count;
        }
    }
}

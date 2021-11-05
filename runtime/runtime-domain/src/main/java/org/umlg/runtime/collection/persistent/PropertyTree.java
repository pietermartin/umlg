package org.umlg.runtime.collection.persistent;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.HasContainer;
import org.apache.tinkerpop.gremlin.process.traversal.util.EmptyTraversal;
import org.apache.tinkerpop.gremlin.structure.*;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.util.PathTree;

import java.util.*;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

/**
 * Date: 2016/05/24
 * Time: 8:24 PM
 */
public class PropertyTree {

    //Only the root may have an label
    private String label;
    private UmlgRuntimeProperty umlgRuntimeProperty;
    private PropertyTree parent;
    private ListOrderedSet<PropertyTree> children = new ListOrderedSet<>();
    private Set<HasContainer> hasContainers = new HashSet<>();

    public void addHasContainer(HasContainer hasContainer) {
        this.hasContainers.add(hasContainer);
    }

    private PropertyTree(String label) {
        this.label = label;
    }

    private PropertyTree(UmlgRuntimeProperty umlgRuntimeProperty) {
        this.umlgRuntimeProperty = umlgRuntimeProperty;
    }

    public static PropertyTree from(UmlgRuntimeProperty umlgRuntimeProperty) {
        return new PropertyTree(umlgRuntimeProperty);
    }

    public static PropertyTree from(String label) {
        return new PropertyTree(label);
    }

    public PropertyTree addChild(PropertyTree propertyTree) {
        this.children.add(propertyTree);
        propertyTree.parent = this;
        return propertyTree;
    }

    public PropertyTree addChild(UmlgRuntimeProperty umlgRuntimeProperty) {
        PropertyTree propertyTree = PropertyTree.from(umlgRuntimeProperty);
        return addChild(propertyTree);
    }

    public UmlgRuntimeProperty getUmlgRuntimeProperty() {
        return umlgRuntimeProperty;
    }

    public List<PathTree> traversal(Graph graph) {
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().hasLabel(this.label).as(this.label);
        for (HasContainer hasContainer : this.hasContainers) {
            traversal.has(hasContainer.getKey(), hasContainer.getPredicate());
        }
        List<GraphTraversal<Vertex, Path>> traversals = walk(traversal);
        List<PathTree> pathTree = PathTree.from(traversals);
        return pathTree;
    }

    public List<PathTree> traversal(Graph graph, Vertex vertex) {
        PropertyTree rootPropertyTree = PropertyTree.from("Root");
        rootPropertyTree.addChild(this);
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V(vertex).as("Root");
        List<GraphTraversal<Vertex, Path>> traversals = walk(traversal);
        List<PathTree> pathTree = PathTree.from(traversals);
        return pathTree;
    }

    List<GraphTraversal<Vertex, Path>> walk(GraphTraversal<Vertex, Vertex> traversal) {
        Set<PropertyTree> leafNodes = leafNodes();
        List<GraphTraversal<Vertex, Path>> graphTraversals = new ArrayList<>();
        for (PropertyTree leafNode : leafNodes) {
            GraphTraversal<Vertex, Vertex> leafTraversal = null;
            List<PropertyTree> rootToLeafPropertyTrees = rootToLeaf(leafNode);
            for (PropertyTree node : rootToLeafPropertyTrees) {
                if (leafTraversal == null) {
                    if (node.umlgRuntimeProperty.isControllingSide()) {
                        leafTraversal = __.outE(node.label()).as("e_" + node.umlgRuntimeProperty.getLabel()).inV();
                        for (HasContainer hasContainer : node.hasContainers) {
                            leafTraversal.has(hasContainer.getKey(), hasContainer.getPredicate());
                        }
                        if (node.umlgRuntimeProperty.isOrdered()) {
                            leafTraversal.order()
                                    .by(select("e_" + node.umlgRuntimeProperty.getLabel())
                                            .by(BaseCollection.IN_EDGE_SEQUENCE_ID), Order.asc);
                        }
                    } else {
                        leafTraversal = __.inE(node.label()).as("e_" + node.umlgRuntimeProperty.getLabel()).outV();
                        for (HasContainer hasContainer : node.hasContainers) {
                            leafTraversal.has(hasContainer.getKey(), hasContainer.getPredicate());
                        }
                        if (node.umlgRuntimeProperty.isOrdered()) {
                            leafTraversal.order()
                                    .by(select("e_" + node.umlgRuntimeProperty.getLabel())
                                            .by(BaseCollection.OUT_EDGE_SEQUENCE_ID), Order.asc);
                        }
                        leafTraversal = __.local(__.optional(leafTraversal));
                    }
                } else {
                    if (node.umlgRuntimeProperty.isControllingSide()) {
                        GraphTraversal<Vertex, Vertex> tmpTraversal = __.outE(node.label()).as("e_" + node.umlgRuntimeProperty.getLabel()).inV();
                        for (HasContainer hasContainer : node.hasContainers) {
                            tmpTraversal.has(hasContainer.getKey(), hasContainer.getPredicate());
                        }
                        if (node.umlgRuntimeProperty.isOrdered()) {
                            tmpTraversal.order()
                                    .by(select("e_" + node.umlgRuntimeProperty.getLabel())
                                            .by(BaseCollection.IN_EDGE_SEQUENCE_ID), Order.asc);
                        }
                        leafTraversal.local(__.optional(tmpTraversal));
                    } else {
                        GraphTraversal<Vertex, Vertex> tmpTraversal = __.inE(node.label()).as("e_" + node.umlgRuntimeProperty.getLabel()).outV();
                        for (HasContainer hasContainer : node.hasContainers) {
                            tmpTraversal.has(hasContainer.getKey(), hasContainer.getPredicate());
                        }
                        if (node.umlgRuntimeProperty.isOrdered()) {
                            tmpTraversal.order()
                                    .by(select("e_" + node.umlgRuntimeProperty.getLabel())
                                            .by(BaseCollection.OUT_EDGE_SEQUENCE_ID), Order.asc);
                        }
                        leafTraversal.local(__.optional(tmpTraversal));
                    }
                }
            }
            if (leafTraversal != null) {
                graphTraversals.add(traversal.asAdmin().clone().local(__.optional(leafTraversal)).path());
            }
        }
        if (graphTraversals.isEmpty()) {
            graphTraversals.add(traversal.path());
        }
        return graphTraversals;
    }

    private List<PropertyTree> rootToLeaf(PropertyTree leafNode) {
        List<PropertyTree> result = new ArrayList<>();
        PropertyTree tmp = leafNode;
        while (tmp.parent != null) {
            result.add(0, tmp);
            tmp = tmp.parent;
        }
        return result;
    }

//    /**
//     * g.V(id).local(
//     * optional(
//     * outE().as('e')
//     * )
//     * )
//     *
//     * @param traversal
//     */
//    void walk(GraphTraversal<Vertex, Vertex> traversal) {
//        Set<PropertyTree> leafNodes = leafNodes();
//        List<GraphTraversal<Vertex, Vertex>> graphTraversals = new ArrayList<>();
//        for (PropertyTree leafNode : leafNodes) {
//            PropertyTree node = leafNode;
//            GraphTraversal<Object, Vertex> leafTraversal = null;
//            while (node.parent != null) {
//                if (leafTraversal == null) {
//                    if (node.umlgRuntimeProperty.isControllingSide()) {
//                        leafTraversal = __.outE(label()).as("e2").inV().order().by(T.label);
//                    } else {
//                        leafTraversal = __.inE(label()).as("e2").inV().order().by(T.label);
//                    }
//                } else {
//                    if (node.umlgRuntimeProperty.isControllingSide()) {
//                        leafTraversal =
//                                __.outE(label()).as("e2").inV().order().by(T.label)
//                                        .local(
//                                                __.optional(
//                                                        leafTraversal
//                                                )
//                                        );
//                    } else {
//                        leafTraversal =
//                                __.inE(label()).as("e2").inV().order().by(T.label)
//                                        .local(
//                                                __.optional(
//                                                        leafTraversal
//                                                )
//                                        );
//                    }
//                }
//                node = node.parent;
//            }
//            graphTraversals.add(__.local(__.optional(leafTraversal)));
//        }
//    }

//    void walk(GraphTraversal<Vertex, Vertex> traversal) {
//        if (!this.children.isEmpty() && this.childrenAreUnique()) {
//            Traversal<Vertex, Vertex> outInnerTraversal = outInnerTraversal();
//            if (!(outInnerTraversal instanceof EmptyTraversal)) {
//                for (PropertyTree child : children) {
//                    child.walk((GraphTraversal<Vertex, Vertex>) outInnerTraversal);
//                }
//            }
//
//            Traversal<Vertex, Vertex> inInnerTraversal = inInnerTraversal();
//            if (!(inInnerTraversal instanceof EmptyTraversal)) {
//                for (PropertyTree child : children) {
//                    child.walk((GraphTraversal<Vertex, Vertex>) inInnerTraversal);
//                }
//            }
//            if (!(outInnerTraversal instanceof EmptyTraversal) && !(inInnerTraversal instanceof EmptyTraversal)) {
//                GraphTraversal<Vertex, Vertex> tmpOut = traversal.asAdmin().clone().optional(outInnerTraversal);
//                GraphTraversal<Vertex, Vertex> tmpIn = traversal.asAdmin().clone().optional(inInnerTraversal);
//                traversal.union(tmpOut, tmpIn);
//            } else if (!(outInnerTraversal instanceof EmptyTraversal)) {
//                traversal.optional(outInnerTraversal);
//            } else if (!(inInnerTraversal instanceof EmptyTraversal)) {
//                traversal.optional(inInnerTraversal);
//            } else {
//                throw new IllegalStateException();
//            }
//        }
//    }

    private Traversal<Vertex, Vertex> outInnerTraversal() {
        String[] labels = outLabels();
        Traversal<Vertex, Vertex> innerTraversal;
        if (labels.length == 1) {
            innerTraversal = __.<Vertex>toE(Direction.OUT, labels).as("e_" + labels[0]).<Edge>otherV();
            Set<HasContainer> hasContainers = getChildrenHasContainers();
            for (HasContainer hasContainer : hasContainers) {
                ((GraphTraversal<?, ?>) innerTraversal).has(hasContainer.getKey(), hasContainer.getPredicate());
            }
        } else if (labels.length > 1) {
            String[] edgeLabels = Arrays.copyOfRange(labels, 1, labels.length);
            for (int i = 0; i < edgeLabels.length; i++) {
                edgeLabels[i] = "e_" + edgeLabels[i];
            }
            innerTraversal = __.<Vertex>toE(Direction.OUT, labels).as("e_" + labels[0], edgeLabels).<Edge>otherV();
            Set<HasContainer> hasContainers = getChildrenHasContainers();
            for (HasContainer hasContainer : hasContainers) {
                ((GraphTraversal<?, ?>) innerTraversal).has(hasContainer.getKey(), hasContainer.getPredicate());
            }
        } else {
            innerTraversal = EmptyTraversal.instance();
        }
        return innerTraversal;
    }

    private Traversal<Vertex, Vertex> inInnerTraversal() {
        String[] labels = inLabels();
        Traversal<Vertex, Vertex> innerTraversal;
        if (labels.length == 1) {
            innerTraversal = __.<Vertex>toE(Direction.IN, labels).as("e_" + labels[0]).<Edge>otherV();
            Set<HasContainer> hasContainers = getChildrenHasContainers();
            for (HasContainer hasContainer : hasContainers) {
                ((GraphTraversal) innerTraversal).has(hasContainer.getKey(), hasContainer.getPredicate());
            }
        } else if (labels.length > 1) {
            String[] edgeLabels = Arrays.copyOfRange(labels, 1, labels.length);
            for (int i = 0; i < edgeLabels.length; i++) {
                edgeLabels[i] = "e_" + edgeLabels[i];
            }
            innerTraversal = __.<Vertex>toE(Direction.IN, labels).as("e_" + labels[0], edgeLabels).<Edge>otherV();
            Set<HasContainer> hasContainers = getChildrenHasContainers();
            for (HasContainer hasContainer : hasContainers) {
                ((GraphTraversal) innerTraversal).has(hasContainer.getKey(), hasContainer.getPredicate());
            }
        } else {
            innerTraversal = EmptyTraversal.instance();
        }
        return innerTraversal;
    }


    private void applyOrder(GraphTraversal<Vertex, Vertex> traversal) {
        if (this.umlgRuntimeProperty != null && this.umlgRuntimeProperty.isOrdered()) {
            traversal = traversal.order().by(
                    select("e_" + this.umlgRuntimeProperty.getLabel()).by(
                            this.umlgRuntimeProperty.isControllingSide() ? BaseCollection.IN_EDGE_SEQUENCE_ID : BaseCollection.OUT_EDGE_SEQUENCE_ID
                    ), Order.asc);
        }
        for (PropertyTree child : this.children) {
            child.applyOrder(traversal);
        }
    }

    private String[] labels() {
        List<String> result = new ArrayList<>();
        for (PropertyTree child : this.children) {
            result.add(child.label());
        }
        return result.toArray(new String[result.size()]);
    }

    private String[] outLabels() {
        List<String> result = new ArrayList<>();
        for (PropertyTree child : this.children) {
            if (child.getUmlgRuntimeProperty().isControllingSide()) {
                result.add(child.label());
            }
        }
        return result.toArray(new String[result.size()]);
    }

    private String[] inLabels() {
        List<String> result = new ArrayList<>();
        for (PropertyTree child : this.children) {
            if (!child.getUmlgRuntimeProperty().isControllingSide()) {
                result.add(child.label());
            }
        }
        return result.toArray(new String[result.size()]);
    }

    private Set<HasContainer> getChildrenHasContainers() {
        Set<HasContainer> result = new HashSet<>();
        for (PropertyTree child : this.children) {
            if (child.getUmlgRuntimeProperty().isControllingSide() && !child.hasContainers.isEmpty()) {
                result.addAll(child.hasContainers);
            }
        }
        return result;
    }

    private boolean childrenAreUnique() {
        Set<String> labels = new HashSet<>();
        for (PropertyTree child : this.children) {
            if (labels.contains(child.label())) {
                return false;
            } else {
                labels.add(child.label());
            }
        }
        return true;
    }

    public Set<PropertyTree> leafNodes() {
        Set<PropertyTree> leafNodes = new HashSet<>();
        leafNodes(leafNodes);
        return leafNodes;
    }

    private void leafNodes(Set<PropertyTree> leafNodes) {
        if (this.children.isEmpty()) {
            leafNodes.add(this);
        }
        for (PropertyTree child : this.children) {
            child.leafNodes(leafNodes);
        }
    }

    private String label() {
        return this.umlgRuntimeProperty.getLabel();
    }

    public ListOrderedSet<PropertyTree> getChildren() {
        return this.children;
    }

    @Override
    public String toString() {
        if (this.umlgRuntimeProperty == null) {
            return "Root " + label;
        } else {
            return this.umlgRuntimeProperty.getLabel();
        }
    }
}

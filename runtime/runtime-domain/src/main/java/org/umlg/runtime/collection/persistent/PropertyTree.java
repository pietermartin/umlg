package org.umlg.runtime.collection.persistent;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.HasContainer;
import org.apache.tinkerpop.gremlin.process.traversal.util.EmptyTraversal;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
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
        for (HasContainer hasContainer : hasContainers) {
            traversal.has(hasContainer.getKey(), hasContainer.getValue());
        }
        walk(traversal);
//        applyOrder(traversal);
        return PathTree.from(traversal.path());
    }

    public List<PathTree> traversal(Graph graph, Vertex vertex) {
        PropertyTree rootPropertyTree = PropertyTree.from("Root");
        rootPropertyTree.addChild(this);
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V(vertex).as("Root");
        rootPropertyTree.walk(traversal);
        rootPropertyTree.applyOrder(traversal);
        return PathTree.from(traversal.path());
    }


    void walk(GraphTraversal<Vertex, Vertex> traversal) {
        if (!this.children.isEmpty() && childrenAreUnique()) {
            Traversal<Vertex, Vertex> outInnerTraversal = outInnerTraversal();
            if (!(outInnerTraversal instanceof EmptyTraversal)) {
                for (PropertyTree child : children) {
                    child.walk((GraphTraversal<Vertex, Vertex>) outInnerTraversal);
                }
            }

            Traversal<Vertex, Vertex> inInnerTraversal = inInnerTraversal();
            if (!(inInnerTraversal instanceof EmptyTraversal)) {
                for (PropertyTree child : children) {
                    child.walk((GraphTraversal<Vertex, Vertex>) inInnerTraversal);
                }
            }
            if (!(outInnerTraversal instanceof EmptyTraversal) && !(inInnerTraversal instanceof EmptyTraversal)) {
                GraphTraversal<Vertex, Vertex> tmpOut = traversal.asAdmin().clone().optional(outInnerTraversal);
                GraphTraversal<Vertex, Vertex> tmpIn = traversal.asAdmin().clone().optional(inInnerTraversal);
                traversal.union(tmpOut, tmpIn);
            } else if (!(outInnerTraversal instanceof EmptyTraversal)) {
                traversal.optional(outInnerTraversal);
            } else if (!(inInnerTraversal instanceof EmptyTraversal)) {
                traversal.optional(inInnerTraversal);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private Traversal<Vertex, Vertex> outInnerTraversal() {
        String[] labels = outLabels();
        Traversal<Vertex, Vertex> innerTraversal;
        if (labels.length == 1) {
            innerTraversal =  __.<Vertex>toE(Direction.OUT, labels).as("e_" + labels[0]).<Edge>otherV();
            Set<HasContainer> hasContainers = getChildrenHasContainers();
            for (HasContainer hasContainer : hasContainers) {
                ((GraphTraversal)innerTraversal).has(hasContainer.getKey(), hasContainer.getPredicate());
            }
        } else if (labels.length > 1) {
            String[] edgeLabels = Arrays.copyOfRange(labels, 1, labels.length);
            for (int i = 0; i < edgeLabels.length; i++) {
                edgeLabels[i] = "e_" + edgeLabels[i];
            }
            innerTraversal =  __.<Vertex>toE(Direction.OUT, labels).as("e_" + labels[0], edgeLabels).<Edge>otherV();
            Set<HasContainer> hasContainers = getChildrenHasContainers();
            for (HasContainer hasContainer : hasContainers) {
                ((GraphTraversal)innerTraversal).has(hasContainer.getKey(), hasContainer.getPredicate());
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
            innerTraversal =  __.<Vertex>toE(Direction.IN, labels).as("e_" + labels[0]).<Edge>otherV();
            Set<HasContainer> hasContainers = getChildrenHasContainers();
            for (HasContainer hasContainer : hasContainers) {
                ((GraphTraversal)innerTraversal).has(hasContainer.getKey(), hasContainer.getPredicate());
            }
        } else if (labels.length > 1) {
            String[] edgeLabels = Arrays.copyOfRange(labels, 1, labels.length);
            for (int i = 0; i < edgeLabels.length; i++) {
                edgeLabels[i] = "e_" + edgeLabels[i];
            }
            innerTraversal =   __.<Vertex>toE(Direction.IN, labels).as("e_" + labels[0], edgeLabels).<Edge>otherV();
            Set<HasContainer> hasContainers = getChildrenHasContainers();
            for (HasContainer hasContainer : hasContainers) {
                ((GraphTraversal)innerTraversal).has(hasContainer.getKey(), hasContainer.getPredicate());
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
                    ), Order.incr);
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
                for (HasContainer hasContainer : child.hasContainers) {
                    result.add(hasContainer);
                }
            }
        }
        return result;
    }

    private boolean childrenAreUnique() {
        Set<String> labels = new HashSet<>();
        for (PropertyTree child : children) {
            if (labels.contains(child.label())) {
                return false;
            } else {
                labels.add(child.label());
            }
        }
        return true;
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

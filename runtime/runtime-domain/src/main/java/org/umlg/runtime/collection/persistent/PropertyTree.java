package org.umlg.runtime.collection.persistent;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.util.TraversalHelper;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.util.PathTree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

/**
 * Date: 2016/05/24
 * Time: 8:24 PM
 */
public class PropertyTree {

    private UmlgRuntimeProperty umlgRuntimeProperty;
    private PropertyTree parent;
    private Set<PropertyTree> children = new HashSet<>();

    private PropertyTree(UmlgRuntimeProperty umlgRuntimeProperty) {
        this.umlgRuntimeProperty = umlgRuntimeProperty;
    }

    public static PropertyTree from(UmlgRuntimeProperty umlgRuntimeProperty) {
        return new PropertyTree(umlgRuntimeProperty);
    }

    private void addChild(PropertyTree propertyTree) {
        this.children.add(propertyTree);
        propertyTree.parent = this;
    }

    public void addChild(UmlgRuntimeProperty umlgRuntimeProperty) {
        addChild(new PropertyTree(umlgRuntimeProperty));
    }

    public UmlgRuntimeProperty getUmlgRuntimeProperty() {
        return umlgRuntimeProperty;
    }

    public List<PathTree> traversal(Graph graph, Vertex vertex) {
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V(vertex).as("root");
        walk(traversal);
        applyOrder(traversal);
        return PathTree.from(traversal.path());
    }

    private void applyOrder(GraphTraversal<Vertex, Vertex> traversal) {
        if (this.umlgRuntimeProperty.isOrdered()) {
            traversal = traversal.order().by(select("e_" + this.umlgRuntimeProperty.getLabel()).by(
                    this.umlgRuntimeProperty.isControllingSide() ? BaseCollection.IN_EDGE_SEQUENCE_ID : BaseCollection.OUT_EDGE_SEQUENCE_ID
            ), Order.incr);
        }
        for (PropertyTree child : this.children) {
            child.walk(traversal);
        }
    }

    private void walk(GraphTraversal<Vertex, Vertex> traversal) {
        if (this.children.isEmpty()) {
            TraversalHelper.insertTraversal(
                    traversal.asAdmin().getSteps().size() - 1,
                    internalOptionalTraversal().asAdmin(),
                    traversal.asAdmin()
            );
        } else {
            traversal = traversal
                    .optional(
                            internalOptionalTraversal()
                    );
        }
        for (PropertyTree child : this.children) {
            child.walk(traversal);
        }
    }

    private GraphTraversal<Vertex, Vertex> internalOptionalTraversal() {
        return __
                .<Vertex>toE(direction(), label()).as("e_" + label())
                .<Edge>otherV().as(label());
    }


    private Direction direction() {
        return this.umlgRuntimeProperty.isControllingSide() ? Direction.OUT : Direction.IN;
    }

    private String label() {
        return this.umlgRuntimeProperty.getLabel();
    }

    public PropertyTree child(Set<String> labels) {
        for (String label : labels) {
            for (PropertyTree child : this.children) {
                if (child.umlgRuntimeProperty.getLabel().equals(label)) {
                    return child;
                }
            }
        }
        return null;
    }

    public Set<PropertyTree> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return this.umlgRuntimeProperty.getLabel();
    }
}

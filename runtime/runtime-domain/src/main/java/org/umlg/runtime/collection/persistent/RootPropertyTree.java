package org.umlg.runtime.collection.persistent;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.util.PathTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2016/05/29
 * Time: 12:20 PM
 */
public class RootPropertyTree {

    private String label;
    private List<PropertyTree> children = new ArrayList<>();

    private RootPropertyTree(String label) {
        this.label = label;
    }

    public static RootPropertyTree from(String label) {
        return  new RootPropertyTree(label);
    }

    public RootPropertyTree addChild(UmlgRuntimeProperty umlgRuntimeProperty) {
        addChild(PropertyTree.from(umlgRuntimeProperty));
        return this;
    }

    private void addChild(PropertyTree propertyTree) {
        this.children.add(propertyTree);
    }

    public List<PathTree> traversal(Graph graph) {
        GraphTraversal<Vertex, Vertex> originalTraversal = graph.traversal().V().hasLabel(this.label).as(this.label);
        for (PropertyTree child : this.children) {
            GraphTraversal<Vertex, Vertex> traversal = originalTraversal.asAdmin().clone();
            child.walk(traversal);
        }
//        return PathTree.from(traversal.path());
        return null;
    }

    public String getLabel() {
        return label;
    }

    public List<PropertyTree> getChildren() {
        return children;
    }
}

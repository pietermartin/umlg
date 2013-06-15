package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jVertex;
import org.joda.time.DateTime;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.umlg.runtime.domain.BaseTinkerAuditable;
import org.umlg.runtime.domain.CompositionNode;
import org.umlg.runtime.domain.TumlNode;
import org.umlg.runtime.util.TumlProperties;
import org.umlg.runtime.validation.TumlConstraintViolation;
import org.umlg.runtime.validation.TumlConstraintViolationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TumlTransactionEventHandler<T> implements TransactionEventHandler<T> {


    public TumlTransactionEventHandler() {
        super();
    }

    @Override
    public T beforeCommit(TransactionData data) throws Exception {
        if (TumlProperties.INSTANCE.isTransactionsMutliThreaded()) {
            Set<Node> deletedNodes = new HashSet<Node>();
            Iterable<Node> deletedNodeIterable = data.deletedNodes();
            for (Node node : deletedNodeIterable) {
                deletedNodes.add(node);
            }

            Set<Node> touchedNodes = new HashSet<Node>();
            Iterable<PropertyEntry<Node>> propertyEntryIterable = data.assignedNodeProperties();
            for (PropertyEntry<Node> propertyEntry : propertyEntryIterable) {
                Node node = propertyEntry.entity();
                touchedNodes.add(node);
            }
            propertyEntryIterable = data.removedNodeProperties();
            for (PropertyEntry<Node> propertyEntry : propertyEntryIterable) {
                Node node = propertyEntry.entity();
                touchedNodes.add(node);
            }
            Iterable<Node> nodeIterable = data.createdNodes();
            for (Node node : nodeIterable) {
                touchedNodes.add(node);
            }
            Iterable<Relationship> relationshipIterable = data.createdRelationships();
            for (Relationship relationship : relationshipIterable) {
                Node startNode = relationship.getStartNode();
                Node endNode = relationship.getEndNode();
                touchedNodes.add(startNode);
                touchedNodes.add(endNode);
            }
            relationshipIterable = data.deletedRelationships();
            for (Relationship relationship : relationshipIterable) {
                Node startNode = relationship.getStartNode();
                Node endNode = relationship.getEndNode();
                touchedNodes.add(startNode);
                touchedNodes.add(endNode);
            }
            for (Node node : touchedNodes) {
                if (deletedNodes.contains(node) || !node.hasProperty("className") || node.getProperty("className").equals(TumlGraph.ROOT_CLASS_NAME)) {
                    continue;
                }
                Class clazz = Class.forName((String) node.getProperty("className"));
                if (clazz.isEnum()) {
                    continue;
                }

                Neo4jVertex touchedVertex = new Neo4jVertex(node, (Neo4jGraph) GraphDb.getDb());
                TumlNode tumlNode = GraphDb.getDb().instantiateClassifier((Long) touchedVertex.getId());
                List<TumlConstraintViolation> requiredConstraintViolations = tumlNode.validateMultiplicities();
                requiredConstraintViolations.addAll(tumlNode.checkClassConstraints());
                if (!requiredConstraintViolations.isEmpty()) {
                    throw new TumlConstraintViolationException(requiredConstraintViolations);
                }
                if (!tumlNode.isTinkerRoot() && tumlNode.getOwningObject() == null) {
                    if (tumlNode instanceof BaseTinkerAuditable && ((BaseTinkerAuditable) tumlNode).getDeletedOn().isBefore(new DateTime())) {
                        return null;
                    }
                    throw new IllegalStateException(String.format("Entity %s %s does not have a composite owner", tumlNode.getClass().getSimpleName(), tumlNode.getId()));
                }
            }
        } else {
            try {
                if (!isEmpty(data) && GraphDb.getDb() != null) {
                    TransactionThreadVar.clear();
                    GraphDb.incrementTransactionCount();
                    List<TumlNode> entities = TransactionThreadEntityVar.get();
                    for (TumlNode tumlNode : entities) {
                        List<TumlConstraintViolation> requiredConstraintViolations = tumlNode.validateMultiplicities();
                        requiredConstraintViolations.addAll(tumlNode.checkClassConstraints());
                        if (!requiredConstraintViolations.isEmpty()) {
                            throw new TumlConstraintViolationException(requiredConstraintViolations);
                        }

                        if (!tumlNode.isTinkerRoot() && tumlNode instanceof CompositionNode && ((CompositionNode) tumlNode).getOwningObject() == null) {
//                            if (entity instanceof BaseTinkerAuditable && ((BaseTinkerAuditable) entity).getDeletedOn().isBefore(new DateTime())) {
//                                return null;
//                            }
                            throw new IllegalStateException(String.format("Entity %s %s does not have a composite owner", tumlNode.getClass().getSimpleName(), tumlNode.getId()));
                        }
                    }
                }
            } finally {
                TransactionThreadEntityVar.remove();
                TransactionThreadMetaNodeVar.remove();
            }
        }
        return null;
    }

    private boolean isEmpty(TransactionData data) {
        return !data.assignedNodeProperties().iterator().hasNext() && !data.assignedRelationshipProperties().iterator().hasNext() && !data.createdNodes().iterator().hasNext()
                && !data.createdRelationships().iterator().hasNext() && !data.deletedNodes().iterator().hasNext() && !data.deletedRelationships().iterator().hasNext()
                && !data.removedNodeProperties().iterator().hasNext() && !data.removedRelationshipProperties().iterator().hasNext();
    }

    @Override
    public void afterCommit(TransactionData data, T state) {
    }

    @Override
    public void afterRollback(TransactionData data, T state) {
        TransactionThreadEntityVar.remove();
        TransactionThreadMetaNodeVar.remove();
    }

}

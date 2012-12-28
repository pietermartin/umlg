package org.tuml.runtime.domain.neo4j;

import java.util.List;

import org.joda.time.DateTime;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.adaptor.TransactionThreadVar;
import org.tuml.runtime.domain.BaseTinkerAuditable;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.runtime.validation.TumlConstraintViolation;
import org.tuml.runtime.validation.TumlConstraintViolationException;

public class NakedTransactionEventHandler<T> implements TransactionEventHandler<T> {


	public NakedTransactionEventHandler() {
		super();
	}

	@Override
	public T beforeCommit(TransactionData data) throws Exception {
		if (!isEmpty(data) && GraphDb.getDb() != null) {
			TransactionThreadVar.clear();
			GraphDb.incrementTransactionCount();
			List<CompositionNode> entities = TransactionThreadEntityVar.get();
			for (CompositionNode entity : entities) {
				TumlNode tumlNode = (TumlNode) entity;
				List<TumlConstraintViolation> requiredConstraintViolations = tumlNode.validateRequiredProperties();
				if (!requiredConstraintViolations.isEmpty()) {
					TransactionThreadEntityVar.clear();
					throw new TumlConstraintViolationException(requiredConstraintViolations);
				}
				if (!entity.isTinkerRoot() && entity.getOwningObject() == null) {
					TransactionThreadEntityVar.clear();
					if (entity instanceof BaseTinkerAuditable && ((BaseTinkerAuditable) entity).getDeletedOn().isBefore(new DateTime())) {
						return null;
					}
					throw new IllegalStateException(String.format("Entity %s %s does not have a composite owner", entity.getClass().getSimpleName(), entity.getId()));
				}
			}
			TransactionThreadEntityVar.clear();
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

	}

}

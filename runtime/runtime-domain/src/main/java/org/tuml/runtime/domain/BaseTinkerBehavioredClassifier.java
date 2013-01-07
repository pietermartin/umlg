package org.tuml.runtime.domain;

import com.tinkerpop.blueprints.Edge;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.domain.activity.AbstractActivity;
import org.tuml.runtime.domain.activity.Token;
import org.tuml.runtime.domain.activity.interf.IActivityNode;
import org.tuml.runtime.domain.activity.interf.IBehavioredClassifier;
import org.tuml.runtime.domain.activity.interf.IEvent;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public abstract class BaseTinkerBehavioredClassifier extends BaseTumlAudit implements Serializable, CompositionNode, IBehavioredClassifier {

	private static final long serialVersionUID = 228929853082097254L;
	protected TinkerSequence<IEvent> events;

	public BaseTinkerBehavioredClassifier() {
		super();
	}

	public abstract void receiveSignal(ISignal signal);
	
	public abstract List<AbstractActivity> getAllActivities();
	
	public AbstractActivity getFirstActivityForEvent(IEvent event) {
		for (AbstractActivity activity : getAllActivities()) {
			Set<IActivityNode<? extends Token, ? extends Token>> nodesToTrigger = activity.getEnabledNodesWithMatchingTrigger(event);
			if (!nodesToTrigger.isEmpty()) {
				return activity;
			}
		}
		return null;
	}
	
	protected void attachToRoot() {
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "classifierBehavior");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public List<IEvent> getEventPool() {
		List<IEvent> result = this.events;
		return result;
	}
	
	public void addToEventPool(IEvent event) {
		if ( event!=null ) {
			this.events.add(event);
		}
	}
	
	public void removeFromEventPool(IEvent event) {
		if ( event!=null ) {
			this.events.remove(event);
		}
	}
	
}

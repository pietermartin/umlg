package org.umlg.runtime.domain;

import com.tinkerpop.blueprints.Edge;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.domain.activity.AbstractActivity;
import org.umlg.runtime.domain.activity.Token;
import org.umlg.runtime.domain.activity.interf.IActivityNode;
import org.umlg.runtime.domain.activity.interf.IBehavioredClassifier;
import org.umlg.runtime.domain.activity.interf.IEvent;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public abstract class BaseTinkerBehavioredClassifier extends BaseUmlgAudit implements Serializable, CompositionNode, IBehavioredClassifier {

	private static final long serialVersionUID = 228929853082097254L;
	protected UmlgSequence<IEvent> events;

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
		Edge edge = UMLG.getDb().addEdge(null, UMLG.getDb().getRoot(), this.vertex, "classifierBehavior");
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

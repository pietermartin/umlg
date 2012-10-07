package org.tuml.runtime.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTumlCompositionNode extends BaseTuml implements CompositionNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6012617567783938431L;

	public List<TumlNode> getPathToCompositionalRoot() {
		List<TumlNode> result = new ArrayList<TumlNode>();
		walkToRoot(result);
		return result;
	}
	
	void walkToRoot(List<TumlNode> nodes) {
		nodes.add(this);
		if (getOwningObject() != null && getOwningObject() instanceof CompositionNode) {
			((BaseTumlCompositionNode) getOwningObject()).walkToRoot(nodes);
		}
	}
	
}

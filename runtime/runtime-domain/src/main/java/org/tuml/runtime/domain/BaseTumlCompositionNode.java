package org.tuml.runtime.domain;

import com.tinkerpop.blueprints.Vertex;
import org.joda.time.DateTime;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTumlCompositionNode extends BaseTuml implements CompositionNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6012617567783938431L;

    public BaseTumlCompositionNode(Boolean persistent) {
        super(persistent);
    }

    public BaseTumlCompositionNode(Vertex vertex)  {
        super(vertex);
    }

    public BaseTumlCompositionNode()  {
    }

	public <T extends TumlNode> List<T> getPathToCompositionalRoot() {
		List<T> result = new ArrayList<T>();
		walkToRoot(result);
		return result;
	}
	
	<T extends TumlNode> void walkToRoot(List<T> nodes) {
		nodes.add((T) this);
		if (getOwningObject() != null && getOwningObject() instanceof CompositionNode) {
			((BaseTumlCompositionNode) getOwningObject()).walkToRoot(nodes);
		}
	}
	
}

package org.umlg.runtime.domain;

import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseUmlgCompositionNode extends BaseUmlg implements CompositionNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6012617567783938431L;

    public BaseUmlgCompositionNode(Boolean persistent) {
        super(persistent);
    }

    public BaseUmlgCompositionNode(Object id)  {
        super(id);
    }

    public BaseUmlgCompositionNode(Vertex vertex)  {
        super(vertex);
    }

    public BaseUmlgCompositionNode()  {
    }

	public <T extends UmlgNode> List<T> getPathToCompositionalRoot() {
		List<T> result = new ArrayList<T>();
		walkToRoot(result);
		return result;
	}
	
	<T extends UmlgNode> void walkToRoot(List<T> nodes) {
		nodes.add((T) this);
        if (getOwningObject() != null && getOwningObject() instanceof CompositionNode) {
			((BaseUmlgCompositionNode) getOwningObject()).walkToRoot(nodes);
		}
	}
	
}

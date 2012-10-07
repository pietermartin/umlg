package org.tuml.runtime.domain;

import java.util.List;

public interface CompositionNode extends TumlNode {
	TumlNode getOwningObject();
	List<TumlNode> getPathToCompositionalRoot();
}

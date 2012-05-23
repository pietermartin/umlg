package org.tuml.runtime.domain;

public interface CompositionNode extends TinkerNode {
	CompositionNode getOwningObject();
	void init(CompositionNode owner);
	boolean hasInitBeenCalled();
	void markDeleted();
}

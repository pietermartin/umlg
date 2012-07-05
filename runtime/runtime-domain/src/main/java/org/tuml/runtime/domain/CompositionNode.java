package org.tuml.runtime.domain;

public interface CompositionNode extends TinkerNode {
	TinkerNode getOwningObject();
//	void init(TinkerNode owner);
//	boolean hasInitBeenCalled();
}

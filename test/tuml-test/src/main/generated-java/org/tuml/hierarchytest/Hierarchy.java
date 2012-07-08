package org.tuml.hierarchytest;

import org.tuml.runtime.domain.TinkerNode;

public interface Hierarchy extends TinkerNode {
	public Hierarchy getChildren();
	
	public Hierarchy getParent();
	
	public Boolean isRoot();

}
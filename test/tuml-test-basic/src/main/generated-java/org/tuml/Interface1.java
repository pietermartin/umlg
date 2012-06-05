package org.tuml;

import java.util.Set;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.TinkerNode;

public interface Interface1 extends TinkerNode {
	public void addToInterface2(Interface2 interface2);
	
	public void addToInterface2(Set<Interface2> interface2);
	
	public void clearInterface2();
	
	public TinkerSet<Interface2> getInterface2();
	
	public void removeFromInterface2(Interface2 interface2);
	
	public void removeFromInterface2(Set<Interface2> interface2);
	
	public void setInterface2(Set<Interface2> interface2);

}
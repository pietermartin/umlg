package org.tuml;

import java.util.Set;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.CompositionNode;

public interface Interface2 extends CompositionNode {
	public void addToInterface1(Interface1 interface1);
	
	public void clearInterface1();
	
	public Interface1 getInterface1();
	
	public void removeFromInterface1(Interface1 interface1);
	
	public void removeFromInterface1(Set<Interface1> interface1);
	
	public void setInterface1(Interface1 interface1);

}
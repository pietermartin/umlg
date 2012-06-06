package org.tuml;

import java.util.Set;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.CompositionNode;

public interface Interface2 extends CompositionNode {
	public void addToInterface1(Interface1 interface1);
	
	public void addToName(String name);
	
	public void clearInterface1();
	
	public void clearName();
	
	public Interface1 getInterface1();
	
	public String getName();
	
	public void removeFromInterface1(Interface1 interface1);
	
	public void removeFromInterface1(Set<Interface1> interface1);
	
	public void removeFromName(Set<String> name);
	
	public void removeFromName(String name);
	
	public void setInterface1(Interface1 interface1);
	
	public void setName(String name);

}
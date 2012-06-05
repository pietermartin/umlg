package org.tuml;

import java.util.Set;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.TinkerNode;

public interface Interface1 extends TinkerNode {
	public void addToInterface2(Interface2 interface2);
	
	public void addToInterface2(Set<Interface2> interface2);
	
	public void addToName(String name);
	
	public void clearInterface2();
	
	public void clearName();
	
	public TinkerSet<Interface2> getInterface2();
	
	public String getName();
	
	public void removeFromInterface2(Interface2 interface2);
	
	public void removeFromInterface2(Set<Interface2> interface2);
	
	public void removeFromName(Set<String> name);
	
	public void removeFromName(String name);
	
	public void setInterface2(Set<Interface2> interface2);
	
	public void setName(String name);

}
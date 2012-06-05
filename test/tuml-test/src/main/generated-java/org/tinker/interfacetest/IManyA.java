package org.tinker.interfacetest;

import java.util.Set;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.TinkerNode;

public interface IManyA extends TinkerNode {
	public void addToIManyB(IManyB iManyB);
	
	public void addToIManyB(Set<IManyB> iManyB);
	
	public void clearIManyB();
	
	public TinkerSet<IManyB> getIManyB();
	
	public void removeFromIManyB(IManyB iManyB);
	
	public void removeFromIManyB(Set<IManyB> iManyB);
	
	public void setIManyB(Set<IManyB> iManyB);

}
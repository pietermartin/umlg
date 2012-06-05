package org.tinker.interfacetest;

import java.util.Set;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.TinkerNode;

public interface IManyB extends TinkerNode {
	public void addToIManyA(IManyA iManyA);
	
	public void addToIManyA(Set<IManyA> iManyA);
	
	public void clearIManyA();
	
	public TinkerSet<IManyA> getIManyA();
	
	public void removeFromIManyA(IManyA iManyA);
	
	public void removeFromIManyA(Set<IManyA> iManyA);
	
	public void setIManyA(Set<IManyA> iManyA);

}
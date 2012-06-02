package org.tinker.interfacetest;

import org.tuml.runtime.collection.TinkerSet;

public interface IManyA {
	public void addToIManyB(IManyB iManyB);
	
	public TinkerSet<IManyB> getIManyB();
	
	public void z_internalAddToIManyB(IManyB iManyB);
	
	public void z_internalRemoveFromIManyB(IManyB iManyB);

}
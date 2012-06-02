package org.tinker.interfacetest;

import org.tuml.runtime.collection.TinkerSet;

public interface IManyB {
	public void addToIManyA(IManyA iManyA);
	
	public TinkerSet<IManyA> getIManyA();
	
	public void z_internalAddToIManyA(IManyA iManyA);
	
	public void z_internalRemoveFromIManyA(IManyA iManyA);

}
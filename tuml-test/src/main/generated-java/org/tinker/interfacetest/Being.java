package org.tinker.interfacetest;

import org.tinker.concretetest.God;
import org.tuml.runtime.collection.TinkerSet;

public interface Being {
	public void addToGod(God god);
	
	public God getGod();
	
	public void setGod(TinkerSet<God> god);
	
	public void z_internalAddToGod(God god);
	
	public void z_internalRemoveFromGod(God god);

}
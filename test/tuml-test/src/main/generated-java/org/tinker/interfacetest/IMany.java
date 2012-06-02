package org.tinker.interfacetest;

import org.tinker.concretetest.God;
import org.tuml.runtime.collection.TinkerSet;

public interface IMany {
	public void addToGod(God god);
	
	public void addToName(String name);
	
	public God getGod();
	
	public String getName();
	
	public void setGod(TinkerSet<God> god);
	
	public void setName(TinkerSet<String> name);
	
	public void z_internalAddToGod(God god);
	
	public void z_internalAddToName(String name);
	
	public void z_internalRemoveFromGod(God god);
	
	public void z_internalRemoveFromName(String name);

}
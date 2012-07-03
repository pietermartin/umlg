package org.tuml.interfacetest;

import java.util.Set;

import org.tuml.concretetest.God;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.CompositionNode;

public interface Being extends CompositionNode {
	public void addToGod(God god);
	
	public void clearGod();
	
	public God getGod();
	
	public void removeFromGod(God god);
	
	public void removeFromGod(Set<God> god);
	
	public void setGod(God god);

}
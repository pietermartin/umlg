package tinker.interfacetest;

import java.util.Set;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.CompositionNode;

import tinker.concretetest.God;

public interface IMany extends CompositionNode {
	public void addToGod(God god);
	
	public void addToName(String name);
	
	public void clearGod();
	
	public void clearName();
	
	public God getGod();
	
	public String getName();
	
	public void removeFromGod(God god);
	
	public void removeFromGod(Set<God> god);
	
	public void removeFromName(Set<String> name);
	
	public void removeFromName(String name);
	
	public void setGod(God god);
	
	public void setName(String name);

}
package org.tuml.runtime.collection;

public interface TinkerMultiplicity {
	boolean isOneToOne(); 
	boolean isOneToMany(); 
	boolean isManyToOne(); 
	boolean isManyToMany();
	int getUpper();
	int getLower();
}

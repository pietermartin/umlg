package org.tinker.embeddedtest;

import java.util.Set;

import org.tinker.concretetest.God;
import org.tuml.runtime.collection.TinkerSet;

public enum REASON {
	GOOD,
	BAD;
	private TinkerSet<God> god;

	public void addToGod(God god) {
		if ( god != null ) {
			this.god.add(god);
		}
	}
	
	public void clearGod() {
		this.god.clear();
	}
	
	public God getGod() {
		TinkerSet<God> tmp = this.god;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public void removeFromGod(God god) {
		if ( god != null ) {
			this.god.remove(god);
		}
	}
	
	public void removeFromGod(Set<God> god) {
		if ( !god.isEmpty() ) {
			this.god.removeAll(god);
		}
	}
	
	public void setGod(God god) {
		clearGod();
		addToGod(god);
	}

}
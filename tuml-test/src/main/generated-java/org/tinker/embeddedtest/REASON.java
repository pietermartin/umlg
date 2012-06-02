package org.tinker.embeddedtest;

import org.tinker.concretetest.God;
import org.tuml.runtime.collection.TinkerSet;

public enum REASON {
;
	private TinkerSet<God> god;

	public void addToGod(God god) {
		if ( god != null ) {
			god.z_internalRemoveFromREASON(god.getREASON());
			god.z_internalAddToREASON(this);
			z_internalAddToGod(god);
		}
	}
	
	public God getGod() {
		TinkerSet<God> tmp = this.god;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public void setGod(TinkerSet<God> god) {
	}
	
	public void z_internalAddToGod(God god) {
		this.god.add(god);
	}
	
	public void z_internalRemoveFromGod(God god) {
		this.god.remove(god);
	}

}
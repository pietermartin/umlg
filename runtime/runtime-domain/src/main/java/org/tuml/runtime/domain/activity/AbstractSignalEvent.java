package org.tuml.runtime.domain.activity;

import org.tuml.runtime.domain.ISignal;

public class AbstractSignalEvent {
	
	private ISignal signal;

	public AbstractSignalEvent(ISignal signal) {
		this.signal = signal;
	}

	public ISignal getSignal() {
		return signal;
	}

	public void setSignal(ISignal signal) {
		this.signal = signal;
	}
	
}

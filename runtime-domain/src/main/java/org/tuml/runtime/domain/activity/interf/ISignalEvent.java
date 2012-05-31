package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.ISignal;


public interface ISignalEvent extends IEvent {

	ISignal getSignal();
	
}

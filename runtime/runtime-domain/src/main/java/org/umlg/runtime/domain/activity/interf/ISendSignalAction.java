package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.ISignal;
import org.umlg.runtime.domain.activity.SingleObjectToken;

public interface ISendSignalAction extends IInvocationAction {
	ISignal getSignal();
	IInputPin<?, ? extends SingleObjectToken<?>> getTarget();
}

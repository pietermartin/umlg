package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.ISignal;
import org.tuml.runtime.domain.activity.SingleObjectToken;

public interface ISendSignalAction extends IInvocationAction {
	ISignal getSignal();
	IInputPin<?, ? extends SingleObjectToken<?>> getTarget();
}

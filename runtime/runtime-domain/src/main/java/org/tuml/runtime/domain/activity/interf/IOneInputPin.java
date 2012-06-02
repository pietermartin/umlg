package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.SingleObjectToken;

public interface IOneInputPin<O> extends IInputPin<O, SingleObjectToken<O>>, IOnePin<O> {
	
}

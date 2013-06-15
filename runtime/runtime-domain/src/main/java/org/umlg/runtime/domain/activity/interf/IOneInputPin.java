package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.SingleObjectToken;

public interface IOneInputPin<O> extends IInputPin<O, SingleObjectToken<O>>, IOnePin<O> {
	
}

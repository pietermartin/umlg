package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.SingleObjectToken;

public interface IOneValuePin<O> extends IValuePin<O, SingleObjectToken<O>> {
	O getValue();
}

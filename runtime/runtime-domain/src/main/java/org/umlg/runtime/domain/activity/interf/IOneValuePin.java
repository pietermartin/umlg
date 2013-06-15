package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.SingleObjectToken;

public interface IOneValuePin<O> extends IValuePin<O, SingleObjectToken<O>> {
	O getValue();
}

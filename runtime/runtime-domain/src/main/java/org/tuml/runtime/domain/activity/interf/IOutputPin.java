package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.ObjectToken;

public interface IOutputPin<O, OUT extends ObjectToken<O>> extends IPin<O, OUT, OUT> {

	void copyTokensToStart();

}

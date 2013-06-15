package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.ObjectToken;

public interface IOutputPin<O, OUT extends ObjectToken<O>> extends IPin<O, OUT, OUT> {

	void copyTokensToStart();

}

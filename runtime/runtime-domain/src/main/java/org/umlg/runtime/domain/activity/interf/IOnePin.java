package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.SingleObjectToken;

import java.util.List;

public interface IOnePin<O> extends IPin<O, SingleObjectToken<O>, SingleObjectToken<O>> {
	@Override
	List<SingleObjectToken<O>> getInTokens();
	@Override
	List<SingleObjectToken<O>> getOutTokens();
}

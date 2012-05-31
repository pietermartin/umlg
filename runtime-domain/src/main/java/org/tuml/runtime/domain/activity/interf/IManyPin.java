package org.tuml.runtime.domain.activity.interf;

import java.util.List;

import org.tuml.runtime.domain.activity.CollectionObjectToken;

public interface IManyPin<O> extends IPin<O, CollectionObjectToken<O>, CollectionObjectToken<O>> {
	@Override
	List<CollectionObjectToken<O>> getInTokens();
	@Override
	List<CollectionObjectToken<O>> getOutTokens();
}

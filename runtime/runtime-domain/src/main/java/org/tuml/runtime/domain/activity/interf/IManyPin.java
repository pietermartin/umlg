package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.CollectionObjectToken;

import java.util.List;

public interface IManyPin<O> extends IPin<O, CollectionObjectToken<O>, CollectionObjectToken<O>> {
	@Override
	List<CollectionObjectToken<O>> getInTokens();
	@Override
	List<CollectionObjectToken<O>> getOutTokens();
}

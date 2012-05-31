package org.tuml.runtime.domain.activity.interf;

import java.util.Collection;

import org.tuml.runtime.domain.activity.CollectionObjectToken;

public interface IManyValuePin<O> extends IValuePin<O, CollectionObjectToken<O>> {
	Collection<O> getValue();
}

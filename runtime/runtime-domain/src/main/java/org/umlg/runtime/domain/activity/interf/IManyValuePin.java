package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.CollectionObjectToken;

import java.util.Collection;

public interface IManyValuePin<O> extends IValuePin<O, CollectionObjectToken<O>> {
	Collection<O> getValue();
}

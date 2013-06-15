package org.umlg.runtime.domain.activity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CollectionObjectTokenInterator<T> extends ObjectTokenInterator<T, CollectionObjectToken<T>> {

	private Iterator<Collection<T>> iterator;
	
	@SuppressWarnings("unchecked")
	public CollectionObjectTokenInterator(String name, Collection<T> collection) {
		super(name);
		this.iterator = Arrays.asList(collection).iterator();
	}

	@Override
	public CollectionObjectToken<T> next() {
		return new CollectionObjectToken<T>(this.name, (Collection<T>) this.iterator.next());
	}

	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public void remove() {
		this.iterator.remove();
	}

}

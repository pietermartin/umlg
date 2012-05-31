package org.tuml.runtime.domain.activity;

import java.util.Iterator;

public class SingleObjectTokenInterator<T> extends ObjectTokenInterator<T, SingleObjectToken<T>> {

	private Iterator<T> iterator;
	
	public SingleObjectTokenInterator(String name, Iterator<T> iterator) {
		super(name);
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public SingleObjectToken<T> next() {
		return new SingleObjectToken<T>(this.name, this.iterator.next());
	}

	@Override
	public void remove() {
		this.iterator.remove();
	}

}

package org.tuml.runtime.domain.activity;

import java.util.Iterator;

public abstract class ObjectTokenInterator<T, TOKEN extends ObjectToken<T>> implements Iterator<TOKEN> {

	protected String name;

	public ObjectTokenInterator(String name) {
		super();
		this.name = name;
	}

	@Override
	public abstract TOKEN next();

}

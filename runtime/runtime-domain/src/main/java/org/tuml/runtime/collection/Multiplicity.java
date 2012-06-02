package org.tuml.runtime.collection;

public enum Multiplicity {

	ONE_TO_ONE, ZERO_TO_ONE, ONE_TO_MANY, ZERO_TO_MANY;

	public boolean isOne() {
		return this==ONE_TO_ONE || this==ZERO_TO_ONE;
	}

	public boolean isMany() {
		return !isOne();
	}
}

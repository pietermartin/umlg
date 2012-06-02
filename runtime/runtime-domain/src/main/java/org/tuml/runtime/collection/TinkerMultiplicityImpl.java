package org.tuml.runtime.collection;

public class TinkerMultiplicityImpl implements TinkerMultiplicity {

	private boolean oneToOne;
	private boolean oneToMany;
	private boolean manyToOne;
	private boolean manyToMany;
	private int lower;
	private int upper;
	
	public TinkerMultiplicityImpl(boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int lower, int upper) {
		super();
		this.oneToOne = oneToOne;
		this.oneToMany = oneToMany;
		this.manyToOne = manyToOne;
		this.manyToMany = manyToMany;
		this.lower = lower;
		this.upper = upper;
	}

	@Override
	public boolean isOneToOne() {
		return this.oneToOne;
	}

	@Override
	public boolean isOneToMany() {
		return this.oneToMany;
	}

	@Override
	public boolean isManyToOne() {
		return this.manyToOne;
	}

	@Override
	public boolean isManyToMany() {
		return this.manyToMany;
	}

	@Override
	public int getUpper() {
		return this.upper;
	}

	@Override
	public int getLower() {
		return this.lower;
	}

}

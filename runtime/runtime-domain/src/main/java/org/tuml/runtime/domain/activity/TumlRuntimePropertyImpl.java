package org.tuml.runtime.domain.activity;

import org.tuml.runtime.collection.TumlRuntimeProperty;

public class TumlRuntimePropertyImpl implements TumlRuntimeProperty {

	public TumlRuntimePropertyImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isControllingSide() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComposite() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOneToOne() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOneToMany() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isManyToOne() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isManyToMany() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getUpper() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLower() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int elementCount) {
		return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
	}

	@Override
	public boolean isOnePrimitive() {
		return false;
	}

	@Override
	public boolean isQualified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInverseQualified() {
		// TODO Auto-generated method stub
		return false;
	}

}

package org.tuml.runtime.domain.ocl;


public final class OclInvalidInstance implements OclInvalid {
 
	public final static OclInvalid INSTANCE = new OclInvalidInstance();
	
	private OclInvalidInstance() {
	}

	@Override
	public boolean equals(Object object) {
		return object == this;
	}

	@Override
	public boolean notEquals(Object object) {
		return object != this;
	}

	@Override
	public Boolean oclIsNew() {
		return false;
	}

	@Override
	public Boolean oclIsUndefined() {
		return true;
	}

	@Override
	public Boolean oclIsInvalid() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T oclAsType(T classifier) {
		return (T)this;
	}

	@Override
	public Boolean oclIsTypeOf(Object object) {
		return object == this;
	}

	/**
	 * All oclInvalid conforms to all types
	 */
	@Override
	public Boolean oclIsKindOf(Object object) {
		return true;
	}

	@Override
	public Boolean oclIsInState(OclState state) {
		// TODO
		throw new RuntimeException("Not implemented");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<OclInvalid> oclType() {
		return OclInvalid.class;
	}

	@Override
	public String oclLocale() {
		// TODO
		throw new RuntimeException("Not implemented");
	}
	
}

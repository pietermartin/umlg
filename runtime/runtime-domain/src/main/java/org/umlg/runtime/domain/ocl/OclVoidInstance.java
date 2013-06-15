package org.umlg.runtime.domain.ocl;

public final class OclVoidInstance implements OclVoid {

	public final static OclVoid INSTANCE = new OclVoidInstance();

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
		return false;
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
		return object != OclInvalidInstance.INSTANCE;
	}

	@Override
	public Boolean oclIsInState(OclState state) {
		// TODO
		throw new RuntimeException("Not implemented");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<OclVoid> oclType() {
		return OclVoid.class;
	}

	@Override
	public String oclLocale() {
		// TODO
		throw new RuntimeException("Not implemented");
	}
}

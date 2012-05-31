package org.tuml.runtime.collection;

import org.tuml.runtime.domain.CompositionNode;


public class Qualifier {
	private String key;
	private Object value;
	private Multiplicity multiplicity;

	public Qualifier(String key, Object value, Multiplicity multiplicity) {
		super();
		this.key = key;
		this.value = value;
		this.multiplicity = multiplicity;
	}

	public String getValue() {
		if (value instanceof CompositionNode) {
			return ((CompositionNode) value).getUid();
		} else {
			if (this.value == null) {
				return "___NULL___";
			} else {
				return this.value.toString();
			}
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Multiplicity getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(Multiplicity multiplicity) {
		this.multiplicity = multiplicity;
	}

	public boolean isOne() {
		return this.multiplicity.isOne();
	}

	public boolean isMany() {
		return this.multiplicity.isMany();
	}

}

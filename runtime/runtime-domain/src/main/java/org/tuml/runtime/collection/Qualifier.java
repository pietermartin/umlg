package org.tuml.runtime.collection;

import org.tuml.runtime.domain.TinkerNode;

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
		if (value instanceof TinkerNode) {
			return ((TinkerNode) value).getUid();
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("key = ");
		sb.append(this.key);
		sb.append(" value = ");
		sb.append(this.value);
		sb.append(" Multiplicity = ");
		sb.append(this.multiplicity);
		return sb.toString();
	}

}

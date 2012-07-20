package org.tuml.runtime.collection;

import org.tuml.runtime.domain.TumlNode;

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

	public Qualifier(String[] keys, Object[] values, Multiplicity multiplicity) {
		super();
		String keyValue = "";
		String objectValue = "";
		for (String k : keys) {
			keyValue += k;
		}
		this.key = keyValue;
		for (Object o : values) {
			objectValue += o;
		}
		this.value = objectValue;
		this.multiplicity = multiplicity;
	}

	public String getValue() {
		if (value instanceof TumlNode) {
			return ((TumlNode) value).getUid();
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

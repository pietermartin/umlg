package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.domain.activity.interf.IValuePin;

public abstract class ValuePin<O, OUT extends ObjectToken<O>> extends InputPin<O, OUT> implements IValuePin<O, OUT> {

	public ValuePin() {
		super();
	}

	public ValuePin(boolean persist, String name) {
		super(persist, name);
	}

	public ValuePin(Vertex vertex) {
		super(vertex);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nValuePin //TODO");
		// sb.append(this.nodeStat.toString());
		return sb.toString();
	}

}

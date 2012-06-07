package org.tuml.runtime.domain.activity;

import java.util.Arrays;
import java.util.List;

import com.tinkerpop.blueprints.Vertex;


public abstract class OneValuePin<O> extends ValuePin<O, SingleObjectToken<O>> {

	public OneValuePin() {
		super();
	}

	public OneValuePin(boolean persist, String name) {
		super(persist, name);
	}

	public OneValuePin(Vertex vertex) {
		super(vertex);
	}

	protected abstract O getValue();

	@SuppressWarnings("unchecked")
	@Override
	public List<SingleObjectToken<O>> getInTokens() {
		return Arrays.<SingleObjectToken<O>>asList(new SingleObjectToken<O>(getName(), getValue()));
	}
	
	@Override
	protected int countNumberOfElementsOnTokens() {
		return getInTokens().size();
	}	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nValuePin //TODO");
		// sb.append(this.nodeStat.toString());
		return sb.toString();
	}

}

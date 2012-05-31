package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tuml.runtime.domain.activity.interf.IOneOutActivityParameterNode;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class OneOutActivityParameterNode<O> extends OutActivityParameterNode<O,SingleObjectToken<O>> implements IOneOutActivityParameterNode<O> {

	public OneOutActivityParameterNode() {
		super();
	}

	public OneOutActivityParameterNode(boolean persist, String name) {
		super(persist, name);
	}

	public OneOutActivityParameterNode(Vertex vertex) {
		super(vertex);
	}

	@Override
	public abstract List<OneObjectFlowKnown<O>> getIncoming();

	@Override
	public List<OneObjectFlowKnown<O>> getOutgoing() {
		return Collections.emptyList();
	}	
	
	@Override
	public List<O> getReturnParameterValues() {
		List<O> result = new ArrayList<O>();
		for (SingleObjectToken<O> token : getInTokens()) {
			result.add(token.getObject());
		}
		return result;
	}

//	@Override
//	protected int countNumberOfElementsOnTokens() {
//		return getInTokens().size();
//	}	

}

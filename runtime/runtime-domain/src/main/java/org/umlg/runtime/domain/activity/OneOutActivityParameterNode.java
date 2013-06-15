package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.domain.activity.interf.IOneOutActivityParameterNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

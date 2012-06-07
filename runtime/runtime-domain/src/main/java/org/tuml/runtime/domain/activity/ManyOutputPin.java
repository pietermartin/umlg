package org.tuml.runtime.domain.activity;

import java.util.Collections;
import java.util.List;

import org.tuml.runtime.domain.activity.interf.IManyOutputPin;

import com.tinkerpop.blueprints.Vertex;

public abstract class ManyOutputPin<O> extends OutputPin<O, CollectionObjectToken<O>> implements IManyOutputPin<O> {

	public ManyOutputPin() {
		super();
	}

	public ManyOutputPin(boolean persist, String name) {
		super(persist, name);
	}

	public ManyOutputPin(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public abstract List<ManyObjectFlowKnown<O>> getIncoming();

	@Override
	public List<ManyObjectFlowKnown<O>> getOutgoing() {
		return Collections.emptyList();
	}
	
	@Override
	protected int countNumberOfElementsOnTokens() {
		int size = 0;
		List<CollectionObjectToken<O>> tokens = getOutTokens();
		for (CollectionObjectToken<O> collectionObjectToken : tokens) {
			size += collectionObjectToken.getElements().size();
		}
		return size;
	}	

}

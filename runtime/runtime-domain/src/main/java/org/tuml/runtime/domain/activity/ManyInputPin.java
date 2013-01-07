package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.domain.activity.interf.IManyInputPin;

import java.util.Collections;
import java.util.List;

public abstract class ManyInputPin<O> extends InputPin<O, CollectionObjectToken<O>> implements IManyInputPin<O> {

	private static final long serialVersionUID = 7759820295342656988L;

	public ManyInputPin() {
		super();
	}

	public ManyInputPin(boolean persist, String name) {
		super(persist, name);
	}

	public ManyInputPin(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public List<ManyObjectFlowKnown<O>> getIncoming() {
		return Collections.emptyList();
	}

	@Override
	public abstract List<ManyObjectFlowKnown<O>> getOutgoing();	

	@Override
	protected int countNumberOfElementsOnTokens() {
		int size = 0;
		List<CollectionObjectToken<O>> tokens = getInTokens();
		for (CollectionObjectToken<O> collectionObjectToken : tokens) {
			size += collectionObjectToken.getElements().size();
		}
		return size;
	}

}

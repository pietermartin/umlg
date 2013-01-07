package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class ManyReturnInformationInputPin<O> extends ReturnInformationInputPin<O,CollectionObjectToken<O>> {

	public ManyReturnInformationInputPin() {
		super();
	}

	public ManyReturnInformationInputPin(boolean persist, String name) {
		super(persist, name);
	}

	public ManyReturnInformationInputPin(Vertex vertex) {
		super(vertex);
	}

	@Override
	public abstract ReplyAction getAction();
	
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

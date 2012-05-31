package org.tuml.runtime.domain.activity;

import java.util.Collection;

import org.tuml.runtime.domain.activity.interf.IInputPin;

import com.tinkerpop.blueprints.pgm.Vertex;


public abstract class CreateObjectAction<O> extends Action {

	private static final long serialVersionUID = 3366764020708078856L;

	public CreateObjectAction() {
		super();
	}

	public CreateObjectAction(boolean persist, String name) {
		super(persist, name);
	}

	public CreateObjectAction(Vertex vertex) {
		super(vertex);
	}
	
	protected abstract O createObject();
	
	public abstract OutputPin<O, SingleObjectToken<O>> getResult();

	@Override
	protected void addToInputPinVariable(IInputPin<?, ?> inputPin, Collection<?> elements) {
		//Not used
	}
	
	@Override
	protected boolean execute() {
		O object = createObject();
		getResult().addIncomingToken(new SingleObjectToken<O>(getResult().getName(), object));
		return true;
	}
	
}

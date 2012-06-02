package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tuml.runtime.domain.activity.interf.IInputPin;
import org.tuml.runtime.domain.activity.interf.IInvocationAction;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class InvocationAction extends Action implements IInvocationAction {

	private static final long serialVersionUID = 4322960181624092985L;

	public InvocationAction() {
		super();
	}

	public InvocationAction(boolean persist, String name) {
		super(persist, name);
	}

	public InvocationAction(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public List<? extends IInputPin<?, ?>> getArgument() {
		return getInput();
	}
	
	protected abstract void addToInputPinVariable(IInputPin<?, ?> inputPin, Collection<?> elements);
	
	/*
	 * This will only be called if the lower multiplicity is reached, all up to
	 * upper multiplicity is consumed
	 */
	protected void transferObjectTokensToAction() {
		for (IInputPin<?,?> inputPin : this.getInput()) {
			int elementsTransferedCount = 0;
			for (ObjectToken<?> token : inputPin.getInTokens()) {
				if (elementsTransferedCount < inputPin.getUpperMultiplicity()) {
					
					if (elementsTransferedCount + token.getNumberOfElements() <= inputPin.getUpperMultiplicity()) {
						// transfer all elements
						elementsTransferedCount += token.getNumberOfElements();
						token.removeEdgeFromActivityNode();
						addToInputPinVariable(inputPin, token.getElements());
						token.remove();
					} else {
						Collection<Object> tmp = new ArrayList<Object>();
						for (Object element : token.getElements()) {
							elementsTransferedCount += 1;
							tmp.add(element);
							if (elementsTransferedCount >= inputPin.getUpperMultiplicity()) {
								break;
							}
						}
						addToInputPinVariable(inputPin, tmp);
					}
				}
			}
		}
	}

}

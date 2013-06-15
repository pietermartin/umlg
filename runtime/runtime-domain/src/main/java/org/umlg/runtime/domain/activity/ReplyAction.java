package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class ReplyAction extends Action {

	private static final long serialVersionUID = 8206990186184448318L;

	public ReplyAction() {
		super();
	}

	public ReplyAction(boolean persist, String name) {
		super(persist, name);
	}

	public ReplyAction(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected boolean execute() {
		return true;
	}
	
//	protected abstract void addToInputPinVariable(IInputPin<?,?> inputPin, Collection<?> elements);
//	
//	/*
//	 * This will only be called if the lower multiplicity is reached, all up to
//	 * upper multiplicity is consumed
//	 */
//	protected void transferObjectTokensToAction() {
//		for (IInputPin<?,?> inputPin : this.getInput()) {
//			int elementsTransferedCount = 0;
//			for (ObjectToken<?> token : inputPin.getInTokens()) {
//				if (elementsTransferedCount < inputPin.getUpperMultiplicity()) {
//					
//					if (elementsTransferedCount + token.getNumberOfElements() <= inputPin.getUpperMultiplicity()) {
//						// transfer all elements
//						elementsTransferedCount += token.getNumberOfElements();
//						token.removeEdgeFromActivityNode();
//						addToInputPinVariable(inputPin, token.getElements());
//						token.remove();
//					} else {
//						Collection<Object> tmp = new ArrayList<Object>();
//						for (Object element : token.getElements()) {
//							elementsTransferedCount += 1;
//							tmp.add(element);
//							if (elementsTransferedCount >= inputPin.getUpperMultiplicity()) {
//								break;
//							}
//						}
//						addToInputPinVariable(inputPin, tmp);
//					}
//				}
//			}
//		}
//	}
	

}

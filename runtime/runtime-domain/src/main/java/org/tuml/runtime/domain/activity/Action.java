package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.activity.interf.IAction;
import org.tuml.runtime.domain.activity.interf.IInputPin;
import org.tuml.runtime.domain.activity.interf.IOutputPin;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class Action extends ExecutableNode implements IAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8761613363370956543L;

	public Action() {
		super();
	}

	public Action(Vertex vertex) {
		super(vertex);
	}

	public Action(boolean persist, String name) {
		super(persist, name);
	}

	protected abstract boolean hasPostConditionPassed();

	protected abstract boolean hasPreConditionPassed();

	@Override
	public abstract List<? extends IInputPin<?, ?>> getInput();

	@Override
	public abstract List<? extends IOutputPin<?, ?>> getOutput();

	protected abstract List<? extends Object> getInputPinVariables();

	protected abstract void addToInputPinVariable(IInputPin<?, ?> inputPin, Collection<?> elements);

	/*
	 * This will only be called if the lower multiplicity is reached, all up to
	 * upper multiplicity is consumed
	 */
	protected void transferObjectTokensToAction() {
		for (IInputPin<?, ?> inputPin : this.getInput()) {
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
						token.getElements().removeAll(tmp);
						addToInputPinVariable(inputPin, tmp);
					}
				}
			}
		}
	}

	@Override
	public boolean mayContinue() {
		return doAllIncomingFlowsHaveTokens() && hasPreConditionPassed() && hasPostConditionPassed() && isTriggered() && isInputPinsSatisfied();
	}

	@Override
	protected Boolean executeNode() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("start executeNode");
			logger.finest(toString());
		}
		preExecute();

		if (execute()) {
			// Only if the action has completed do we continue
			return postExecute();
		} else {
			return false;
		}

	}

	protected abstract void clearInputPinVariables();

	protected Boolean postExecute() {
		clearInputPinVariables();

		List<Boolean> flowResult = new ArrayList<Boolean>();

		this.nodeStat.increment();

		for (ControlToken token : getInTokens()) {
			token.remove();
		}
		// For each out flow add a token
		for (ControlFlow flow : getOutgoing()) {
			ControlToken duplicate = new ControlToken(flow.getName());
			addOutgoingToken(duplicate);
		}

		// Continue each out flow with its tokens
		for (ActivityEdge<ControlToken> flow : getOutgoing()) {
			flow.setStarts(getOutTokens(flow.getName()));
			flowResult.add(flow.processNextStart());
		}

		for (IOutputPin<?, ?> outputPin : getOutput()) {
			// The output pins starts must be set in concrete actions
			outputPin.copyTokensToStart();
			flowResult.add(outputPin.processNextStart());
		}

		// TODO Start transaction
		setNodeStatus(NodeStatus.COMPLETE);
		// TODO End transaction
		boolean result = true;
		for (Boolean b : flowResult) {
			if (!b) {
				result = false;
				break;
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("end executeNode");
			logger.finest(toString());
		}
		return result;
	}

	protected void preExecute() {
		transferObjectTokensToAction();
		setNodeStatus(NodeStatus.ENABLED);
		setNodeStatus(NodeStatus.ACTIVE);
	}

	protected boolean isInputPinsSatisfied() {
		for (IInputPin<?, ?> inputPin : this.getInput()) {
			if (!(inputPin instanceof ValuePin) && !inputPin.mayContinue()) {
				return false;
			}
		}
		return true;
	}

	protected abstract boolean execute();

	protected boolean isTriggered() {
		return true;
	}

	@Override
	protected boolean mayAcceptToken() {
		return true;
	}

	@Override
	public List<ControlToken> getInTokens() {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ControlFlow flow : getIncoming()) {
			Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				result.add(new ControlToken(edge.getInVertex()));
			}
		}
		return result;
	}

	@Override
	public List<ControlToken> getInTokens(String inFlowName) {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ControlFlow flow : getIncoming()) {
			if (inFlowName.equals(flow.getName())) {
				Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
				for (Edge edge : iter) {
					result.add(new ControlToken(edge.getInVertex()));
				}
			}
		}
		return result;
	}

	@Override
	public List<ControlToken> getOutTokens() {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ControlFlow flow : getOutgoing()) {
			Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				result.add(new ControlToken(edge.getInVertex()));
			}
		}
		return result;
	}

	@Override
	public List<ControlToken> getOutTokens(String outFlowName) {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ControlFlow flow : getOutgoing()) {
			if (flow.getName().equals(outFlowName)) {
				Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
				for (Edge edge : iter) {
					ControlToken e = new ControlToken(edge.getInVertex());
					result.add(e);
				}
			}
		}
		return result;
	}

	@Override
	public abstract List<ControlFlow> getIncoming();

	@Override
	public abstract List<ControlFlow> getOutgoing();

	@Override
	public CompositionNode getOwningObject() {
		return getActivity();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append("\n");
		sb.append("InputPins:");
		for (IInputPin<?, ?> o : getInput()) {
			sb.append("		");
			sb.append(o.toString());
			sb.append("\n");
		}
		sb.append("\nVariables:");
		for (Object o : getInputPinVariables()) {
			sb.append("		");
			sb.append(o.toString());
			sb.append("\n");
		}
		sb.append("\nOutputPin:");
		for (IOutputPin<?, ?> outputPin : getOutput()) {
			sb.append("		");
			sb.append(outputPin.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

}

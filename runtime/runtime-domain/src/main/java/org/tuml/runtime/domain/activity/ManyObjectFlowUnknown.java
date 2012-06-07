package org.tuml.runtime.domain.activity;

import java.util.Collection;

import com.tinkerpop.blueprints.Edge;

public abstract class ManyObjectFlowUnknown extends ObjectFlowUnknown<CollectionObjectToken<?>> {

	public ManyObjectFlowUnknown(Edge edge) {
		super(edge);
	}

	protected abstract boolean evaluateGuardConditions(Collection<?> tokenValue);
	
	@Override
	protected boolean evaluateGuardConditions(CollectionObjectToken<?> token) {
		return evaluateGuardConditions(token.getElements());	
	}

	public <O> ManyObjectFlowKnown<O> convertToKnownObjectFlow() {
		return new ManyObjectFlowKnown<O>(ManyObjectFlowUnknown.this.edge) {

			@Override
			protected boolean evaluateGuardConditions(Collection<O> tokenValue) {
				return ManyObjectFlowUnknown.this.evaluateGuardConditions(tokenValue);
			}

			@Override
			public <IN extends Token, OUT extends Token> ActivityNode<IN, OUT> getTarget() {
				return ManyObjectFlowUnknown.this.getTarget();
			}

			@Override
			public <IN extends Token, OUT extends Token> ActivityNode<IN, OUT> getSource() {
				return ManyObjectFlowUnknown.this.getSource();
			}

			@Override
			public String getName() {
				return ManyObjectFlowUnknown.this.getName();
			}

			@Override
			protected int getWeigth() {
				return ManyObjectFlowUnknown.this.getWeigth();
			}

		};
	}

}

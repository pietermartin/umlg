package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Edge;

public abstract class OneObjectFlowUnknown extends ObjectFlowUnknown<SingleObjectToken<?>> {

	public OneObjectFlowUnknown(Edge edge) {
		super(edge);
	}

	protected abstract boolean evaluateGuardConditions(Object tokenValue);
	
	@Override
	protected boolean evaluateGuardConditions(SingleObjectToken<?> token) {
		return evaluateGuardConditions(token.getObject());	
	}

	public <O> OneObjectFlowKnown<O> convertToKnownObjectFlow() {
		return new OneObjectFlowKnown<O>(OneObjectFlowUnknown.this.edge) {

			@Override
			protected boolean evaluateGuardConditions(O tokenValue) {
				return OneObjectFlowUnknown.this.evaluateGuardConditions(tokenValue);
			}

			@Override
			public <IN extends Token, OUT extends Token> ActivityNode<IN, OUT> getTarget() {
				return OneObjectFlowUnknown.this.getTarget();
			}

			@Override
			public <IN extends Token, OUT extends Token> ActivityNode<IN, OUT> getSource() {
				return OneObjectFlowUnknown.this.getSource();
			}

			@Override
			public String getName() {
				return OneObjectFlowUnknown.this.getName();
			}

			@Override
			protected int getWeigth() {
				return OneObjectFlowUnknown.this.getWeigth();
			}

		};
	}

}

package org.tuml.runtime.domain.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.activity.interf.IActivityEdge;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.pipes.AbstractPipe;

public abstract class ActivityEdge<T extends Token> extends AbstractPipe<T, Boolean> implements IActivityEdge<T> {

	protected Edge edge;
	private List<T> tokens = new ArrayList<T>();

	public ActivityEdge(Edge edge) {
		super();
		this.edge = edge;
	}

	@Override
	protected Boolean processNextStart() throws NoSuchElementException {
		this.tokens.clear();
		// Take all tokens
		while (starts.hasNext()) {
			T token = this.starts.next();
			if (evaluateGuardConditions(token)) {
				this.tokens.add(token);
			} else {
				GraphDb.getDb().removeVertex(token.getVertex());
			}
		}
		if (hasWeightPassed()) {
			ActivityNode<T,?> target = getTarget();
			target.setStarts(this.tokens);
			return target.next();
		} else {
			return false;
		}
	}

	@Override
	public abstract <IN extends Token, OUT extends Token> ActivityNode<IN, OUT> getTarget();

	@Override
	public abstract <IN extends Token, OUT extends Token> ActivityNode<IN, OUT> getSource();

	public abstract String getName();

	protected abstract boolean evaluateGuardConditions(T token);

	protected abstract int getWeigth();

	protected boolean hasWeightPassed() {
		return getWeigth() <= this.tokens.size();
	}
	
}

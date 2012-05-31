package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.Token;

import com.tinkerpop.pipes.Pipe;

public interface IActivityEdge<T extends Token> extends INamedElement, Pipe<T, Boolean> {
	<IN extends Token, OUT extends Token> IActivityNode<IN, OUT> getTarget();
	<IN extends Token, OUT extends Token> IActivityNode<IN, OUT> getSource();
}

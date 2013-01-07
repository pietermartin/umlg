package org.tuml.runtime.domain.activity.interf;

import com.tinkerpop.pipes.Pipe;
import org.tuml.runtime.domain.activity.Token;

public interface IActivityEdge<T extends Token> extends INamedElement, Pipe<T, Boolean> {
	<IN extends Token, OUT extends Token> IActivityNode<IN, OUT> getTarget();
	<IN extends Token, OUT extends Token> IActivityNode<IN, OUT> getSource();
}

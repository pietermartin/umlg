package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.AbstractActivity;
import org.tuml.runtime.domain.activity.Token;

public interface IControlNode<IN extends Token, OUT extends Token> extends IActivityNode<IN, OUT> {
	AbstractActivity getActivity();
}

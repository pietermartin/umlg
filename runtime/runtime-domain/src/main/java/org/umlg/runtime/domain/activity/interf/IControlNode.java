package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.AbstractActivity;
import org.umlg.runtime.domain.activity.Token;

public interface IControlNode<IN extends Token, OUT extends Token> extends IActivityNode<IN, OUT> {
	AbstractActivity getActivity();
}

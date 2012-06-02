package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.AbstractActivity;
import org.tuml.runtime.domain.activity.ObjectToken;

public interface IActivityParameterNode<O, OUT extends ObjectToken<O>> extends IObjectNode<O, OUT, OUT> {
	AbstractActivity getActivity();
}

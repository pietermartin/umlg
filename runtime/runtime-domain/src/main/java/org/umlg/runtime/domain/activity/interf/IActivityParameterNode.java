package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.AbstractActivity;
import org.umlg.runtime.domain.activity.ObjectToken;

public interface IActivityParameterNode<O, OUT extends ObjectToken<O>> extends IObjectNode<O, OUT, OUT> {
	AbstractActivity getActivity();
}

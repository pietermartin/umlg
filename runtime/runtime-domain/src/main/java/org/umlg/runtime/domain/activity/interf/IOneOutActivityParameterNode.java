package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.SingleObjectToken;

public interface IOneOutActivityParameterNode<O> extends IOneActivityParameterNode<O>, IInActivityParameterNode<O, SingleObjectToken<O>> {

}

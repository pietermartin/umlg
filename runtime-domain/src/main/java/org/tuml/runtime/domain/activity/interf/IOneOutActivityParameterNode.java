package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.SingleObjectToken;

public interface IOneOutActivityParameterNode<O> extends IOneActivityParameterNode<O>, IInActivityParameterNode<O, SingleObjectToken<O>> {

}

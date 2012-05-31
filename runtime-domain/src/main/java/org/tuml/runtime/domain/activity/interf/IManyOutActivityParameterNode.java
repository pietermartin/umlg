package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.CollectionObjectToken;

public interface IManyOutActivityParameterNode<O> extends IManyActivityParameterNode<O>, IInActivityParameterNode<O, CollectionObjectToken<O>> {

}

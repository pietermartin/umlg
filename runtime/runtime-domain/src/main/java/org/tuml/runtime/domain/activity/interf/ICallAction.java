package org.tuml.runtime.domain.activity.interf;

import java.util.List;

import org.tuml.runtime.domain.activity.AbstractActivity;

public interface ICallAction extends IInvocationAction {
	boolean isSynchronous();
	List<? extends IOutputPin<?, ?>> getResult();
	AbstractActivity getBehavior();
}

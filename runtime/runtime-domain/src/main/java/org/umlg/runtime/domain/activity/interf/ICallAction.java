package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.AbstractActivity;

import java.util.List;

public interface ICallAction extends IInvocationAction {
	boolean isSynchronous();
	List<? extends IOutputPin<?, ?>> getResult();
	AbstractActivity getBehavior();
}

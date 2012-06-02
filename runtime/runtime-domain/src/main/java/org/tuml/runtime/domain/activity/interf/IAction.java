package org.tuml.runtime.domain.activity.interf;

import java.util.List;

import org.tuml.runtime.domain.activity.AbstractActivity;

public interface IAction extends IExecutableNode {
	List<? extends IInputPin<?, ?>> getInput();
	List<? extends IOutputPin<?, ?>> getOutput();
//	IClassifier getContext();
	AbstractActivity getActivity();
}

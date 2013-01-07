package org.tuml.runtime.domain.activity.interf;

import org.tuml.runtime.domain.activity.AbstractActivity;

import java.util.List;

public interface IAction extends IExecutableNode {
	List<? extends IInputPin<?, ?>> getInput();
	List<? extends IOutputPin<?, ?>> getOutput();
//	IClassifier getContext();
	AbstractActivity getActivity();
}

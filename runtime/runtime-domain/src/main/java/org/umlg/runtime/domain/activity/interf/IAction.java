package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.AbstractActivity;

import java.util.List;

public interface IAction extends IExecutableNode {
	List<? extends IInputPin<?, ?>> getInput();
	List<? extends IOutputPin<?, ?>> getOutput();
//	IClassifier getContext();
	AbstractActivity getActivity();
}

package org.umlg.runtime.domain.activity.interf;

import java.util.List;

public interface IAcceptEventAction extends IAction {
	List<? extends ITrigger> getTrigger();
	List<? extends IOutputPin<?,?>> getResult();
}

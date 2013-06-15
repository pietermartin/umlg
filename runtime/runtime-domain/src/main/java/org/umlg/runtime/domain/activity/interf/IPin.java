package org.umlg.runtime.domain.activity.interf;

import org.umlg.runtime.domain.activity.ObjectToken;

import java.util.List;

public interface IPin<O,IN extends ObjectToken<O>,OUT extends ObjectToken<O>> extends IObjectNode<O,IN,OUT>, IMultiplicityElement {
	List<IN> getInTokens();
	List<OUT> getOutTokens();
	IAction getAction();
}

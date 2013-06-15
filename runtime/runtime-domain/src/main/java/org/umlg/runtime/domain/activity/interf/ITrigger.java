package org.umlg.runtime.domain.activity.interf;


public interface ITrigger extends INamedElement {
	IEvent getEvent();
	boolean accepts(IEvent event);
	Class<? extends IEvent> getEventClass();	
}

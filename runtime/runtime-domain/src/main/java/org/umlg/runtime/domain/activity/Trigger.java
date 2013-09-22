package org.umlg.runtime.domain.activity;

import org.umlg.runtime.domain.BaseUmlgAudit;
import org.umlg.runtime.domain.activity.interf.IEvent;
import org.umlg.runtime.domain.activity.interf.ITrigger;

public abstract class Trigger extends BaseUmlgAudit implements ITrigger {

	private static final long serialVersionUID = 5709531503304555463L;

	@Override
	public boolean accepts(IEvent event) {
		return event.getClass().isAssignableFrom(getEventClass());
	}

}

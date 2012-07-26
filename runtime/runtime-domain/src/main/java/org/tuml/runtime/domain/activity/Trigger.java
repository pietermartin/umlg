package org.tuml.runtime.domain.activity;

import org.tuml.runtime.domain.BaseTumlAudit;
import org.tuml.runtime.domain.activity.interf.IEvent;
import org.tuml.runtime.domain.activity.interf.ITrigger;

public abstract class Trigger extends BaseTumlAudit implements ITrigger {

	private static final long serialVersionUID = 5709531503304555463L;

	@Override
	public boolean accepts(IEvent event) {
		return event.getClass().isAssignableFrom(getEventClass());
	}

}

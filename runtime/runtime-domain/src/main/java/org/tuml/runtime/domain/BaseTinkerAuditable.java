package org.tuml.runtime.domain;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.tuml.runtime.adaptor.TransactionThreadVar;
import org.tuml.runtime.util.TinkerFormatter;

public abstract class BaseTinkerAuditable extends BaseTumlAudit implements TinkerAuditableNode, Serializable{

	private static final long serialVersionUID = 3751023772087546585L;
	
	public BaseTinkerAuditable() {
		super();
	}
	
	public void setDeletedOn(DateTime deletedOn) {
		super.setDeletedOn(deletedOn);
		if ( TransactionThreadVar.hasNoAuditEntry(getClass().getName() + getUid()) ) {
			createAuditVertex(false);
		}
		getAuditVertex().setProperty("deletedOn", TinkerFormatter.format(deletedOn));

	}

}

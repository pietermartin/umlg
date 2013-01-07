package org.tuml.runtime.domain;

import org.joda.time.DateTime;
import org.tuml.runtime.adaptor.TransactionThreadVar;
import org.tuml.runtime.util.TumlFormatter;

import java.io.Serializable;

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
		getAuditVertex().setProperty("deletedOn", TumlFormatter.format(deletedOn));

	}

}

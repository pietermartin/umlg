package org.umlg.runtime.domain;

import org.joda.time.DateTime;
import org.umlg.runtime.adaptor.TransactionThreadVar;
import org.umlg.runtime.util.UmlgFormatter;

import java.io.Serializable;

public abstract class BaseTinkerAuditable extends BaseUmlgAudit implements TinkerAuditableNode, Serializable{

	private static final long serialVersionUID = 3751023772087546585L;
	
	public BaseTinkerAuditable() {
		super();
	}
	
	public void setDeletedOn(DateTime deletedOn) {
		super.setDeletedOn(deletedOn);
		if ( TransactionThreadVar.hasNoAuditEntry(getClass().getName() + getUid()) ) {
			createAuditVertex(false);
		}
		getAuditVertex().property("deletedOn", UmlgFormatter.format(deletedOn));

	}

}

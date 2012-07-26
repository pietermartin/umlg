package org.tuml.runtime.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.tuml.runtime.adaptor.TransactionThreadVar;
import org.tuml.runtime.util.TinkerFormatter;

import com.tinkerpop.blueprints.Vertex;

public abstract class BaseTumlAudit extends BaseTuml implements TinkerAuditableNode, Serializable {

	private static final long serialVersionUID = 3751023772087546585L;
	protected Vertex auditVertex;

	public BaseTumlAudit() {
		super();
	}

	public Vertex getAuditVertex() {
		return auditVertex;
	}

	public Date getDeletedOn() {
		return TinkerFormatter.parse((String) this.vertex.getProperty("deletedOn"));
	}

	public void setDeletedOn(Date deletedOn) {
		this.vertex.setProperty("deletedOn", TinkerFormatter.format(deletedOn));
		if ( TransactionThreadVar.hasNoAuditEntry(getClass().getName() + getUid()) ) {
			createAuditVertex(false);
		}
		getAuditVertex().setProperty("deletedOn", TinkerFormatter.format(deletedOn));
	}

	public void defaultCreate() {
		super.defaultCreate();
		this.vertex.setProperty("deletedOn", TinkerFormatter.format(new Timestamp(1000L * 60 * 60 * 24 * 365 * 1000)));
	}

}

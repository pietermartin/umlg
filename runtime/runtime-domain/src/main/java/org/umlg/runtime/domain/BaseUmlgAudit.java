package org.umlg.runtime.domain;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.joda.time.DateTime;
import org.umlg.runtime.adaptor.TransactionThreadVar;
import org.umlg.runtime.util.UmlgFormatter;

import java.io.Serializable;

public abstract class BaseUmlgAudit extends BaseUmlg implements TinkerAuditableNode, Serializable {

	private static final long serialVersionUID = 3751023772087546585L;
	protected Vertex auditVertex;

	public BaseUmlgAudit() {
		super();
	}

	public Vertex getAuditVertex() {
		return auditVertex;
	}

	public DateTime getDeletedOn() {
		return UmlgFormatter.parseDateTime((String) this.vertex.value("deletedOn"));
	}

	public void setDeletedOn(DateTime deletedOn) {
		this.vertex.property("deletedOn", UmlgFormatter.format(deletedOn));
		if ( TransactionThreadVar.hasNoAuditEntry(getClass().getName() + getUid()) ) {
			createAuditVertex(false);
		}
		getAuditVertex().property("deletedOn", UmlgFormatter.format(deletedOn));
	}

//	public void defaultCreate() {
//		super.defaultCreate();
//		this.vertex.property("deletedOn", UmlgFormatter.format(new DateTime(1000L * 60 * 60 * 24 * 365 * 1000)));
//	}

}

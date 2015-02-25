package org.umlg.runtime.domain;

import org.apache.tinkerpop.gremlin.structure.Vertex;


public interface TinkerAuditNode extends PersistentObject {
	Long getTransactionNo();
	TinkerAuditNode getNextAuditEntry();
	TinkerAuditableNode getOriginal();
	String getOriginalUid();
	Vertex getVertex();
	boolean isTinkerRoot();
	String getName();
}

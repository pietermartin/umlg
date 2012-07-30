package org.tuml.runtime.domain;

import com.tinkerpop.blueprints.Vertex;


public interface TinkerAuditNode extends PersistentObject {
	Long getTransactionNo();
	TinkerAuditNode getNextAuditEntry();
	TinkerAuditableNode getOriginal();
	String getOriginalUid();
	Vertex getVertex();
	boolean isTinkerRoot();
}

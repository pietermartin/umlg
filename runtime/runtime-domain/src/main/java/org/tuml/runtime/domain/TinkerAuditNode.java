package org.tuml.runtime.domain;


public interface TinkerAuditNode extends TumlNode {
	Long getTransactionNo();
	TinkerAuditNode getNextAuditEntry();
	TinkerAuditableNode getOriginal();
	String getOriginalUid();
}

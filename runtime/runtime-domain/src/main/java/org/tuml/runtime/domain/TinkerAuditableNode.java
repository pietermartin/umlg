package org.tuml.runtime.domain;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;

public interface TinkerAuditableNode extends TumlNode {
	void createAuditVertex(boolean createParentVertex);
	Vertex getAuditVertex();
	List<? extends TinkerAuditNode> getAudits();
}

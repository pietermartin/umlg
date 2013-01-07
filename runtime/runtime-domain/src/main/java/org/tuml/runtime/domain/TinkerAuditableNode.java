package org.tuml.runtime.domain;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public interface TinkerAuditableNode extends TumlNode {
	void createAuditVertex(boolean createParentVertex);
	Vertex getAuditVertex();
	List<? extends TinkerAuditNode> getAudits();
}

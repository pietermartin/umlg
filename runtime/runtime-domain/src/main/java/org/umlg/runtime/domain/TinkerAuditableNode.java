package org.umlg.runtime.domain;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public interface TinkerAuditableNode extends UmlgNode {
	void createAuditVertex(boolean createParentVertex);
	Vertex getAuditVertex();
	List<? extends TinkerAuditNode> getAudits();
}

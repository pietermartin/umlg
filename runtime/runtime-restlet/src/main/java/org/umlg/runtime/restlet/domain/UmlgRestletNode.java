package org.umlg.runtime.restlet.domain;

import org.umlg.runtime.domain.UmlgNode;


public interface UmlgRestletNode extends UmlgNode {
	String getUri();
	String getQualifiedName();
	String getUmlName();
}

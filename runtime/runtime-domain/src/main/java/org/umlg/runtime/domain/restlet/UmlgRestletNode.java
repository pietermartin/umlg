package org.umlg.runtime.domain.restlet;

import org.umlg.runtime.domain.UmlgNode;

/**
 * This class does not have a dependency on restlet.
 * However if it is implemented it indicates the entities have been through the restlet generation visitors.
 */
public interface UmlgRestletNode extends UmlgNode {
	String getUri();
	String getQualifiedName();
	String getUmlName();
}

package org.tuml.runtime.domain;

public interface CompositionNode extends TumlNode {
	TumlNode getOwningObject();
}

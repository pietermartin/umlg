package org.tuml.runtime.restlet.domain;

import org.tuml.runtime.domain.TumlNode;


public interface TumlRestletNode extends TumlNode {
	String getUri();
	String getQualifiedName();
	String getUmlName();
}

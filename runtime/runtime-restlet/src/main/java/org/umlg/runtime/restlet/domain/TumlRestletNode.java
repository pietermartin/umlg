package org.umlg.runtime.restlet.domain;

import org.umlg.runtime.domain.TumlNode;


public interface TumlRestletNode extends TumlNode {
	String getUri();
	String getQualifiedName();
	String getUmlName();
}

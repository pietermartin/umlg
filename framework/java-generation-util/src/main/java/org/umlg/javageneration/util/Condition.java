package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.Element;

public interface Condition {
	
	boolean evaluateOn(Element e);
	
}

package org.tuml.framework;

import org.eclipse.uml2.uml.Element;

public interface Visitor<T extends Element> {
	void visitBefore(T element);
	void visitAfter(T element);
}

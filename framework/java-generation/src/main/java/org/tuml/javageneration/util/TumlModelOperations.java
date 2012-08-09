package org.tuml.javageneration.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.internal.operations.ModelOperations;

public class TumlModelOperations extends ModelOperations {

	public static List<? extends Element> findElements(Model model, Condition c) {
		List<Element> result = new ArrayList<Element>();
		visit(model, c, result);
		return result;
	}

	private static void visit(Element e, Condition c, List<Element> result) {
		if (c.evaluateOn(e)) {
			result.add(e);
		}
		for (Element element : e.getOwnedElements()) {
			visit(element, c, result);
		}
	}
	
}

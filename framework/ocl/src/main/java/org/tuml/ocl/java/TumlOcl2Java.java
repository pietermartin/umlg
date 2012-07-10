package org.tuml.ocl.java;

import org.eclipse.uml2.uml.MultiplicityElement;

public class TumlOcl2Java {

	public static String getCollectionInterface(MultiplicityElement multiplicityElement) {
		if (multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
			return "OrderedSet";
		} else if (multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
			return "Set";
		} else if (!multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
			return "Bag";
		} else if (!multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
			return "Sequence";
		} else {
			throw new IllegalStateException("Not supported");
		}
	}
	
}

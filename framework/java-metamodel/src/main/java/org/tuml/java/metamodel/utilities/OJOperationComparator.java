
package org.tuml.java.metamodel.utilities;

import java.util.Comparator;

import org.tuml.java.metamodel.OJOperation;
import org.tuml.java.metamodel.OJVisibilityKind;



public class OJOperationComparator implements Comparator<OJOperation> {

	public int compare(OJOperation first, OJOperation second) {
		if (first.getVisibility().equals(second.getVisibility())) {
			String firstSignature = first.getSignature();
			String secondSignature = second.getSignature();
			int compareTo = firstSignature.compareTo(secondSignature);
			return compareTo;
        } else {
            Integer value1 = first.getVisibility().getValue();
            Integer value2 = second.getVisibility().getValue();
            return value1.compareTo(value2);
        }
	}

}

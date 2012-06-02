
package org.opaeum.java.metamodel.utilities;

import java.util.Comparator;

import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJVisibilityKind;



public class OJOperationComparator implements Comparator<OJOperation> {

	public int compare(OJOperation first, OJOperation second) {
		if (first.getVisibility().equals(second.getVisibility())) {
			String firstSignature = first.getSignature();
			String secondSignature = second.getSignature();
			int compareTo = firstSignature.compareTo(secondSignature);
			return compareTo;
		} else if (first.getVisibility().equals(OJVisibilityKind.PRIVATE) && second.getVisibility().equals(OJVisibilityKind.PUBLIC)) {
			return 1;
		} else if (first.getVisibility().equals(OJVisibilityKind.PUBLIC) && second.getVisibility().equals(OJVisibilityKind.PRIVATE)) {
			return -1;
		}
		return 0;
	}

}

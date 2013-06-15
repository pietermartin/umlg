package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.internal.operations.BehavioredClassifierOperations;

public class TumlBehavioredClassifierOperations extends BehavioredClassifierOperations {

	public static boolean hasBehavior(BehavioredClassifier behavioredClassifier) {
		if (behavioredClassifier.getClassifierBehavior() != null || !behavioredClassifier.getOwnedBehaviors().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
}

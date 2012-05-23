package org.tuml.javageneration.naming;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

public class Namer {

	public static String name(Classifier classifier) {
		return classifier.getName();
	}

	public static String name(Package nearestPackage) {
		return StringUtils.replace(nearestPackage.getQualifiedName(), "::", ".");
	}

	public static String qualifiedName(NamedElement namedElement) {
		return StringUtils.replace(namedElement.getQualifiedName(), "::", ".");
	}

}

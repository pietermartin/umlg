package org.tuml.javageneration.naming;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Type;

public class Namer {

	public static String name(Type type) {
		return type.getName();
	}

	public static String name(Package nearestPackage) {
		return StringUtils.replace(nearestPackage.getQualifiedName(), "::", ".");
	}

	public static String qualifiedName(NamedElement namedElement) {
		return StringUtils.replace(namedElement.getQualifiedName(), "::", ".");
	}

}

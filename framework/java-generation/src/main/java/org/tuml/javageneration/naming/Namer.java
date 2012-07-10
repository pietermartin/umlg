package org.tuml.javageneration.naming;

import java.util.Arrays;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Type;

public class Namer {

	public static String name(Type type) {
		return type.getName();
	}

	public static String nameIncludingModel(Package nearestPackage) {
		String[] packageParts = nearestPackage.getQualifiedName().split("::");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String s : packageParts) {
			sb.append(s);
			i++;
			if (i < packageParts.length) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	public static String name(Package nearestPackage) {
		String[] packageParts = nearestPackage.getQualifiedName().split("::");
		String[] javaPackageParts = Arrays.copyOfRange(packageParts, 1, packageParts.length);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String s : javaPackageParts) {
			sb.append(s);
			i++;
			if (i < javaPackageParts.length) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	public static String qualifiedName(NamedElement namedElement) {
		String[] packageParts = namedElement.getQualifiedName().split("::");
		String[] javaPackageParts = Arrays.copyOfRange(packageParts, 1, packageParts.length);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String s : javaPackageParts) {
			sb.append(s);
			i++;
			if (i < javaPackageParts.length) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

}

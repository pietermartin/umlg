package org.umlg.java.metamodel.annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.umlg.java.metamodel.OJElement;
import org.umlg.java.metamodel.OJPathName;

public class AnnotationHelper {
	public static Set<OJPathName> getImportsFrom(Collection<? extends OJElement> sources) {
		Set<OJPathName> result = new HashSet<OJPathName>();
		for (OJElement v : sources) {
			if (v instanceof OJAnnotatedElement) {
				addTypesUsed((OJAnnotatedElement) v, result);
			}
		}
		return result;
	}
	public static Set<OJPathName> getImportsFrom(OJAnnotatedElement element) {
		Set<OJPathName> result = new HashSet<OJPathName>();
		addTypesUsed(element, result);
		return result;
	}
	private static void addTypesUsed(OJAnnotatedElement element, Set<OJPathName> result) {
		for (OJAnnotationValue v : element.getAnnotations()) {
			result.addAll(v.getAllTypesUsed());
		}
	}
	/**
	 * Adds an annotation to the target element, but only if an annotation value of this type has not been associated
	 * with the target yet. Returns true if the annotation value was added, false if an exeisting value was found for
	 * that annotation
	 * 
	 * @param value
	 * @param target
	 * @return
	 */
	public static boolean maybeAddAnnotation(OJAnnotationValue value, OJAnnotatedElement target) {
		Collection<OJAnnotationValue> sourceSet = target.getAnnotations();
		for (Iterator<OJAnnotationValue> iter = sourceSet.iterator(); iter.hasNext();) {
			if (value.getType().equals(iter.next().getType())) {
				return false;
			}
		}
		sourceSet.add(value);
		return true;
	}
	public static OJAnnotationValue getAnnotation(OJAnnotatedElement target, OJPathName path) {
		for (OJAnnotationValue v:target.getAnnotations()) {
			if (v.getType().equals(path)) {
				return v;
			}
		}
		return null;
	}
}

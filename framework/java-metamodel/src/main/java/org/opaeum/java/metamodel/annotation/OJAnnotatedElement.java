package org.opaeum.java.metamodel.annotation;

import java.util.Collection;

import org.opaeum.java.metamodel.OJPathName;

public interface OJAnnotatedElement {
	Collection<OJAnnotationValue> getAnnotations();

	String toJavaString();

	OJAnnotationValue putAnnotation(OJAnnotationValue an);

	OJAnnotationValue removeAnnotation(OJPathName type);

	boolean addAnnotationIfNew(OJAnnotationValue value);

	OJAnnotationValue findAnnotation(OJPathName ojPathName);

}

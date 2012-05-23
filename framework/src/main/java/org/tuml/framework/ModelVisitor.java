package org.tuml.framework;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.eclipse.uml2.uml.Element;

public class ModelVisitor {

	@SuppressWarnings("unchecked")
	public static void visitModel(Element element, @SuppressWarnings("rawtypes") Visitor visitor) {
		Type[] types = visitor.getClass().getGenericInterfaces();
		if (types.length != 1) {
			throw new IllegalStateException();
		}
		ParameterizedType t = (ParameterizedType) types[0];
		Type[] paramTypes = t.getActualTypeArguments();
		if (paramTypes.length != 1) {
			throw new IllegalStateException();
		}
		//This is the type the visitor is visiting
		Type visitingType = paramTypes[0];
		try {
			Class<?> visitingTypeClass = (Class<?>) visitingType;
			Method method = visitor.getClass().getMethod("visitBefore", visitingTypeClass);
			if (method != null && method.isAnnotationPresent(VisitSubclasses.class)) {
				if (visitingTypeClass.isAssignableFrom(element.getClass())) {
					visitor.visitBefore(element);
				}
			} else {
				Class<?>[] elementInterfaces = element.getClass().getInterfaces();
				for (Class<?> elementInterface : elementInterfaces) {
					if (visitingTypeClass.equals(elementInterface)) {
						visitor.visitBefore(element);
					}
				}
			}
			for (Element e : element.getOwnedElements()) {
				visitModel(e, visitor);
			}
			method = visitor.getClass().getMethod("visitAfter", visitingTypeClass);
			if (method != null && method.isAnnotationPresent(VisitSubclasses.class)) {
				if (visitingTypeClass.isAssignableFrom(element.getClass())) {
					visitor.visitAfter(element);
				}
			} else {
				Class<?>[] elementInterfaces = visitingTypeClass.getInterfaces();
				for (Class<?> elementInterface : elementInterfaces) {
					if (visitingTypeClass.equals(elementInterface)) {
						visitor.visitAfter(element);
					}
				}
			}
		} catch (SecurityException e1) {
			throw new RuntimeException(e1);
		} catch (NoSuchMethodException e1) {
			throw new RuntimeException(e1);
		}
	}

}

package org.umlg.framework;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.PackageImport;

public class ModelVisitor {

	@SuppressWarnings("unchecked")
	public static void visitModel(Element element, @SuppressWarnings("rawtypes") Visitor visitor) {
		Type[] types = visitor.getClass().getGenericInterfaces();
		if (types.length != 1) {
			throw new IllegalStateException(String.format("Visitor must have one and only one generic interface, visitor %s has %s", visitor.getClass()
					.getSimpleName(), types.length));
		}
		ParameterizedType t = (ParameterizedType) types[0];
		Type[] paramTypes = t.getActualTypeArguments();
		if (paramTypes.length != 1) {
			throw new IllegalStateException();
		}
		// This is the type the visitor is visiting
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
			if (element instanceof PackageImport) {
				PackageImport pi = (PackageImport)element;
				org.eclipse.uml2.uml.Package p = pi.getImportedPackage();
				for (Element e : p.getOwnedElements()) {
					visitModel(e, visitor);
				}
			} else {
				for (Element e : element.getOwnedElements()) {
					visitModel(e, visitor);
				}
			}
			method = visitor.getClass().getMethod("visitAfter", visitingTypeClass);
			if (method != null && method.isAnnotationPresent(VisitSubclasses.class)) {
				if (visitingTypeClass.isAssignableFrom(element.getClass())) {
					visitor.visitAfter(element);
				}
			} else {
				Class<?>[] elementInterfaces = element.getClass().getInterfaces();
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

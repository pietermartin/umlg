package org.tuml.javageneration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.ModelPrinter;

public class Workspace {

	private final static Map<String, OJAnnotatedClass> javaClassMap = new HashMap<String, OJAnnotatedClass>(); 
	
	public static void addToClassMap(OJAnnotatedClass ojClass) {
		javaClassMap.put(ojClass.getQualifiedName(), ojClass);
	}

	public static void toText(File project) {
		for (Map.Entry<String, OJAnnotatedClass> entry : javaClassMap.entrySet()) {
			ModelPrinter.addToSource(entry.getKey(), entry.getValue().toJavaString());
		}
		ModelPrinter.toText(project);
	}

	public static OJAnnotatedClass findOJClass(String name) {
		return javaClassMap.get(name);
	}
	
}

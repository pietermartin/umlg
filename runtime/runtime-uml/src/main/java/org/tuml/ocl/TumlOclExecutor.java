package org.tuml.ocl;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.ruml.runtime.groovy.TumlGroovyShell;
import org.tuml.framework.ModelLoader;
import org.tuml.javageneration.ocl.TumlOcl2Java;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.runtime.domain.PersistentObject;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.runtime.domain.json.ToJsonUtil;

public class TumlOclExecutor {

	/**
	 * This is for static ocl, including allInstances
	 * @param contextQualifiedName
	 * @param query
	 * @return
	 */
	public static Object executeOclQuery(String contextQualifiedName, String query) {
		Classifier contextClassifier = (Classifier) ModelLoader.findNamedElement(contextQualifiedName);
		OJAnnotatedClass oclClass = new OJAnnotatedClass("OclQuery");
		oclClass.setSuperclass(TumlClassOperations.getPathName(contextClassifier));
		OJPackage ojPackage = new OJPackage(Namer.name(contextClassifier.getNearestPackage()));
		oclClass.setMyPackage(ojPackage);
		oclClass.getDefaultConstructor();
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
		constructor.getBody().addToStatements("super(vertex)");
		oclClass.addToConstructors(constructor);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TumlOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
		try {
			OCLExpression<Classifier> expr = TumlOcl2Parser.INSTANCE.getHelper().createQuery(query);
			OJAnnotatedOperation getter = new OJAnnotatedOperation("execute");
			getter.setStatic(true);
			getter.setReturnType(TumlOcl2Java.calcReturnType(expr));
			getter.getBody().addToStatements(getter.getReturnType().getLast() + " result = " + TumlOcl2Java.oclToJava(oclClass, expr));
			getter.getBody().addToStatements("return result");
			oclClass.addToOperations(getter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
		String javaString = oclClass.toJavaString();
		Object result = TumlGroovyShell.executeQuery(javaString);
		return result;
	}

	public static Object executeOclQuery(String contextQualifiedName, TumlNode contextTumlNode, String query) {
		Classifier contextClassifier = (Classifier) ModelLoader.findNamedElement(contextQualifiedName);
		OJAnnotatedClass oclClass = new OJAnnotatedClass("OclQuery");
		oclClass.setSuperclass(TumlClassOperations.getPathName(contextClassifier));
		OJPackage ojPackage = new OJPackage(Namer.name(contextClassifier.getNearestPackage()));
		oclClass.setMyPackage(ojPackage);
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
		constructor.getBody().addToStatements("super(vertex)");
		oclClass.addToConstructors(constructor);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TumlOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
		try {
			OCLExpression<Classifier> expr = TumlOcl2Parser.INSTANCE.getHelper().createQuery(query);
			OJAnnotatedOperation getter = new OJAnnotatedOperation("execute");
			getter.setReturnType(TumlOcl2Java.calcReturnType(expr));
			getter.getBody().addToStatements(getter.getReturnType().getLast() + " result = " + TumlOcl2Java.oclToJava(oclClass, expr));
			getter.getBody().addToStatements("return result");
			oclClass.addToOperations(getter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
		String javaString = oclClass.toJavaString();
		Object result = TumlGroovyShell.executeQuery(javaString, contextTumlNode.getVertex());
		return result;
	}

	@SuppressWarnings("unchecked")
	public static String executeOclQueryToJson(String contextQualifiedName, TumlNode contextTumlNode, String query) {
		Object result = executeOclQuery(contextQualifiedName, contextTumlNode, query);
		if (result instanceof Map) {
			return tupleMapToJson((Map<String, Object>) result);
		} else if (result instanceof Collection) {
			return ToJsonUtil.toJsonWithoutCompositeParent((Collection<? extends PersistentObject>) result);
		} else if (result instanceof PersistentObject) {
			PersistentObject po = (PersistentObject) result;
			return po.toJsonWithoutCompositeParent();
		} else {
            return result.toString();
//			throw new IllegalStateException(String.format("Unhandled result from ocl query, result = %s", result.getClass().getName()));
		}
	}

	private static String tupleMapToJson(Map<String, Object> result) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		int count = 1;
		for (String key : result.keySet()) {
			Object value = result.get(key);
			sb.append("\"");
			sb.append(key);
			sb.append("\": ");
			valueToJson(sb, value);
			if (count++ != result.size()) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	private static void valueToJson(StringBuilder sb, Object value) {
		if (value instanceof PersistentObject) {
			sb.append( ((PersistentObject) value).toJsonWithoutCompositeParent() );
		} else if (value instanceof String) {
			sb.append( "\"" + value + "\"" );
		} else if (value instanceof Collection) {
			Collection<Object> c = (Collection<Object>)value;
			sb.append("[");
			int count = 1;
			for (Object object : c) {
				valueToJson(sb, object);
				if (count++ != c.size()) {
					sb.append(", ");
				}
			}
			sb.append("]");
		} else {
			sb.append( value );
		}
	}

}

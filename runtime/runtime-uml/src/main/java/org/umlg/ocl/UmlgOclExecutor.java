package org.umlg.ocl;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.ruml.runtime.groovy.UmlgGroovyShell;
import org.umlg.java.metamodel.OJConstructor;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.ModelLoader;
import org.umlg.javageneration.ocl.UmlgOcl2Java;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgNode;

public class UmlgOclExecutor {

	/**
	 * This is for static ocl, including allInstances
	 * @param contextQualifiedName
	 * @param query
	 * @return
	 */
	public static Object executeOclQuery(String contextQualifiedName, String query) {
		Classifier contextClassifier = (Classifier) ModelLoader.INSTANCE.findNamedElement(contextQualifiedName);

        if (contextClassifier == null) {
            throw new RuntimeException("ocl \"" + query + "\" has no context!");
        }

		OJAnnotatedClass oclClass = new OJAnnotatedClass("OclQuery");
		oclClass.setSuperclass(UmlgClassOperations.getPathName(contextClassifier));
		OJPackage ojPackage = new OJPackage(Namer.name(contextClassifier.getNearestPackage()));
		oclClass.setMyPackage(ojPackage);
		oclClass.getDefaultConstructor();
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("vertex", UmlgGenerationUtil.vertexPathName);
		constructor.getBody().addToStatements("super(vertex)");
		oclClass.addToConstructors(constructor);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		UmlgOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
		try {
			OCLExpression<Classifier> expr = UmlgOcl2Parser.INSTANCE.getHelper().createQuery(query);
			OJAnnotatedOperation getter = new OJAnnotatedOperation("execute");
			getter.setStatic(true);
			getter.setReturnType(UmlgOcl2Java.calcReturnType(expr));
			getter.getBody().addToStatements(getter.getReturnType().getLast() + " result = " + UmlgOcl2Java.oclToJava(oclClass, expr));
			getter.getBody().addToStatements("return result");
			oclClass.addToOperations(getter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
		String javaString = oclClass.toJavaString();
		Object result = UmlgGroovyShell.executeQuery(javaString);
		return result;
	}

	public static <T> T executeOclQuery(UmlgNode contextTumlNode, String query) {
		Classifier contextClassifier = (Classifier) ModelLoader.INSTANCE.findNamedElement(contextTumlNode.getQualifiedName());
		OJAnnotatedClass oclClass = new OJAnnotatedClass("OclQuery");
		oclClass.setSuperclass(UmlgClassOperations.getPathName(contextClassifier));
		OJPackage ojPackage = new OJPackage(Namer.name(contextClassifier.getNearestPackage()));
		oclClass.setMyPackage(ojPackage);
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("vertex", UmlgGenerationUtil.vertexPathName);
		constructor.getBody().addToStatements("super(vertex)");
		oclClass.addToConstructors(constructor);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		UmlgOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
		try {
			OCLExpression<Classifier> expr = UmlgOcl2Parser.INSTANCE.getHelper().createQuery(query);
			OJAnnotatedOperation getter = new OJAnnotatedOperation("execute");
			getter.setReturnType(UmlgOcl2Java.calcReturnType(expr));
			getter.getBody().addToStatements(getter.getReturnType().getLast() + " result = " + UmlgOcl2Java.oclToJava(oclClass, expr));
			getter.getBody().addToStatements("return result");
			oclClass.addToOperations(getter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
		String javaString = oclClass.toJavaString();
		Object result = UmlgGroovyShell.executeQuery(javaString, contextTumlNode.getVertex());
		return (T)result;
	}

    //This is called via reflection from UmlgGraph
	@SuppressWarnings("unchecked")
	public static String executeOclQueryToJson(UmlgNode contextTumlNode, String query) {
		Object result = executeOclQuery(contextTumlNode, query);
		if (result instanceof Map) {
			return tupleMapToJson((Map<String, Object>) result);
		} else if (result instanceof Collection) {

            //TODO need to sort out polymorphic queries
            Collection<PersistentObject> poCollection = (Collection<PersistentObject>) result;
            StringBuilder json = new StringBuilder();
            json.append("[");
            json.append("{\"data\": [");
            int count = 0;
            PersistentObject poForMetaData = null;
            for (PersistentObject po : poCollection) {
                count++;
                String objectAsJson = po.toJsonWithoutCompositeParent();
                String objectAsJsonWithRow = "{\"row\": " + count + ", " + objectAsJson.substring(1);
                json.append(objectAsJsonWithRow);
                if (count != poCollection.size()) {
                    json.append(",");
                } else {
                    poForMetaData = po;
                }
            }
            json.append("],");
            json.append(" \"meta\" : {");
            //TODO some hardcoding to sort out
            json.append("\"qualifiedName\": \"restAndJson::org::umlg::test::Hand::finger\"");
            json.append(", \"to\": ");
            if (poForMetaData != null) {
                json.append(poForMetaData.getMetaDataAsJson());
            } else {
                json.append("null");
            }
            json.append("}");
            json.append("}]");
            return json.toString();
        } else if (result instanceof PersistentObject) {
			PersistentObject po = (PersistentObject) result;
			return po.toJsonWithoutCompositeParent();
		} else {
            return "{\"result\": " + "\"" + result.toString() + "\"}";
		}
	}

	public static String tupleMapToJson(Map<String, Object> result) {
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

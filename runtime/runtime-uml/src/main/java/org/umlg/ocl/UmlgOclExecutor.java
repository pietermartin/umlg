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
     * This is for static ocl, mostly allInstances
     *
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
        OJPackage ojPackage = new OJPackage(Namer.name(contextClassifier.getNearestPackage()));
        oclClass.setMyPackage(ojPackage);
        OJConstructor constructor = new OJConstructor();
        //Do not call the default constructor, as it will create try a persistent OclQuery which is all wrong like you know.
        constructor.addParam("jippo", "boolean");
        oclClass.addToConstructors(constructor);
        oclClass.addToImports(UmlgClassOperations.getPathName(contextClassifier));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        UmlgOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
        try {
            OCLExpression<Classifier> expr = UmlgOcl2Parser.INSTANCE.getHelper().createQuery(query);
            OJAnnotatedOperation getter = new OJAnnotatedOperation("execute");
            getter.setStatic(true);
            getter.setReturnType(UmlgOcl2Java.calcReturnType(expr));
            getter.getBody().addToStatements(getter.getReturnType().getLast() + " result = " + UmlgOcl2Java.oclToJava(contextClassifier, oclClass, expr));
            getter.getBody().addToStatements("return result");
            oclClass.addToOperations(getter);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
        String javaString = oclClass.toJavaString();
        Object result = UmlgGroovyShell.executeQuery(javaString);
        return result;
    }

    public static String executeOclQueryAsJson(String contextQualifiedName, String query) {
        return toJson(executeOclQuery(contextQualifiedName, query));
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
        oclClass.addToImports("org.joda.time.*");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        UmlgOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
        try {
            OCLExpression<Classifier> expr = UmlgOcl2Parser.INSTANCE.getHelper().createQuery(query);
            OJAnnotatedOperation getter = new OJAnnotatedOperation("execute");
            getter.setReturnType(UmlgOcl2Java.calcReturnType(expr));
            getter.getBody().addToStatements(getter.getReturnType().getLast() + " result = " + UmlgOcl2Java.oclToJava(contextClassifier, oclClass, expr));
            getter.getBody().addToStatements("return result");
            oclClass.addToOperations(getter);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
        String javaString = oclClass.toJavaString();
        Object result = UmlgGroovyShell.executeQuery(javaString, contextTumlNode.getVertex());
        return (T) result;
    }

    //This is called via reflection from UmlgGraph
    @SuppressWarnings("unchecked")
    public static String executeOclQueryAsJson(UmlgNode contextTumlNode, String query) {
        Object result = executeOclQuery(contextTumlNode, query);
        return toJson(result);
    }

    private static String toJson(Object result) {
        if (result instanceof Map) {
//            return UmlgOclExecutor.tupleMapToJson((Map<String, Object>) result);
            //TODO
            return result.toString();
        } else if (result instanceof Collection) {

            //TODO need to sort out polymorphic queries
            Collection<Object> poCollection = (Collection<Object>) result;

            StringBuilder json = new StringBuilder();
            json.append("[");
            json.append("{\"data\": [");
            int count = 0;
            PersistentObject poForMetaData = null;
            for (Object o : poCollection) {
                count++;
                if (o instanceof PersistentObject) {
                    PersistentObject po = (PersistentObject) o;
                    String objectAsJson = po.toJsonWithoutCompositeParent();
                    String objectAsJsonWithRow = "{\"row\": " + count + ", " + objectAsJson.substring(1);
                    json.append(objectAsJsonWithRow);
                    if (count != poCollection.size()) {
                        json.append(",");
                    } else {
                        poForMetaData = po;
                    }
                } else {
                    String objectAsJson = o.toString();
                    String objectAsJsonWithRow = "{\"row\": " + count + ", \"value\": \"" + objectAsJson + "\"}";
                    json.append(objectAsJsonWithRow);
                    if (count != poCollection.size()) {
                        json.append(",");
                    }

                }
            }
            json.append("],");
            json.append(" \"meta\" : {");
            //TODO create some meta data strategy for tuples and lists of primitives or datatypes
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
            return getRepresentation(po);
        } else {
            return "{\"result\": " + "\"" + (result == null ? "No result" : result.toString()) + "\"}";
        }
    }

    protected static String getRepresentation(PersistentObject po) {
        StringBuilder json = new StringBuilder();
        json.append("[{\"data\": [");
        String poAsJson = po.toJson();
        json.append(poAsJson.substring(0, poAsJson.length() - 1) + ", \"row\": 1}");
        json.append("], \"meta\" : {");
        json.append("\"qualifiedName\": \"restAndJson::org::umlg::test::Human\"");
        json.append(", \"to\": ");
        json.append(po.getMetaDataAsJson());
        json.append("}}]");
        return json.toString();
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
            sb.append(((PersistentObject) value).toJsonWithoutCompositeParent());
        } else if (value instanceof String) {
            sb.append("\"" + value + "\"");
        } else if (value instanceof Collection) {
            Collection<Object> c = (Collection<Object>) value;
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
            sb.append(value);
        }
    }

}

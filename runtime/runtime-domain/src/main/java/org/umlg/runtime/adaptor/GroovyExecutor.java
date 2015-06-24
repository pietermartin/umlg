package org.umlg.runtime.adaptor;

import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Date: 2015/06/24
 * Time: 1:06 PM
 */
public class GroovyExecutor {

    public static GroovyExecutor INSTANCE = new GroovyExecutor();
    private GremlinExecutor gremlinExecutor;

    private GroovyExecutor() {
        start();
    }

    public Object executeGroovy(Object context, String groovy) {
//        groovy = groovy.replaceAll("::", "____");
        if (context != null) {
            if (context instanceof UmlgNode) {
                Object id = ((UmlgNode) context).getId();
                groovy = groovy.replaceAll("self(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", "g.V(\"" + id.toString() + "\")");
            } else if (!(context instanceof Long)) {
                groovy = groovy.replaceAll("self(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", "g.V(\"" + context.toString() + "\")");
            } else if (context instanceof Long) {
                groovy = groovy.replaceAll("self(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", "g.V(\"" + context.toString() + "L\")");
            } else {
                groovy = groovy.replaceAll("self(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", "g.V(" + context.toString() + ")");
            }
        }
        try {
            return eval(groovy);
        } catch (Exception e) {
            throw UmlgExceptionUtilFactory.getTumlExceptionUtil().handle(e);
        }
    }

    /**
     * Executes a groovy query. If the contextId is null then it is ignored.
     * If it is not null then all instances of the keywork "this" will be replaced with "g.v(contextId)"
     *
     * @param contextId
     * @param groovy
     * @return
     */
    public String executeGroovyAsString(Object contextId, String groovy) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = executeGroovy(contextId, groovy);

        Iterator tempIterator = Collections.emptyIterator();
        StringBuilder sb = new StringBuilder();

        while (true) {
            if (tempIterator.hasNext()) {
                while (tempIterator.hasNext()) {
                    final Object object = tempIterator.next();
                    sb.append(((null == object) ? null : object.toString()));
                    sb.append("\n");
                }
                break;
            } else {
                try {
                    if (result instanceof Iterator) {
                        tempIterator = (Iterator) result;
                        if (!tempIterator.hasNext()) break;
                    } else if (result instanceof Iterable) {
                        tempIterator = ((Iterable) result).iterator();
                        if (!tempIterator.hasNext()) break;
                    } else if (result instanceof Object[]) {
                        tempIterator = new ArrayIterator((Object[]) result);
                        if (!tempIterator.hasNext()) break;
                    } else if (result instanceof Map) {
                        tempIterator = ((Map) result).entrySet().iterator();
                        if (!tempIterator.hasNext()) break;
                    } else {
                        sb.append(((null == result) ? null : result.toString()));
                        break;
                    }
                } catch (final Exception e) {
                    throw e;
                }
            }
        }

        stopWatch.stop();
        sb.append("Time to execute query = ");
        sb.append(stopWatch.toString());
        return sb.toString();
    }

    public String evalAsTemplate(String script, Map<String, Object> bindings) throws Exception {
        bindings.put("g", UMLG.get().getUnderlyingGraph().traversal());
        SimpleTemplateEngine engine = new SimpleTemplateEngine();
        Writable w = engine.createTemplate(script).make(bindings);
        return w.toString();
    }

    public Object eval(String script) throws Exception {
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("g", UMLG.get().getUnderlyingGraph().traversal());
        return this.gremlinExecutor.eval(script, bindings).get();
    }

    public Object eval(String script, Map<String, Object> bindings) throws Exception {
        bindings.put("g", UMLG.get().getUnderlyingGraph().traversal());
        return this.gremlinExecutor.eval(script, bindings).get();
    }

    public void close() throws Exception {
        this.gremlinExecutor.close();
    }

    public void restart() throws Exception {
        this.gremlinExecutor.close();
        INSTANCE = new GroovyExecutor();
    }

    private void start() {
        List imports = new ArrayList<>();
        List staticImports = new ArrayList<>();
        Class<?> umlgGroovyImporter;
        try {
            umlgGroovyImporter = Class.forName("org.umlg.runtime.adaptor.UmlgGroovyImporter");
            Field importsField = umlgGroovyImporter.getField("imports");
            imports.addAll((Set<String>) importsField.get(null));
            Field importsStaticField = umlgGroovyImporter.getField("importStatic");
            staticImports.addAll((Set<String>) importsStaticField.get(null));
            this.gremlinExecutor = GremlinExecutor.build()
                    .addEngineSettings("gremlin-groovy",
                            imports,
                            staticImports,
                            Arrays.asList("/home/pieter/Downloads/cm/cm-entity/src/main/resources/groovy/GremlinExecutorInit.groovy"),
                            Collections.emptyMap())
                    .afterSuccess(
                            t -> UMLG.get().rollback()
                    )
                    .afterFailure(
                            (t, e) -> UMLG.get().rollback()
                    )
                    .create();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize GremlinExecutor", e);
        }
    }
}

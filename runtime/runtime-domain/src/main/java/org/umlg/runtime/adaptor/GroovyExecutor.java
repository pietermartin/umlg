package org.umlg.runtime.adaptor;

import com.tinkerpop.gremlin.groovy.DefaultImportCustomizerProvider;
import com.tinkerpop.gremlin.structure.Graph;
import org.apache.commons.lang.time.StopWatch;
import org.umlg.runtime.domain.UmlgNode;

import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Date: 2013/06/09
 * Time: 8:34 PM
 */
public class GroovyExecutor {

    public static GroovyExecutor INSTANCE = new GroovyExecutor();
    private UmlgGremlinGroovyScriptEngine scriptEngine;
    private boolean isNeo4j;

    private static Set<String> getUmlgImports() {
        try {
            Class<?> umlgGroovyImporter = Class.forName("org.umlg.runtime.adaptor.UmlgGroovyImporter");
            Field imports = umlgGroovyImporter.getField("imports");
            return (Set<String>) imports.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<String> getUmlgStaticImports() {
        try {
            Class<?> umlgGroovyImporter = Class.forName("org.umlg.runtime.adaptor.UmlgGroovyImporter");
            Field imports = umlgGroovyImporter.getField("importStatic");
            return (Set<String>) imports.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GroovyExecutor() {
        try {
            Class.forName("org.umlg.runtime.adaptor.UmlgNeo4jGraph");
            this.isNeo4j = true;
        } catch (ClassNotFoundException e) {
            this.isNeo4j = false;
        }
        DefaultImportCustomizerProvider.initializeStatically(getUmlgImports(), getUmlgStaticImports());
        this.refresh();
    }

    public void refresh() {
        this.scriptEngine = new UmlgGremlinGroovyScriptEngine("GremlinExecutorBaseClass");
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

    public Object executeGroovy(Object context, String groovy) {
        groovy = groovy.replaceAll("::", "____");
        if (context != null) {
            if (context instanceof UmlgNode) {
                Object id = ((UmlgNode) context).getId();
                groovy = groovy.replaceAll("self(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", "g.v(\"" + id.toString() + "\")");
            } else if (!(context instanceof Long)) {
                groovy = groovy.replaceAll("self(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", "g.v(\"" + context.toString() + "\")");
            } else {
                groovy = groovy.replaceAll("self(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", "g.v(" + context + ")");
            }
        }
        Graph graph = ((UmlgAdminGraph)UMLG.get()).getReadOnlyGraph();
        GremlinExecutorBaseClass.load(graph);
        this.scriptEngine.put("g", graph);
        Object result;
        try {
            result = this.scriptEngine.eval(groovy);
        } catch (ScriptException e) {
            throw UmlgExceptionUtilFactory.getTumlExceptionUtil().handle(e);
        }
        return result;
    }

    private void seperateOutDefFromGroovy(String groovy, StringBuilder groovyToIntercept, StringBuilder groovyDef) {
        while (groovy.indexOf("def") != -1) {
            int indexOfDef = groovy.indexOf("def");
            groovyToIntercept.append(groovy.substring(0, indexOfDef));
            groovy = groovy.substring(indexOfDef, groovy.length());
            int indexOfCurly = groovy.indexOf("}");
            groovyDef.append(groovy.substring(0, indexOfCurly + 1));
            groovy = groovy.substring(indexOfCurly + 1, groovy.length());

            //check for semicolon
            int indexOfSemicolon = groovy.indexOf(";");
            if (indexOfSemicolon != -1) {
                groovyDef.append(groovy.substring(0, indexOfSemicolon + 1));
                groovy = groovy.substring(indexOfSemicolon + 1, groovy.length());
            }
            //check for new line
            int indexOfNewline = groovy.indexOf("\n");
            if (indexOfNewline != -1) {
                groovyDef.append(groovy.substring(0, indexOfNewline + 1));
                groovy = groovy.substring(indexOfNewline + 1, groovy.length());
            }
        }
        groovyToIntercept.append(groovy);
    }
}
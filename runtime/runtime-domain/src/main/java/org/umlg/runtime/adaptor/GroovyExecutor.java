package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.gremlin.groovy.jsr223.DefaultImportCustomizerProvider;
import com.tinkerpop.pipes.transform.ToStringPipe;
import com.tinkerpop.pipes.util.iterators.SingleIterator;
import org.apache.commons.lang.time.StopWatch;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.gremlin.UmlgGremlinReadOnlyKeyIndexableGraph;

import javax.script.ScriptException;
import java.lang.reflect.Field;
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
        Object pipe = executeGroovy(contextId, groovy);
        ToStringPipe toStringPipe = new ToStringPipe();
        toStringPipe.setStarts(new SingleIterator<>(pipe));
        StringBuilder result = new StringBuilder();
        while (toStringPipe.hasNext()) {
            result.append(toStringPipe.next());
            result.append("\n");
        }
        stopWatch.stop();
        result.append("Time to execute query = ");
        result.append(stopWatch.toString());
        return result.toString();
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
        Graph graph = new UmlgGremlinReadOnlyKeyIndexableGraph(UMLG.get());
        GremlinExecutorBaseClass.load(graph);
        this.scriptEngine.put("g", graph);
        Object result;
        try {
//            if (this.isNeo4j) {
//                StringBuilder groovyToIntercept = new StringBuilder();
//                StringBuilder groovyDef = new StringBuilder();
//                this.seperateOutDefFromGroovy(groovy, groovyToIntercept, groovyDef);
//                groovy = "useInterceptor( GremlinGroovyPipeline, GremlinGroovyPipelineInterceptor) {" + groovyToIntercept.toString() + "}";
//                groovy = groovyDef.toString() + groovy;
//            }
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
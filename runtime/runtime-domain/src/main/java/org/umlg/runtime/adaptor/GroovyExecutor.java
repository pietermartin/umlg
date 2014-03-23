package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.gremlin.groovy.jsr223.DefaultImportCustomizerProvider;
import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;
import com.tinkerpop.pipes.transform.ToStringPipe;
import com.tinkerpop.pipes.util.iterators.SingleIterator;
import org.apache.commons.lang.time.StopWatch;
import org.umlg.runtime.gremlin.UmlgGremlinReadOnlyKeyIndexableGraph;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Date: 2013/06/09
 * Time: 8:34 PM
 */
public class GroovyExecutor {

    public static GroovyExecutor INSTANCE = new GroovyExecutor();
    private ScriptEngine scriptEngine;

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
        DefaultImportCustomizerProvider.initializeStatically(getUmlgImports(), getUmlgStaticImports());
        this.scriptEngine = new GremlinGroovyScriptEngine();
    }

    public void refresh() {
        this.scriptEngine = new GremlinGroovyScriptEngine();
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
        toStringPipe.setStarts(new SingleIterator<Object>(pipe));
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

    public Object executeGroovy(Object contextId, String groovy) {
        groovy = groovy.replaceAll("::", "____");
        if (contextId != null) {
            if (!(contextId instanceof Long)) {
                groovy = groovy.replace("self", "g.v(\"" + contextId.toString() + "\")");
            } else {
                groovy = groovy.replace("self", "g.v(" + contextId + ")");
            }
        }
        Graph graph = new UmlgGremlinReadOnlyKeyIndexableGraph(UMLG.get());
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
}
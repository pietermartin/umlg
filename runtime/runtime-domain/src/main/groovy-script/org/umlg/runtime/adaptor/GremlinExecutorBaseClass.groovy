package org.umlg.runtime.adaptor

import com.tinkerpop.blueprints.Graph
import com.tinkerpop.gremlin.groovy.Gremlin

/**
 * Date: 2013/06/23
 * Time: 10:01 PM
 */
abstract class GremlinExecutorBaseClass extends Script {

    public static void load(Graph graph) {
        Gremlin.load();
//        Class.forName("org.umlg.gremlin.groovy.UmlgGremlinGroovyGraphPropertyNames").definePropertyNames();
        Class.forName("org.umlg.runtime.gremlin.UmlgGremlinAddon").defineUmlgSteps(graph);
    }

    def useInterceptor= { Class theClass, Class theInterceptor, Closure theCode->
        def proxy= ProxyMetaClass.getInstance( theClass )
        def interceptor= theInterceptor.newInstance()
        proxy.interceptor= interceptor
        proxy.use( theCode )
    }

}

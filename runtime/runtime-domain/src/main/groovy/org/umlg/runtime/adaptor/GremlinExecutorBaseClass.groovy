package org.umlg.runtime.adaptor

import com.tinkerpop.gremlin.groovy.GremlinLoader
import com.tinkerpop.gremlin.structure.Graph

/**
 * Date: 2013/06/23
 * Time: 10:01 PM
 */
abstract class GremlinExecutorBaseClass extends Script {

    public static void load(Graph graph) {
        GremlinLoader.load();
//        Class.forName("org.umlg.gremlin.groovy.UmlgGremlinGroovyGraphPropertyNames").definePropertyNames();
//        Class.forName("org.umlg.runtime.gremlin.UmlgGremlinAddon").defineUmlgSteps(graph);
    }

    def useInterceptor= { Class theClass, Class theInterceptor, Closure theCode->
        def proxy= ProxyMetaClass.getInstance( theClass )
        def interceptor= theInterceptor.newInstance()
        proxy.interceptor= interceptor
        proxy.use( theCode )
    }

}

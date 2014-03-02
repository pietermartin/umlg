package org.umlg.gremlin.groovy

import com.tinkerpop.gremlin.groovy.Gremlin
import com.tinkerpop.pipes.Pipe

class UmlgGremlinGroovyGraphPropertyNames {

    public static void definePropertyNames() {
        <#list properties as x>
    Gremlin.addStep("${x.gremlinName}");
        Pipe.metaClass.${x.gremlinName} = {
            return _().property("${x.qualifiedName}");
        }
        </#list>
    }

}

package org.umlg.runtime.adaptor

import com.tinkerpop.gremlin.groovy.Gremlin
import org.umlg.runtime.util.UmlgProperties

/**
 * Date: 2013/06/23
 * Time: 10:01 PM
 */
abstract class GremlinExecutorBaseClass extends Script {

    static {
        Gremlin.load();
//        UmlgProperties.INSTANCE.get
        Class.forName("org.umlg.gremlin.TinkerGraphPropertyNames").definePropertyNames();
    }

}

package org.umlg.runtime.adaptor

import com.tinkerpop.blueprints.Edge
import com.tinkerpop.gremlin.groovy.Gremlin
import com.tinkerpop.pipes.Pipe

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 2013/06/23
 * Time: 10:01 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class GremlinExecutorBaseClass extends Script {

    static {
        Gremlin.load();
//        v.outE.has('weight', T.lt, 1.0f).inV
    }

    GremlinExecutorBaseClass() {
        Gremlin.defineStep("hasG", [Pipe, Edge],
                {final String... params -> _().map}
        );
    }

}

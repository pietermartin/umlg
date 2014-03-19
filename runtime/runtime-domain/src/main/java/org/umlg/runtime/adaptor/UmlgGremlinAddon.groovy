package org.umlg.runtime.adaptor

import com.tinkerpop.blueprints.Edge
import com.tinkerpop.gremlin.groovy.Gremlin
import com.tinkerpop.pipes.Pipe

/**
 * Created by pieter on 2014/03/19.
 */
class UmlgGremlinAddon {

    public static void defineUmlgSteps() {
        Gremlin.defineStep("hasAC", [Pipe, Edge], {final String... params -> _()});
    }
}
#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.gremlin

import com.tinkerpop.gremlin.groovy.Gremlin
import com.tinkerpop.pipes.Pipe

/**
 * Created by pieter on 2014/02/13.
 */


class TinkerGraphPropertyNames {

    public static void definePropertyNames() {

        Gremlin.addStep("tinkergraph____org____umlg____tinkergraph____Person____name");
        Pipe.metaClass.tinkergraph____org____umlg____tinkergraph____Person____name = {
            return _().property("tinkergraph::org::umlg::tinkergraph::Person::name");
        }

    }
}


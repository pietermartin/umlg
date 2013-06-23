package org.umlg.runtime.adaptor

import com.tinkerpop.gremlin.groovy.Gremlin

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
    }

}

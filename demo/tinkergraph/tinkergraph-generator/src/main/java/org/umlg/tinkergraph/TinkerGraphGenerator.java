package org.umlg.tinkergraph;

import org.umlg.generation.JavaGenerator;
import org.umlg.restlet.generation.RestletVisitors;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Date: 2014/01/13
 * Time: 9:19 PM
 */
public class TinkerGraphGenerator {

    public static void main(String[] args) throws URISyntaxException {
        if (args.length == 0) {
            args = new String[]{"."};
        }
        JavaGenerator javaGenerator = new JavaGenerator();
        javaGenerator.generate(
                new File(args[0] + "/demo/tinkergraph/tinkergraph-application/tinkergraph-entities/src/main/model/tinkergraph.uml"),
                new File(args[0] + "/demo/tinkergraph/tinkergraph-application/tinkergraph-entities"),
                new File(args[0] + "/demo/tinkergraph/tinkergraph-application/tinkergraph-restlet"),
                RestletVisitors.getDefaultJavaVisitors());

        ///demo/tinkergraph
    }

}

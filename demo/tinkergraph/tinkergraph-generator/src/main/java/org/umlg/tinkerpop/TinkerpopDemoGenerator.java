package org.umlg.tinkerpop;

import org.umlg.generation.JavaGenerator;
import org.umlg.restlet.generation.RestletVisitors;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Date: 2014/01/13
 * Time: 9:19 PM
 */
public class TinkerpopDemoGenerator {

    public static void main(String[] args) throws URISyntaxException {
        if (args.length == 0) {
            args = new String[]{"."};
        }
        JavaGenerator javaGenerator = new JavaGenerator();
        javaGenerator.generate(
                new File(args[0] + "/demo/tinkergraph/tinkergraph-application/src/main/model/tinkergraph.uml"),
                new File(args[0] + "/demo/tinkergraph/tinkergraph-application"), RestletVisitors.getDefaultJavaVisitors());

        ///demo/tinkergraph
    }

}

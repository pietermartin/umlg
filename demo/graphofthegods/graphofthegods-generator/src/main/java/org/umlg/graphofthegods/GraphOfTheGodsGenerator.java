package org.umlg.graphofthegods;

import org.umlg.generation.JavaGenerator;
import org.umlg.restlet.generation.RestletVisitors;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Date: 2014/01/13
 * Time: 9:19 PM
 */
public class GraphOfTheGodsGenerator {

    public static void main(String[] args) throws URISyntaxException {
        if (args.length == 0) {
            args = new String[]{"."};
        }
        JavaGenerator javaGenerator = new JavaGenerator();
        javaGenerator.generate(
                new File(args[0] + "/demo/graphofthegods/graphofthegods-application/graphofthegods-entities/src/main/model/graphofthegods.uml"),
                new File(args[0] + "/demo/graphofthegods/graphofthegods-application/graphofthegods-entities"),
                new File(args[0] + "/demo/graphofthegods/graphofthegods-application/graphofthegods-restlet"),
                RestletVisitors.getDefaultJavaVisitors());

        ///demo/graphofthegods
    }

}

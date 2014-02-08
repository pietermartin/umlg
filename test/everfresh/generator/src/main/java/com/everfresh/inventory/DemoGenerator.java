package com.everfresh.inventory;

import org.umlg.generation.JavaGenerator;
import org.umlg.restlet.generation.RestletVisitors;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Date: 2014/01/13
 * Time: 9:19 PM
 */
public class DemoGenerator {

    public static void main(String[] args) throws URISyntaxException {
        if (args.length == 0) {
            args = new String[]{"."};
        }
        JavaGenerator javaGenerator = new JavaGenerator();
        javaGenerator.generate(
                new File(args[0] + "/test/everfresh/application/src/main/model/everfresh.uml"),
                new File(args[0] + "/test/everfresh/application"), RestletVisitors.getDefaultJavaVisitors());

    }

}

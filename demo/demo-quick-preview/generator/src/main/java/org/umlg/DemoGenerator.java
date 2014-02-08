package org.umlg;

import org.umlg.generation.JavaGenerator;
import org.umlg.javageneration.DefaultVisitors;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Date: 2014/01/13
 * Time: 9:19 PM
 */
public class DemoGenerator {

    public static void main(String[] args) throws URISyntaxException {
        JavaGenerator javaGenerator = new JavaGenerator();
        javaGenerator.generate(
                new File("demo/demo-quick-preview/application/src/main/model/umlg-demo1.uml"),
                new File("demo/demo-quick-preview/application"), DefaultVisitors.getDefaultJavaVisitors());

    }

}

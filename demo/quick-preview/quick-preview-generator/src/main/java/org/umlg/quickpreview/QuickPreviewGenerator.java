package org.umlg.quickpreview;

import org.umlg.generation.JavaGenerator;
import org.umlg.restlet.generation.RestletVisitors;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Date: 2014/01/13
 * Time: 9:19 PM
 */
public class QuickPreviewGenerator {

    public static void main(String[] args) throws URISyntaxException {
        if (args.length == 0) {
            args = new String[]{"."};
        }
        JavaGenerator javaGenerator = new JavaGenerator();
        javaGenerator.generate(
                new File(args[0] + "/demo/quick-preview/quick-preview-application/quick-preview-entities/src/main/model/quick-preview.uml"),
                new File(args[0] + "/demo/quick-preview/quick-preview-application/quick-preview-entities"),
                new File(args[0] + "/demo/quick-preview/quick-preview-application/quick-preview-restlet"),
                RestletVisitors.getDefaultJavaVisitors());

        ///demo/quick-preview
    }

}

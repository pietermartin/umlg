#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

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
                new File(args[0] + "/application/src/main/model/umlg-demo1.uml"),
                new File(args[0] + "/application"), RestletVisitors.getDefaultJavaVisitors());

    }

}

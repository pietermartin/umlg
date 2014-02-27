#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * Date: 2014/02/19
 * Time: 8:34 PM
 */
public class JettyDemo {

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("umlg.jetty.properties"));
        Server server = new Server(new InetSocketAddress(prop.getProperty("webserver.ip"), 8111));

        ContextHandlerCollection contexts = new ContextHandlerCollection();

        WebAppContext graph = new WebAppContext();
        graph.setContextPath("/demo");
        graph.setWar("./${rootArtifactId}-application/${rootArtifactId}-war/target/${rootArtifactId}-war");

        contexts.setHandlers(new Handler[] {  graph });
        server.setHandler(contexts);

        server.start();
        server.join();
    }
}

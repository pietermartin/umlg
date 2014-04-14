package org.umlg.demo;

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
        prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("umlg.env.properties"));
        Server server = new Server(new InetSocketAddress(prop.getProperty("webserver.ip"), Integer.valueOf(prop.getProperty("webserver.port"))));

        ContextHandlerCollection contexts = new ContextHandlerCollection();

        WebAppContext demo = new WebAppContext();
        demo.setContextPath("/demo");
        demo.setDescriptor("./demo/demo-application/src/main/webapp/WEB-INF/web.xml");
        demo.setResourceBase("./demo/demo-application/src/main/webapp");
        demo.setParentLoaderPriority(true);

        WebAppContext graphOfTheGods = new WebAppContext();
        graphOfTheGods.setContextPath("/graphofthegods");
        graphOfTheGods.setWar("./demo/graphofthegods/graphofthegods-application/graphofthegods-war/target/graphofthegods-war");

        WebAppContext tinkerGraph = new WebAppContext();
        tinkerGraph.setContextPath("/tinkergraph");
        tinkerGraph.setWar("./demo/tinkergraph/tinkergraph-application/tinkergraph-war/target/tinkergraph-war");

        contexts.setHandlers(new Handler[] {  demo, graphOfTheGods, tinkerGraph });
        server.setHandler(contexts);

        server.start();
        server.join();
    }
}

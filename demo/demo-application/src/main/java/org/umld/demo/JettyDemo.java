package org.umld.demo;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.InetSocketAddress;

/**
 * Date: 2014/02/19
 * Time: 8:34 PM
 */
public class JettyDemo {

    public static void main(String[] args) throws Exception {
        Server server = new Server(new InetSocketAddress("localhost", 8111));

        ContextHandlerCollection contexts = new ContextHandlerCollection();

        WebAppContext demo = new WebAppContext();
        demo.setContextPath("/demo");
        demo.setDescriptor("./demo/demo-application/src/main/webapp/WEB-INF/web.xml");
        demo.setResourceBase("./demo/demo-application/src/main/webapp");
        demo.setParentLoaderPriority(true);

        WebAppContext graphOfTheGods = new WebAppContext();
        graphOfTheGods.setContextPath("/graphofthegods");
        graphOfTheGods.setWar("./demo/graphofthegods/graphofthegods-application/target/graphofthegods-application");

        WebAppContext tinkerGraph = new WebAppContext();
        tinkerGraph.setContextPath("/tinkergraph");
        tinkerGraph.setWar("./demo/tinkergraph/tinkergraph-application/target/tinkergraph-application");

        contexts.setHandlers(new Handler[] {  demo, graphOfTheGods, tinkerGraph });
        server.setHandler(contexts);

        server.start();
        server.join();
    }
}

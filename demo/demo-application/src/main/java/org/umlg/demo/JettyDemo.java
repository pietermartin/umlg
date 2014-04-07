package org.umlg.demo;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.umlg.runtime.util.UmlgProperties;

import java.net.InetSocketAddress;

/**
 * Date: 2014/02/19
 * Time: 8:34 PM
 */
public class JettyDemo {

    public static void main(String[] args) throws Exception {
        Server server = new Server(new InetSocketAddress(UmlgProperties.INSTANCE.getWebserverIp(), UmlgProperties.INSTANCE.getWebserverPort()));

        ContextHandlerCollection contexts = new ContextHandlerCollection();

        WebAppContext demo = new WebAppContext();
        demo.setContextPath("/demo");
        demo.setDescriptor("./demo/demo-application/src/main/webapp/WEB-INF/web.xml");
        demo.setResourceBase("./demo/demo-application/src/main/webapp");
        demo.setParentLoaderPriority(true);

        WebAppContext graphOfTheGods = new WebAppContext();
        graphOfTheGods.setContextPath("/graphofthegods");
//        graphOfTheGods.setWar("./demo/graphofthegods/graphofthegods-application/graphofthegods-war/target/graphofthegods-war");
        graphOfTheGods.setWar("../lib/graphofthegods-war.war");

        WebAppContext tinkerGraph = new WebAppContext();
        tinkerGraph.setContextPath("/tinkergraph");
//        tinkerGraph.setWar("./demo/tinkergraph/tinkergraph-application/tinkergraph-war/target/tinkergraph-war");
        tinkerGraph.setWar("../lib/tinkergraph-war.war");

        contexts.setHandlers(new Handler[] {  demo, graphOfTheGods, tinkerGraph });
        server.setHandler(contexts);

        server.start();
        server.join();
    }
}

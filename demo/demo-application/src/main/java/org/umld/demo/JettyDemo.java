package org.umld.demo;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.restlet.ext.servlet.ServerServlet;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * Date: 2014/02/19
 * Time: 8:34 PM
 */
public class JettyDemo {

    public static void main(String[] args) throws Exception {
        Server server = new Server(new InetSocketAddress("localhost", 8111));

        ContextHandlerCollection contexts = new ContextHandlerCollection();

//        WebAppContext demo = new WebAppContext();
//        demo.setDescriptor("./demo/demo-application/src/main/webapp/WEB-INF/web.xml");
//        demo.setResourceBase(".demo/demo-application/src/main/webapp");
//        demo.setContextPath("/demo");
//        context.setParentLoaderPriority(true);

        WebAppContext graphOfTheGods = new WebAppContext();
        graphOfTheGods.setContextPath("/graphofthegods");
        graphOfTheGods.setWar("./demo/graphofthegods/graphofthegods-application/target/graphofthegods-application");
        graphOfTheGods.setInitParameter("org.restlet.application", "org.umlg.graphofthegods.GraphofthegodsApplication");
        graphOfTheGods.setInitParameter("org.restlet.clients", "HTTP FILE CLAP");

        WebAppContext tinkerGraph = new WebAppContext();
        tinkerGraph.setContextPath("/tinkergraph");
        tinkerGraph.setWar("./demo/tinkergraph/tinkergraph-application/target/tinkergraph-application");
        tinkerGraph.setInitParameter("org.restlet.application", "org.umlg.tinkergraph.TinkergraphApplication");
        tinkerGraph.setInitParameter("org.restlet.clients", "HTTP FILE CLAP");

        contexts.setHandlers(new Handler[] {  graphOfTheGods, tinkerGraph });
        server.setHandler(contexts);

        server.start();
        server.join();
    }
}

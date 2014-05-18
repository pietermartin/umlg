package org.umlg.testrestletminimal.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.restlet.ext.servlet.ServerServlet;
import org.umlg.framework.ModelLoader;
import org.umlg.jetty.websocket.UmlgWebsocketServlet;

import java.net.URL;


/**
 * Date: 2014/04/04
 * Time: 9:22 PM
 */
public class RestletMinimalJetty {

    public static void main(String[] args) throws Exception {
        //Load the uml model
        URL modelFileURL = Thread.currentThread().getContextClassLoader().getResource("test-restlet-minimal.uml");
        if (modelFileURL == null) {
            throw new IllegalStateException(String.format("Model file %s not found. The model's file name must be on the classpath.", "restAndJson.uml"));
        }
        ModelLoader.INSTANCE.loadModel(modelFileURL.toURI());

        Server server = new Server(8111);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/testRestletMinimal");
        server.setHandler(context);

        //Restlet servlet
        ServletHolder restletServletHolder = new ServletHolder(new ServerServlet());
        restletServletHolder.setName("org.umlg.testrestletminimal.TestRestletMinimalApplication");
        restletServletHolder.setInitParameter("org.restlet.application", "org.umlg.testrestletminimal.TestRestletMinimalApplication");
        restletServletHolder.setInitParameter("org.restlet.clients", "HTTP FILE CLAP");
        context.addServlet(restletServletHolder, "/*");

        //Websocket servlet
        ServletHolder websocketServletHolder = new ServletHolder(new UmlgWebsocketServlet());
        websocketServletHolder.setName("Umlg WebSocket Servlet");
        context.addServlet(websocketServletHolder, "/websocket");

        server.start();
        server.join();
    }
}

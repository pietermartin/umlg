package org.umlg.tinkergraph;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.restlet.ext.servlet.ServerServlet;
import org.umlg.framework.ModelLoader;
import org.umlg.jetty.websocket.UmlgWebsocketServlet;

import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Properties;

/**
 * Date: 2014/02/19
 * Time: 8:34 PM
 */
public class TinkerGraphDemo {

    public static void main(String[] args) throws Exception {

        URL modelFileURL = Thread.currentThread().getContextClassLoader().getResource("tinkergraph.uml");
        if (modelFileURL == null) {
            throw new IllegalStateException(String.format("Model file %s not found. The model's file name must be on the classpath.", "tinkergraph.uml"));
        }

        ModelLoader.INSTANCE.loadModel(modelFileURL.toURI());

        Properties prop = new Properties();
        prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("umlg.jetty.properties"));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        Server server = new Server(new InetSocketAddress(prop.getProperty("webserver.ip"), Integer.valueOf(prop.getProperty("webserver.port"))));
        context.setContextPath("/tinkergraph");
        server.setHandler(context);

        //Restlet servlet
        ServletHolder restletServletHolder = new ServletHolder(new ServerServlet());
        restletServletHolder.setName("org.umlg.tinkergraph.TinkergraphApplication");
        restletServletHolder.setInitParameter("org.restlet.application", "org.umlg.tinkergraph.TinkergraphApplication");
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

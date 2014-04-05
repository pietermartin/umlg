package org.umlg.restandjson.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.restlet.ext.servlet.ServerServlet;
import org.umlg.jetty.websocket.UmlgWebsocketServlet;


/**
 * Date: 2014/04/04
 * Time: 9:22 PM
 */
public class RestAndJsonJetty {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8111);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/restAndJson");
        server.setHandler(context);

        //Restlet servlet
        ServletHolder restletServletHolder = new ServletHolder(new ServerServlet());
        restletServletHolder.setName("org.umlg.restandjson.RestAndJsonApplication");
        restletServletHolder.setInitParameter("org.restlet.application", "org.umlg.restandjson.RestAndJsonApplication");
        restletServletHolder.setInitParameter("org.restlet.clients", "HTTP FILE CLAP");
        context.addServlet(restletServletHolder, "/*");

        //Websocket servlet
        ServletHolder websocketServletHolder = new ServletHolder(new UmlgWebsocketServlet());
        websocketServletHolder.setName("Umlg WebSocket Servlet");
        context.addServlet(websocketServletHolder, "/echo");

        server.start();
        server.join();
    }
}

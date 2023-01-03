package org.umlg.jetty.websocket;

/**
 * Date: 2014/04/04
 * Time: 6:50 PM
 */

import org.eclipse.jetty.websocket.server.JettyWebSocketServlet;
import org.eclipse.jetty.websocket.server.JettyWebSocketServletFactory;

import java.util.logging.Logger;

public class UmlgWebsocketServlet extends JettyWebSocketServlet {

    private static Logger logger = Logger.getLogger(UmlgWebsocketServlet.class.getPackage().getName());
    @Override
    public void configure(JettyWebSocketServletFactory factory) {
        logger.fine("UmlgWebsocketServlet start");
//        factory.getPolicy().setIdleTimeout(10000);
        factory.register(UmlgWebsocket.class);
    }

}

package org.umlg.jetty.websocket;

/**
 * Date: 2014/04/04
 * Time: 6:50 PM
 */

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.util.logging.Logger;

public class UmlgWebsocketServlet extends WebSocketServlet {

    private static Logger logger = Logger.getLogger(UmlgWebsocketServlet.class.getPackage().getName());
    @Override
    public void configure(WebSocketServletFactory factory) {
        logger.fine("UmlgWebsocketServlet start");
        factory.getPolicy().setIdleTimeout(10000);
        factory.register(UmlgWebsocket.class);
    }

}

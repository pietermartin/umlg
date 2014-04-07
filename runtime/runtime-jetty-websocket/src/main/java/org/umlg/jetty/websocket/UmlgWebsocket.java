package org.umlg.jetty.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.io.IOException;

/**
 * Date: 2014/04/04
 * Time: 6:32 PM
 */
public class UmlgWebsocket implements WebSocketListener {

    private Session outbound;

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        UmlgWebsocketSessionManager.INSTANCE.removeSession(this.outbound);
        this.outbound = null;
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.outbound = session;
        UmlgWebsocketSessionManager.INSTANCE.addSession(
                session,
                session.getRemoteAddress().toString());
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }

    @Override
    public void onWebSocketText(String message) {
        if ((outbound != null) && (outbound.isOpen())) {
            System.out.printf("Echoing back message [%s]%n", message);
            outbound.getRemote().sendString(message, null);
        }
    }

}

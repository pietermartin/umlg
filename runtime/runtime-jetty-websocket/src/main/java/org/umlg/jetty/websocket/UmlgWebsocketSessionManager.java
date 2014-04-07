package org.umlg.jetty.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Date: 2014/04/05
 * Time: 8:20 PM
 */
public class UmlgWebsocketSessionManager {

    public static final UmlgWebsocketSessionManager INSTANCE = new UmlgWebsocketSessionManager();
    private static Logger logger = Logger.getLogger(UmlgWebsocketSessionManager.class.getPackage().getName());
    private final Map<String, Session> sessionMap = new HashMap<String, Session>();
    private final ScheduledExecutorService scheduledExecutorService;

    private UmlgWebsocketSessionManager() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    public void run()  {
                        for (Session session : UmlgWebsocketSessionManager.this.sessionMap.values()) {
                            try {
                                session.getRemote().sendString(String.valueOf(UmlgWebsocketSessionManager.this.sessionMap.size()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                },
                1,
                5,
                TimeUnit.SECONDS
        );
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                UmlgWebsocketSessionManager.this.scheduledExecutorService.shutdownNow();
            }
        });
    }

    public void addSession(Session session, String remoteAddress) {
        this.sessionMap.put(remoteAddress, session);
        try {
            session.getRemote().sendString(String.valueOf(this.sessionMap.size()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info(String.format("UmlgWebsocketSessionManager.addSession, number of websocket hosts are %d", new Integer[]{this.sessionMap.size()}));
    }

    public void removeSession(Session session) {
        this.sessionMap.remove(session.getRemoteAddress().toString());
        logger.info(String.format("UmlgWebsocketSessionManager.removeSession, number of websocket hosts are %d", new Integer[]{this.sessionMap.size()}));
    }
}

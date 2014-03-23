package org.umlg.runtime.adaptor;

import java.util.logging.Logger;

//import org.neo4j.server.WrappingNeoServerBootstrapper;

/**
 * Date: 2012/12/31
 * Time: 12:50 PM
 */
public class Neo4jAdminApp {

    private static final Logger logger = Logger.getLogger(Neo4jAdminApp.class.getPackage().getName());

    public static void startAdminApplication() {
//        ExecutorService es = Executors.newFixedThreadPool(1, new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread(r, "neo4j-startup-thread");
//            }
//        });
//        Future f = es.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //Start the neo4j server
//                    InternalAbstractGraphDatabase graphdb = (InternalAbstractGraphDatabase) ((UmlgNeo4jGraph) GraphDb.get()).getRawGraph();
//                    WrappingNeoServerBootstrapper srv;
//                    srv = new WrappingNeoServerBootstrapper(graphdb);
//                    logger.info("starting neo4j server");
//                    srv.start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        es.shutdown();
    }
}

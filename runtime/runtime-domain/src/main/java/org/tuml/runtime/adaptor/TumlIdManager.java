package org.tuml.runtime.adaptor;

import org.tuml.runtime.domain.TumlMetaNode;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Date: 2013/03/16
 * Time: 9:10 PM
 */
public class TumlIdManager {

    public static TumlIdManager INSTANCE = new TumlIdManager();
    private ConcurrentMap<TumlMetaNode, Long> vertexIdMap = new ConcurrentHashMap<TumlMetaNode, Long>();

    private TumlIdManager() {
    }

    //TODO do this properly, i.e. use the map to synchronize
    public synchronized Long getNext(TumlMetaNode tumlMetaNode) {
        //Lazily load the current high id
        Long result;
        if (this.vertexIdMap.get(tumlMetaNode) == null) {
            result = tumlMetaNode.getIdHigh();
        } else {
            result = this.vertexIdMap.get(tumlMetaNode);
        }
        this.vertexIdMap.put(tumlMetaNode, result + 1);
        return result + 1;
    }

    //TODO work out the db specific locking
    public synchronized void persistHighId(TumlMetaNode tumlMetaNode) {
        if (GraphDb.getDb().isTransactionActive()) {
            throw new IllegalStateException("TumlIdManager.persistHighId may not be called in an existing transaction!");
        }
        GraphDb.getDb().acquireWriteLock(tumlMetaNode.getVertex());
        tumlMetaNode.getVertex().setProperty("highId", this.vertexIdMap.get(tumlMetaNode));
        GraphDb.getDb().commit();
    }

}

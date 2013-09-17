package org.umlg.runtime.adaptor;

/**
 * Date: 2013/03/16
 * Time: 9:10 PM
 */
public class TumlIdManager {

//    public static TumlIdManager INSTANCE = new TumlIdManager();
//    private ConcurrentMap<TumlMetaNode, Long> vertexIdMap = new ConcurrentHashMap<TumlMetaNode, Long>();
//
//    private TumlIdManager() {
//    }
//
//    //TODO do this properly, i.e. use the map to synchronize
//    public synchronized Long getNext(TumlMetaNode tumlMetaNode) {
//        //Lazily load the current high id
//        Long result;
//        if (this.vertexIdMap.get(tumlMetaNode) == null) {
//            result = tumlMetaNode.getIdHigh();
//            if (result == null) {
//                result = 0L;
//            }
//        } else {
//            result = this.vertexIdMap.get(tumlMetaNode);
//        }
//        this.vertexIdMap.put(tumlMetaNode, result + 1);
//        return result + 1;
//    }
//
//    public void persistHighId(TumlMetaNode tumlMetaNode) {
//        Long highId = this.vertexIdMap.get(tumlMetaNode);
//        //This null check is because every commit calls this.
//        //It is possible for MetaNodes to be in the threadvar even tho no entities have been created
//        if (highId != null) {
//            tumlMetaNode.getVertex().setProperty("highId", highId);
//        }
//    }
//
//    public void clear() {
//        this.vertexIdMap.clear();
//    }

}

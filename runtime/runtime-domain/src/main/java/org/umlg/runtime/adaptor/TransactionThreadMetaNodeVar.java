package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.TumlMetaNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionThreadMetaNodeVar {

    private TransactionThreadMetaNodeVar() {
    }

    private static ThreadLocal<Map<Object, TumlMetaNode>> transactionEntityVar = new ThreadLocal<Map<Object, TumlMetaNode>>() {
        @Override
        protected Map<Object, TumlMetaNode> initialValue() {
            return new HashMap<Object, TumlMetaNode>();
        }
    };

    public static boolean hasNoAuditEntry(String clazzAndId) {
        Map<Object, TumlMetaNode> newVertexMap = transactionEntityVar.get();
        TumlMetaNode newVertex = newVertexMap.get(clazzAndId);
        return newVertex == null;
    }

    public static void remove() {
        transactionEntityVar.remove();
    }

    public static void setNewEntity(TumlMetaNode node) {
        transactionEntityVar.get().put(node.getId(), node);
    }

    public static List<TumlMetaNode> get() {
        return new ArrayList<TumlMetaNode>(transactionEntityVar.get().values());
    }

    public static TumlMetaNode remove(TumlMetaNode node) {
        return transactionEntityVar.get().remove(node.getId());
    }


}

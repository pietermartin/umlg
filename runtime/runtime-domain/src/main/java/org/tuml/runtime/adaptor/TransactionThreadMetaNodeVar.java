package org.tuml.runtime.adaptor;

import org.tuml.runtime.domain.TumlMetaNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionThreadMetaNodeVar {

    private TransactionThreadMetaNodeVar() {
    }

    private static ThreadLocal<Map<Long, TumlMetaNode>> transactionEntityVar = new ThreadLocal<Map<Long, TumlMetaNode>>() {
        @Override
        protected Map<Long, TumlMetaNode> initialValue() {
            return new HashMap<Long, TumlMetaNode>();
        }
    };

    public static boolean hasNoAuditEntry(String clazzAndId) {
        Map<Long, TumlMetaNode> newVertexMap = transactionEntityVar.get();
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

package org.tuml.runtime.adaptor;

import org.tuml.runtime.domain.TumlMetaNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionThreadMetaNodeVar {

    private TransactionThreadMetaNodeVar() {
    }

    private static ThreadLocal<Map<String, TumlMetaNode>> transactionEntityVar = new ThreadLocal<Map<String, TumlMetaNode>>() {
        @Override
        protected Map<String, TumlMetaNode> initialValue() {
            return new HashMap<String, TumlMetaNode>();
        }
    };

    public static boolean hasNoAuditEntry(String clazzAndId) {
        Map<String, TumlMetaNode> newVertexMap = transactionEntityVar.get();
        TumlMetaNode newVertex = newVertexMap.get(clazzAndId);
        return newVertex == null;
    }

    public static void remove() {
        transactionEntityVar.remove();
    }

    public static void setNewEntity(TumlMetaNode node) {
        transactionEntityVar.get().put(node.getVertex().getId().toString(), node);
    }

    public static List<TumlMetaNode> get() {
        return new ArrayList<TumlMetaNode>(transactionEntityVar.get().values());
    }

    public static TumlMetaNode remove(String key) {
        return transactionEntityVar.get().remove(key);
    }


}

package org.tuml.runtime.adaptor;

import org.tuml.runtime.domain.TumlNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionThreadEntityVar {

    private TransactionThreadEntityVar() {
    }

    private static ThreadLocal<Map<String, TumlNode>> transactionEntityVar = new ThreadLocal<Map<String, TumlNode>>() {
        @Override
        protected Map<String, TumlNode> initialValue() {
            return new HashMap<String, TumlNode>();
        }
    };

    public static boolean hasNoAuditEntry(String clazzAndId) {
        Map<String, TumlNode> newVertexMap = transactionEntityVar.get();
        TumlNode newVertex = newVertexMap.get(clazzAndId);
        return newVertex == null;
    }

    public static void remove() {
        transactionEntityVar.remove();
    }

    public static void setNewEntity(TumlNode node) {
        transactionEntityVar.get().put(node.getVertex().getId().toString(), node);
    }

    public static List<TumlNode> get() {
        return new ArrayList<TumlNode>(transactionEntityVar.get().values());
    }

    public static TumlNode remove(String key) {
        return transactionEntityVar.get().remove(key);
    }


}

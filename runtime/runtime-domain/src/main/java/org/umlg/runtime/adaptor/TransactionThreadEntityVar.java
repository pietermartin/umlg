package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.TumlNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionThreadEntityVar {

    private TransactionThreadEntityVar() {
    }

    private static ThreadLocal<Map<Object, TumlNode>> transactionEntityVar = new ThreadLocal<Map<Object, TumlNode>>() {
        @Override
        protected Map<Object, TumlNode> initialValue() {
            return new HashMap<Object, TumlNode>();
        }
    };

    public static boolean hasNoAuditEntry(String clazzAndId) {
        Map<Object, TumlNode> newVertexMap = transactionEntityVar.get();
        TumlNode newVertex = newVertexMap.get(clazzAndId);
        return newVertex == null;
    }

    public static void remove() {
        transactionEntityVar.remove();
    }

    public static void setNewEntity(TumlNode node) {
        transactionEntityVar.get().put(node.getId(), node);
    }

    public static List<TumlNode> get() {
        return new ArrayList<TumlNode>(transactionEntityVar.get().values());
    }

    public static TumlNode remove(TumlNode node) {
        return transactionEntityVar.get().remove(node.getId());
    }


}

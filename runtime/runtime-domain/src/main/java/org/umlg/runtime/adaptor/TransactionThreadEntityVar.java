package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.UmlgNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionThreadEntityVar {

    private TransactionThreadEntityVar() {
    }

    private static ThreadLocal<Map<Object, UmlgNode>> transactionEntityVar = new ThreadLocal<Map<Object, UmlgNode>>() {
        @Override
        protected Map<Object, UmlgNode> initialValue() {
            return new HashMap<Object, UmlgNode>();
        }
    };

    public static boolean hasNoAuditEntry(String clazzAndId) {
        Map<Object, UmlgNode> newVertexMap = transactionEntityVar.get();
        UmlgNode newVertex = newVertexMap.get(clazzAndId);
        return newVertex == null;
    }

    public static void remove() {
        transactionEntityVar.remove();
    }

    public static void setNewEntity(UmlgNode node) {
        transactionEntityVar.get().put(node.getId(), node);
    }

    public static List<UmlgNode> get() {
        return new ArrayList<UmlgNode>(transactionEntityVar.get().values());
    }

    public static UmlgNode remove(UmlgNode node) {
        return transactionEntityVar.get().remove(node.getId());
    }


}

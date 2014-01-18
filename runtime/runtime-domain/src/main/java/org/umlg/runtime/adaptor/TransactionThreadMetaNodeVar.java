package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.UmlgMetaNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionThreadMetaNodeVar {

    private TransactionThreadMetaNodeVar() {
    }

    private static ThreadLocal<Map<Object, UmlgMetaNode>> transactionEntityVar = new ThreadLocal<Map<Object, UmlgMetaNode>>() {
        @Override
        protected Map<Object, UmlgMetaNode> initialValue() {
            return new HashMap<Object, UmlgMetaNode>();
        }
    };

    public static boolean hasNoAuditEntry(String clazzAndId) {
        Map<Object, UmlgMetaNode> newVertexMap = transactionEntityVar.get();
        UmlgMetaNode newVertex = newVertexMap.get(clazzAndId);
        return newVertex == null;
    }

    public static void remove() {
        transactionEntityVar.remove();
    }

    public static void setNewEntity(UmlgMetaNode node) {
        transactionEntityVar.get().put(node.getId(), node);
    }

    public static List<UmlgMetaNode> get() {
        return new ArrayList<UmlgMetaNode>(transactionEntityVar.get().values());
    }

    public static UmlgMetaNode remove(UmlgMetaNode node) {
        return transactionEntityVar.get().remove(node.getId());
    }


}

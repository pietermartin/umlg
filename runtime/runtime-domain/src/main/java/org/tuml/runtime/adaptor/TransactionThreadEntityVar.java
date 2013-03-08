package org.tuml.runtime.adaptor;

import org.tuml.runtime.domain.CompositionNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionThreadEntityVar {

    private TransactionThreadEntityVar() {
    }

    private static ThreadLocal<Map<String, CompositionNode>> transactionEntityVar = new ThreadLocal<Map<String, CompositionNode>>() {
        @Override
        protected Map<String, CompositionNode> initialValue() {
            return new HashMap<String, CompositionNode>();
        }
    };

    public static boolean hasNoAuditEntry(String clazzAndId) {
        Map<String, CompositionNode> newVertexMap = transactionEntityVar.get();
        CompositionNode newVertex = newVertexMap.get(clazzAndId);
        return newVertex == null;
    }

    public static void remove() {
        transactionEntityVar.remove();
    }

    public static void setNewEntity(CompositionNode node) {
        transactionEntityVar.get().put(node.getVertex().getId().toString(), node);
    }

    public static List<CompositionNode> get() {
        return new ArrayList<CompositionNode>(transactionEntityVar.get().values());
    }

    public static CompositionNode remove(String key) {
        return transactionEntityVar.get().remove(key);
    }


}

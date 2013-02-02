package org.tuml.runtime.adaptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2013/02/01
 * Time: 8:21 PM
 */
public class TransactionCache {

    private Map<String, TransactionIdentifier> transactionIdentifierMap = new HashMap<String, TransactionIdentifier>();

    public final static TransactionCache INSTANCE = new TransactionCache();

    private TransactionCache() {

    }

    public void put(TransactionIdentifier transactionIdentifier) {
        this.transactionIdentifierMap.put(transactionIdentifier.toString(), transactionIdentifier);
    }

    public TransactionIdentifier get(String uid) {
        return this.transactionIdentifierMap.get(uid);
    }
}

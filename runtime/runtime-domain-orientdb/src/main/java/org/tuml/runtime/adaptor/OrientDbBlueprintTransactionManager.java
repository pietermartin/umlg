package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2013/01/09
 * Time: 9:36 PM
 */
public class OrientDbBlueprintTransactionManager implements BlueprintTransactionManager {

    public static final OrientDbBlueprintTransactionManager INSTANCE = new OrientDbBlueprintTransactionManager();

    private Map<Integer, OrientGraph> orientDbMap = new HashMap<Integer, OrientGraph>();

    private static ThreadLocal<OrientGraph> currentGraph = new ThreadLocal<OrientGraph>() {
    };

    private OrientDbBlueprintTransactionManager() {
    }

    private Integer getNextTransactionNumber() {
        return this.orientDbMap.size();
    }

    public Integer suspend() {
        OrientGraph orientGraph = currentGraph.get();
        currentGraph.remove();
        Integer nextTransactionNumber = getNextTransactionNumber();
        this.orientDbMap.put(nextTransactionNumber, orientGraph);
        return nextTransactionNumber;
    }

    public TransactionalGraph resume(Integer transactionNumber) {
        OrientGraph orientGraph = this.orientDbMap.get(transactionNumber);
        this.orientDbMap.remove(transactionNumber);
        currentGraph.set(orientGraph);
        return orientGraph;
    }


}

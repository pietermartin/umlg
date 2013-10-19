package org.umlg.runtime.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.restlet.util.UmlgURLDecoder;

/**
 * Date: 2013/10/19
 * Time: 10:47 AM
 */
public class QueryExecuteServerResourceImpl extends BaseOclExecutionServerResourceImpl {

    /**
     * default constructor for QueryExecuteServerResourceImpl
     */
    public QueryExecuteServerResourceImpl() {
        setNegotiated(false);
    }

    @Override
    public Representation get() throws ResourceException {
        try {
            String type = getQuery().getFirstValue("type");
            String query = getQuery().getFirstValue("query");
            Object context = UmlgURLDecoder.decode((String) getRequestAttributes().get("contextId"));
            if (context != null) {
                Object contextId = (String) context;
                return execute(query, contextId, type);
            } else {
                return execute(query);
            }
        } finally {
            GraphDb.getDb().rollback();
        }
    }

}

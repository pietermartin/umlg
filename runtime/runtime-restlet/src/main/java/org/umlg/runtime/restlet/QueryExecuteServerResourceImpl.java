package org.umlg.runtime.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.umlg.runtime.adaptor.UMLG;
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
            String contextClassifierQualifiedName = getQuery().getFirstValue("contextClassifierQualifiedName");
            String contextId = (String) getRequestAttributes().get("contextId");
            if (contextId != null) {
                contextId = UmlgURLDecoder.decode(contextId);
                return execute(query, contextId, type);
            } else {
                return executeStatic(query, contextClassifierQualifiedName, type);
            }
        } finally {
            UMLG.getDb().rollback();
        }
    }

}

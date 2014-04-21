package org.umlg.runtime.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgExceptionUtilFactory;
import org.umlg.runtime.restlet.util.UmlgURLDecoder;

/**
 * Date: 2013/10/19
 * Time: 10:47 AM
 */
public class QueryExecuteServerResourceImpl extends BaseQueryExecutionServerResourceImpl {

    /**
     * default constructor for QueryExecuteServerResourceImpl
     */
    public QueryExecuteServerResourceImpl() {
        setNegotiated(false);
    }

    @Override
    public Representation get() throws ResourceException {
        String type = getQuery().getFirstValue("type");
        String query = getQuery().getFirstValue("query");
        String contextClassifierQualifiedName = getQuery().getFirstValue("contextClassifierQualifiedName");
        String contextId = (String) getRequestAttributes().get("contextId");
        if (type.equalsIgnoreCase("groovy")) {
            try {
                Representation result;
                if (contextId != null) {
                    contextId = UmlgURLDecoder.decode(contextId);
                    result = execute(query, contextId, type);
                } else {
                    result = executeStatic(query, contextClassifierQualifiedName, type);
                }
                UMLG.get().rollback();
                return result;
            } catch (Exception e) {
                UMLG.get().rollback();
                throw UmlgExceptionUtilFactory.getTumlExceptionUtil().handle(e);
            }
        } else {
            try {
                Representation result;
                if (contextId != null) {
                    contextId = UmlgURLDecoder.decode(contextId);
                    result = execute(query, contextId, type);
                } else {
                    result = executeStatic(query, contextClassifierQualifiedName, type);
                }
                return result;
            } finally {
                UMLG.get().rollback();
            }
        }
    }

}

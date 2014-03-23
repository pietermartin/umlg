package org.umlg.runtime.restlet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.routing.Filter;
import org.umlg.runtime.adaptor.UMLG;

/**
 * Date: 2013/03/14
 * Time: 7:05 PM
 */
public class UmlgRestletFilter extends Filter {

    public UmlgRestletFilter(Context context) {
        super(context);
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        return CONTINUE;
    }

    @Override
    protected void afterHandle(Request request, Response response) {
        if (response.getStatus() != Status.REDIRECTION_NOT_MODIFIED) {
            if (UMLG.get().isTransactionActive()) {
                throw new IllegalStateException("Transaction is still active!");
            }
            UMLG.get().afterThreadContext();
        }
    }

}

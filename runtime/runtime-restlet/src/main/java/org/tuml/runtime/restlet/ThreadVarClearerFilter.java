package org.tuml.runtime.restlet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Filter;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.adaptor.TransactionThreadVar;

/**
 * Date: 2013/01/04
 * Time: 8:29 PM
 */
public class ThreadVarClearerFilter extends Filter {

    public ThreadVarClearerFilter (Context context) {
        super(context);
    }

    @Override
    protected void afterHandle(Request request, Response response) {
        TransactionThreadEntityVar.remove();
        TransactionThreadVar.remove();
    }

}

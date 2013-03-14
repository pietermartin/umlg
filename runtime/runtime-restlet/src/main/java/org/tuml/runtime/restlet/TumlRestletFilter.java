package org.tuml.runtime.restlet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Filter;
import org.tuml.runtime.adaptor.GraphDb;

/**
 * Date: 2013/03/14
 * Time: 7:05 PM
 */
public class TumlRestletFilter extends Filter {

    public TumlRestletFilter (Context context) {
        super(context);
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        GraphDb.getDb().clearTxThreadVar();
        return CONTINUE;
    }

}

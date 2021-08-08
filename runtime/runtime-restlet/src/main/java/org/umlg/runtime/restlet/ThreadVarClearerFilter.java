package org.umlg.runtime.restlet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Filter;
import org.umlg.runtime.adaptor.TransactionThreadBypassValidationVar;
import org.umlg.runtime.adaptor.TransactionThreadEntityVar;
import org.umlg.runtime.adaptor.TransactionThreadNotificationVar;

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
        TransactionThreadBypassValidationVar.remove();
        TransactionThreadNotificationVar.remove();
    }

}

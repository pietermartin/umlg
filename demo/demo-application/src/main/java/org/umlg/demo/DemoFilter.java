package org.umlg.demo;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Filter;

/**
 * Date: 2013/03/14
 * Time: 7:05 PM
 */
public class DemoFilter extends Filter {

    public DemoFilter(Context context) {
        super(context);
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        return CONTINUE;
    }

}

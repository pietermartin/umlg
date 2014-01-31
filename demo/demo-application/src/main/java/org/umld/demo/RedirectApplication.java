package org.umld.demo;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.umlg.runtime.restlet.ErrorStatusService;

/**
 * Date: 2014/01/29
 * Time: 8:26 AM
 */
public class RedirectApplication extends Application  {

    /**
     * default constructor for RestAndJsonApplication
     */
    public RedirectApplication()  {
        setStatusService(new ErrorStatusService());
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/", RedirectResource.class, Template.MODE_EQUALS);
        DemoFilter demoFilter = new DemoFilter(getContext());
        demoFilter.setNext(router);
        return demoFilter;
    }

}

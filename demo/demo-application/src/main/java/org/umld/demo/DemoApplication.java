package org.umld.demo;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.umlg.runtime.restlet.ErrorStatusService;
import org.umlg.runtime.restlet.UmlgRestletFilter;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;

/**
 * Date: 2014/01/27
 * Time: 8:39 PM
 */
public class DemoApplication extends Application {

    /**
     * default constructor for RestAndJsonApplication
     */
    public DemoApplication()  {
        setStatusService(new ErrorStatusService());
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        Directory bootstrap = new Directory(getContext(), "clap://bootstrap/");
        bootstrap.setListingAllowed(true);
        router.attach("/bootstrap/", bootstrap);
        Directory javascripts = new Directory(getContext(), "clap://javascripts");
        javascripts.setListingAllowed(true);
        router.attach("/javascripts/", javascripts);
        Directory stylesheets = new Directory(getContext(), "clap://stylesheets");
        stylesheets.setListingAllowed(true);
        router.attach("/stylesheets/", stylesheets);

        router.attach("/app", DemoApplicationResource.class, Template.MODE_STARTS_WITH);
        DemoFilter demoFilter = new DemoFilter(getContext());
        demoFilter.setNext(router);
        return demoFilter;
    }


}

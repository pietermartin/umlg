package org.umlg.demo;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.umlg.runtime.restlet.ErrorStatusService;

/**
 * Date: 2014/01/27
 * Time: 8:39 PM
 */
public class DemoApplication extends Application {

    public DemoApplication()  {
        setStatusService(new ErrorStatusService());
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
//        Directory bootstrap = new Directory(getContext(), "clap://bootstrap/");
//        bootstrap.setListingAllowed(true);
//        router.attach("/bootstrap/", bootstrap);
//        router.attach("/demo/bootstrap/", bootstrap);
//        Directory javascripts = new Directory(getContext(), "clap://javascripts");
//        javascripts.setListingAllowed(true);
//        router.attach("/demo/javascripts/", javascripts);
//        Directory stylesheets = new Directory(getContext(), "clap://stylesheets");
//        stylesheets.setListingAllowed(true);
//        router.attach("/stylesheets/", stylesheets);
//        router.attach("/demo/stylesheets/", stylesheets);

        Directory resources = new Directory(getContext(), "war://resources/");
        resources.setListingAllowed(true);
        router.attach("/resources/", resources);

        router.attach("", DemoApplicationResource.class, Template.MODE_EQUALS);
        router.attach("/", DemoApplicationResource.class, Template.MODE_EQUALS);
        DemoFilter demoFilter = new DemoFilter(getContext());
        demoFilter.setNext(router);
        return demoFilter;
    }


}

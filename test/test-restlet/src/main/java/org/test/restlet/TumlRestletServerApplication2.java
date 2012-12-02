package org.test.restlet;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.tuml.runtime.restlet.BaseOclExecutionServerResourceImpl;
import org.tuml.ui.TumlGuiServerResource2;

public class TumlRestletServerApplication2 extends Application {

	public TumlRestletServerApplication2() {
		super();
		setName("RESTful Test Server");
		setDescription("Warawara");
		setOwner("who");
		setAuthor("The Dream Team");
	}

	public TumlRestletServerApplication2(Context context) {
		super(context);
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		restlet.RestletRouterEnum.attachAll(router);
		router.attach("/ui2", TumlGuiServerResource2.class, Template.MODE_STARTS_WITH);
		
		//This will load everthing under /src/main/javascript/javascript. The second javascript matches the route so the url looks good
//        Directory slickgrid = new Directory(getContext(), "clap://javascript/javascript/");
        //Dev mode
        Directory slickgrid = new Directory(getContext(), "file:///home/pieter/workspace-tuml/tuml/runtime/runtime-jquery/src/main/javascript/javascript");
        slickgrid.setListingAllowed(true);
        router.attach("/javascript/", slickgrid);
        
		//This will load everthing under /src/main/javascript/css. The second javascript matches the route so the url looks good
//        Directory css = new Directory(getContext(), "clap://javascript/css");

        //Dev mode
        Directory css = new Directory(getContext(), "file:///home/pieter/workspace-tuml/tuml/runtime/runtime-jquery/src/main/javascript/css");
        css.setListingAllowed(true);
        router.attach("/css/", css);
		return router;
	};

}

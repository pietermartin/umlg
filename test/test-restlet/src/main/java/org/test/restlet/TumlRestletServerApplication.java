package org.test.restlet;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.tuml.ui.TumlGuiServerResource;

public class TumlRestletServerApplication extends Application {

	public TumlRestletServerApplication() {
		super();
		setName("RESTful Test Server");
		setDescription("Warawara");
		setOwner("who");
		setAuthor("The Dream Team");
	}

	public TumlRestletServerApplication(Context context) {
		super(context);
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		restlet.RestletRouterEnum.attachAll(router);
		router.attach("/ui", TumlGuiServerResource.class, Template.MODE_STARTS_WITH);
		
		//This will load everthing under /src/main/javascript/javascript. The second javascript matches the route so the url looks good
        Directory slickgrid = new Directory(getContext(), "clap://class/javascript");
        slickgrid.setListingAllowed(true);
        router.attach("/javascript", slickgrid);
        
		return router;
	};

}

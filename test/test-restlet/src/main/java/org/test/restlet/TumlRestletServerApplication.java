package org.test.restlet;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.test.restlet.gui.HumanGuiServerResource;

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
		router.attach("/view/humans/{humanId}", HumanGuiServerResource.class);
        Directory slickgrid = new Directory(getContext(), "clap://slickgrid");
        slickgrid.setListingAllowed(true);
        router.attach("/view", slickgrid);
        Directory jquery = new Directory(getContext(), "clap://jquery");
        jquery.setListingAllowed(true);
        router.attach("/view", slickgrid);
        Directory css = new Directory(getContext(), "clap://jquery");
        css.setListingAllowed(true);
        router.attach("/view", slickgrid);
		return router;
	};

}

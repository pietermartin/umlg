package org.test.restlet;

import org.restlet.*;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.tuml.ui.TumlGuiServerResource2;

/**
 * Date: 2012/12/29
 * Time: 11:10 AM
 */
public class RestAndJsonApplication extends Application {

    public static void main(String[] args) throws Exception {
        Server restAndJsonServer = new Server(Protocol.HTTP, 8111);
        restAndJsonServer.setNext(new RestAndJsonApplication());
        restAndJsonServer.start();
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
    }


//    @Override
//    public Restlet createInboundRoot() {
//        return new Restlet() {
//            @Override
//            public void handle(Request request, Response response) {
//                String entity = "Method : " + request.getMethod()
//                        + "\nResource URI : "
//                        + request.getResourceRef()
//                        + "\nIP address : "
//                        + request.getClientInfo().getAddress()
//                        + "\nAgent name : "
//                        + request.getClientInfo().getAgentName()
//                        + "\nAgent version: "
//                        + request.getClientInfo().getAgentVersion();
//                response.setEntity(entity, MediaType.TEXT_PLAIN);
//            }
//        };
//    }

}

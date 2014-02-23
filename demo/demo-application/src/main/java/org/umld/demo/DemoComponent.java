package org.umld.demo;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.umlg.graphofthegods.GraphofthegodsApplication;
import org.umlg.tinkergraph.TinkergraphApplication;

/**
 * Date: 2014/01/27
 * Time: 8:38 PM
 */
public class DemoComponent extends Component {

    public static void main(String[] args) throws Exception {
        new DemoComponent().start();
    }

    /**
     * default constructor for DemoComponent
     */
    public DemoComponent()  {
        setName("DemoComponent");
        setDescription("This is UMLG's demo application.");
        getClients().add(Protocol.FILE);
        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.RIAP);
        Server server = new Server(new Context(), Protocol.HTTP, 8111);
        server.getContext().getParameters().set("tracing", "true");
        getServers().add(server);


        getDefaultHost().attach("", new RedirectApplication());
        getDefaultHost().attach("/demo", new DemoApplication());
        getDefaultHost().attach("/tinkergraph", new TinkergraphApplication());
        getDefaultHost().attach("/graphofthegods", new GraphofthegodsApplication());
    }

}

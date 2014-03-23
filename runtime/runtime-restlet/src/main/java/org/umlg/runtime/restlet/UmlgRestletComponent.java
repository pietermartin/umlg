package org.umlg.runtime.restlet;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.umlg.runtime.adaptor.UMLG;

/**
 * Date: 2014/01/15
 * Time: 4:14 PM
 */
public abstract class UmlgRestletComponent extends Component {

    /**
     * default constructor for UmlgRestletComponent
     */
    public UmlgRestletComponent()  {
        setName("restAndJson");
        setDescription("Halo, I am your first comment.");
        getClients().add(Protocol.FILE);
        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.RIAP);
        Server server = new Server(new Context(), Protocol.HTTP, 8111);
        server.getContext().getParameters().set("tracing", "true");
        getServers().add(server);
        attachApplications();
    }

    @Override
    public void stop() throws Exception {
        UMLG.get().shutdown();
        UMLG.remove();
        super.stop();
    }

    /**
     * Override this method to attach the restlet application to the component
     */
    protected abstract void attachApplications();

}

package org.umlg.demo;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Date: 2014/01/29
 * Time: 8:29 AM
 */
public class RedirectResource extends ServerResource {

    public RedirectResource() {
        setNegotiated(false);
    }

    @Override
    protected Representation get() throws ResourceException {
        redirectPermanent("http://" + this.getHostRef().getHostDomain() + ":8111/demo");
        return new StringRepresentation("Redirected to /demo");
    }
}

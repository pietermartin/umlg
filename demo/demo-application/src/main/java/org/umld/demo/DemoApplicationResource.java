package org.umld.demo;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Date: 2014/01/27
 * Time: 8:43 PM
 */
public class DemoApplicationResource extends ServerResource {

    public DemoApplicationResource() {
        setNegotiated(false);
    }

    @Override
    protected Representation get() throws ResourceException {
        Representation demoFreemarker = new ClientResource("clap:///demoApplication.ftl").get();
        return new TemplateRepresentation(demoFreemarker, null, MediaType.TEXT_HTML);
    }
}

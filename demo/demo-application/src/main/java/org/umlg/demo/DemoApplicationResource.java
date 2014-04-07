package org.umlg.demo;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Directory;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.io.File;

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
        File demoFtl = new File("demo/demo-application/src/main/webapp/resources/demoHome.html");
        return new FileRepresentation(demoFtl, MediaType.TEXT_HTML);

//        Representation demoFreemarker = new ClientResource("clap:///demoHome.ftl").get();
//        return new TemplateRepresentation(demoFreemarker, null, MediaType.TEXT_HTML);
    }
}

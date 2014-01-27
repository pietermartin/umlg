package org.umlg.runtime.restlet;

import org.restlet.data.MediaType;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.umlg.runtime.restlet.util.UmlgURLDecoder;

/**
 * Date: 2014/01/26
 * Time: 2:29 PM
 */
public class UmlgDiagramResource extends ServerResource {

    public UmlgDiagramResource() {
        setNegotiated(false);
    }

    @Override
    protected Representation get() throws ResourceException {
        String path = UmlgURLDecoder.decode(getQueryValue("path"));
        if (path.endsWith("SVG") || path.endsWith("svg")) {
            return new InputRepresentation(ClassLoader.getSystemResourceAsStream(path), MediaType.IMAGE_SVG);
        } else {
            return new InputRepresentation(ClassLoader.getSystemResourceAsStream(path), MediaType.IMAGE_PNG);
        }
    }

}

package org.test.restlet.gui;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class TumlGuiServerResource extends ServerResource {
	
	public TumlGuiServerResource() {
		setNegotiated(false);
	}

	@Override
	protected Representation get() throws ResourceException {
//		return new FileRepresentation("/home/pieter/workspace-tuml/tuml/test/test-restlet/src/main/resources/viewhuman.xhtml", MediaType.TEXT_HTML);
		return new FileRepresentation("/home/pieter/workspace-tuml/tuml/test/test-restlet/src/main/resources/layout.html", MediaType.TEXT_HTML);
	}

}
package org.tuml.test.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface Hand_finger_ServerResource {
	@Get(	"json")
	public Representation get();


}
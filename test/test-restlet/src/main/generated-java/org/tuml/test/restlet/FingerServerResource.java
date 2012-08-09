package org.tuml.test.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface FingerServerResource {
	@Get(	"json")
	public Representation get();


}
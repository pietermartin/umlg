package org.tuml.runtime.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface OclExecution_ServerResource {
	@Get(	"json")
	public Representation get();


}
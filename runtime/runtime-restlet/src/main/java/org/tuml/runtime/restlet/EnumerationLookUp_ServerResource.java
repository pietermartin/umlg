package org.tuml.runtime.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface EnumerationLookUp_ServerResource {
	@Get(	"json")
	public Representation get();


}
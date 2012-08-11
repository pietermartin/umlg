package org.tuml.test.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface HandServerResource {
	@Get(	"json")
	public Representation get();
	
	@Put(	"json")
	public Representation put(Representation entity);


}
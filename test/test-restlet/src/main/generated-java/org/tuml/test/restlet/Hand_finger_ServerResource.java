package org.tuml.test.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface Hand_finger_ServerResource {
	@Get(	"json")
	public Representation get();
	
	@Put(	"json")
	public Representation put(Representation entity);


}
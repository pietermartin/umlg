package org.tuml.test.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.test.Hand;
import org.tuml.test.Hand.HandRuntimePropertyEnum;

public class HandServerResourceImpl extends ServerResource implements HandServerResource {
	private int handId;

	/**
	 * default constructor for HandServerResourceImpl
	 */
	public HandServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		this.handId= Integer.parseInt((String)getRequestAttributes().get("handId"));;
		Hand c = new Hand(GraphDb.getDb().getVertex(this.handId));
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(c.toJson());
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(HandRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
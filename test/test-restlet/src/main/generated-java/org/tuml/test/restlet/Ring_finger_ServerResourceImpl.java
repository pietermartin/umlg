package org.tuml.test.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.json.ToJsonUtil;
import org.tuml.test.Ring;
import org.tuml.test.Ring.RingRuntimePropertyEnum;

public class Ring_finger_ServerResourceImpl extends ServerResource implements Ring_finger_ServerResource {
	private int ringId;

	/**
	 * default constructor for Ring_finger_ServerResourceImpl
	 */
	public Ring_finger_ServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		this.ringId = Integer.parseInt((String)getRequestAttributes().get("ringId"));
		Ring parentResource = new Ring(GraphDb.getDb().getVertex(this.ringId));
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getFinger()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(RingRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
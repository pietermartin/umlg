package org.tuml.test.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.json.ToJsonUtil;
import org.tuml.test.Finger;
import org.tuml.test.Finger.FingerRuntimePropertyEnum;

public class Finger_ring_ServerResourceImpl extends ServerResource implements Finger_ring_ServerResource {
	private int fingerId;

	/**
	 * default constructor for Finger_ring_ServerResourceImpl
	 */
	public Finger_ring_ServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		this.fingerId = Integer.parseInt((String)getRequestAttributes().get("fingerId"));
		Finger parentResource = new Finger(GraphDb.getDb().getVertex(this.fingerId));
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getRing()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(FingerRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
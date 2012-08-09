package org.tuml.test.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.json.ToJsonUtil;
import org.tuml.test.Hand;
import org.tuml.test.Hand.HandRuntimePropertyEnum;

public class Hand_human_ServerResourceImpl extends ServerResource implements Hand_human_ServerResource {
	private int handId;

	/**
	 * default constructor for Hand_human_ServerResourceImpl
	 */
	public Hand_human_ServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		this.handId = Integer.parseInt((String)getRequestAttributes().get("handId"));
		Hand parentResource = new Hand(GraphDb.getDb().getVertex(this.handId));
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getHuman()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(HandRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
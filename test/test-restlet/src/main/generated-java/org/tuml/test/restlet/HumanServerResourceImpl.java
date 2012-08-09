package org.tuml.test.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.test.Human;
import org.tuml.test.Human.HumanRuntimePropertyEnum;

public class HumanServerResourceImpl extends ServerResource implements HumanServerResource {
	private int humanId;

	/**
	 * default constructor for HumanServerResourceImpl
	 */
	public HumanServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		this.humanId= Integer.parseInt((String)getRequestAttributes().get("humanId"));;
		Human c = new Human(GraphDb.getDb().getVertex(this.humanId));
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(c.toJson());
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(HumanRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
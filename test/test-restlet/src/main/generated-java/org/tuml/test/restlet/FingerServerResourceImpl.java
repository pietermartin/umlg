package org.tuml.test.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.test.Finger;
import org.tuml.test.Finger.FingerRuntimePropertyEnum;

public class FingerServerResourceImpl extends ServerResource implements FingerServerResource {
	private int fingerId;

	/**
	 * default constructor for FingerServerResourceImpl
	 */
	public FingerServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		this.fingerId= Integer.parseInt((String)getRequestAttributes().get("fingerId"));;
		Finger c = new Finger(GraphDb.getDb().getVertex(this.fingerId));
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(c.toJson());
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(FingerRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
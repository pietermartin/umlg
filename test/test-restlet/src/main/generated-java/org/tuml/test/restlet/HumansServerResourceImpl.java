package org.tuml.test.restlet;

import java.util.List;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.root.Root;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.json.ToJsonUtil;
import org.tuml.test.Human;
import org.tuml.test.Human.HumanRuntimePropertyEnum;

public class HumansServerResourceImpl extends ServerResource implements HumansServerResource {
	private int humanId;

	/**
	 * default constructor for HumansServerResourceImpl
	 */
	public HumansServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		StringBuilder json = new StringBuilder();
		List<Human> resource = Root.INSTANCE.getHuman();
		json.append("[");
		json.append(ToJsonUtil.toJson(resource));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(HumanRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
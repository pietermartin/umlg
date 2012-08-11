package org.tuml.test.restlet;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.json.ToJsonUtil;
import org.tuml.test.Human;
import org.tuml.test.Human.HumanRuntimePropertyEnum;
import org.tuml.test.Ring;
import org.tuml.test.Ring.RingRuntimePropertyEnum;

public class Ring_human_ServerResourceImpl extends ServerResource implements Ring_human_ServerResource {
	private int ringId;

	/**
	 * default constructor for Ring_human_ServerResourceImpl
	 */
	public Ring_human_ServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		this.ringId = Integer.parseInt((String)getRequestAttributes().get("ringId"));
		Ring parentResource = new Ring(GraphDb.getDb().getVertex(this.ringId));
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getHuman()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(RingRuntimePropertyEnum.asJson());
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(HumanRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}
	
	@Override
	public Representation put(Representation entity) throws ResourceException {
		this.ringId = Integer.parseInt((String)getRequestAttributes().get("ringId"));
		Ring parentResource = new Ring(GraphDb.getDb().getVertex(this.ringId));
		GraphDb.getDb().startTransaction();
		try {
			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings(	"unchecked")
			 Map<String,Object> propertyMap = mapper.readValue(entity.getText(), Map.class);
			Human childResource = new Human(GraphDb.getDb().getVertex(propertyMap.get("id")));
			childResource.fromJson(propertyMap);
			parentResource.addToHuman(childResource);
			GraphDb.getDb().stopTransaction(Conclusion.SUCCESS);
		} catch (Exception e) {
			GraphDb.getDb().stopTransaction(Conclusion.FAILURE);
			throw new RuntimeException(e);
		}
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getHuman()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(RingRuntimePropertyEnum.asJson());
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(HumanRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
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
import org.tuml.test.Finger;
import org.tuml.test.Finger.FingerRuntimePropertyEnum;
import org.tuml.test.Hand;
import org.tuml.test.Hand.HandRuntimePropertyEnum;

public class Finger_hand_ServerResourceImpl extends ServerResource implements Finger_hand_ServerResource {
	private int fingerId;

	/**
	 * default constructor for Finger_hand_ServerResourceImpl
	 */
	public Finger_hand_ServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		this.fingerId = Integer.parseInt((String)getRequestAttributes().get("fingerId"));
		Finger parentResource = new Finger(GraphDb.getDb().getVertex(this.fingerId));
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getHand()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(FingerRuntimePropertyEnum.asJson());
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(HandRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}
	
	@Override
	public Representation put(Representation entity) throws ResourceException {
		this.fingerId = Integer.parseInt((String)getRequestAttributes().get("fingerId"));
		Finger parentResource = new Finger(GraphDb.getDb().getVertex(this.fingerId));
		GraphDb.getDb().startTransaction();
		try {
			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings(	"unchecked")
			 Map<String,Object> propertyMap = mapper.readValue(entity.getText(), Map.class);
			Hand childResource = new Hand(GraphDb.getDb().getVertex(propertyMap.get("id")));
			childResource.fromJson(propertyMap);
			parentResource.addToHand(childResource);
			GraphDb.getDb().stopTransaction(Conclusion.SUCCESS);
		} catch (Exception e) {
			GraphDb.getDb().stopTransaction(Conclusion.FAILURE);
			throw new RuntimeException(e);
		}
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getHand()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(FingerRuntimePropertyEnum.asJson());
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(HandRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
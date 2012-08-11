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

public class Hand_finger_ServerResourceImpl extends ServerResource implements Hand_finger_ServerResource {
	private int handId;

	/**
	 * default constructor for Hand_finger_ServerResourceImpl
	 */
	public Hand_finger_ServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		this.handId = Integer.parseInt((String)getRequestAttributes().get("handId"));
		Hand parentResource = new Hand(GraphDb.getDb().getVertex(this.handId));
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getFinger()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(HandRuntimePropertyEnum.asJson());
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(FingerRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}
	
	@Override
	public Representation put(Representation entity) throws ResourceException {
		this.handId = Integer.parseInt((String)getRequestAttributes().get("handId"));
		Hand parentResource = new Hand(GraphDb.getDb().getVertex(this.handId));
		GraphDb.getDb().startTransaction();
		try {
			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings(	"unchecked")
			 Map<String,Object> propertyMap = mapper.readValue(entity.getText(), Map.class);
			Finger childResource = new Finger(GraphDb.getDb().getVertex(propertyMap.get("id")));
			childResource.fromJson(propertyMap);
			parentResource.getFinger().add(3, childResource);
//			parentResource.addToFinger(childResource);
			GraphDb.getDb().stopTransaction(Conclusion.SUCCESS);
		} catch (Exception e) {
			GraphDb.getDb().stopTransaction(Conclusion.FAILURE);
			throw new RuntimeException(e);
		}
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getFinger()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(HandRuntimePropertyEnum.asJson());
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(FingerRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
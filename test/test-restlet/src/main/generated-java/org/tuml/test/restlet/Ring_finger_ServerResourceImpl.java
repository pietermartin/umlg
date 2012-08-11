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
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(FingerRuntimePropertyEnum.asJson());
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
			Finger childResource = new Finger(GraphDb.getDb().getVertex(propertyMap.get("id")));
			childResource.fromJson(propertyMap);
			parentResource.addToFinger(childResource);
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
		json.append(RingRuntimePropertyEnum.asJson());
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(FingerRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
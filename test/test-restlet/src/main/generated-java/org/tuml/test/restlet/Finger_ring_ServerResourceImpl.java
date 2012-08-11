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
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(RingRuntimePropertyEnum.asJson());
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
			Ring childResource = new Ring(GraphDb.getDb().getVertex(propertyMap.get("id")));
			childResource.fromJson(propertyMap);
			parentResource.addToRing(childResource);
			GraphDb.getDb().stopTransaction(Conclusion.SUCCESS);
		} catch (Exception e) {
			GraphDb.getDb().stopTransaction(Conclusion.FAILURE);
			throw new RuntimeException(e);
		}
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(ToJsonUtil.toJson(parentResource.getRing()));
		json.append(",");
		json.append(" {\"meta\" : ");
		json.append(FingerRuntimePropertyEnum.asJson());
		json.append("}, ");
		json.append(" {\"meta\" : ");
		json.append(RingRuntimePropertyEnum.asJson());
		json.append("}]");
		return new JsonRepresentation(json.toString());
	}


}
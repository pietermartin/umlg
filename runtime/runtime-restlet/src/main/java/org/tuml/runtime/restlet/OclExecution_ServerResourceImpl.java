package org.tuml.runtime.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.ocl.TumlOclExecutor;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TumlNode;

public class OclExecution_ServerResourceImpl extends ServerResource implements OclExecution_ServerResource {

	/**
	 * default constructor for Finger_finger_lookUpRing_ServerResourceImpl
	 */
	public OclExecution_ServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		String ocl = getQuery().getFirst("ocl").getValue();
		Integer contextId= Integer.parseInt((String)getRequestAttributes().get("contextId"));
		TumlNode context = GraphDb.getDb().<TumlNode>instantiateClassifier(Long.valueOf(contextId));
		String json = TumlOclExecutor.executeOclQueryToJson(context.getQualifiedName(), context, ocl);

//		StringBuilder json = new StringBuilder();
//		json.append("{\"data\": [");	
//		json.append(ToJsonUtil.enumsToJson(Arrays.asList(enumConstants)));
//		json.append("]");
//		json.append("}");
		return new JsonRepresentation(json);
	}


}
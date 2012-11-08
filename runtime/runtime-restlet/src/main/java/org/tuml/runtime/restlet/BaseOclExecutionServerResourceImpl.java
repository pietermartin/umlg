package org.tuml.runtime.restlet;

import org.restlet.resource.ServerResource;
import org.tuml.ocl.TumlOclExecutor;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TumlNode;

public abstract class BaseOclExecutionServerResourceImpl extends ServerResource {

	public BaseOclExecutionServerResourceImpl() {
	}

	protected String execute(String ocl, Integer contextId) {
		TumlNode context = GraphDb.getDb().<TumlNode>instantiateClassifier(Long.valueOf(contextId));
		return TumlOclExecutor.executeOclQueryToJson(context.getQualifiedName(), context, ocl);
	}


}
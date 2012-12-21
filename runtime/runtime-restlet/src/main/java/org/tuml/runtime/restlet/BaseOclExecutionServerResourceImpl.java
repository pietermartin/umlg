package org.tuml.runtime.restlet;

import org.restlet.resource.ServerResource;
import org.tuml.ocl.TumlOclExecutor;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TumlNode;

public abstract class BaseOclExecutionServerResourceImpl extends ServerResource {

	public BaseOclExecutionServerResourceImpl() {
	}

	protected Object execute(String ocl, Integer contextId) {
		TumlNode context = GraphDb.getDb().<TumlNode>instantiateClassifier(Long.valueOf(contextId));
		return TumlOclExecutor.executeOclQuery(context.getQualifiedName(), context, ocl);
	}


}
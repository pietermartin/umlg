package org.tuml.runtime.adaptor;

import java.util.HashMap;
import java.util.Map;

import com.tinkerpop.blueprints.Vertex;


public class TransactionThreadVar {
	
	private TransactionThreadVar() {
	}

	private static ThreadLocal<Map<String, Vertex>> transactionVar = new ThreadLocal<Map<String, Vertex>>() {

		@Override
	    protected Map<String, Vertex> initialValue() {
	        return new HashMap<String, Vertex>();
	    }

	};

	public static Boolean hasNoAuditEntry(String clazzAndId) {
		Vertex newVertex = getAuditEntry(clazzAndId);
		return newVertex==null?true:false;
	}
	
	public static Vertex getAuditEntry(String clazzAndId) {
		Map<String, Vertex> auditVertexMap = transactionVar.get();
		Vertex auditVertex = auditVertexMap.get(clazzAndId);
		return auditVertex;
	}	
	
	public static void clear() {
		transactionVar.get().clear();
	}

	public static void putAuditVertexFalse(String clazzAndId, Vertex auditVertex) {
		transactionVar.get().put(clazzAndId, auditVertex);
	}

}

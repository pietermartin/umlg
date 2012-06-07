package org.tuml.runtime.adaptor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.tuml.runtime.domain.PersistentObject;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

public interface NakedGraph extends TransactionalGraph, IndexableGraph, Serializable  {
	void incrementTransactionCount();
	long getTransactionCount();
	Vertex getRoot();
	Vertex addVertex(String className);
	Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels);
	void addRoot();
	long countVertices();
	long countEdges();
	void registerListeners();
	void createSchema(Map<String, Class<?>> classNames);
	void clearAutoIndices();
	List<PersistentObject> getCompositeRoots(); 
	<T> List<T> query(Class<?> className, int first, int pageSize);
	<T> T instantiateClassifier(Long id);
	TransactionManager getTransactionManager();
    void resume(Transaction tobj);
    Transaction suspend();
    Transaction getTransaction();
    public <T extends Element> NakedTinkerIndex<T> getIndex(String indexName, Class<T> indexClass);
    public <T extends Element> NakedTinkerIndex<T> createKeyIndex(String indexName, Class<T> indexClass);
    public boolean isTransactionActive();
}

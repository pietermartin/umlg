package org.tuml.runtime.adaptor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import com.tinkerpop.blueprints.util.wrappers.event.listener.GraphChangedListener;
import org.tuml.runtime.domain.PersistentObject;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

public interface TumlGraph extends TransactionalGraph, IndexableGraph, Serializable  {
	void incrementTransactionCount();
	long getTransactionCount();
	Vertex getRoot();
	Vertex addVertex(String className);
	Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels);
	void addRoot();
	long countVertices();
	long countEdges();
	void registerListeners();
//	<T> List<T> query(Class<?> className, int first, int pageSize);
	<T> T instantiateClassifier(Long id);

    TransactionManager getTransactionManager();
    void resume(Transaction t);
    Transaction suspend();
    Transaction getTransaction();

    <T extends Element> TumlTinkerIndex<T> createIndex(String indexName, Class<T> indexClass);
    <T extends Element> TumlTinkerIndex<T> getIndex(String indexName, Class<T> indexClass);
    boolean hasEdgeBeenDeleted(Edge edge);


}

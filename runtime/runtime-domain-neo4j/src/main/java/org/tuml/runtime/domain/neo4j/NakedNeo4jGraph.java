package org.tuml.runtime.domain.neo4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.tuml.runtime.adaptor.NakedGraph;
import org.tuml.runtime.adaptor.NakedTinkerIndex;
import org.tuml.runtime.adaptor.TinkerSchemaHelper;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.domain.PersistentObject;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jEdge;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jVertex;

public class NakedNeo4jGraph implements NakedGraph {

	private static final long serialVersionUID = 7025198246796291511L;
	private Neo4jGraph neo4jGraph;
	private TransactionEventHandler<PersistentObject> transactionEventHandler;
	private TinkerSchemaHelper schemaHelper;

	private final ThreadLocal<Integer> txCount = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return null;
		}
	};

	public NakedNeo4jGraph(Neo4jGraph orientGraph, TinkerSchemaHelper schemaHelper) {
		super();
		this.neo4jGraph = orientGraph;
		this.schemaHelper = schemaHelper;
	}

	public NakedNeo4jGraph(Neo4jGraph orientGraph, TinkerSchemaHelper schemaHelper, boolean withSchema) {
		this(orientGraph, schemaHelper);
	}

	@Override
	public void startTransaction() {
		txCount.set(1);
	}

	@Override
	public void stopTransaction(Conclusion conclusion) {
		neo4jGraph.stopTransaction(conclusion);
		// txCount.remove();
	}

	@Override
	public Vertex addVertex(Object id) {
		return neo4jGraph.addVertex(id);
	}

	@Override
	public Vertex getVertex(Object id) {
		return neo4jGraph.getVertex(id);
	}

	@Override
	public Iterable<Vertex> getVertices() {
		return neo4jGraph.getVertices();
	}

	@Override
	public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
		return neo4jGraph.addEdge(id, outVertex, inVertex, label);
	}

	@Override
	public Edge getEdge(Object id) {
		return neo4jGraph.getEdge(id);
	}

	@Override
	public void removeEdge(Edge edge) {
		neo4jGraph.removeEdge(edge);
	}

	@Override
	public Iterable<Edge> getEdges() {
		return neo4jGraph.getEdges();
	}

	@Override
	public void shutdown() {
		neo4jGraph.shutdown();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends Element> NakedTinkerIndex<T> createIndex(String indexName, Class<T> indexClass) {
		return new NakedNeo4jIndex(this.neo4jGraph.createIndex(indexName, indexClass));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends Element> NakedTinkerIndex<T> getIndex(String indexName, Class<T> indexClass) {
		Index<T> index = this.neo4jGraph.getIndex(indexName, indexClass);
		if (index != null) {
			return new NakedNeo4jIndex(index);
		} else {
			return null;
		}
	}

	@Override
	public Iterable<Index<? extends Element>> getIndices() {
		return neo4jGraph.getIndices();
	}

	@Override
	public void dropIndex(String indexName) {
		neo4jGraph.dropIndex(indexName);
	}

	@Override
	public void incrementTransactionCount() {
		getRoot().setProperty("transactionCount", (Integer) getRoot().getProperty("transactionCount") + 1);
	}

	@Override
	public long getTransactionCount() {
		return (Integer) getRoot().getProperty("transactionCount");
	}

	@Override
	public Vertex addVertex(String className) {
		Vertex v = neo4jGraph.addVertex(null);
		if (className != null) {
			v.setProperty("className", className);
		}
		return v;
	}

	@Override
	public void removeVertex(Vertex vertex) {
		TransactionThreadEntityVar.remove(vertex.getId().toString());
		neo4jGraph.removeVertex(vertex);
	}

	@Override
	public Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels) {
		Node n1 = ((Neo4jVertex) v1).getRawVertex();
		Node n2 = ((Neo4jVertex) v2).getRawVertex();
		List<DynamicRelationshipType> dynaRel = new ArrayList<DynamicRelationshipType>(labels.length);
		for (String label : labels) {
			dynaRel.add(DynamicRelationshipType.withName(label));
		}
		Set<Edge> result = new HashSet<Edge>(dynaRel.size());
		Iterable<Relationship> relationships = n1.getRelationships(dynaRel.toArray(new DynamicRelationshipType[] {}));
		for (Relationship relationship : relationships) {
			if ((relationship.getStartNode().equals(n1) && relationship.getEndNode().equals(n2))
					|| (relationship.getStartNode().equals(n2) && relationship.getEndNode().equals(n1))) {

				result.add(neo4jGraph.getEdge(relationship.getId()));
			}
		}
		return result;
	}

	@Override
	public void addRoot() {
		try {
			neo4jGraph.getRawGraph().getNodeById(1);
		} catch (NotFoundException e) {
			try {
				((EmbeddedGraphDatabase) neo4jGraph.getRawGraph()).getTxManager().begin();
				((EmbeddedGraphDatabase) neo4jGraph.getRawGraph()).getNodeManager().setReferenceNodeId(neo4jGraph.getRawGraph().createNode().getId());
				((EmbeddedGraphDatabase) neo4jGraph.getRawGraph()).getTxManager().commit();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			Vertex root = getRoot();
			root.setProperty("transactionCount", 1);
		}
	}

	@Override
	public Vertex getRoot() {
		return this.neo4jGraph.getVertex(1L);
	}

	@Override
	public long countVertices() {
		return ((EmbeddedGraphDatabase) neo4jGraph.getRawGraph()).getNodeManager().getNumberOfIdsInUse(Node.class) - 1;
	}

	@Override
	public long countEdges() {
		return ((EmbeddedGraphDatabase) neo4jGraph.getRawGraph()).getNodeManager().getNumberOfIdsInUse(Relationship.class);
	}

	@Override
	public void registerListeners() {
		if (transactionEventHandler == null) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			transactionEventHandler = new NakedTransactionEventHandler<PersistentObject>(validator);
			neo4jGraph.getRawGraph().registerTransactionEventHandler(transactionEventHandler);
		}
	}

	@Override
	public void clearAutoIndices() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<PersistentObject> getCompositeRoots() {
		List<PersistentObject> result = new ArrayList<PersistentObject>();
		Iterable<Edge> iter = getRoot().getEdges(Direction.OUT, "root");
		for (Edge edge : iter) {
			try {
				Class<?> c = Class.forName((String) edge.getProperty("inClass"));
				result.add((PersistentObject) c.getConstructor(Vertex.class).newInstance(edge.getVertex(Direction.IN)));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T instantiateClassifier(Long id) {
		try {
			Vertex v = neo4jGraph.getVertex(id);
			//TODO reimplement schemaHelper
			Class<?> c = Class.forName((String) v.getProperty("className"));
//			Class<?> c = schemaHelper.getClassNames().get((String) v.getProperty("className"));
			return (T) c.getConstructor(Vertex.class).newInstance(v);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> List<T> query(Class<?> className, int first, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createSchema(Map<String, Class<?>> classNames) {
		// TODO Auto-generated method stub

	}

	@Override
	public TransactionManager getTransactionManager() {
		return ((AbstractGraphDatabase) neo4jGraph.getRawGraph()).getTxManager();
	}

	@Override
	public void resume(Transaction t) {
		try {
			getTransactionManager().resume(t);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Transaction suspend() {
		try {
			return getTransactionManager().suspend();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Transaction getTransaction() {
		try {
			return getTransactionManager().getTransaction();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isTransactionActive() {
		return txCount.get() != null;
	}

	@Override
	public Features getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Vertex> getVertices(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Edge> getEdges(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Element> Index<T> createIndex(String indexName, Class<T> indexClass, Parameter... indexParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCheckElementsInTransaction(boolean b) {
		neo4jGraph.setCheckElementsInTransaction(b);
	}

	@Override
	public boolean hasEdgeBeenDeleted(Edge edge) {
		Neo4jEdge neo4jEdge = (Neo4jEdge) edge;
		try {
			neo4jEdge.getRawEdge().hasProperty("asd");
			return false;
		} catch (Exception e) {
			return true;
		}
		//The way below requires a transaction to have been started.
		
//		Neo4jEdge neo4jEdge = (Neo4jEdge) edge;
//		EmbeddedGraphDatabase g = (EmbeddedGraphDatabase)this.neo4jGraph.getRawGraph();
//		for (Relationship r : g.getNodeManager().getTransactionData().deletedRelationships()) {
//			if (neo4jEdge.getRawEdge().equals(r)) {
//				return true;
//			}
//		}
//		return false;
	}

}

package org.tuml.runtime.collection.persistent;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.NakedTinkerIndex;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.adaptor.TransactionThreadVar;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.IterateExpressionAccumulator;
import org.tuml.runtime.collection.ocl.OclStdLibCollection;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerAuditableNode;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.runtime.domain.ocl.OclState;
import org.tuml.runtime.util.TinkerFormatter;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;

public abstract class BaseCollection<E> implements Collection<E>, TumlRuntimeProperty, OclStdLibCollection<E> {

	protected Collection<E> internalCollection;
	protected OclStdLibCollection<E> oclStdLibCollection;
	protected NakedTinkerIndex<Edge> index;
	protected boolean loaded = false;
	protected TumlNode owner;
	// This is the vertex of the owner of the collection
	protected Vertex vertex;
	protected Class<?> parentClass;
	protected Map<Object, Vertex> internalVertexMap = new HashMap<Object, Vertex>();
	protected TumlRuntimeProperty tumlRuntimeProperty;

	public BaseCollection(TumlRuntimeProperty runtimeProperty) {
		this.tumlRuntimeProperty = runtimeProperty;
	}

	public BaseCollection(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
		super();
		this.owner = owner;
		this.vertex = owner.getVertex();
		this.parentClass = owner.getClass();
		this.tumlRuntimeProperty = runtimeProperty;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void loadFromVertex() {
		if (!isOnePrimitive()) {
			for (Iterator<Edge> iter = getEdges(); iter.hasNext();) {
				Edge edge = iter.next();
				E node = null;
				try {
					Class<?> c = this.getClassToInstantiate(edge);
					if (c.isEnum()) {
						Object value = this.getVertexForDirection(edge).getProperty("value");
						node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
						this.internalVertexMap.put(value, this.getVertexForDirection(edge));
					} else if (TumlNode.class.isAssignableFrom(c)) {
						node = (E) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
					} else {
						Object value = this.getVertexForDirection(edge).getProperty("value");
						node = (E) value;
						this.internalVertexMap.put(value, this.getVertexForDirection(edge));
					}
					this.internalCollection.add(node);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		} else {
			E property = (E) this.vertex.getProperty(getLabel());
			if (property != null) {
				this.internalCollection.add(property);
			}
		}
		this.loaded = true;
	}

	protected Iterator<Edge> getEdges() {
		if (this.isControllingSide()) {
			return this.vertex.getEdges(Direction.OUT, this.getLabel()).iterator();
		} else {
			return this.vertex.getEdges(Direction.IN, this.getLabel()).iterator();
		}
	}

	protected Iterable<Edge> getEdges(Vertex v) {
		if (!this.isControllingSide()) {
			return v.getEdges(Direction.OUT, this.getLabel());
		} else {
			return v.getEdges(Direction.IN, this.getLabel());
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		maybeLoad();
		boolean result = true;
		for (E e : c) {
			if (!this.add(e)) {
				result = false;
			}
		}
		return result;
	}

	@Override
	public boolean add(E e) {
		validateMultiplicityForAdditionalElement();
		if (isQualified() || isInverseQualified()) {
			validateQualifiedAssociation(e);
		}
		boolean result = this.internalCollection.add(e);
		if (result) {
			Edge edge = addInternal(e);

			// Edge can only be null on a one primitive
			if (edge == null && !isOnePrimitive()) {
				throw new IllegalStateException("Edge can only be null on isOne which is a String, Interger, Boolean or primitive");
			}

			if (isQualified() || isInverseQualified()) {
				// Can only qualify TinkerNode's
				if (!(e instanceof TumlNode)) {
					throw new IllegalStateException("Primitive properties can not be qualified!");
				}
				addQualifierToIndex(edge, (TumlNode) e);
			}

			if (isOrdered() || isInverseOrdered()) {
				// Can only qualify TinkerNode's
				if (!(e instanceof TumlNode)) {
					throw new IllegalStateException("Primitive properties can not be qualified!");
				}
				if (isOrdered()) {
					addOrderToIndex(edge, (TumlNode) e);
				}
				if (isInverseOrdered()) {
					addOrderToInverseIndex(edge, (TumlNode) e);
				}
			}

		}
		return result;
	}

	private void validateQualifiedAssociation(E e) {
		if (!(e instanceof TumlNode)) {
			throw new IllegalStateException("Primitive properties can not be qualified!");
		}
		TumlNode node = (TumlNode) e;
		if (isQualified()) {
			for (Qualifier qualifier : this.owner.getQualifiers(this.tumlRuntimeProperty, node)) {
				validateQualifiedMultiplicity(index, qualifier);
			}
		}
		if (isInverseQualified()) {
			Index<Edge> tmpIndex;
			tmpIndex = GraphDb.getDb().getIndex(((TumlNode) e).getUid() + ":::" + getLabel(), Edge.class);
			if (tmpIndex == null) {
				tmpIndex = GraphDb.getDb().createIndex(((TumlNode) e).getUid() + ":::" + getLabel(), Edge.class);
			}
			for (Qualifier qualifier : node.getQualifiers(this.tumlRuntimeProperty, this.owner)) {
				validateQualifiedMultiplicity(tmpIndex, qualifier);
			}
		}
	}

	protected void validateMultiplicityForAdditionalElement() {
		if (!isValid(size() + 1)) {
			throw new IllegalStateException(String.format("The collection's multiplicity is (lower = %s, upper = %s). Current size = %s. It can not accept another element.",
					getLower(), getUpper(), size()));
		}
	}

	@Override
	public boolean remove(Object o) {
		maybeLoad();
		boolean result = this.internalCollection.remove(o);
		if (result) {
			@SuppressWarnings("unchecked")
			E e = (E) o;
			Vertex v;
			if (o instanceof TumlNode) {
				TumlNode node = (TumlNode) o;
				v = node.getVertex();

				if (node instanceof CompositionNode) {
					TransactionThreadEntityVar.setNewEntity((CompositionNode) node);
				}

				Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.getLabel());
				for (Edge edge : edges) {
					if (o instanceof TinkerAuditableNode) {
						createAudit(e, true);
					}
					if (isInverseOrdered()) {
						Index<Edge> index = GraphDb.getDb().getIndex(node.getUid() + ":::" + getLabel(), Edge.class);
						index.remove("index", this.owner.getVertex().getProperty("tinkerIndex"), edge);
					}
					GraphDb.getDb().removeEdge(edge);
					break;
				}
			} else if (o.getClass().isEnum()) {
				v = this.internalVertexMap.get(((Enum<?>) o).name());
				GraphDb.getDb().removeVertex(v);
			} else if (isOnePrimitive()) {
				// Do nothing
			} else {
				v = this.internalVertexMap.get(o);
				if (this.owner instanceof TinkerAuditableNode) {
					createAudit(e, true);
				}
				GraphDb.getDb().removeVertex(v);
			}
		}
		return result;
	}

	protected Edge addInternal(E e) {
		Vertex v = null;
		if (e instanceof TumlNode) {
			TumlNode node = (TumlNode) e;
			if (e instanceof CompositionNode) {
				TransactionThreadEntityVar.setNewEntity((CompositionNode) node);
			}
			v = node.getVertex();
			if (this.isUnique() && (this.isOneToMany() || this.isOneToOne())) {
				// Check that the existing one from the element does not exist,
				// the user must first remove it
				Iterator<Edge> iteratorToOne = getEdges(v).iterator();
				if (iteratorToOne.hasNext()) {
					throw new IllegalStateException("Its a 1");
				}

				// Even if the user cleared the one, a reference on the other
				// side may remain in memory.
				// Clearing this property is not performance issue as it is a
				// one
				node.initialiseProperty(tumlRuntimeProperty);

			}
		} else if (e.getClass().isEnum()) {
			v = GraphDb.getDb().addVertex(null);
			v.setProperty("value", ((Enum<?>) e).name());
			this.internalVertexMap.put(((Enum<?>) e).name(), v);
		} else if (isOnePrimitive()) {
			this.vertex.setProperty(getLabel(), e);
		} else {
			v = GraphDb.getDb().addVertex(null);
			v.setProperty("value", e);
			this.internalVertexMap.put(e, v);
		}
		if (v != null) {
			Edge edge = null;
			boolean createdEdge = false;
			if (edge == null) {
				createdEdge = true;
				edge = createEdge(e, v);
			}
			if (createdEdge && this.owner instanceof TinkerAuditableNode) {
				createAudit(e, false);
			}
			return edge;
		} else {
			if (owner instanceof TinkerAuditableNode) {
				createPrimitiveAudit(e);
			}
			return null;
		}

	}

	private Edge createEdge(E e, Vertex v) {
		Edge edge;
		if (this.isControllingSide()) {
			edge = GraphDb.getDb().addEdge(null, this.vertex, v, this.getLabel());
			edge.setProperty("outClass", this.parentClass.getName());
			edge.setProperty("inClass", e.getClass().getName());
			if (this.isManyToMany()) {
				edge.setProperty("manyToManyCorrelationInverseTRUE", "SETTED");
			}
		} else {
			edge = GraphDb.getDb().addEdge(null, v, this.vertex, this.getLabel());
			edge.setProperty("outClass", e.getClass().getName());
			edge.setProperty("inClass", this.parentClass.getName());
			if (this.isManyToMany()) {
				edge.setProperty("manyToManyCorrelationInverseFALSE", "SETTED");
			}
		}
		return edge;
	}

	private void createPrimitiveAudit(E e) {
		TinkerAuditableNode auditOwner = (TinkerAuditableNode) owner;
		if (TransactionThreadVar.hasNoAuditEntry(owner.getClass().getName() + owner.getUid())) {
			auditOwner.createAuditVertex(false);
		}
		auditOwner.getAuditVertex().setProperty(getLabel(), e);
	}

	protected void createAudit(E e, boolean deletion) {
		if (!(owner instanceof TinkerAuditableNode)) {
			throw new IllegalStateException("if the collection member is an TinkerAuditableNode, then the owner must be a TinkerAuditableNode!");
		}
		TinkerAuditableNode auditOwner = (TinkerAuditableNode) owner;
		if (TransactionThreadVar.hasNoAuditEntry(owner.getClass().getName() + owner.getUid())) {
			auditOwner.createAuditVertex(false);
		}
		if (e instanceof TinkerAuditableNode) {
			TinkerAuditableNode node = (TinkerAuditableNode) e;
			if (TransactionThreadVar.hasNoAuditEntry(node.getClass().getName() + node.getUid())) {
				node.createAuditVertex(false);
			}
			Edge auditEdge;
			if (isControllingSide()) {
				auditEdge = GraphDb.getDb().addEdge(null, auditOwner.getAuditVertex(), node.getAuditVertex(), this.getLabel());
				auditEdge.setProperty("outClass", auditOwner.getClass().getName() + "Audit");
				auditEdge.setProperty("inClass", node.getClass().getName() + "Audit");
			} else {
				auditEdge = GraphDb.getDb().addEdge(null, node.getAuditVertex(), auditOwner.getAuditVertex(), this.getLabel());
				auditEdge.setProperty("inClass", auditOwner.getClass().getName() + "Audit");
				auditEdge.setProperty("outClass", node.getClass().getName() + "Audit");
			}
			if (deletion) {
				auditEdge.setProperty("deletedOn", TinkerFormatter.format(new Date()));
			}
		} else if (e.getClass().isEnum()) {
			Vertex v = GraphDb.getDb().addVertex(null);
			v.setProperty("value", ((Enum<?>) e).name());
			Edge auditEdge = GraphDb.getDb().addEdge(null, auditOwner.getAuditVertex(), v, this.getLabel());
			auditEdge.setProperty("outClass", auditOwner.getClass().getName() + "Audit");
			auditEdge.setProperty("inClass", e.getClass().getName());
		} else {
			if (TransactionThreadVar.hasNoAuditEntry(owner.getClass().getName() + e.getClass().getName() + e.toString())) {
				Vertex auditVertex = GraphDb.getDb().addVertex(null);
				auditVertex.setProperty("value", e);
				TransactionThreadVar.putAuditVertexFalse(owner.getClass().getName() + e.getClass().getName() + e.toString(), auditVertex);
				auditVertex.setProperty("transactionNo", GraphDb.getDb().getTransactionCount());
				Edge auditEdge;
				if (isControllingSide()) {
					auditEdge = GraphDb.getDb().addEdge(null, auditOwner.getAuditVertex(), auditVertex, this.getLabel());
					auditEdge.setProperty("outClass", this.parentClass.getName());
					auditEdge.setProperty("inClass", e.getClass().getName() + "Audit");
				} else {
					auditEdge = GraphDb.getDb().addEdge(null, auditVertex, auditOwner.getAuditVertex(), this.getLabel());
					auditEdge.setProperty("inClass", this.parentClass.getName());
					auditEdge.setProperty("outClass", e.getClass().getName() + "Audit");
				}
				if (deletion) {
					auditEdge.setProperty("transactionNo", GraphDb.getDb().getTransactionCount());
					auditEdge.setProperty("deletedOn", TinkerFormatter.format(new Date()));
				}
			}
		}
	}

	protected void maybeLoad() {
		if (!this.loaded) {
			loadFromVertex();
		}
	}

	protected Vertex getVertexForDirection(Edge edge) {
		if (this.isControllingSide()) {
			return edge.getVertex(Direction.IN);
		} else {
			return edge.getVertex(Direction.OUT);
		}
	}

	protected Vertex getOppositeVertexForDirection(Edge edge) {
		if (this.isControllingSide()) {
			return edge.getVertex(Direction.OUT);
		} else {
			return edge.getVertex(Direction.IN);
		}
	}

	protected Class<?> getClassToInstantiate(Edge edge) {
		try {
			if (this.isControllingSide()) {
				return Class.forName((String) edge.getProperty("inClass"));
			} else {
				return Class.forName((String) edge.getProperty("outClass"));
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int size() {
		maybeLoad();
		return this.internalCollection.size();
	}

	@Override
	public boolean isEmpty() {
		maybeLoad();
		return this.internalCollection.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		maybeLoad();
		return this.internalCollection.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		maybeLoad();
		return this.internalCollection.iterator();
	}

	@Override
	public Object[] toArray() {
		maybeLoad();
		return this.internalCollection.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		maybeLoad();
		return this.internalCollection.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		maybeLoad();
		return this.internalCollection.containsAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (!this.loaded) {
			loadFromVertex();
		}
		boolean result = true;
		for (E e : this.internalCollection) {
			if (!c.contains(e)) {
				if (!this.remove(e)) {
					result = false;
				}
			}
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		maybeLoad();
		boolean result = true;
		for (Object object : c) {
			if (!this.remove(object)) {
				result = false;
			}
		}
		return result;
	}

	private void validateQualifiedMultiplicity(Index<Edge> index, Qualifier qualifier) {
		if (qualifier.isOne()) {
			long count = index.count(qualifier.getKey(), qualifier.getValue());
			if (count > 0) {
				// Add info to exception
				throw new IllegalStateException(String.format("Qualifier fails, qualifier multiplicity is one and an entry for key '%s' and value '%s' already exist",
						qualifier.getKey(), qualifier.getValue()));
			}
		}
	}

	protected void addQualifierToIndex(Edge edge, TumlNode node) {
		// if is qualified update index
		if (isQualified()) {
			addQualifierToIndex(this.index, edge, this.owner, node);
		}

		// if is qualified update index
		if (isInverseQualified()) {
			Index<Edge> index = GraphDb.getDb().getIndex(node.getUid() + ":::" + getLabel(), Edge.class);
			if (index == null) {
				index = GraphDb.getDb().createIndex(node.getUid() + ":::" + getLabel(), Edge.class);
			}
			addQualifierToIndex(index, edge, node, this.owner);
		}
	}

	protected void addOrderToIndex(Edge edge, TumlNode node) {
		// The element is always added to the end of the list
		if (!isOrdered()) {
			throw new IllegalStateException("addOrderToIndex can only be called where the association end is ordered");
		}
		Edge edgeToLastElementInSequence = index.getEdgeToLastElementInSequence();
		Float size;
		if (edgeToLastElementInSequence == null) {
			size = 1F;
		} else {
			size = (Float) getVertexForDirection(edgeToLastElementInSequence).getProperty("tinkerIndex") + 1F;
		}
		this.index.put("index", size, edge);
		getVertexForDirection(edge).setProperty("tinkerIndex", size);
	}

	protected void addOrderToInverseIndex(Edge edge, TumlNode node) {
		// The element is always added to the end of the list
		if (!isInverseOrdered()) {
			throw new IllegalStateException("addOrderToInverseIndex can only be called where the inverse side of the association is ordered");
		}
		NakedTinkerIndex<Edge> index = GraphDb.getDb().getIndex(node.getUid() + ":::" + getLabel(), Edge.class);
		if (index == null) {
			index = GraphDb.getDb().createIndex(node.getUid() + ":::" + getLabel(), Edge.class);
		}
		Edge edgeToLastElementInSequence = index.getEdgeToLastElementInSequence();
		Float size;
		if (edgeToLastElementInSequence == null) {
			size = 1F;
		} else {
			size = (Float) getOppositeVertexForDirection(edgeToLastElementInSequence).getProperty("tinkerIndex") + 1F;
		}
		index.put("index", size, edge);
		getOppositeVertexForDirection(edge).setProperty("tinkerIndex", size);
	}

	/**
	 * element is the context for the ocl expression representing the qualifier
	 * value
	 * 
	 * @param index
	 * @param qualifiedNode
	 * @param qualifierNode
	 */
	private void addQualifierToIndex(Index<Edge> index, Edge edge, TumlNode qualifiedNode, TumlNode qualifierNode) {
		for (Qualifier qualifier : qualifiedNode.getQualifiers(this.tumlRuntimeProperty, qualifierNode)) {
			index.put(qualifier.getKey(), qualifier.getValue(), edge);
			edge.setProperty("index" + qualifier.getKey(), qualifier.getValue());
		}
	}

	@Override
	public String toJson() {
		maybeLoad();
		StringBuilder sb = new StringBuilder();
		if (isManyPrimitive() || isManyEnumeration()) {
			sb.append("[");
			int count = 0;
			for (E e : this.internalCollection) {
				count++;
				if (e instanceof String) {
					sb.append("\"");
					sb.append(e);
					sb.append("\"");
				} else {
					sb.append(e);
				}
				if (count < size()) {
					sb.append(",");
				}
			}
			sb.append("]");
		}
		return sb.toString();
	}

	@Override
	public boolean isOnePrimitive() {
		return this.tumlRuntimeProperty.isOnePrimitive();
	}

	@Override
	public boolean isOneEnumeration() {
		return this.tumlRuntimeProperty.isOneEnumeration();
	}

	@Override
	public boolean isManyPrimitive() {
		return this.tumlRuntimeProperty.isManyPrimitive();
	}

	@Override
	public boolean isManyEnumeration() {
		return this.tumlRuntimeProperty.isManyEnumeration();
	}

	@Override
	public String getLabel() {
		return this.tumlRuntimeProperty.getLabel();
	}

	@Override
	public boolean isOneToOne() {
		return this.tumlRuntimeProperty.isOneToOne();
	}

	@Override
	public boolean isOneToMany() {
		return this.tumlRuntimeProperty.isOneToMany();
	}

	@Override
	public boolean isManyToOne() {
		return this.tumlRuntimeProperty.isManyToOne();
	}

	@Override
	public boolean isManyToMany() {
		return this.tumlRuntimeProperty.isManyToMany();
	}

	@Override
	public int getUpper() {
		return this.tumlRuntimeProperty.getUpper();
	}

	@Override
	public int getLower() {
		return this.tumlRuntimeProperty.getLower();
	}

	@Override
	public boolean isControllingSide() {
		return this.tumlRuntimeProperty.isControllingSide();
	}

	@Override
	public boolean isComposite() {
		return this.tumlRuntimeProperty.isComposite();
	}

	@Override
	public boolean isInverseComposite() {
		return this.tumlRuntimeProperty.isInverseComposite();
	}

	@Override
	public boolean isValid(int elementCount) {
		return this.tumlRuntimeProperty.isValid(elementCount);
	}

	@Override
	public boolean isQualified() {
		return this.tumlRuntimeProperty.isQualified();
	}

	@Override
	public boolean isInverseQualified() {
		return this.tumlRuntimeProperty.isInverseQualified();
	}

	@Override
	public boolean isOrdered() {
		return this.tumlRuntimeProperty.isOrdered();
	}

	@Override
	public boolean isInverseOrdered() {
		return this.tumlRuntimeProperty.isInverseOrdered();
	}

	@Override
	public boolean isUnique() {
		return this.tumlRuntimeProperty.isUnique();
	}

	@Override
	public boolean equals(TinkerCollection<E> c) {
		maybeLoad();
		return this.oclStdLibCollection.equals(c);
	}

	@Override
	public boolean notEquals(TinkerCollection<E> c) {
		maybeLoad();
		return this.oclStdLibCollection.notEquals(c);
	}

	@Override
	public E any(BooleanExpressionEvaluator<E> v) {
		maybeLoad();
		return this.oclStdLibCollection.any(v);
	}

	@Override
	public TinkerSet<E> asSet() {
		maybeLoad();
		return this.oclStdLibCollection.asSet();
	}

	@Override
	public TinkerOrderedSet<E> asOrderedSet() {
		maybeLoad();
		return this.oclStdLibCollection.asOrderedSet();
	}

	@Override
	public TinkerSequence<E> asSequence() {
		maybeLoad();
		return this.oclStdLibCollection.asSequence();
	}

	@Override
	public TinkerBag<E> asBag() {
		maybeLoad();
		return this.oclStdLibCollection.asBag();
	}

	@Override
	public <R> R iterate(IterateExpressionAccumulator<R, E> v) {
		maybeLoad();
		return this.oclStdLibCollection.iterate(v);
	}

	@Override
	public boolean equals(Object object) {
		maybeLoad();
		return this.oclStdLibCollection.equals(object);
	}

	@Override
	public boolean notEquals(Object object) {
		maybeLoad();
		return this.oclStdLibCollection.notEquals(object);
	}

	@Override
	public Boolean oclIsNew() {
		maybeLoad();
		return this.oclStdLibCollection.oclIsNew();
	}

	@Override
	public Boolean oclIsUndefined() {
		maybeLoad();
		return this.oclStdLibCollection.oclIsUndefined();
	}

	@Override
	public Boolean oclIsInvalid() {
		maybeLoad();
		return this.oclStdLibCollection.oclIsInvalid();
	}

	@Override
	public <T> T oclAsType(T classifier) {
		maybeLoad();
		return this.oclStdLibCollection.oclAsType(classifier);
	}

	@Override
	public Boolean oclIsTypeOf(Object object) {
		maybeLoad();
		return this.oclStdLibCollection.oclIsTypeOf(object);
	}

	@Override
	public Boolean oclIsKindOf(Object object) {
		maybeLoad();
		return this.oclStdLibCollection.oclIsKindOf(object);
	}

	@Override
	public Boolean oclIsInState(OclState state) {
		maybeLoad();
		return this.oclStdLibCollection.oclIsInState(state);
	}

	@Override
	public <T extends Object> Class<T> oclType() {
		maybeLoad();
		return this.oclStdLibCollection.oclType();
	}

	@Override
	public String oclLocale() {
		maybeLoad();
		return this.oclStdLibCollection.oclLocale();
	}

	@Override
	public boolean includes(E t) {
		maybeLoad();
		return this.oclStdLibCollection.includes(t);
	}

	@Override
	public boolean excludes(E t) {
		maybeLoad();
		return this.oclStdLibCollection.excludes(t);
	}

	@Override
	public int count(E e) {
		maybeLoad();
		return this.oclStdLibCollection.count(e);
	}

	@Override
	public Boolean includesAll(TinkerCollection<E> c) {
		maybeLoad();
		return this.oclStdLibCollection.includesAll(c);
	}

	@Override
	public Boolean excludesAll(TinkerCollection<E> c) {
		maybeLoad();
		return this.oclStdLibCollection.excludesAll(c);
	}

	@Override
	public Boolean notEmpty() {
		maybeLoad();
		return this.oclStdLibCollection.notEmpty();
	}

	@Override
	public E max() {
		maybeLoad();
		return this.oclStdLibCollection.max();
	}

	@Override
	public E min() {
		maybeLoad();
		return this.oclStdLibCollection.min();
	}

	@Override
	public E sum() {
		maybeLoad();
		return this.oclStdLibCollection.sum();
	}

	@Override
	public TinkerSet<?> product(TinkerCollection<E> c) {
		maybeLoad();
		return this.oclStdLibCollection.product(c);
	}

	@Override
	public <T2> TinkerCollection<T2> flatten() {
		maybeLoad();
		return this.oclStdLibCollection.flatten();
	}

	@Override
	public TinkerCollection<E> select(BooleanExpressionEvaluator<E> e) {
		maybeLoad();
		return this.oclStdLibCollection.select(e);
	}

	@Override
	public <T, R> TinkerCollection<T> collect(BodyExpressionEvaluator<R, E> e) {
		maybeLoad();
		return this.oclStdLibCollection.collect(e);
	}

	@Override
	public <R> TinkerCollection<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		maybeLoad();
		return this.oclStdLibCollection.collectNested(e);
	}

}

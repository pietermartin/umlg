package org.tuml.runtime.domain.activity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Logger;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.domain.BaseTinkerBehavioredClassifier;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.runtime.domain.activity.interf.IActivityEdge;
import org.tuml.runtime.domain.activity.interf.IActivityNode;
import org.tuml.runtime.util.TinkerUtil;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.AbstractPipe;

public abstract class ActivityNode<IN extends Token, OUT extends Token> extends AbstractPipe<IN, Boolean> implements IActivityNode<IN, OUT> {

	private static final long serialVersionUID = 6087164127026017426L;
	protected Vertex vertex;
	protected NodeStat nodeStat;
	protected static final Logger logger = Logger.getLogger(ActivityNode.class.getName());
	protected boolean hasInitBeenCalled = false;
	
	public ActivityNode() {
		super();
	}

	public ActivityNode(Vertex vertex) {
		super();
		this.vertex = vertex;
		this.nodeStat = new NodeStat(vertex);
	}

	public ActivityNode(boolean persist, String name) {
		super();
		this.vertex = GraphDb.getDb().addVertex(null);
		nodeStat = new NodeStat(vertex, true);
		this.vertex.setProperty("name", name);
	}
	
	@Override
	public abstract boolean mayContinue();
	protected abstract boolean mayAcceptToken();
	protected abstract Boolean executeNode();
	@Override
	public abstract List<? extends IActivityEdge<? extends IN>> getIncoming();
	@Override
	public abstract List<? extends IActivityEdge<OUT>> getOutgoing();
	@Override
	public abstract List<? extends Token> getInTokens();
	@Override
	public abstract List<?> getInTokens(String inFlowName);
	@Override
	public abstract List<?> getOutTokens();
	@Override
	public abstract List<?> getOutTokens(String outFlowName);
	@Override
	public abstract BaseTinkerBehavioredClassifier getContextObject();

	@Override
	public Vertex getVertex() {
		return vertex;
	}

	@Override
	public Boolean processNextStart() throws NoSuchElementException {
		// Persist incoming control tokens
		while (mayAcceptToken() && this.starts.hasNext()) {
			IN token = this.starts.next();
			// This also removes the token from the source
			addIncomingToken(token);
		}
		if (mayContinue()) {
			return executeNode();
		} else {
			return false;
		}
	}

	public NodeStatus getNodeStatus() {
		return (NodeStatus) TinkerUtil.convertEnumFromPersistence(NodeStatus.class, (String) this.vertex.getProperty("nodeStatus"));
	}

	public void setNodeStatus(NodeStatus nodeStatus) {
		this.vertex.setProperty("nodeStatus", TinkerUtil.convertEnumForPersistence(nodeStatus));
	}

	protected void addIncomingToken(IN token) {
		token.removeEdgeFromActivityNode();
		token.addEdgeToActivityNode(this);
	}	
	
	public void addOutgoingToken(OUT token) {
		Edge edge = GraphDb.getDb().addEdge(null, this.vertex, token.getVertex(), Token.TOKEN + token.getEdgeName());
		edge.setProperty("tokenClass", token.getClass().getName());
		edge.setProperty("outClass", this.getClass().getName());
	}

	protected boolean doAllIncomingFlowsHaveTokens() {
		for (IActivityEdge<?> flow : getIncoming()) {
			Iterable<Edge> iter = this.vertex.getEdges(Direction.OUT, Token.TOKEN + flow.getName());
			if (iter.iterator().hasNext()) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public NodeStat getNodeStat() {
		return nodeStat;
	}

	@Override
	public String getName() {
		return (String) this.vertex.getProperty("name");
	}

	public boolean isEnabled() {
		return getNodeStatus() == NodeStatus.ENABLED;
	}

	public boolean isActive() {
		return getNodeStatus() == NodeStatus.ACTIVE;
	}

	public boolean isComplete() {
		return getNodeStatus() == NodeStatus.COMPLETE;
	}

	public boolean isInActive() {
		return getNodeStatus() == NodeStatus.INACTIVE;
	}
	
	public String getUid() {
		String uid = (String) this.vertex.getProperty("uid");
		if ( uid==null || uid.trim().length()==0 ) {
			uid=UUID.randomUUID().toString();
			this.vertex.setProperty("uid", uid);
		}
		return uid;
	}	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append("\n");
		sb.append(this.nodeStat.toString());
		return sb.toString();
	}
	

	//CompositionNode interface
	
//	@Override
//	public boolean hasInitBeenCalled() {
//		return this.hasInitBeenCalled;
//	}
	
//	@Override
//	public void init(TinkerNode owner) {
//		//not used
//	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}

	@Override
	public void initialiseProperties() {
		// TODO Auto-generated method stub
	}

	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}

	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}

	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}

	@Override
	public abstract TumlNode getOwningObject();

  	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}	
	
}

package org.tinker.componenttest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.nakeduml.tinker.runtime.TinkerIdUtil;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.TinkerCompositionNode;
import org.tuml.tinker.runtime.TransactionThreadEntityVar;

public class SpaceTime extends BaseTinker implements TinkerCompositionNode {


	/** Constructor for SpaceTime
	 * 
	 * @param vertex 
	 */
	public SpaceTime(Vertex vertex) {
		this.vertex=vertex;
	}
	
	/** Constructor for SpaceTime
	 * 
	 * @param persistent 
	 */
	public SpaceTime(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}

	@Override
	public void clearCache() {
		this.name = null;
		this.space = null;
		this.time = null;
	}
	
	@Override
	public Long getId() {
		return TinkerIdUtil.getId(this.vertex);
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtil.getVersion(this.vertex);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtil.setId(this.vertex, id);
	}

}
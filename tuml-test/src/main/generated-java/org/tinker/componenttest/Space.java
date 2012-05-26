package org.tinker.componenttest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.nakeduml.tinker.runtime.TinkerIdUtil;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.TinkerCompositionNode;
import org.tuml.tinker.runtime.TransactionThreadEntityVar;

public class Space extends BaseTinker implements TinkerCompositionNode {


	/** Constructor for Space
	 * 
	 * @param vertex 
	 */
	public Space(Vertex vertex) {
		this.vertex=vertex;
	}
	
	/** Constructor for Space
	 * 
	 * @param persistent 
	 */
	public Space(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}

	@Override
	public void clearCache() {
		this.name = null;
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
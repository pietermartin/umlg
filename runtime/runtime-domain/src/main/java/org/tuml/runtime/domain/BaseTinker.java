package org.tuml.runtime.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.tuml.runtime.util.TinkerFormatter;

import com.tinkerpop.blueprints.Vertex;

public abstract class BaseTinker implements TinkerNode, Serializable {

	private static final long serialVersionUID = 3751023772087546585L;
	protected Vertex vertex;
	protected boolean hasInitBeenCalled = false;

	public BaseTinker() {
		super();
	}

	public Vertex getVertex() {
		return vertex;
	}

	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}

	public Date getCreatedOn() {
		return TinkerFormatter.parse((String) this.vertex.getProperty("createdOn"));
	}

	public void setCreatedOn(Date createdOn) {
		this.vertex.setProperty("createdOn", TinkerFormatter.format(createdOn));
	}

	public Date getUpdatedOn() {
		return TinkerFormatter.parse((String) this.vertex.getProperty("updatedOn"));
	}

	public void setUpdatedOn(Date updatedOn) {
		this.vertex.setProperty("updatedOn", TinkerFormatter.format(updatedOn));
	}

	public void defaultCreate() {
		setCreatedOn(new Timestamp(System.currentTimeMillis()));
		setUpdatedOn(new Timestamp(System.currentTimeMillis()));
		getUid();
	}

	public void defaultUpdate() {
		setUpdatedOn(new Timestamp(System.currentTimeMillis()));
	}

	public String getName() {
		return getClass().getName() + "[" + getId() + "]";
	}

	public boolean hasInitBeenCalled() {
		return hasInitBeenCalled;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		BaseTinker rhs = (BaseTinker) obj;
		return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
	}

}

package org.tuml.runtime.domain;

import java.io.Serializable;

public interface PersistentObject extends Serializable {
	Long getId();
	void setId(Long id);
	String getUid();
	int getObjectVersion();
}

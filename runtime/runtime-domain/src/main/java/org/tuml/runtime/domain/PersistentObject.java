package org.tuml.runtime.domain;

import java.io.Serializable;
import java.util.Map;

public interface PersistentObject extends Serializable {
	Long getId();
	void setId(Long id);
	String getUid();
	int getObjectVersion();
	String toJson();
	void fromJson(String json);
	void fromJson(Map<String,Object> propertyMap);
}

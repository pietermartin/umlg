package org.tuml.runtime.domain;

import java.io.Serializable;
import java.util.Map;

public interface PersistentObject extends Serializable {
	Long getId();
//	void setId(String id);
//    void internalSetId();
	String getUid();
//	int getObjectVersion();
    String toJson(Boolean deep);
    String toJsonWithoutCompositeParent(Boolean deep);
	String toJson();
	String toJsonWithoutCompositeParent();
    String getMetaDataAsJson();
	void fromJson(String json);
	void fromJson(Map<String,Object> propertyMap);
}

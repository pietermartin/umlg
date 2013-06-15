package org.umlg.runtime.domain;

import java.io.Serializable;
import java.util.Map;

public interface PersistentObject extends Serializable {
	Long getId();
	String getUid();
    String toJson(Boolean deep);
    String toJsonWithoutCompositeParent(Boolean deep);
	String toJson();
	String toJsonWithoutCompositeParent();
    String getMetaDataAsJson();
	void fromJson(String json);
	void fromJson(Map<String,Object> propertyMap);
    void fromJsonDataTypeAndComposite(Map<String,Object> propertyMap);
    void fromJsonNonCompositeOne(Map<String,Object> propertyMap);

}

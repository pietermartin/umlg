package org.tuml.runtime.domain.json;

import java.util.Collection;

import org.tuml.runtime.domain.PersistentObject;

public class ToJsonUtil {

	public static String toJson(Collection<? extends PersistentObject> entities) {
		StringBuilder json = new StringBuilder();
		if (entities != null) {
			int count = 0;
			for (PersistentObject p : entities) {
				count++;
				json.append(p.toJson());
				if (count != entities.size()) {
					json.append(",");
				}
			}
		}
		return json.toString();
	}

	public static String toJson(PersistentObject entity) {
		StringBuilder json = new StringBuilder();
		json.append(entity.toJson());
		return json.toString();
	}

}

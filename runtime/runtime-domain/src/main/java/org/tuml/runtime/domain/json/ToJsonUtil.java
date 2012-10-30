package org.tuml.runtime.domain.json;

import java.util.Collection;
import java.util.List;

import org.tuml.runtime.domain.PersistentObject;
import org.tuml.runtime.domain.TumlEnum;
import org.tuml.runtime.domain.TumlNode;

public class ToJsonUtil {
	public static String pathToCompositionRootAsJson(List<String> uriList, List<String> nameList, List<TumlNode> entities, String rootName, String rootUri) {
		StringBuilder json = new StringBuilder();
		if (entities != null) {
			int count = 0;
			for (TumlNode p : entities) {
				String uri = uriList.get(count);
				String name = nameList.get(count);
				count++;
				json.append("{");
				json.append("\"name\": ");
				json.append("\"" + name + "\", ");
				json.append("\"uri\": \"");
				json.append(uri.replaceAll("\\{(\\s*?.*?)*?\\}", String.valueOf(p.getId())));
				json.append("\"}");
				if (count != entities.size()) {
					json.append(",");
				}
			}
			json.append(", {");
			json.append("\"name\": ");
			json.append("\"" + rootName + "\", ");
			json.append("\"uri\": \"");
			json.append(rootUri);
			json.append("\"}");
		}
		return json.toString();
	}
	
	public static void main(String[] args) {
		System.out.println("restAndJson/humans/{humanId}".replaceAll("\\{(\\s*?.*?)*?\\}", String.valueOf(5)));
	}

	public static String enumsToJson(Collection<? extends TumlEnum> enums) {
		StringBuilder json = new StringBuilder();
		if (enums != null) {
			int count = 0;
			for (TumlEnum p : enums) {
				count++;
				json.append("\"");
				json.append(p.toJson());
				json.append("\"");
				if (count != enums.size()) {
					json.append(",");
				}
			}
		}
		return json.toString();
	}

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

	public static String toJsonWithoutCompositeParent(Collection<? extends PersistentObject> entities) {
		StringBuilder json = new StringBuilder();
		if (entities != null) {
			int count = 0;
			for (PersistentObject p : entities) {
				count++;
				json.append(p.toJsonWithoutCompositeParent());
				if (count != entities.size()) {
					json.append(",");
				}
			}
		}
		return json.toString();
	}

	public static String toJson(PersistentObject entity) {
		if (entity != null) {
			StringBuilder json = new StringBuilder();
			json.append(entity.toJson());
			return json.toString();
		} else {
			return "";
		}
	}

	public static String toJsonWithoutCompositeParent(PersistentObject entity) {
		if (entity != null) {
			StringBuilder json = new StringBuilder();
			json.append(entity.toJsonWithoutCompositeParent());
			return json.toString();
		} else {
			return "null";
		}
	}
}

package org.tuml.runtime.domain.json;

import org.tuml.runtime.domain.PersistentObject;
import org.tuml.runtime.domain.TumlApplicationNode;
import org.tuml.runtime.domain.TumlEnum;

import java.util.Collection;

public class ToJsonUtil {
	
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

	public static <T> String primitivesToJson(Collection<T> primitives) {
		StringBuilder json = new StringBuilder();
		if (primitives != null) {
			json.append("[");
			int count = 0;
			for (T p : primitives) {
				count++;
				if (p instanceof String) {
					json.append("\"");
				}
				json.append(p.toString());
				if (p instanceof String) {
					json.append("\"");
				}
				if (count != primitives.size()) {
					json.append(",");
				}
			}
			json.append("]");
		} else {
			json.append("null");
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

    public static String toJsonWithoutCompositeParentForQuery(Collection<? extends PersistentObject> entities) {
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

	public static String toJson(TumlApplicationNode entity) {
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

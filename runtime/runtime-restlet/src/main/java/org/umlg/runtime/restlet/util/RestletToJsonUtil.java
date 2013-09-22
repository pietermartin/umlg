package org.umlg.runtime.restlet.util;

import java.util.List;

import org.umlg.runtime.restlet.domain.UmlgRestletNode;

public class RestletToJsonUtil {

	public static String pathToCompositionRootAsJson(List<UmlgRestletNode> entities, String rootName, String rootUri) {
		StringBuilder json = new StringBuilder();
		if (entities != null) {
			int count = 0;
			for (UmlgRestletNode p : entities) {
				count++;
				json.append("{");
				json.append("\"name\": ");
				json.append("\"" + p.getUmlName() + "\", ");
				json.append("\"uri\": \"");
				json.append(p.getUri().replace("\"", "").replaceAll("\\{(\\s*?.*?)*?\\}", String.valueOf(p.getId())));
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

}

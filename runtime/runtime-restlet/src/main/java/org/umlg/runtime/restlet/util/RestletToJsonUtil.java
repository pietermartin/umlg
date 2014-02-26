package org.umlg.runtime.restlet.util;

import org.umlg.runtime.domain.restlet.UmlgRestletNode;

import java.util.List;

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
				json.append(p.getUri().replace("\"", "").replaceAll("\\{(\\s*?.*?)*?\\}", UmlgURLDecoder.encode(p.getId().toString())));
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

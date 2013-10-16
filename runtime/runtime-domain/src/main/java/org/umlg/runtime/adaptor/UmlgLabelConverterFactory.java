package org.umlg.runtime.adaptor;

import org.umlg.runtime.util.UmlgUtil;

import java.lang.reflect.Method;

public class UmlgLabelConverterFactory {

	private static UmlgLabelConverter umlgLabelConverter;

	@SuppressWarnings("unchecked")
	public static UmlgLabelConverter getUmlgLabelConverter() {
		if (umlgLabelConverter == null) {
			try {
                UmlgAdaptorImplementation umlgAdaptorImplementation = UmlgAdaptorImplementation.fromName(UmlgUtil.getBlueprintsImplementation());
				Class factory = Class.forName(umlgAdaptorImplementation.getUmlgLabelConverter());
				Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                UmlgLabelConverter idUtil = (UmlgLabelConverter) m.invoke(null);
				umlgLabelConverter = idUtil;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return umlgLabelConverter;
	}

}

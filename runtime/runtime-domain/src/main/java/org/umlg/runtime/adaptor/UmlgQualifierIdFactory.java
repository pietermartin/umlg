package org.umlg.runtime.adaptor;

import org.umlg.runtime.util.UmlgUtil;

import java.lang.reflect.Method;

public class UmlgQualifierIdFactory {

	private static UmlgQualifierId umlgQualifierId;

	@SuppressWarnings("unchecked")
	public static UmlgQualifierId getUmlgQualifierId() {
		if (umlgQualifierId == null) {
			try {
                UmlgAdaptorImplementation umlgAdaptorImplementation = UmlgAdaptorImplementation.fromName(UmlgUtil.getBlueprintsImplementation());
				Class factory = Class.forName(umlgAdaptorImplementation.getUmlgQualifierId());
				Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                UmlgQualifierId idUtil = (UmlgQualifierId) m.invoke(null);
                umlgQualifierId = idUtil;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return umlgQualifierId;
	}

}

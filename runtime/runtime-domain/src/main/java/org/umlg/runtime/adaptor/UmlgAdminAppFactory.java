package org.umlg.runtime.adaptor;

import org.umlg.runtime.util.UmlgProperties;

import java.lang.reflect.Method;

public class UmlgAdminAppFactory {

	private static UmlgAdminApp umlgAdminApp;

	@SuppressWarnings("unchecked")
	public static UmlgAdminApp getUmlgAdminApp() {
		if (umlgAdminApp == null) {
			try {
                UmlgAdaptorImplementation umlgAdaptorImplementation = UmlgAdaptorImplementation.fromName(UmlgProperties.INSTANCE.getTinkerImplementation());
				Class factory = Class.forName(umlgAdaptorImplementation.getUmlgAdminApp());
				Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                umlgAdminApp = (UmlgAdminApp) m.invoke(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return umlgAdminApp;
	}

}

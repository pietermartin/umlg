package org.umlg.runtime.adaptor;

import org.umlg.runtime.util.UmlgAdaptorImplementation;
import org.umlg.runtime.util.UmlgProperties;

import java.lang.reflect.Method;

public class TinkerIdUtilFactory {

	private static TinkerIdUtil tinkerIdUtil;

	@SuppressWarnings("unchecked")
	public static TinkerIdUtil getIdUtil() {
		if (tinkerIdUtil == null) {
			try {
                UmlgAdaptorImplementation umlgAdaptorImplementation = UmlgAdaptorImplementation.fromName(UmlgProperties.INSTANCE.getTinkerImplementation());
				Class<TinkerIdUtil> factory = (Class<TinkerIdUtil>) Class.forName(umlgAdaptorImplementation.getTumlIdUtil());
				Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
				TinkerIdUtil idUtil = (TinkerIdUtil) m.invoke(null);
				tinkerIdUtil = idUtil;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return tinkerIdUtil;
	}

}

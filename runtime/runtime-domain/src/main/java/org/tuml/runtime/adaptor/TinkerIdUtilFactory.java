package org.tuml.runtime.adaptor;

import java.lang.reflect.Method;
import java.util.Properties;

public class TinkerIdUtilFactory {

	private static TinkerIdUtil tinkerIdUtil;

	@SuppressWarnings("unchecked")
	public static TinkerIdUtil getIdUtil() {
		if (tinkerIdUtil == null) {
			Properties p = new Properties();
			try {
				p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("tuml.env.properties"));
				Class<TinkerIdUtil> factory = (Class<TinkerIdUtil>) Class.forName(p.getProperty("tuml.tinkeridutil"));
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

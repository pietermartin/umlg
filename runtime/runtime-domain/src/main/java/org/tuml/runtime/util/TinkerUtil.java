package org.tuml.runtime.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TinkerUtil {

	public static String[] convertEnumsForPersistence(Collection<? extends Enum<?>> multiEmbeddedReason) {
		Collection<String> persistentCollection;
		if (multiEmbeddedReason instanceof Set) {
			persistentCollection = new HashSet<String>();
		} else {
			persistentCollection = new ArrayList<String>();
		}
		for (Enum<?> e : multiEmbeddedReason) {
			persistentCollection.add(e.toString());
		}
		return persistentCollection.toArray(new String[]{});
	}

	public static String convertEnumForPersistence(Enum<?> embeddedEnum) {
		return embeddedEnum.toString();
	}

	@SuppressWarnings("unchecked")
	public static Enum<?> convertEnumFromPersistence(Class<? extends Enum> embeddedEnum, String value) {
		return Enum.valueOf(embeddedEnum, value);
	}
	
	public static Collection<?> convertEnumsFromPersistence(Object multiEmbeddedReason, Class<? extends Enum> e, boolean isOrdered) {
		if (multiEmbeddedReason != null) {
			Collection<Enum> persistentCollection;
			if (!isOrdered) {
				persistentCollection = new HashSet<Enum>();
			} else {
				persistentCollection = new ArrayList<Enum>();
			}
			Collection<String> enums;
			if (multiEmbeddedReason instanceof List) {
				enums = (List<String>)multiEmbeddedReason;
			} else {
				enums = Arrays.asList((String[])multiEmbeddedReason);
			}
			for (String s : enums) {
				persistentCollection.add(Enum.valueOf(e, s));
			}
			return persistentCollection;
		} else {
			return isOrdered ? new ArrayList(): new HashSet();
		}
	}
}

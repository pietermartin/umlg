package org.umlg.runtime.util;

import java.util.*;

public class UmlgUtil {

    public static String getBlueprintsImplementation() {
        String poweredBy = "";
        try {
            Class.forName("org.umlg.runtime.adaptor.UmlgNeo4jGraph");
            poweredBy = "Neo4j";
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("org.umlg.runtime.adaptor.UmlgBitsyGraph");
                poweredBy = "Bitsy";
            } catch (ClassNotFoundException ee) {
                try {
                    Class.forName("org.umlg.runtime.adaptor.UmlgTitanGraph");
                    poweredBy = "Titan";
                } catch (ClassNotFoundException eee) {
                    try {
                        Class.forName("org.umlg.runtime.adaptor.UmlgOrientDbGraph");
                        poweredBy = "OrientDb";
                    } catch (ClassNotFoundException eeee) {
                    }
                }
            }
        }
        return poweredBy;
    }

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
                enums = Arrays.asList((String[]) multiEmbeddedReason);
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

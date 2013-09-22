package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.PersistentObject;

/**
 * Date: 2013/09/22
 * Time: 6:32 PM
 */
public class UmlgDefaultQualifierId implements UmlgQualifierId {

    private static UmlgDefaultQualifierId INSTANCE = new UmlgDefaultQualifierId();

    private UmlgDefaultQualifierId() {
        super();
    }

    public static UmlgDefaultQualifierId getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId(PersistentObject persistentObject) {
        return persistentObject.getId().toString();
    }

}

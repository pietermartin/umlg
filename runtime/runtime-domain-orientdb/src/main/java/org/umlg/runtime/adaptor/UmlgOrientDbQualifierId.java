package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.PersistentObject;

/**
 * Date: 2013/09/22
 * Time: 6:32 PM
 */
public class UmlgOrientDbQualifierId implements UmlgQualifierId {

    private static UmlgOrientDbQualifierId INSTANCE = new UmlgOrientDbQualifierId();

    private UmlgOrientDbQualifierId() {
        super();
    }

    public static UmlgOrientDbQualifierId getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId(PersistentObject persistentObject) {
        return persistentObject.getUid();
    }

}

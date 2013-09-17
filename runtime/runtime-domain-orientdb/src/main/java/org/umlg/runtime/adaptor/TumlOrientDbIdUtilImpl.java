package org.umlg.runtime.adaptor;

import com.orientechnologies.orient.core.id.ORecordId;
import com.tinkerpop.blueprints.Vertex;

public class TumlOrientDbIdUtilImpl implements TinkerIdUtil {

    private static TinkerIdUtil INSTANCE = new TumlOrientDbIdUtilImpl();

    private TumlOrientDbIdUtilImpl() {
        super();
    }

    public static TinkerIdUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId(Vertex v) {
        ORecordId oRecordId = (ORecordId)v.getId();
        return oRecordId.toString();
    }

}

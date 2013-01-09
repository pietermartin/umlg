package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.Vertex;

public class TumlOrientDbIdUtilImpl implements TinkerIdUtil {

    private static TinkerIdUtil INSTANCE = new TumlOrientDbIdUtilImpl();
    private static final String ORIENT_DB_ID = "orientdbid";

    private TumlOrientDbIdUtilImpl() {
        super();
    }

    public static TinkerIdUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public Long getId(Vertex v) {
        String id = (String)v.getId();
        return Long.valueOf(id.substring(id.indexOf(":")));
    }

    @Override
    public void setId(Vertex v, Long id) {
        throw new IllegalStateException("Id can not be set using OrientDb");
    }

    @Override
    public int getVersion(Vertex v) {
        return -1;
    }

}

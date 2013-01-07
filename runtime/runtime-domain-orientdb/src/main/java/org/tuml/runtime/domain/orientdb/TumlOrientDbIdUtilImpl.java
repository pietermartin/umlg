package org.tuml.runtime.domain.orientdb;

import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtil;

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
        Object id = v.getProperty(ORIENT_DB_ID);
        if (id == null) {
            return ((TumlOrientDbGraph)GraphDb.getDb()).getNextVertexId();
        } else {
            return (Long) id;
        }
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

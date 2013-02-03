package org.tuml.runtime.restlet;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TumlTransactionManager;
import org.tuml.runtime.adaptor.TransactionIdentifier;

import java.io.IOException;
import java.util.Map;

/**
 * Date: 2012/12/26
 * Time: 6:01 PM
 */
public class TumlTransactionServerResourceImpl extends ServerResource implements TumlTransactionServerResource {

    @Override
    public Representation delete(Representation entity) {
        String uid = (String) getRequestAttributes().get("transactionId");
        try {
            String commitValue = entity.getText();
            TransactionIdentifier transactionIdentifier = TumlTransactionManager.INSTANCE.get(uid);
            GraphDb.getDb().resume(transactionIdentifier);
            GraphDb.getDb().rollback();
            return new StringRepresentation("rolled back");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This creates a transaction and suspends it immediatly
     * @param entity
     * @return
     */
    @Override
    public Representation post(Representation entity) {
        TransactionIdentifier transactionIdentifier = GraphDb.getDb().suspend();
        TumlTransactionManager.INSTANCE.put(transactionIdentifier);
        return new StringRepresentation(transactionIdentifier.toString());
    }

    @Override
    public Representation put(Representation entity) {
        String uid = (String) getRequestAttributes().get("transactionId");
        try {
            String commitValue = entity.getText();
            TransactionIdentifier transactionIdentifier = TumlTransactionManager.INSTANCE.get(uid);
            GraphDb.getDb().resume(transactionIdentifier);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Boolean> commitValueAsMap = objectMapper.readValue(commitValue, Map.class);
            if (commitValueAsMap.get(TumlTransactionServerResource.COMMIT)) {
                GraphDb.getDb().commit();
                return new StringRepresentation(TumlTransactionServerResource.COMMITTED);
            } else {
                GraphDb.getDb().rollback();
                return new StringRepresentation(TumlTransactionServerResource.ROLLED_BACK);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

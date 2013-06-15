package org.umlg.runtime.adaptor;

import java.util.List;

import com.orientechnologies.orient.core.db.ODatabase;
import org.joda.time.DateTime;
import org.umlg.runtime.domain.BaseTinkerAuditable;
import org.umlg.runtime.domain.TumlNode;
import org.umlg.runtime.validation.TumlConstraintViolation;
import org.umlg.runtime.validation.TumlConstraintViolationException;

import com.orientechnologies.orient.core.db.ODatabaseListener;

public class TumlOrientDbTransactionEventHandler<T> implements ODatabaseListener {


    public TumlOrientDbTransactionEventHandler() {
        super();
    }

    @Override
    public void onCreate(ODatabase iDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDelete(ODatabase iDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onOpen(ODatabase iDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onBeforeTxBegin(ODatabase iDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onBeforeTxRollback(ODatabase iDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAfterTxRollback(ODatabase iDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onBeforeTxCommit(ODatabase iDatabase) {
        try {
            if (!isEmpty(iDatabase) && GraphDb.getDb() != null) {
                TransactionThreadVar.clear();
                GraphDb.incrementTransactionCount();
                List<TumlNode> entities = TransactionThreadEntityVar.get();
                for (TumlNode entity : entities) {
                    TumlNode tumlNode = entity;
                    List<TumlConstraintViolation> requiredConstraintViolations = tumlNode.validateMultiplicities();
                    if (!requiredConstraintViolations.isEmpty()) {
                        throw new TumlConstraintViolationException(requiredConstraintViolations);
                    }
                    if (!entity.isTinkerRoot() && entity.getOwningObject() == null) {
                        if (entity instanceof BaseTinkerAuditable && ((BaseTinkerAuditable) entity).getDeletedOn().isBefore(new DateTime())) {
                            return;
                        }
                        throw new IllegalStateException(String.format("Entity %s %s does not have a composite owner", entity.getClass().getSimpleName(), entity.getId()));
                    }
                }
            }
        } finally {
            TransactionThreadEntityVar.remove();
        }
    }

    private boolean isEmpty(ODatabase data) {
//        return !data.assignedNodeProperties().iterator().hasNext() && !data.assignedRelationshipProperties().iterator().hasNext() && !data.createdNodes().iterator().hasNext()
//                && !data.createdRelationships().iterator().hasNext() && !data.deletedNodes().iterator().hasNext() && !data.deletedRelationships().iterator().hasNext()
//                && !data.removedNodeProperties().iterator().hasNext() && !data.removedRelationshipProperties().iterator().hasNext();
        return false;
    }

    @Override
    public void onAfterTxCommit(ODatabase iDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onClose(ODatabase iDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onCorruptionRepairDatabase(ODatabase iDatabase, String iReason, String iWhatWillbeFixed) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

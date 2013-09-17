package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.CompositionNode;
import org.umlg.runtime.domain.TumlNode;
import org.umlg.runtime.validation.TumlConstraintViolation;
import org.umlg.runtime.validation.TumlConstraintViolationException;

import java.util.List;

public class UmlgTransactionEventHandlerImpl implements UmlgTransactionEventHandler {


    public UmlgTransactionEventHandlerImpl() {
        super();
    }

    @Override
    public void beforeCommit() {
        try {
            if (GraphDb.getDb() != null) {
                TransactionThreadVar.clear();
                GraphDb.incrementTransactionCount();
                List<TumlNode> entities = TransactionThreadEntityVar.get();
                for (TumlNode tumlNode : entities) {
                    List<TumlConstraintViolation> requiredConstraintViolations = tumlNode.validateMultiplicities();
                    requiredConstraintViolations.addAll(tumlNode.checkClassConstraints());
                    if (!requiredConstraintViolations.isEmpty()) {
                        throw new TumlConstraintViolationException(requiredConstraintViolations);
                    }

                    if (!tumlNode.isTinkerRoot() && tumlNode instanceof CompositionNode && ((CompositionNode) tumlNode).getOwningObject() == null) {
//                            if (entity instanceof BaseTinkerAuditable && ((BaseTinkerAuditable) entity).getDeletedOn().isBefore(new DateTime())) {
//                                return null;
//                            }
                        throw new IllegalStateException(String.format("Entity %s %s does not have a composite owner", tumlNode.getClass().getSimpleName(), tumlNode.getId()));
                    }
                }
            }
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
        }
    }

}

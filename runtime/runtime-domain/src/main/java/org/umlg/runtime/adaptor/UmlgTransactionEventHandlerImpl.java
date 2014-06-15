package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.validation.UmlgConstraintViolation;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

import java.util.List;

/**
 * This class validates what is being committed.
 * On every UmlgNode is calls validateMultiplicities.
 * futher it validates that a non root class has one and only one composite parent
 *
 *
 */
public class UmlgTransactionEventHandlerImpl implements UmlgTransactionEventHandler {

    //This is needed in Neo4j when starting up the graph.
    //In Particular when creating indexes and doing schema updates the transaction count should not be updated as updates
    // are not allowed when doing schema updates.
    private boolean bypass = false;

    public UmlgTransactionEventHandlerImpl() {
        super();
    }

    @Override
    public void beforeCommit() {
        try {
            if (!this.bypass && UMLG.get() != null) {
                TransactionThreadVar.clear();
                ((UmlgAdminGraph) UMLG.get()).incrementTransactionCount();
                List<UmlgNode> entities = TransactionThreadEntityVar.get();
                for (UmlgNode umlgNode : entities) {
                    List<UmlgConstraintViolation> requiredConstraintViolations = umlgNode.validateMultiplicities();
                    requiredConstraintViolations.addAll(umlgNode.checkClassConstraints());
                    if (!requiredConstraintViolations.isEmpty()) {
                        throw new UmlgConstraintViolationException(requiredConstraintViolations);
                    }
                    if (!umlgNode.isTinkerRoot() && (!umlgNode.hasOnlyOneCompositeParent() || umlgNode.getOwningObject() == null)) {
                        throw new IllegalStateException(String.format("Entity %s %s does not have a composite owner", umlgNode.getClass().getSimpleName(), umlgNode.getId()));
                    }
                    umlgNode.doBeforeCommit();
                }
            }
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
        }
    }

    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }

}

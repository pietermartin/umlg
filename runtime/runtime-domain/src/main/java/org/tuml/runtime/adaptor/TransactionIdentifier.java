package org.tuml.runtime.adaptor;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * Date: 2013/02/01
 * Time: 5:52 PM
 */
public final class TransactionIdentifier {

    private String uid;

    public TransactionIdentifier() {
        uid = UUID.randomUUID().toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.uid).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        TransactionIdentifier rhs = (TransactionIdentifier) obj;
        return new EqualsBuilder().append(this.uid, rhs.uid).isEquals();
    }

    @Override
    public String toString() {
        return this.uid;
    }

}

package org.umlg.quickpreview.constraint;

import org.junit.Test;
import org.umlg.constraint.ATMTransaction;
import org.umlg.constraint.Friend;
import org.umlg.quickpreview.BaseTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2014/05/09
 * Time: 7:03 PM
 */
public class TestConstraint extends BaseTest {

    @Test(expected = UmlgConstraintViolationException.class)
    public void testConstraintValueFails() {
        ATMTransaction atmTransaction = new ATMTransaction();
        atmTransaction.setValue(-1);
        db.commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testConstraintNotSelfFail() {
        Friend john = new Friend();
        john.addToKnows(john);
        db.commit();
    }

}

package org.umlg.tinker.constraint;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.constraints.ConstraintRoot;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2013/03/10
 * Time: 2:05 PM
 */
public class ConstrainedClassTest extends BaseLocalDbTest {

    @Test
    public void testClassConstraintFail() {
        boolean exceptionHappened = false;
        try {
            ConstraintRoot constraintRoot = new ConstraintRoot(true);
            constraintRoot.setName("constraintRootX");
            db.commit();
        } catch (Exception e) {
            exceptionHappened = true;
            Assert.assertTrue("excepting UmlgConstraintViolationException", e instanceof UmlgConstraintViolationException);
        }
        Assert.assertTrue(exceptionHappened);
    }

    @Test
    public void testClassConstraintPass() {
        ConstraintRoot constraintRoot = new ConstraintRoot(true);
        constraintRoot.setName("constraintRoot");
        db.commit();
    }

}

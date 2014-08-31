package org.umlg.tests.ocl.ocloperator;

import org.junit.Test;
import org.umlg.ocl.ocloperator.OclExists1;
import org.umlg.ocl.ocloperator.OclExists2;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2014/08/26
 * Time: 3:25 PM
 */
public class OclExistsTest extends BaseLocalDbTest {

    @Test(expected = UmlgConstraintViolationException.class)
    public void testExistsFail() {
        OclExists1 oclExists1 = new OclExists1();
        oclExists1.setName("oclExists1");

        OclExists2 oclExists2_1 = new OclExists2();
        oclExists2_1.setName("oclExists2_1");
        oclExists1.addToOclExists2(oclExists2_1);

        OclExists2 oclExists2_2 = new OclExists2();
        oclExists2_2.setName("oclExists2_2");
        oclExists1.addToOclExists2(oclExists2_2);

        OclExists2 oclExists2_3 = new OclExists2();
        oclExists2_3.setName("oclExists2_3");
        oclExists1.addToOclExists2(oclExists2_3);

        UMLG.get().commit();
    }

    @Test
    public void testExistsPass() {
        OclExists1 oclExists1 = new OclExists1();
        oclExists1.setName("oclExists1");

        OclExists2 oclExists2_1 = new OclExists2();
        oclExists2_1.setName("oclExists2_1");
        oclExists1.addToOclExists2(oclExists2_1);

        OclExists2 oclExists2_2 = new OclExists2();
        oclExists2_2.setName("oclExists2_2");
        oclExists1.addToOclExists2(oclExists2_2);

        OclExists2 oclExists2_3 = new OclExists2();
        oclExists2_3.setName("joe");
        oclExists1.addToOclExists2(oclExists2_3);

        UMLG.get().commit();
    }

}

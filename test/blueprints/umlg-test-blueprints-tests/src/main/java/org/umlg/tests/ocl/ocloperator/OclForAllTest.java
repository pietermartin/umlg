package org.umlg.tests.ocl.ocloperator;

import org.junit.Test;
import org.umlg.ocl.ocloperator.ForAll1;
import org.umlg.ocl.ocloperator.ForAll2;
import org.umlg.ocl.ocloperator.OclExists1;
import org.umlg.ocl.ocloperator.OclExists2;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2014/08/26
 * Time: 3:25 PM
 */
public class OclForAllTest extends BaseLocalDbTest {

    @Test(expected = UmlgConstraintViolationException.class)
    public void testExistsFail() {
        ForAll1 forAll1 = new ForAll1();
        forAll1.setName("forAll1");

        ForAll2 forAll2_1 = new ForAll2(forAll1);
        forAll2_1.setName("joe");
        ForAll2 forAll2_2 = new ForAll2(forAll1);
        forAll2_2.setName("joe");
        ForAll2 forAll2_3 = new ForAll2(forAll1);
        forAll2_3.setName("joeFAIL");

        UMLG.get().commit();
    }

    @Test
    public void testExistsPass() {
        ForAll1 forAll1 = new ForAll1();
        forAll1.setName("forAll1");

        ForAll2 forAll2_1 = new ForAll2(forAll1);
        forAll2_1.setName("joe");
        ForAll2 forAll2_2 = new ForAll2(forAll1);
        forAll2_2.setName("joe");
        ForAll2 forAll2_3 = new ForAll2(forAll1);
        forAll2_3.setName("joe");

        UMLG.get().commit();
    }

}

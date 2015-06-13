package org.umlg.tests.ocl.ocloperator;

import org.junit.Test;
import org.umlg.ocl.ocloperator.IncludesAll1;
import org.umlg.ocl.ocloperator.IncludesAll2;
import org.umlg.ocl.ocloperator.testimport.IncludesAll3;
import org.umlg.ocl.ocloperator.testimport.IncludesAll4;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2014/08/07
 * Time: 5:25 PM
 */
public class OclIncludesAllTest extends BaseLocalDbTest {

    @Test
    public void testOclIncludesAllPass() {
        IncludesAll1 includesAll1 = new IncludesAll1();
        IncludesAll2 includesAll21 = new IncludesAll2();
        IncludesAll2 includesAll22 = new IncludesAll2();
        IncludesAll3 includesAll31 = new IncludesAll3();
        IncludesAll3 includesAll32 = new IncludesAll3();
        IncludesAll3 includesAll33 = new IncludesAll3();
        IncludesAll4 includesAll41 = new IncludesAll4();
        IncludesAll4 includesAll42 = new IncludesAll4();

        includesAll1.addToIncludesAll2(includesAll21);
        includesAll1.addToIncludesAll2(includesAll22);

        includesAll21.addToIncludesAll3(includesAll31);
        includesAll21.addToIncludesAll3(includesAll32);
        includesAll21.addToIncludesAll3(includesAll33);
        includesAll22.addToIncludesAll3(includesAll31);
        includesAll22.addToIncludesAll3(includesAll32);
        includesAll22.addToIncludesAll3(includesAll33);

        includesAll31.addToIncludesAll4(includesAll41);
        includesAll31.addToIncludesAll4(includesAll42);
        includesAll32.addToIncludesAll4(includesAll41);
        includesAll33.addToIncludesAll4(includesAll42);
        includesAll33.addToIncludesAll4(includesAll41);
        includesAll32.addToIncludesAll4(includesAll42);

        includesAll1.addToIncludesAll4(includesAll41);
        includesAll1.addToIncludesAll4(includesAll42);

        UMLG.get().commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testOclIncludesAllFail() {
        IncludesAll1 includesAll1 = new IncludesAll1();
        IncludesAll2 includesAll21 = new IncludesAll2();
        IncludesAll2 includesAll22 = new IncludesAll2();
        IncludesAll3 includesAll31 = new IncludesAll3();
        IncludesAll3 includesAll32 = new IncludesAll3();
        IncludesAll3 includesAll33 = new IncludesAll3();
        IncludesAll4 includesAll41 = new IncludesAll4();
        IncludesAll4 includesAll42 = new IncludesAll4();

        includesAll1.addToIncludesAll2(includesAll21);
        includesAll1.addToIncludesAll2(includesAll22);

        includesAll21.addToIncludesAll3(includesAll31);
        includesAll21.addToIncludesAll3(includesAll32);
        includesAll21.addToIncludesAll3(includesAll33);
        includesAll22.addToIncludesAll3(includesAll31);
        includesAll22.addToIncludesAll3(includesAll32);
        includesAll22.addToIncludesAll3(includesAll33);

        includesAll31.addToIncludesAll4(includesAll41);
        includesAll31.addToIncludesAll4(includesAll42);
        includesAll32.addToIncludesAll4(includesAll41);
        includesAll33.addToIncludesAll4(includesAll42);
        includesAll33.addToIncludesAll4(includesAll41);
        includesAll32.addToIncludesAll4(includesAll42);

        includesAll1.addToIncludesAll4(includesAll41);

        UMLG.get().commit();
    }
}

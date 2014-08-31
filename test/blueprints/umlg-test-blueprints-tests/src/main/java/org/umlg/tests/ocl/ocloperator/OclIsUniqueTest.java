package org.umlg.tests.ocl.ocloperator;

import org.junit.Test;
import org.umlg.ocl.ocloperator.OclIsUnique1;
import org.umlg.ocl.ocloperator.OclIsUnique2;
import org.umlg.ocl.ocloperator.OclIsUnique3;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2014/08/22
 * Time: 9:54 PM
 */
public class OclIsUniqueTest extends BaseLocalDbTest {

    @Test(expected = UmlgConstraintViolationException.class)
    public void testIsUniqueFail() {
        OclIsUnique1 oclIsUnique1 = new OclIsUnique1();
        oclIsUnique1.setName("oclIsUnique1");

        OclIsUnique2 oclIsUnique2 = new OclIsUnique2(oclIsUnique1);
        oclIsUnique2.setName("oclIsUnique2");
        OclIsUnique3 oclIsUnique3 = new OclIsUnique3();
        oclIsUnique3.setName("oclIsUnique3");
        oclIsUnique2.addToOclIsUnique3(oclIsUnique3);

        OclIsUnique2 oclIsUnique2_2 = new OclIsUnique2(oclIsUnique1);
        oclIsUnique2_2.setName("oclIsUnique2_2");
        oclIsUnique2_2.addToOclIsUnique3(oclIsUnique3);

        UMLG.get().commit();
    }

    @Test
    public void testIsUniquePass() {
        OclIsUnique1 oclIsUnique1 = new OclIsUnique1();
        oclIsUnique1.setName("oclIsUnique1");

        OclIsUnique2 oclIsUnique2 = new OclIsUnique2(oclIsUnique1);
        oclIsUnique2.setName("oclIsUnique2");
        OclIsUnique3 oclIsUnique3 = new OclIsUnique3();
        oclIsUnique3.setName("oclIsUnique3");
        oclIsUnique2.addToOclIsUnique3(oclIsUnique3);

        OclIsUnique2 oclIsUnique2_2 = new OclIsUnique2(oclIsUnique1);
        oclIsUnique2_2.setName("oclIsUnique2_2");
        OclIsUnique3 oclIsUnique3_2 = new OclIsUnique3();
        oclIsUnique3_2.setName("oclIsUnique3_2");
        oclIsUnique2_2.addToOclIsUnique3(oclIsUnique3_2);

        UMLG.get().commit();
    }


}

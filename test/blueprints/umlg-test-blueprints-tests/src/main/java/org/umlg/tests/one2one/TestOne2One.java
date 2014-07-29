package org.umlg.tests.one2one;

import org.junit.Test;
import org.umlg.onetoone.One1;
import org.umlg.onetoone.One2;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/07/29
 * Time: 12:52 PM
 */
public class TestOne2One extends BaseLocalDbTest {

    @Test
    public void testOne2One() {
        One1 one1 = new One1();
        one1.setName("one1");
        One2 one2 = new One2(one1);
        one2.setName("one2");
        UMLG.get().commit();
    }

}

package org.umlg.tests.optional;

import org.junit.Test;
import org.umlg.optional.AOptional;
import org.umlg.optional.BOptional;
import org.umlg.optional.COptional;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2016/05/24
 * Time: 11:36 AM
 */
public class TestOptional extends BaseLocalDbTest {

    @Test
    public void testOptional1() {
        AOptional a = new AOptional();
        a.setName("aOptional1");
        BOptional bOptional = new BOptional();
        bOptional.setName("bOptional1");
        a.addToBOptional(bOptional);
        COptional cOptional = new COptional();

        bOptional.addToCOptional(cOptional);
        UMLG.get().commit();
        a.reload();

        System.out.println(a.getName());

        for (BOptional b: a.getBOptional()) {

            System.out.println(b.getName());

            for (COptional c : b.getCOptional()) {

                System.out.println(c);

            }

        }
    }
}

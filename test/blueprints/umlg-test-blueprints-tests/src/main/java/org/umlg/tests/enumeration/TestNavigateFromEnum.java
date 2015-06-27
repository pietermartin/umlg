package org.umlg.tests.enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.embeddedtest.REASON;
import org.umlg.enumeration.Gender;
import org.umlg.enumeration.Human;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2015/06/26
 * Time: 6:12 PM
 */
public class TestNavigateFromEnum extends BaseLocalDbTest {

    @Test
    public void testNavFromEnum() {
        God g = new God();
        g.setName("THEGOD");
        g.addToREASON(REASON.GOOD);
        g.addToREASON(REASON.BAD);
        UMLG.get().commit();
        Assert.assertEquals(2, g.getREASON().size());
        Assert.assertEquals(1, REASON.GOOD.getGod().size());
        Assert.assertEquals(1, REASON.BAD.getGod().size());

        Human human = new Human();
        human.setName("human1");
        human.setGender(Gender.FEMALE);
        UMLG.get().commit();
        Assert.assertEquals(1, Gender.FEMALE.getHuman().size());
        Assert.assertEquals(human, Gender.FEMALE.getHuman().any(a -> true));
    }
}

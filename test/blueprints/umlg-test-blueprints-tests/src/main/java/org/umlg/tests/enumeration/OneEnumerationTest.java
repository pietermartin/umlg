package org.umlg.tests.enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.enumeration.Gender;
import org.umlg.enumeration.Human;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/06/08
 * Time: 4:09 PM
 */
public class OneEnumerationTest extends BaseLocalDbTest {

    @Test
    public void testOneEnumeration() {
        Human human1 = new Human();
        human1.setGender(Gender.MALE);
        db.commit();

        human1.reload();
        Assert.assertEquals(Gender.MALE, human1.getGender());

        human1.setGender(Gender.FEMALE);
        db.commit();

        human1.reload();
        Assert.assertEquals(Gender.FEMALE, human1.getGender());
    }
}

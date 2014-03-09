package org.umlg.ocl.test;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.oclassociationrole.Many;
import org.umlg.oclassociationrole.One;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/03/08
 * Time: 8:03 PM
 */
public class TestOclAssociationRole extends BaseLocalDbTest {

    @Test
    public void testAssociationRole() {
        One one = new One();
        one.setName("one");
        Many many1 = new Many();
        many1.setName("many1");
        one.addToMany(many1);
        Many many2 = new Many();
        many2.setName("many2");
        one.addToMany(many2);
        Many many3 = new Many();
        many3.setName("many3");
        one.addToMany(many3);
        db.commit();

        one.reload();
        Assert.assertEquals(3, one.getMany().size());


    }
}

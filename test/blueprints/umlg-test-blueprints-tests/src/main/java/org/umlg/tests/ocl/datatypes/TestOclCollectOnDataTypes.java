package org.umlg.tests.ocl.datatypes;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.datatype.OclDataType1;
import org.umlg.ocl.datatype.OclDataType2;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/08/06
 * Time: 4:13 AM
 */
public class TestOclCollectOnDataTypes extends BaseLocalDbTest {

    @Test
    public void testCollectOnDataTypes() {

        OclDataType1 oclDataType1 = new OclDataType1();
        OclDataType2 oclDataType2 = new OclDataType2(oclDataType1);
        oclDataType2.setEmail("this1@that.com");
        OclDataType2 oclDataType3 = new OclDataType2(oclDataType1);
        oclDataType3.setEmail("this2@that.com");
        OclDataType2 oclDataType4 = new OclDataType2(oclDataType1);
        oclDataType4.setEmail("this3@that.com");
        UMLG.get().commit();

        oclDataType1.reload();
        Assert.assertEquals(3, oclDataType1.getOclDataType2Emails().size());
        Assert.assertTrue(oclDataType1.getOclDataType2Emails().contains("this1@that.com"));
        Assert.assertTrue(oclDataType1.getOclDataType2Emails().contains("this2@that.com"));
        Assert.assertTrue(oclDataType1.getOclDataType2Emails().contains("this3@that.com"));

    }
}

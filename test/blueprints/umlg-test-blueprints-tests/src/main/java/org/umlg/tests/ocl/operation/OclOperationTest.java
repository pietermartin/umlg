package org.umlg.tests.ocl.operation;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.operation.OclOperationA;
import org.umlg.ocl.operation.ocloperationtest.OclOperationEnum;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/08/30
 * Time: 2:07 PM
 */
public class OclOperationTest extends BaseLocalDbTest {

    @Test
    public void testOclOperationParamNamespace() {
        OclOperationA oclOperationA = new OclOperationA();
        oclOperationA.setOclOperationEnum(OclOperationEnum.TEST1);
        Assert.assertEquals("TEST1", oclOperationA.getStringForOclOperationEnum(OclOperationEnum.TEST1));
    }
}

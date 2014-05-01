package org.umlg.ocl.meta;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifier.Bank;
import org.umlg.qualifier.Employee;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/03/09
 * Time: 9:33 AM
 */
public class TestMetaNode extends BaseLocalDbTest {

    @Test
    public void testMetaNode1() {
        Bank bank = new Bank(true);
        bank.setName("thebank");
        Employee employee1 = new Employee(true);
        employee1.setName("employee1");
        bank.addToEmployee(employee1);
        Employee employee2 = new Employee(true);
        employee2.setName("employee2");
        bank.addToEmployee(employee2);
        Employee employee3 = new Employee(true);
        employee3.setName("employee3");
        bank.addToEmployee(employee3);
        db.commit();
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
    }
}

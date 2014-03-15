package org.umlg.ocl.test;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.runtime.domain.ocl.OclIsInvalidException;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.testocl.OclTest1;
import org.umlg.testocl.OclTestCollection;
import org.umlg.testocl.OclTestCollection2;

public class TestOcl extends BaseLocalDbTest {

	@Test
	public void testOcl1() {
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");

		OclTestCollection oclTestCollection1 = new OclTestCollection(oclTest1);
		oclTestCollection1.setName("oclTestCollection1");
		OclTestCollection oclTestCollection2 = new OclTestCollection(oclTest1);
		oclTestCollection2.setName("oclTestCollection2");
		OclTestCollection oclTestCollectionJohn = new OclTestCollection(oclTest1);
		oclTestCollectionJohn.setName("john");

		OclTestCollection2 oclTestCollection2_1_1 = new OclTestCollection2(oclTestCollection1);
		oclTestCollection2_1_1.setName("1_1");
		OclTestCollection2 oclTestCollection2_1_2 = new OclTestCollection2(oclTestCollection1);
		oclTestCollection2_1_2.setName("1_2");
		OclTestCollection2 oclTestCollection2_1_3 = new OclTestCollection2(oclTestCollection1);
		oclTestCollection2_1_3.setName("1_3");

		OclTestCollection2 oclTestCollection2_2_1 = new OclTestCollection2(oclTestCollection2);
		oclTestCollection2_2_1.setName("2_1");
		OclTestCollection2 oclTestCollection2_2_2 = new OclTestCollection2(oclTestCollection2);
		oclTestCollection2_2_2.setName("2_2");
		OclTestCollection2 oclTestCollection2_2_3 = new OclTestCollection2(oclTestCollection2);
		oclTestCollection2_2_3.setName("2_3");

        db.commit();
		Assert.assertEquals(3, oclTest1.getOclTestCollection().size());
		Assert.assertEquals(oclTest1.getProperty1(), oclTest1.getDerivedProperty1());
		Assert.assertEquals(1, oclTest1.getOclSelect().size());
		Assert.assertNotNull(oclTest1.getOclAny());
		Assert.assertEquals(6, oclTest1.getOclCollectName().size());
		Assert.assertEquals(6, oclTest1.getOclCollectAsSet().size());
		Assert.assertEquals(6, oclTest1.getOclCollectNested().size());
		Assert.assertEquals(3, oclTest1.getOclCollectNameAsSet().size());
		Assert.assertEquals(3, oclTest1.getOclCollectNameAsSet().size());

	}

	@Test
	public void testOcl2() {
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");

		OclTestCollection oclTestCollection1 = new OclTestCollection(oclTest1);
		oclTestCollection1.setName("oclTestCollection1");
		OclTestCollection oclTestCollection2 = new OclTestCollection(oclTest1);
		oclTestCollection2.setName("oclTestCollection2");
		OclTestCollection oclTestCollectionJohn1 = new OclTestCollection(oclTest1);
		oclTestCollectionJohn1.setName("john");
		OclTestCollection oclTestCollectionJohn2 = new OclTestCollection(oclTest1);
		oclTestCollectionJohn2.setName("john");

		OclTestCollection2 oclTestCollection2_1_1 = new OclTestCollection2(oclTestCollectionJohn1);
		oclTestCollection2_1_1.setName("1_1");
		OclTestCollection2 oclTestCollection2_1_2 = new OclTestCollection2(oclTestCollectionJohn1);
		oclTestCollection2_1_2.setName("1_2");
		OclTestCollection2 oclTestCollection2_1_3 = new OclTestCollection2(oclTestCollection1);
		oclTestCollection2_1_3.setName("1_3");

		OclTestCollection2 oclTestCollection2_2_1 = new OclTestCollection2(oclTestCollectionJohn2);
		oclTestCollection2_2_1.setName("2_1");
		OclTestCollection2 oclTestCollection2_2_2 = new OclTestCollection2(oclTestCollectionJohn2);
		oclTestCollection2_2_2.setName("2_2");
		OclTestCollection2 oclTestCollection2_2_3 = new OclTestCollection2(oclTestCollection2);
		oclTestCollection2_2_3.setName("2_3");

        db.commit();
		Assert.assertEquals(4, oclTest1.getOclSelectCollectAsSequence().size());
		Assert.assertEquals("oclTestCollection1oclTestCollection2johnjohn".length(), oclTest1.getOclIterateExp().length());
	}

	@Test
	public void testOclIterate1() {
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");

		OclTestCollection oclTestCollection1 = new OclTestCollection(oclTest1);
		oclTestCollection1.setName("oclTestCollection1");
		OclTestCollection oclTestCollection2 = new OclTestCollection(oclTest1);
		oclTestCollection2.setName("oclTestCollection2");

		OclTestCollection2 oclTestCollection2_1_1 = new OclTestCollection2(oclTestCollection1);
		oclTestCollection2_1_1.setName("john");
		OclTestCollection2 oclTestCollection2_1_2 = new OclTestCollection2(oclTestCollection1);
		oclTestCollection2_1_2.setName("john");
		OclTestCollection2 oclTestCollection2_1_3 = new OclTestCollection2(oclTestCollection1);
		oclTestCollection2_1_3.setName("john3");

        db.commit();

		Assert.assertEquals(2, oclTest1.getOclTestCollection().size());
		Assert.assertEquals(1, oclTest1.getOclIterateExp2().size());
	}

	@Test
	public void testOclFirst() {
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");
		OclTestCollection oclTestCollection1 = new OclTestCollection(oclTest1);
		oclTestCollection1.setName("oclTestCollection1");
		OclTestCollection oclTestCollection2 = new OclTestCollection(oclTest1);
		oclTestCollection2.setName("oclTestCollection2");
        db.commit();
		Assert.assertEquals(2, oclTest1.getOclTestCollection().size());
		Assert.assertNotNull(oclTest1.getTestOclFirst());
	}

	@Test(expected=OclIsInvalidException.class)
	public void testOclFirstOclInvalid1() {
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");
		Assert.assertEquals(0, oclTest1.getOclTestCollection().size());
		Assert.assertNull(oclTest1.getTestOclFirst());
	}

	@Test()
	public void testOclFirstOclInvalid2() {
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");
		Assert.assertEquals(0, oclTest1.getOclTestCollection().size());
		Assert.assertTrue(oclTest1.getTestFirstOclInvalid());
	}

	@Test()
	public void testOclIfExp1() {
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");
		Assert.assertEquals("halo", oclTest1.getTestOclIfExp());
	}

	@Test()
	public void testOclIfExp2() {
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");
		Assert.assertNotNull(oclTest1.getTestOclExpResultMany());
		Assert.assertEquals(0, oclTest1.getTestOclExpResultMany().size());
	}

    @Test()
    public void testOclGreaterThan() {
        OclTest1 oclTest1 = new OclTest1(true);
        oclTest1.setTestNumber(1);
        Assert.assertFalse(oclTest1.getTestGreaterThanOne());
        Assert.assertTrue(oclTest1.getTestGreaterThanEqualOne());
    }

    @Test()
    public void testOclSmallerThan() {
        OclTest1 oclTest1 = new OclTest1(true);
        oclTest1.setTestNumber(1);
        Assert.assertFalse(oclTest1.getTestSmallerThanOne());
        Assert.assertTrue(oclTest1.getTestSmallerThanEqualOne());
    }


}

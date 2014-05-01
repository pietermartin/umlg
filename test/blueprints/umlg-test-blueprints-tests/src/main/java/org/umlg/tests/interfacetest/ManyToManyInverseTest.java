package org.umlg.tests.interfacetest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.interfacetest.ManyA;
import org.umlg.interfacetest.ManyB;
import org.umlg.runtime.test.BaseLocalDbTest;

public class ManyToManyInverseTest extends BaseLocalDbTest {

    @Test
    public void testManyToMany1() {
        God god = new God(true);
        god.setName("THEGOD");
        ManyA manyA1 = new ManyA(god);
        manyA1.setName("manyA1");
        ManyA manyA2 = new ManyA(god);
        manyA2.setName("manyA2");
        ManyB manyB1 = new ManyB(god);
        manyB1.setName("manyB1");
        ManyB manyB2 = new ManyB(god);
        manyB2.setName("manyB2");

        manyA1.addToIManyB(manyB1);

        db.commit();
        Assert.assertEquals(1, manyB1.getIManyA().size());

        manyA2.addToIManyB(manyB1);
        db.commit();
        Assert.assertEquals(2, manyB1.getIManyA().size());
    }

	@Test
	public void testManyToMany() {
		God god = new God(true);
		god.setName("THEGOD");
		ManyA manyA1 = new ManyA(god);
		manyA1.setName("manyA1");
		ManyA manyA2 = new ManyA(god);
		manyA2.setName("manyA2");
		ManyB manyB1 = new ManyB(god);
		manyB1.setName("manyB1");
		ManyB manyB2 = new ManyB(god);
		manyB2.setName("manyB2");

		manyA1.addToIManyB(manyB1);

        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getIMany().size());
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6 + 5, countEdges());
		ManyA manyA1Test = new ManyA(manyA1.getVertex());
		Assert.assertEquals(1, manyA1Test.getIManyB().size());
		ManyB manyB1Test = new ManyB(manyB1.getVertex());
		Assert.assertEquals(1, manyB1Test.getIManyA().size());
	}

	@Test
	public void testManyManyToManies() {
		God god = new God(true);
		god.setName("THEGOD");
		ManyA manyA1 = new ManyA(god);
		manyA1.setName("manyA1");
		ManyA manyA2 = new ManyA(god);
		manyA2.setName("manyA2");
		ManyB manyB1 = new ManyB(god);
		manyB1.setName("manyB1");
		ManyB manyB2 = new ManyB(god);
		manyB2.setName("manyB2");

		manyA1.addToIManyB(manyB1);
		manyA1.addToIManyB(manyB2);
		manyA2.addToIManyB(manyB1);
		manyA2.addToIManyB(manyB2);

        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getIMany().size());
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(9 + 5, countEdges());
		ManyA manyA1Test = new ManyA(manyA1.getVertex());
		Assert.assertEquals(2, manyA1Test.getIManyB().size());
		ManyB manyB1Test = new ManyB(manyB1.getVertex());
		Assert.assertEquals(2, manyB1Test.getIManyA().size());

		ManyA manyA2Test = new ManyA(manyA2.getVertex());
		Assert.assertEquals(2, manyA2Test.getIManyB().size());
		ManyB manyB2Test = new ManyB(manyB2.getVertex());
		Assert.assertEquals(2, manyB2Test.getIManyA().size());
	}

	@Test
	public void testManyToManyInversed() {
		God god = new God(true);
		god.setName("THEGOD");
		ManyA manyA1 = new ManyA(god);
		manyA1.setName("manyA1");
		ManyA manyA2 = new ManyA(god);
		manyA2.setName("manyA2");
		ManyB manyB1 = new ManyB(god);
		manyB1.setName("manyB1");
		ManyB manyB2 = new ManyB(god);
		manyB2.setName("manyB2");

		manyB1.addToIManyA(manyA1);

        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getIMany().size());
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6 + 5, countEdges());
		ManyA manyA1Test = new ManyA(manyA1.getVertex());
		Assert.assertEquals(1, manyA1Test.getIManyB().size());
		ManyB manyB1Test = new ManyB(manyB1.getVertex());
		Assert.assertEquals(1, manyB1Test.getIManyA().size());
	}

	@Test
	public void testManyManyToManiesInversed() {
		God god = new God(true);
		god.setName("THEGOD");
		ManyA manyA1 = new ManyA(god);
		manyA1.setName("manyA1");
		ManyA manyA2 = new ManyA(god);
		manyA2.setName("manyA2");
		ManyB manyB1 = new ManyB(god);
		manyB1.setName("manyB1");
		ManyB manyB2 = new ManyB(god);
		manyB2.setName("manyB2");

		manyB1.addToIManyA(manyA1);
		manyB1.addToIManyA(manyA2);
		manyB2.addToIManyA(manyA1);
		manyB2.addToIManyA(manyA2);

        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getIMany().size());
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(9 + 5, countEdges());
		ManyA manyA1Test = new ManyA(manyA1.getVertex());
		Assert.assertEquals(2, manyA1Test.getIManyB().size());
		ManyB manyB1Test = new ManyB(manyB1.getVertex());
		Assert.assertEquals(2, manyB1Test.getIManyA().size());

		ManyA manyA2Test = new ManyA(manyA2.getVertex());
		Assert.assertEquals(2, manyA2Test.getIManyB().size());
		ManyB manyB2Test = new ManyB(manyB2.getVertex());
		Assert.assertEquals(2, manyB2Test.getIManyA().size());
	}
	
}

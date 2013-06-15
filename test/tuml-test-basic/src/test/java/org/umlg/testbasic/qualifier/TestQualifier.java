package org.umlg.testbasic.qualifier;

import junit.framework.Assert;

import org.junit.Test;
import org.umlg.qualifier.Angel;
import org.umlg.qualifier.God1;
import org.umlg.qualifier.Nature;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestQualifier extends BaseLocalDbTest {
	
	@Test
	public void testQualifier() {
		God1 god = new God1(true);
		god.setName("God");
		Nature nature1 = new Nature(true);
		nature1.setNatureName("natureName1");
		nature1.addToGod(god);
		Nature nature2 = new Nature(true);
		nature2.setNatureName("natureName2");
		nature2.addToGod(god);
		Nature nature3 = new Nature(true);
		nature3.setNatureName("natureName3");
		nature3.addToGod(god);
        db.commit();
		
		God1 testGod = new God1(god.getVertex());
		Nature testNature = testGod.getNatureForNatureQualifier1("natureName2");
		Assert.assertNotNull(testNature);
		testNature = testGod.getNatureForNatureQualifier1("natureNameX");
		Assert.assertNull(testNature);
	}
	
	@Test
	public void testQualifiedWithMultipleQualifiers() {
		God1 god = new God1(true);
		god.setName("God");
		Angel angel1 = new Angel(true);
		angel1.setName("angelName1");
		angel1.setRank(1);
		angel1.addToGod(god);
		Angel angel2 = new Angel(true);
		angel2.setName("angelName2");
		angel2.setRank(2);
		angel2.addToGod(god);
		Angel angel3 = new Angel(true);
		angel3.setName("angelName3");
		angel3.setRank(3);
		angel3.addToGod(god);
        db.commit();

		God1 testGod = new God1(god.getVertex());
		Angel testAngel = testGod.getAngelForAngelNameQualifierAngelRankQualifier("angelName1", 1);
		Assert.assertNotNull(testAngel);
		testAngel = testGod.getAngelForAngelNameQualifierAngelRankQualifier("angelName1", 2);
		Assert.assertNull(testAngel);
	}

}

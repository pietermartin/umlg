package org.umlg.tests.allinstances;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.BaseModelUmlg;
import org.umlg.concretetest.God;
import org.umlg.hierarchytest.Folder;
import org.umlg.hierarchytest.RealRootFolder;
import org.umlg.inheritencetest.AbstractSpecies;
import org.umlg.inheritencetest.Biped;
import org.umlg.inheritencetest.Mamal;
import org.umlg.inheritencetest.Quadped;
import org.umlg.interfacetest.ManyA;
import org.umlg.interfacetest.ManyB;
import org.umlg.query.InstanceQuery;
import org.umlg.query.QueryEnum;
import org.umlg.rootallinstances.BaseRoot;
import org.umlg.rootallinstances.TopRoot;
import org.umlg.runtime.collection.Filter;
import org.umlg.runtime.test.BaseLocalDbTest;

public class AllInstancesTest extends BaseLocalDbTest {

//	@SuppressWarnings("unused")
//	@Test
//	public void testAllInstances1() {
//		God god = new God(true);
//		god.setName("THEGOD");
//		Mamal mamal1 = new Mamal(god);
//		Mamal mamal2 = new Mamal(god);
//		Mamal mamal3 = new Mamal(god);
//		Mamal mamal4 = new Mamal(god);
//		Mamal mamal5 = new Mamal(god);
//
//		Biped biped1 = new Biped(god);
//		Biped biped2 = new Biped(god);
//		Biped biped3 = new Biped(god);
//		Biped biped4 = new Biped(god);
//		Biped biped5 = new Biped(god);
//
//		Quadped quadPed1 = new Quadped(god);
//		Quadped quadPed2 = new Quadped(god);
//		Quadped quadPed3 = new Quadped(god);
//		Quadped quadPed4 = new Quadped(god);
//		Quadped quadPed5 = new Quadped(god);
//
//        db.commit();
//		Assert.assertEquals(15, AbstractSpecies.allInstances().size());
//		Assert.assertEquals(15, Mamal.allInstances().size());
//		Assert.assertEquals(5, Biped.allInstances().size());
//		Assert.assertEquals(5, Quadped.allInstances().size());
//		Assert.assertEquals(16, BaseModelUmlg.allInstances().size());
//	}
//
//	@Test
//	public void testHierarciesAllInstances() {
//		God god = new God(true);
//		god.setName("THEGOD");
//		RealRootFolder realRootFolder = new RealRootFolder(god);
//		realRootFolder.setName("realRootFolder");
//		Folder folder1 = new Folder(realRootFolder);
//		folder1.setName("folder1");
//		Folder folder1_1 = new Folder(folder1);
//		folder1_1.setName("folder1_1");
//
//		Folder folder2 = new Folder(realRootFolder);
//		folder2.setName("folder2");
//		Folder folder2_1 = new Folder(folder2);
//		folder2_1.setName("folder2_1");
//		Folder folder2_2 = new Folder(folder2);
//		folder2_2.setName("folder2_2");
//
//		Folder folder2_2_1 = new Folder(folder2_1);
//		folder2_2_1.setName("folder2_2_1");
//		Folder folder2_2_2 = new Folder(folder2_1);
//		folder2_2_2.setName("folder2_2_2");
//
//        db.commit();
//		Assert.assertEquals(1, RealRootFolder.allInstances().size());
//		Assert.assertEquals(7, Folder.allInstances().size());
//		Assert.assertEquals(9, BaseModelUmlg.allInstances().size());
//	}
//
//	@Test
//	public void testAllInstancesOnQueries() {
//		God god = new God(true);
//		god.setName("THEGOD");
//        InstanceQuery query1 = new InstanceQuery(god);
//		query1.setQueryString("");
//		query1.setName("q1");
//		query1.setQueryEnum(QueryEnum.OCL);
//
//		RealRootFolder realRootFolder = new RealRootFolder(god);
//		realRootFolder.setName("realRootFolder");
//        InstanceQuery query2 = new InstanceQuery(realRootFolder);
//		query2.setQueryString("");
//		query2.setName("q2");
//		query2.setQueryEnum(QueryEnum.OCL);
//
//		Folder folder1 = new Folder(realRootFolder);
//		folder1.setName("folder1");
//        InstanceQuery query3 = new InstanceQuery(folder1);
//		query3.setName("q3");
//		query3.setQueryString("");
//		query3.setQueryEnum(QueryEnum.OCL);
//
//		Folder folder1_1 = new Folder(folder1);
//		folder1_1.setName("folder1_1");
//        InstanceQuery query4 = new InstanceQuery(folder1_1);
//		query4.setQueryString("");
//		query4.setName("q4");
//		query4.setQueryEnum(QueryEnum.OCL);
//
//		Folder folder2 = new Folder(realRootFolder);
//		folder2.setName("folder2");
//        InstanceQuery query5 = new InstanceQuery(folder2);
//		query5.setQueryString("");
//		query5.setQueryEnum(QueryEnum.OCL);
//		query5.setName("q5");
//
//		Folder folder2_1 = new Folder(folder2);
//		folder2_1.setName("folder2_1");
//        InstanceQuery query6 = new InstanceQuery(folder2_1);
//		query6.setQueryString("");
//		query6.setName("q6");
//		query6.setQueryEnum(QueryEnum.OCL);
//
//		Folder folder2_2 = new Folder(folder2);
//		folder2_2.setName("folder2_2");
//        InstanceQuery query7 = new InstanceQuery(folder2_2);
//		query7.setQueryString("");
//		query7.setName("q7");
//		query7.setQueryEnum(QueryEnum.OCL);
//
//		Folder folder2_2_1 = new Folder(folder2_1);
//		folder2_2_1.setName("folder2_2_1");
//        InstanceQuery query8 = new InstanceQuery(folder2_2_1);
//		query8.setQueryString("");
//		query8.setName("q8");
//		query8.setQueryEnum(QueryEnum.OCL);
//
//		Folder folder2_2_2 = new Folder(folder2_1);
//		folder2_2_2.setName("folder2_2_2");
//        InstanceQuery query9 = new InstanceQuery(folder2_2_2);
//		query9.setQueryString("");
//		query9.setName("q9");
//		query9.setQueryEnum(QueryEnum.OCL);
//
//        db.commit();
//
//		Assert.assertEquals(18, BaseModelUmlg.allInstances().size());
//		Assert.assertEquals(9, InstanceQuery.allInstances().size());
//
//	}
//
//	@Test
//	public void testAllInstancesOnInterfaces() {
//		God god = new God(true);
//		god.setName("THEGOD");
//		ManyA manyA1 = new ManyA(god);
//		manyA1.setName("manyA1");
//		ManyA manyA2 = new ManyA(god);
//		manyA2.setName("manyA2");
//		ManyB manyB1 = new ManyB(god);
//		manyB1.setName("manyB1");
//		ManyB manyB2 = new ManyB(god);
//		manyB2.setName("manyB2");
//
//		manyA1.addToIManyB(manyB1);
//		manyA1.addToIManyB(manyB2);
//		manyA2.addToIManyB(manyB1);
//		manyA2.addToIManyB(manyB2);
//
//        db.commit();
//		Assert.assertEquals(2, ManyA.allInstances().size());
//		Assert.assertEquals(2, ManyB.allInstances().size());
//		//TODO eish
////		Assert.assertEquals(2, IManyA.allInstances().size());
//	}
//
//    @Test
//    public void testRootAllInstances() {
//        TopRoot topRoot1 = new TopRoot();
//        topRoot1.setName("topRoot1");
//        topRoot1.setIndexedName("asd");
//        TopRoot topRoot2 = new TopRoot();
//        topRoot2.setIndexedName("asdasd");
//        topRoot2.setName("topRoot2");
//        TopRoot topRoot3 = new TopRoot();
//        topRoot3.setIndexedName("asdasdasd");
//        topRoot3.setName("topRoot3");
//        db.commit();
//        Assert.assertEquals(3, BaseRoot.allInstances().size());
//
//    }

    @SuppressWarnings("unused")
    @Test
    public void testAllInstances1WithFilter() {
        God god = new God(true);
        god.setName("THEGOD");
        Mamal mamal1 = new Mamal(god);
        mamal1.setName("mamal1");
        Mamal mamal2 = new Mamal(god);
        mamal2.setName("mamal2");
        Mamal mamal3 = new Mamal(god);
        mamal3.setName("mamal3");
        Mamal mamal4 = new Mamal(god);
        mamal4.setName("mamal4");
        Mamal mamal5 = new Mamal(god);
        mamal5.setName("mamal5");

        Biped biped1 = new Biped(god);
        Biped biped2 = new Biped(god);
        Biped biped3 = new Biped(god);
        Biped biped4 = new Biped(god);
        Biped biped5 = new Biped(god);

        Quadped quadPed1 = new Quadped(god);
        Quadped quadPed2 = new Quadped(god);
        Quadped quadPed3 = new Quadped(god);
        Quadped quadPed4 = new Quadped(god);
        Quadped quadPed5 = new Quadped(god);

        db.commit();
        Assert.assertEquals(1, AbstractSpecies.allInstances(new Filter<AbstractSpecies>() {
            @Override
            public boolean filter(AbstractSpecies t) {
                return t.getName().equals("mamal1");
            }
        }).size());
    }
}
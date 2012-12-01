package org.nakeuml.tinker.allinstances;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.BaseModelTuml;
import org.tuml.concretetest.God;
import org.tuml.hierarchytest.Folder;
import org.tuml.hierarchytest.RealRootFolder;
import org.tuml.inheritencetest.AbstractSpecies;
import org.tuml.inheritencetest.Biped;
import org.tuml.inheritencetest.Mamal;
import org.tuml.inheritencetest.Quadped;
import org.tuml.interfacetest.ManyA;
import org.tuml.interfacetest.ManyB;
import org.tuml.query.Query;
import org.tuml.query.QueryEnum;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class AllInstancesTest extends BaseLocalDbTest {

	@SuppressWarnings("unused")
	@Test
	public void testAllInstances1() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Mamal mamal1 = new Mamal(god);
		Mamal mamal2 = new Mamal(god);
		Mamal mamal3 = new Mamal(god);
		Mamal mamal4 = new Mamal(god);
		Mamal mamal5 = new Mamal(god);

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

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(15, AbstractSpecies.allInstances().size());
		Assert.assertEquals(15, Mamal.allInstances().size());
		Assert.assertEquals(5, Biped.allInstances().size());
		Assert.assertEquals(5, Quadped.allInstances().size());
		Assert.assertEquals(16, BaseModelTuml.allInstances().size());
	}

	@Test
	public void testHierarciesAllInstances() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		RealRootFolder realRootFolder = new RealRootFolder(god);
		realRootFolder.setName("realRootFolder");
		Folder folder1 = new Folder(realRootFolder);
		folder1.setName("folder1");
		Folder folder1_1 = new Folder(folder1);
		folder1_1.setName("folder1_1");

		Folder folder2 = new Folder(realRootFolder);
		folder2.setName("folder2");
		Folder folder2_1 = new Folder(folder2);
		folder2_1.setName("folder2_1");
		Folder folder2_2 = new Folder(folder2);
		folder2_2.setName("folder2_2");

		Folder folder2_2_1 = new Folder(folder2_1);
		folder2_2_1.setName("folder2_2_1");
		Folder folder2_2_2 = new Folder(folder2_1);
		folder2_2_2.setName("folder2_2_2");

		db.stopTransaction(Conclusion.SUCCESS);
//		Assert.assertEquals(1, RealRootFolder.allInstances().size());
		Assert.assertEquals(7, Folder.allInstances().size());
//		Assert.assertEquals(9, BaseModelTuml.allInstances().size());
	}

	@Test
	public void testAllInstancesOnQueries() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Query query1 = new Query(god);
		query1.setQueryString("");
		query1.setName("q1");
		query1.setQueryEnum(QueryEnum.OCL);

		RealRootFolder realRootFolder = new RealRootFolder(god);
		realRootFolder.setName("realRootFolder");
		Query query2 = new Query(realRootFolder);
		query2.setQueryString("");
		query2.setName("q2");
		query2.setQueryEnum(QueryEnum.OCL);

		Folder folder1 = new Folder(realRootFolder);
		folder1.setName("folder1");
		Query query3 = new Query(folder1);
		query3.setName("q3");
		query3.setQueryString("");
		query3.setQueryEnum(QueryEnum.OCL);

		Folder folder1_1 = new Folder(folder1);
		folder1_1.setName("folder1_1");
		Query query4 = new Query(folder1_1);
		query4.setQueryString("");
		query4.setName("q4");
		query4.setQueryEnum(QueryEnum.OCL);

		Folder folder2 = new Folder(realRootFolder);
		folder2.setName("folder2");
		Query query5 = new Query(folder2);
		query5.setQueryString("");
		query5.setQueryEnum(QueryEnum.OCL);
		query5.setName("q5");

		Folder folder2_1 = new Folder(folder2);
		folder2_1.setName("folder2_1");
		Query query6 = new Query(folder2_1);
		query6.setQueryString("");
		query6.setName("q6");
		query6.setQueryEnum(QueryEnum.OCL);

		Folder folder2_2 = new Folder(folder2);
		folder2_2.setName("folder2_2");
		Query query7 = new Query(folder2_2);
		query7.setQueryString("");
		query7.setName("q7");
		query7.setQueryEnum(QueryEnum.OCL);

		Folder folder2_2_1 = new Folder(folder2_1);
		folder2_2_1.setName("folder2_2_1");
		Query query8 = new Query(folder2_2_1);
		query8.setQueryString("");
		query8.setName("q8");
		query8.setQueryEnum(QueryEnum.OCL);

		Folder folder2_2_2 = new Folder(folder2_1);
		folder2_2_2.setName("folder2_2_2");
		Query query9 = new Query(folder2_2_2);
		query9.setQueryString("");
		query9.setName("q9");
		query9.setQueryEnum(QueryEnum.OCL);

		db.stopTransaction(Conclusion.SUCCESS);

		Assert.assertEquals(18, BaseModelTuml.allInstances().size());
		Assert.assertEquals(9, Query.allInstances().size());

	}

	@Test
	public void testAllInstancesOnInterfaces() {
		db.startTransaction();
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

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, ManyA.allInstances().size());
		Assert.assertEquals(2, ManyB.allInstances().size());
		//TODO eish
//		Assert.assertEquals(2, IManyA.allInstances().size());
	}

}
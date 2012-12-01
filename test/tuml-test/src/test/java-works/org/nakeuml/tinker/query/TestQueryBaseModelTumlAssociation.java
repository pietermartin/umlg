package org.nakeuml.tinker.query;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.componenttest.Space;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.Time;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.query.Query;
import org.tuml.query.QueryEnum;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestQueryBaseModelTumlAssociation extends BaseLocalDbTest {

	//This seems to be happening in restlet scenario
	@SuppressWarnings("unused")
	@Test
	public void testCollectionCleared() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st = new SpaceTime(universe1);
		Space s = new Space(st);
		Time t = new Time(st);

		Query query1 = new Query(true);
		query1.setQueryEnum(QueryEnum.OCL);
		query1.setQueryString("asd");
		query1.setName("asd");
		universe1.addToQuery(query1);
		Assert.assertNotNull(query1.getBaseTumlWithQuery());
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test
	public void testCollectionCleared2() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st = new SpaceTime(universe1);
		Space s = new Space(st);
		Time t = new Time(st);

		Query query1 = new Query(true);
		query1.setQueryEnum(QueryEnum.OCL);
		query1.setQueryString("asd");
		query1.setName("asd");
		universe1.getQuery().add(query1);
		Assert.assertNotNull(query1.getBaseTumlWithQuery());
		db.stopTransaction(Conclusion.SUCCESS);
	}

}

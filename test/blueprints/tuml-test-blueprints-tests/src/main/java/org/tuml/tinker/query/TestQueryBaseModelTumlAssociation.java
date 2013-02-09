package org.tuml.tinker.query;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.componenttest.Space;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.Time;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.query.InstanceQuery;
import org.tuml.query.QueryEnum;
import org.tuml.runtime.test.BaseLocalDbTest;

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

		InstanceQuery query1 = new InstanceQuery(true);
		query1.setQueryEnum(QueryEnum.OCL);
		query1.setQueryString("asd");
		query1.setName("asd");
		universe1.addToInstanceQuery(query1);
		Assert.assertNotNull(query1.getBaseTumlWithQuery());
        db.commit();
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

        InstanceQuery query1 = new InstanceQuery(true);
		query1.setQueryEnum(QueryEnum.OCL);
		query1.setQueryString("asd");
		query1.setName("asd");
		universe1.getInstanceQuery().add(query1);
		Assert.assertNotNull(query1.getBaseTumlWithQuery());
        db.commit();
	}

}

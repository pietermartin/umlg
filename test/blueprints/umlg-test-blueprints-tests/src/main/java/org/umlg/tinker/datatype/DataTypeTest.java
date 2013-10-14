package org.umlg.tinker.datatype;

import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.datatype.DataTypeEntity;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2012/12/15
 * Time: 10:36 AM
 */
public class DataTypeTest extends BaseLocalDbTest {

//    @Test
//    public void testEmailDataType() {
//        God g = new God(true);
//        g.setName("g");
//        Universe universe = new Universe(g);
//        universe.setName("u");
//        universe.setEmail("ding.dong@lalaland.com");
//        SpaceTime spaceTime = new SpaceTime(universe);
//        new Space(spaceTime);
//        new Time(spaceTime);
//        db.commit();
//        Universe testUniverse1 = new Universe(universe.getVertex());
//        Assert.assertEquals("ding.dong@lalaland.com", testUniverse1.getEmail());
//    }
//
//    @Test
//    public void testManyDataTypeEmail() {
//        DataTypeEntity dataTypeEntity = new DataTypeEntity(true);
//        dataTypeEntity.addToEmail1("asd@asd.asd");
//        dataTypeEntity.addToEmailList("j@jj.jj");
//        dataTypeEntity.addToEmailList("jj@jj.jj");
//        dataTypeEntity.addToEmailList("jjj@jj.jj");
//        dataTypeEntity.addToEmailList("jjjj@jj.jj");
//        dataTypeEntity.addToEmailList("jjjjj@jj.jj");
//        db.commit();
//        dataTypeEntity.reload();
//        Assert.assertEquals(5, dataTypeEntity.getEmailList().size());
//        Assert.assertEquals("jjjj@jj.jj", dataTypeEntity.getEmailList().get(3));
//    }
//
//    @Test
//    public void testManyDataTypeDateSet() {
//        DataTypeEntity dataTypeEntity = new DataTypeEntity(true);
//        dataTypeEntity.addToDateSet(new LocalDate("2000-06-01"));
//        dataTypeEntity.addToDateSet(new LocalDate("2000-06-02"));
//        dataTypeEntity.addToDateSet(new LocalDate("2000-06-03"));
//        dataTypeEntity.addToDateSet(new LocalDate("2000-06-04"));
//        db.commit();
//        dataTypeEntity.reload();
//        Assert.assertEquals(4, dataTypeEntity.getDateSet().size());
//        dataTypeEntity.getDateSet().remove(new LocalDate("2000-06-03"));
//        db.commit();
//        dataTypeEntity.reload();
//        Assert.assertEquals(3, dataTypeEntity.getDateSet().size());
//        Assert.assertTrue(dataTypeEntity.getDateSet().contains(new LocalDate("2000-06-01")));
//        Assert.assertTrue(dataTypeEntity.getDateSet().contains(new LocalDate("2000-06-02")));
//        Assert.assertTrue(dataTypeEntity.getDateSet().contains(new LocalDate("2000-06-04")));
//        Assert.assertFalse(dataTypeEntity.getDateSet().contains(new LocalDate("2000-06-03")));
//    }

}

package org.umlg.tests.datatype;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.datatype.DataTypeEntity;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.types.Password;
import org.umlg.runtime.util.PasswordEncryptionService;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Date: 2012/12/15
 * Time: 10:36 AM
 */
public class DataTypeTest extends BaseLocalDbTest {

    @Test
    public void testEmailDataType() {
        God g = new God(true);
        g.setName("g");
        Universe universe = new Universe(g);
        universe.setName("u");
        universe.setEmail("ding.dong@lalaland.com");
        SpaceTime spaceTime = new SpaceTime(universe);
        new Space(spaceTime);
        new Time(spaceTime);
        db.commit();
        Universe testUniverse1 = new Universe(universe.getVertex());
        Assert.assertEquals("ding.dong@lalaland.com", testUniverse1.getEmail());
    }

    @Test
    public void testManyDataTypeEmail() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity(true);
        dataTypeEntity.addToEmail1("john@register.com");
        dataTypeEntity.addToEmailList("jjohn@register.com");
        dataTypeEntity.addToEmailList("jjjohn@register.com");
        dataTypeEntity.addToEmailList("jjjjohn@register.com");
        dataTypeEntity.addToEmailList("jjjjjohn@register.com");
        dataTypeEntity.addToEmailList("jjjjjjohn@register.com");
        db.commit();
        dataTypeEntity.reload();
        Assert.assertEquals(5, dataTypeEntity.getEmailList().size());
        Assert.assertEquals("jjjjjohn@register.com", dataTypeEntity.getEmailList().get(3));
    }

    @Test
    public void testDataTypeEmailWithSameValue() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity(true);
        dataTypeEntity.addToEmail1("john@register.com");
        dataTypeEntity.addToEmail2("john@register.com");
        db.commit();
        dataTypeEntity.reload();
        Assert.assertEquals("john@register.com", dataTypeEntity.getEmail1());
        Assert.assertEquals("john@register.com", dataTypeEntity.getEmail2());

        dataTypeEntity.setEmail1(null);
        db.commit();
        dataTypeEntity.reload();
        Assert.assertEquals(null, dataTypeEntity.getEmail1());
        Assert.assertEquals("john@register.com", dataTypeEntity.getEmail2());
    }

    @Test
    public void testManyDataTypeDateSet() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity(true);
        dataTypeEntity.addToDateSet(LocalDate.parse("2000-06-01"));
        dataTypeEntity.addToDateSet(LocalDate.parse("2000-06-02"));
        dataTypeEntity.addToDateSet(LocalDate.parse("2000-06-03"));
        dataTypeEntity.addToDateSet(LocalDate.parse("2000-06-04"));
        db.commit();
        dataTypeEntity.reload();
        Assert.assertEquals(4, dataTypeEntity.getDateSet().size());
        dataTypeEntity.getDateSet().remove(LocalDate.parse("2000-06-03"));
        db.commit();
        dataTypeEntity.reload();
        Assert.assertEquals(3, dataTypeEntity.getDateSet().size());
        Assert.assertTrue(dataTypeEntity.getDateSet().contains(LocalDate.parse("2000-06-01")));
        Assert.assertTrue(dataTypeEntity.getDateSet().contains(LocalDate.parse("2000-06-02")));
        Assert.assertTrue(dataTypeEntity.getDateSet().contains(LocalDate.parse("2000-06-04")));
        Assert.assertFalse(dataTypeEntity.getDateSet().contains(LocalDate.parse("2000-06-03")));
    }

    @Test
    public void testDataTypeDataList() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity(true);
        dataTypeEntity.addToDateList(LocalDate.parse("2000-06-01"));
        dataTypeEntity.addToDateList(LocalDate.parse("2000-06-02"));
        dataTypeEntity.addToDateList(LocalDate.parse("2000-06-03"));
        db.commit();
        dataTypeEntity.reload();
        Assert.assertEquals(3, dataTypeEntity.getDateList().size());
        Assert.assertEquals(0, dataTypeEntity.getDateList().indexOf(LocalDate.parse("2000-06-01")));
        Assert.assertEquals(1, dataTypeEntity.getDateList().indexOf(LocalDate.parse("2000-06-02")));
        Assert.assertEquals(2, dataTypeEntity.getDateList().indexOf(LocalDate.parse("2000-06-03")));

        dataTypeEntity.getDateList().remove(LocalDate.parse("2000-06-02"));

        db.commit();
        dataTypeEntity.reload();
        Assert.assertEquals(2, dataTypeEntity.getDateList().size());
        Assert.assertEquals(0, dataTypeEntity.getDateList().indexOf(LocalDate.parse("2000-06-01")));
        Assert.assertEquals(1, dataTypeEntity.getDateList().indexOf(LocalDate.parse("2000-06-03")));
        Assert.assertFalse(dataTypeEntity.getDateList().contains(LocalDate.parse("2000-06-02")));
    }

    @Test
    public void testDataTypeOrderedSet() {

        DataTypeEntity dataTypeEntity = new DataTypeEntity(true);
        dataTypeEntity.addToDateOrderedSet(LocalDate.parse("2000-06-01"));
        dataTypeEntity.addToDateOrderedSet(LocalDate.parse("2000-06-02"));
        dataTypeEntity.addToDateOrderedSet(LocalDate.parse("2000-06-03"));
        db.commit();
        dataTypeEntity.reload();
        Assert.assertEquals(3, dataTypeEntity.getDateOrderedSet().size());
        Assert.assertEquals(0, dataTypeEntity.getDateOrderedSet().indexOf(LocalDate.parse("2000-06-01")));
        Assert.assertEquals(1, dataTypeEntity.getDateOrderedSet().indexOf(LocalDate.parse("2000-06-02")));
        Assert.assertEquals(2, dataTypeEntity.getDateOrderedSet().indexOf(LocalDate.parse("2000-06-03")));

        dataTypeEntity.removeFromDateOrderedSet(LocalDate.parse("2000-06-02"));
        db.commit();
        dataTypeEntity.reload();
        Assert.assertEquals(2, dataTypeEntity.getDateOrderedSet().size());
        Assert.assertEquals(0, dataTypeEntity.getDateOrderedSet().indexOf(LocalDate.parse("2000-06-01")));
        Assert.assertEquals(1, dataTypeEntity.getDateOrderedSet().indexOf(LocalDate.parse("2000-06-03")));
        Assert.assertFalse(dataTypeEntity.getDateOrderedSet().contains(LocalDate.parse("2000-06-02")));
    }

    @Test
    public void testDataTypeBag() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity(true);
        dataTypeEntity.addToDateBag(LocalDate.parse("2000-06-01"));
        dataTypeEntity.addToDateBag(LocalDate.parse("2000-06-02"));
        dataTypeEntity.addToDateBag(LocalDate.parse("2000-06-03"));
        db.commit();

        dataTypeEntity.reload();
        Assert.assertEquals(3, dataTypeEntity.getDateBag().size());
        Assert.assertTrue(dataTypeEntity.getDateBag().contains(LocalDate.parse("2000-06-01")));
        Assert.assertTrue(dataTypeEntity.getDateBag().contains(LocalDate.parse("2000-06-02")));
        Assert.assertTrue(dataTypeEntity.getDateBag().contains(LocalDate.parse("2000-06-03")));

        dataTypeEntity.addToDateBag(LocalDate.parse("2000-06-03"));
        db.commit();

        dataTypeEntity.reload();
        Assert.assertEquals(4, dataTypeEntity.getDateBag().size());
        Assert.assertTrue(dataTypeEntity.getDateBag().contains(LocalDate.parse("2000-06-01")));
        Assert.assertTrue(dataTypeEntity.getDateBag().contains(LocalDate.parse("2000-06-02")));
        Assert.assertTrue(dataTypeEntity.getDateBag().contains(LocalDate.parse("2000-06-03")));

        dataTypeEntity.removeFromDateBag(LocalDate.parse("2000-06-03"));
        db.commit();

        dataTypeEntity.reload();
        Assert.assertEquals(3, dataTypeEntity.getDateBag().size());
        Assert.assertTrue(dataTypeEntity.getDateBag().contains(LocalDate.parse("2000-06-01")));
        Assert.assertTrue(dataTypeEntity.getDateBag().contains(LocalDate.parse("2000-06-02")));
        Assert.assertTrue(dataTypeEntity.getDateBag().contains(LocalDate.parse("2000-06-03")));
    }

    @Test
    public void testHost() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity();
        dataTypeEntity.setHost("172.28.200.147");
        UMLG.get().commit();
        dataTypeEntity.reload();
        Assert.assertEquals("172.28.200.147", dataTypeEntity.getHost());

        boolean fail = false;
        try {
            dataTypeEntity.setHost("572.28.200.147");
        } catch (UmlgConstraintViolationException e) {
            fail = true;
        }
        Assert.assertTrue(fail);

        dataTypeEntity.setHost("umlg.org");
        Assert.assertEquals("umlg.org", dataTypeEntity.getHost());

        fail = false;
        try {
            dataTypeEntity.setHost("aaa");
        } catch (UmlgConstraintViolationException e) {
            fail = true;
        }
        Assert.assertTrue(fail);
    }

    @Test
    public void testQuartzCron() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity();
        dataTypeEntity.setQuartzCron("* * * * * ? *");
        UMLG.get().commit();
        dataTypeEntity.reload();
        Assert.assertEquals("* * * * * ? *", dataTypeEntity.getQuartzCron());

        boolean fail = false;
        try {
            dataTypeEntity.setQuartzCron("572.28.200.147");
        } catch (UmlgConstraintViolationException e) {
            fail = true;
        }
        Assert.assertTrue(fail);
    }

    @Test
    public void testByteArray() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity();
        dataTypeEntity.setByteArray(new byte[]{1,2,3});
        UMLG.get().commit();
        dataTypeEntity.reload();
        Assert.assertTrue(Arrays.equals(new byte[]{1,2,3}, dataTypeEntity.getByteArray()));

        dataTypeEntity.setByteArray(new byte[]{4,5,6});
        UMLG.get().commit();
        dataTypeEntity.reload();
        Assert.assertTrue(Arrays.equals(new byte[]{4,5,6}, dataTypeEntity.getByteArray()));
    }

    @Test
    public void testPassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
        DataTypeEntity dataTypeEntity = new DataTypeEntity();
        dataTypeEntity.setPassword(new Password("password"));
        UMLG.get().commit();
        dataTypeEntity.reload();
        Password password = dataTypeEntity.getPassword();
        Assert.assertTrue(new PasswordEncryptionService().authenticate("password", password.getEncryptedPassword(), password.getSalt()));

        dataTypeEntity.setPassword(new Password("password123"));
        UMLG.get().commit();
        dataTypeEntity.reload();
        password = dataTypeEntity.getPassword();
        Assert.assertTrue(new PasswordEncryptionService().authenticate("password123", password.getEncryptedPassword(), password.getSalt()));

        dataTypeEntity = new DataTypeEntity(dataTypeEntity.getVertex());
        password = dataTypeEntity.getPassword();
        Assert.assertTrue(new PasswordEncryptionService().authenticate("password123", password.getEncryptedPassword(), password.getSalt()));
    }

    @Test
    public void testUnsecurePassword() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity();
        dataTypeEntity.setUnsecurePassword("12345");
        UMLG.get().commit();
        dataTypeEntity.reload();
        String password = dataTypeEntity.getUnsecurePassword();
        Assert.assertEquals("12345", password);

        dataTypeEntity.setUnsecurePassword("54321");
        UMLG.get().commit();
        dataTypeEntity.reload();
        password = dataTypeEntity.getUnsecurePassword();
        Assert.assertEquals("54321", password);
    }

}

package org.tuml.tinker.validationtest;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.collectiontest.FWomen;
import org.tuml.collectiontest.Fantasy;
import org.tuml.collectiontest.Finger;
import org.tuml.collectiontest.Hand;
import org.tuml.componenttest.Space;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.Time;
import org.tuml.componenttest.ValidationTest;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.runtime.validation.TumlConstraintViolationException;

public class TestValidation extends BaseLocalDbTest {

    @SuppressWarnings("unused")
    @Test(expected = TumlConstraintViolationException.class)
    public void testValidationMaxLengthFail() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setMaxLength("12345678911");
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test
    public void testValidationMaxLengthPass() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        u1.setMaxLength("1234567891");
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test(expected = TumlConstraintViolationException.class)
    public void testValidationMinLengthFail() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setTestMinLength("123");
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test
    public void testValidationMinLengthPass() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setTestMinLength("12345");
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test(expected = TumlConstraintViolationException.class)
    public void testValidationRangeLengthFail1() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setRangeLength("123");
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test(expected = TumlConstraintViolationException.class)
    public void testValidationRangeLengthFail2() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setRangeLength("12345678911");
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test
    public void testValidationRangeLengthPass() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setRangeLength("123456789");
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test(expected = TumlConstraintViolationException.class)
    public void testValidationMinFail() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setMin(5);
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test
    public void testValidationMinPass() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setMin(51);
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test(expected = TumlConstraintViolationException.class)
    public void testValidationMaxFail() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setMax(101);
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test
    public void testValidationMaxPass() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setMax(51);
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test(expected = TumlConstraintViolationException.class)
    public void testValidationRangeFail1() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setRange(1);
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test(expected = TumlConstraintViolationException.class)
    public void testValidationRangeFail2() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setRange(5);
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test
    public void testValidationRangePass() {
        God g = new God(true);
        Universe u1 = new Universe(g);
        u1.setRange(3);
        SpaceTime st = new SpaceTime(u1);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test
    public void testValidationRequiredFail() {
        try {
            God g = new God(true);
            ValidationTest validationTest = new ValidationTest(g);
            db.commit();
            Assert.fail("Expected TransactionFailureException");
        } catch (Exception e) {
            Assert.assertTrue(isTransactionFailedException(e));
        }
    }

    @Test
    public void testValidationRequiredPass() {
        God g = new God(true);
        ValidationTest validationTest = new ValidationTest(g);
        validationTest.setRequiredName("asd");
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test
    public void testFail() {
        try {
            God g = new God(true);
            ValidationTest validationTest = new ValidationTest(g);
            // validationTest.setRequiredName("asd");
            db.commit();
            Assert.fail("Expected TransactionFailureException");
        } catch (Exception e) {
            Assert.assertTrue(isTransactionFailedException(e));
        }
    }

    @SuppressWarnings("unused")
    @Test(expected = TumlConstraintViolationException.class)
    public void testEmailValidationFail() {
        God g = new God(true);
        g.setName("asda");
        Universe universe = new Universe(g);
        universe.setEmail("asdasd");
        SpaceTime st = new SpaceTime(universe);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @SuppressWarnings("unused")
    @Test
    public void testEmailValidationPass() {
        God g = new God(true);
        g.setName("asda");
        Universe universe = new Universe(g);
        universe.setEmail("asdasd@asd.com");
        SpaceTime st = new SpaceTime(universe);
        Space s = new Space(st);
        Time t = new Time(st);
        db.commit();
    }

    @Test
    public void testRequired1() {
        try {
            God g = new God(true);
            g.setName("asda");
            Hand hand = new Hand(g);
            hand.setName("hand1");
            Finger finger1 = new Finger(hand);
            db.commit();
        } catch (Exception e) {
            Assert.assertTrue(isTransactionFailedException(e));
            return;
        }
        Assert.fail("Expected transaction failed exception");
    }

    @Test
    public void testRequired2() {
        God g = new God(true);
        g.setName("asda");
        Hand hand = new Hand(g);
        hand.setName("hand1");
        Finger finger1 = new Finger(hand);
        finger1.setName("name1");
        db.commit();
        try {
            finger1.setName(null);
            db.commit();
        } catch (Exception e) {
            Assert.assertTrue(isTransactionFailedException(e));
            return;
        }
        Assert.fail("Expected transaction failed exception");
    }

    @Test
    public void testMultiplicity() {
        try {
            God g = new God(true);
            g.setName("g");
            Hand h = new Hand(g);
            h.setName("hand");
            Finger f1 = new Finger(h);
            f1.setName("f1");
            Finger f2 = new Finger(h);
            f2.setName("f2");
            Finger f3 = new Finger(h);
            f3.setName("f3");
            Finger f4 = new Finger(h);
            f4.setName("f4");
            Finger f5 = new Finger(h);
            f5.setName("f5");
            Finger f6 = new Finger(h);
            f6.setName("f6");
            db.commit();
        } catch (Exception e) {
            Assert.assertTrue(isTransactionFailedException(e));
            return;
        }
        Assert.fail("Expected transaction failed exception");

    }

    @Test
    public void testMultiplicityLower() {
        try {
            God g = new God(true);
            g.setName("g");
            Fantasy h = new Fantasy(g);
            h.setName("hand");
            FWomen f1 = new FWomen(h);
            f1.setName("f1");
            db.commit();
        } catch (Exception e) {
            Assert.assertTrue(isTransactionFailedException(e));
            return;
        }
        Assert.fail("Expected transaction failed exception");
    }

    @Test
    public void testMultiplicityUpper() {
        try {
            God g = new God(true);
            g.setName("g");
            Fantasy h = new Fantasy(g);
            h.setName("hand");
            FWomen f1 = new FWomen(h);
            f1.setName("f1");
            FWomen f2 = new FWomen(h);
            f2.setName("f2");
            FWomen f3 = new FWomen(h);
            f3.setName("f3");
            FWomen f4 = new FWomen(h);
            f4.setName("f4");
            FWomen f5 = new FWomen(h);
            f5.setName("f5");
            FWomen f6 = new FWomen(h);
            f6.setName("f6");
            db.commit();
        } catch (Exception e) {
            Assert.assertTrue(isTransactionFailedException(e));
            return;
        }
        Assert.fail("Expected transaction failed exception");
    }

    @Test
    public void testValidationFailureAndRollback() {
        God g = new God(true);
        g.setName("g");
        Fantasy fantasy = new Fantasy(g);
        fantasy.setName("hand");
        FWomen w1 = new FWomen(fantasy);
        w1.setName("f1");
        FWomen w2 = new FWomen(fantasy);
        w2.setName("f2");
        FWomen w3 = new FWomen(fantasy);
        w3.setName("f3");
        FWomen w4 = new FWomen(fantasy);
        w4.setName("f4");
        db.commit();
        try {
            fantasy = new Fantasy(fantasy.getVertex());
            FWomen w5 = new FWomen(fantasy);
            w5.setName("f6");
            db.commit();
        } catch (Exception e) {
            db.rollback();
        }
        Assert.assertEquals(6, countVertices());
    }
}

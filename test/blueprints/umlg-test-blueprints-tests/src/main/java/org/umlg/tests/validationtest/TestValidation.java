package org.umlg.tests.validationtest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.FWomen;
import org.umlg.collectiontest.Fantasy;
import org.umlg.collectiontest.Finger;
import org.umlg.collectiontest.Hand;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.componenttest.ValidationTest;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolation;
import org.umlg.runtime.validation.UmlgConstraintViolationException;
import org.umlg.validation.TestManyValidation;

public class TestValidation extends BaseLocalDbTest {

    @SuppressWarnings("unused")
    @Test(expected = UmlgConstraintViolationException.class)
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
    @Test(expected = UmlgConstraintViolationException.class)
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
    @Test(expected = UmlgConstraintViolationException.class)
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
    @Test(expected = UmlgConstraintViolationException.class)
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
    @Test(expected = UmlgConstraintViolationException.class)
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
    @Test(expected = UmlgConstraintViolationException.class)
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
    @Test(expected = UmlgConstraintViolationException.class)
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
    @Test(expected = UmlgConstraintViolationException.class)
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
            Assert.assertTrue("excepting UmlgConstraintViolationException", e instanceof UmlgConstraintViolationException);
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
            Assert.assertTrue("excepting UmlgConstraintViolationException", e instanceof UmlgConstraintViolationException);
        }
    }

    @SuppressWarnings("unused")
    @Test(expected = UmlgConstraintViolationException.class)
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
            Assert.assertTrue("excepting UmlgConstraintViolationException", e instanceof UmlgConstraintViolationException);
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
            Assert.assertTrue("excepting UmlgConstraintViolationException", e instanceof UmlgConstraintViolationException);
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
            Assert.assertTrue("excepting UmlgConstraintViolationException", e instanceof UmlgConstraintViolationException);
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
            Assert.assertTrue("excepting UmlgConstraintViolationException", e instanceof UmlgConstraintViolationException);
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
            Assert.assertTrue("excepting UmlgConstraintViolationException", e instanceof UmlgConstraintViolationException);
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

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinIntegerFail() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMinInteger(4);
    }

    @Test
    public void testMinIntegerPass() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMinInteger(6);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxIntegerFail() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMaxInteger(6);
    }

    @Test
    public void testMaxIntegerPass() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMaxInteger(4);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeIntegerFail() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setARangeInteger(6);
    }

    @Test
    public void testRangeIntegerPass() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setARangeInteger(4);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinUnlimitedNaturalFail() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMinUnlimitedNatural(2147483645);
    }

    @Test
    public void testMinUnlimitedNaturalPass() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMinUnlimitedNatural(2147483647);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxUnlimitedNaturalFail() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMaxUnlimitedNatural(2);
    }

    @Test
    public void testMaxUnlimitedNaturalPass() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMaxUnlimitedNatural(0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeUnlimitedNaturalFail() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setARangeUnlimitedNatural(-2147483646);
    }

    @Test
    public void testRangeUnlimitedNaturalPass() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setARangeUnlimitedNatural(1);
        testValidation.setARangeUnlimitedNatural(2147483645);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinRealFail() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMinReal(5.122D);
    }

    @Test
    public void testMinRealPass() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMinReal(5.124D);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxRealFail() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMaxReal(5.124D);
    }

    @Test
    public void testMaxRealPass() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMaxReal(5.123D);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeRealFail() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setARangeReal(-1D);
    }

    @Test
    public void testRangeRealPass() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setARangeReal(0.001D);
        testValidation.setARangeReal(5.123D);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testUnlimitedNaturalIsBiggerOrEqualToZero() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setAMaxUnlimitedNatural(-1);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testUnlimitedNaturalIsBiggerOrEqualToZeroAgain() {
        org.umlg.validation.TestValidation testValidation = new org.umlg.validation.TestValidation();
        testValidation.setTestUnlimitedNatural(-1);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testManyMaxRealFail() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMaxRealMany(1D);
        testManyValidation.addToAMaxRealMany(2D);
        testManyValidation.addToAMaxRealMany(3D);
        testManyValidation.addToAMaxRealMany(4D);
        testManyValidation.addToAMaxRealMany(5D);
        testManyValidation.addToAMaxRealMany(6D);
        db.commit();
    }

    @Test
    public void testManyMaxRealPass() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMaxRealMany(1D);
        testManyValidation.addToAMaxRealMany(2D);
        testManyValidation.addToAMaxRealMany(3D);
        testManyValidation.addToAMaxRealMany(4D);
        testManyValidation.addToAMaxRealMany(5D);
        db.commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testManyMinRealFail() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMinRealMany(1D);
        db.commit();
    }

    @Test
    public void testManyMinRealPass() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMinRealMany(6D);
        testManyValidation.addToAMinRealMany(7D);
        db.commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testManyRangeRealFail() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToARangeRealMany(-5.124D);
        db.commit();
    }

    @Test
    public void testManyRangeRealPass() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToARangeRealMany(-5.123D);
        testManyValidation.addToARangeRealMany(5.11D);
        db.commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testManyMaxUnlimitedNaturalFail() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMaxUnlimitedNaturalMany(1);
        testManyValidation.addToAMaxUnlimitedNaturalMany(2);
        testManyValidation.addToAMaxUnlimitedNaturalMany(3);
        testManyValidation.addToAMaxUnlimitedNaturalMany(4);
        testManyValidation.addToAMaxUnlimitedNaturalMany(5);
        testManyValidation.addToAMaxUnlimitedNaturalMany(6);
        db.commit();
    }

    @Test
    public void testManyMaxUnlimitedNaturalPass() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMaxUnlimitedNaturalMany(1);
        testManyValidation.addToAMaxUnlimitedNaturalMany(2);
        testManyValidation.addToAMaxUnlimitedNaturalMany(3);
        testManyValidation.addToAMaxUnlimitedNaturalMany(4);
        testManyValidation.addToAMaxUnlimitedNaturalMany(5);
        db.commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testManyMinUnlimitedNaturalFail() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMinUnlimitedNaturalMany(1);
        db.commit();
    }

    @Test
    public void testManyMinUnlimitedNaturalPass() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMinUnlimitedNaturalMany(6);
        testManyValidation.addToAMinUnlimitedNaturalMany(7);
        db.commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testManyRangeUnlimitedNaturalFail() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToARangeUnlimitedNaturalMany(0);
        db.commit();
    }

    @Test
    public void testManyRangeUnlimitedNaturalPass() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToARangeUnlimitedNaturalMany(1);
        testManyValidation.addToARangeUnlimitedNaturalMany(4);
        db.commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testManyMaxIntegerFail() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMaxIntegerMany(1);
        testManyValidation.addToAMaxIntegerMany(2);
        testManyValidation.addToAMaxIntegerMany(3);
        testManyValidation.addToAMaxIntegerMany(4);
        testManyValidation.addToAMaxIntegerMany(5);
        testManyValidation.addToAMaxIntegerMany(6);
        db.commit();
    }

    @Test
    public void testManyMaxIntegerPass() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMaxIntegerMany(1);
        testManyValidation.addToAMaxIntegerMany(2);
        testManyValidation.addToAMaxIntegerMany(3);
        testManyValidation.addToAMaxIntegerMany(4);
        testManyValidation.addToAMaxIntegerMany(5);
        db.commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testManyMinIntegerFail() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMinIntegerMany(1);
        db.commit();
    }

    @Test
    public void testManyMinIntegerPass() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToAMinIntegerMany(6);
        testManyValidation.addToAMinIntegerMany(7);
        db.commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testManyRangeIntegerFail() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToARangeIntegerMany(-6);
        db.commit();
    }

    @Test
    public void testManyRangeIntegerPass() {
        TestManyValidation testManyValidation = new TestManyValidation();
        testManyValidation.addToARangeIntegerMany(1);
        testManyValidation.addToARangeIntegerMany(4);
        db.commit();
    }

//    @Test
//    public void testValidationMultipleThreadsFailsSuccessfully() throws ExecutionException, InterruptedException {
//        if (UmlgProperties.INSTANCE.isTransactionsMutliThreaded()) {
//            God g = new God(true);
//            g.setName("god");
//            Fantasy fantasy = new Fantasy(g);
//            fantasy.setName("hand");
//            final FWomen w1 = new FWomen(fantasy);
//            w1.setName("f1");
//            final FWomen w2 = new FWomen(fantasy);
//            w2.setName("f2");
//            FWomen w3 = new FWomen(fantasy);
//            w3.setName("f3");
//            FWomen w4 = new FWomen(fantasy);
//            w4.setName("f4");
//            db.commit();
//
//            FWomen w5 = new FWomen(fantasy);
//            w5.setName("f5");
//            FWomen w6 = new FWomen(fantasy);
//            w6.setName("f6");
//            final TransactionIdentifier transactionIdentifier = db.suspend();
//
//            ExecutorService es = Executors.newFixedThreadPool(1);
//            Future<Boolean> f = es.submit(new Callable<Boolean>() {
//                @Override
//                public Boolean call() throws Exception {
//                    GraphDb.get().resume(transactionIdentifier);
//                    FWomen w1ToDelete = new FWomen(w1.getVertex());
//                    w1ToDelete.delete();
//                    GraphDb.get().suspend();
//                    return true;
//                }
//            });
//            es.shutdown();
//            Assert.assertTrue(f.get());
//            db.resume(transactionIdentifier);
//            boolean failed = false;
//            try {
//                db.commit();
//            } catch (Exception e) {
//                if (isTransactionFailedException(e)) {
//                    failed = true;
//                }
//            }
//            Assert.assertTrue(failed);
//        }
//    }

    //TODO rethink this.
//    @Test
//    public void testValidationMultipleThreads() throws ExecutionException, InterruptedException {
//        God g = new God(true);
//        g.setName("god");
//        Fantasy fantasy = new Fantasy(g);
//        fantasy.setName("hand");
//        final FWomen w1 = new FWomen(fantasy);
//        w1.setName("f1");
//        final FWomen w2 = new FWomen(fantasy);
//        w2.setName("f2");
//        FWomen w3 = new FWomen(fantasy);
//        w3.setName("f3");
//        FWomen w4 = new FWomen(fantasy);
//        w4.setName("f4");
//        db.commit();
//
//        FWomen w5 = new FWomen(fantasy);
//        w5.setName("f5");
//        FWomen w6 = new FWomen(fantasy);
//        w6.setName("f6");
//        final TransactionIdentifier transactionIdentifier = db.suspend();
//
//        ExecutorService es = Executors.newFixedThreadPool(1);
//        Future<Boolean> f = es.submit(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                GraphDb.get().resume(transactionIdentifier);
//                FWomen w1ToDelete = new FWomen(w1.getVertex());
//                w1ToDelete.delete();
//
//                FWomen w2ToDelete = new FWomen(w2.getVertex());
//                w2ToDelete.delete();
//
//                GraphDb.get().suspend();
//                return true;
//            }
//        });
//        es.shutdown();
//
//        Assert.assertTrue(f.get());
//        db.resume(transactionIdentifier);
//        db.commit();
//    }

}

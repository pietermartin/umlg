package org.umlg.tinker.validationtest;

import junit.framework.Assert;
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
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.adaptor.TransactionIdentifier;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.UmlgProperties;
import org.umlg.runtime.validation.TumlConstraintViolationException;

import java.util.concurrent.*;

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

    @Test
    public void testValidationMultipleThreadsFailsSuccessfully() throws ExecutionException, InterruptedException {
        if (UmlgProperties.INSTANCE.isTransactionsMutliThreaded()) {
            God g = new God(true);
            g.setName("god");
            Fantasy fantasy = new Fantasy(g);
            fantasy.setName("hand");
            final FWomen w1 = new FWomen(fantasy);
            w1.setName("f1");
            final FWomen w2 = new FWomen(fantasy);
            w2.setName("f2");
            FWomen w3 = new FWomen(fantasy);
            w3.setName("f3");
            FWomen w4 = new FWomen(fantasy);
            w4.setName("f4");
            db.commit();

            FWomen w5 = new FWomen(fantasy);
            w5.setName("f5");
            FWomen w6 = new FWomen(fantasy);
            w6.setName("f6");
            final TransactionIdentifier transactionIdentifier = db.suspend();

            ExecutorService es = Executors.newFixedThreadPool(1);
            Future<Boolean> f = es.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    GraphDb.getDb().resume(transactionIdentifier);
                    FWomen w1ToDelete = new FWomen(w1.getVertex());
                    w1ToDelete.delete();
                    GraphDb.getDb().suspend();
                    return true;
                }
            });
            es.shutdown();
            Assert.assertTrue(f.get());
            db.resume(transactionIdentifier);
            boolean failed = false;
            try {
                db.commit();
            } catch (Exception e) {
                if (isTransactionFailedException(e)) {
                    failed = true;
                }
            }
            Assert.assertTrue(failed);
        }
    }

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
//                GraphDb.getDb().resume(transactionIdentifier);
//                FWomen w1ToDelete = new FWomen(w1.getVertex());
//                w1ToDelete.delete();
//
//                FWomen w2ToDelete = new FWomen(w2.getVertex());
//                w2ToDelete.delete();
//
//                GraphDb.getDb().suspend();
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

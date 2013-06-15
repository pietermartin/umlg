package org.umlg.transaction.test;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.collectiontest.World;
import org.umlg.concretetest.God;
import org.umlg.root.Root;
import org.umlg.runtime.adaptor.TransactionIdentifier;
import org.umlg.runtime.collection.TinkerSequence;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.concurrent.*;

/**
 * Date: 2013/02/01
 * Time: 6:08 PM
 */
public class TransactionSuspendResumeTest extends BaseLocalDbTest {

    @Test
    public void testSuspendResume() throws InterruptedException, ExecutionException {
        God god = new God(true);
        god.setName("god1");
        final TransactionIdentifier transactionIdentifier = db.suspend();

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<TransactionIdentifier> f = es.submit(new Callable<TransactionIdentifier>() {
            @Override
            public TransactionIdentifier call() {
                try {
                    db.resume(transactionIdentifier);
                    TinkerSequence<God> gods = Root.INSTANCE.getGod();
                    Assert.assertEquals(1, gods.size());
                    God god = gods.get(0);
                    World world = new World(god);
                    world.setName("universe1");
                    return db.suspend();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    semaphore.release();
                }
            }
        });
        es.shutdown();
        TransactionIdentifier transactionIdentifier1 = f.get();
        semaphore.acquire();
        db.resume(transactionIdentifier1);
        World world = new World(god);
        world.setName("universe2");
        db.commit();

        Assert.assertEquals(3, countVertices());

    }


}

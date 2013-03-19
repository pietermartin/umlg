package org.tuml.tinker.speed;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import org.tuml.collectiontest.Finger;
import org.tuml.collectiontest.Hand;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/02/15
 * Time: 7:45 PM
 */
public class SpeedTest extends BaseLocalDbTest {

    //0:01:32.267
    @Test
    public void testSpeed() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        God god = new God(true);
        god.setName("god");

        for (int i = 0; i < 1000000; i++) {
            Hand hand = new Hand(god);
            hand.setName("hand" + i);
//            for (int j = 0; j < 5; j++) {
//                Finger finger = new Finger(hand);
//                finger.setName("finger" + i + "_" + j);
//            }
            if (i % 1000 == 0) {
                System.out.println(i + " " + stopWatch.toString());
                db.commit();
            }
        }

        db.commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }
}

package org.test.restlet.ocl;

import junit.framework.Assert;
import org.junit.Test;
import org.test.restlet.TestRestletDefaultDataCreator;
import org.umlg.root.Root;
import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.TinkerOrderedSet;
import org.umlg.runtime.collection.TinkerSequence;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.test.standard.Finger;
import org.umlg.test.standard.Hand;
import org.umlg.test.standard.Human;
import org.umlg.test.standard.Ring;

/**
 * Date: 2013/02/13
 * Time: 1:21 PM
 */
public class TestOcl extends BaseLocalDbTest  {

    @Test
    public void testocl1() {
        TestRestletDefaultDataCreator testRestletDefaultDataCreator = new TestRestletDefaultDataCreator();
        testRestletDefaultDataCreator.createData();
        Human human = Root.INSTANCE.getHuman().get(0);
        TinkerBag<Human> humans = execute(human);
        Assert.assertEquals(10, humans.size());

    }

    private TinkerBag<Human> execute(Human human) {
        TinkerBag<Human> result = human.getHand().<Finger, TinkerOrderedSet<Finger>>collect(new BodyExpressionEvaluator<TinkerOrderedSet<Finger>, Hand>() {
            @Override
            public TinkerOrderedSet<Finger> evaluate(Hand temp1) {
                return temp1.getFinger();
            }
        }).<Ring, Ring>collect(new BodyExpressionEvaluator<Ring, Finger>() {
            @Override
            public Ring evaluate(Finger temp2) {
                return temp2.getRing();
            }
        }).<Human, Human>collect(new BodyExpressionEvaluator<Human, Ring>() {
            @Override
            public Human evaluate(Ring temp3) {
                return temp3.getHuman();
            }
        });
        return result;
    }

    @Test
    public void testocl2() {
        TestRestletDefaultDataCreator testRestletDefaultDataCreator = new TestRestletDefaultDataCreator();
        testRestletDefaultDataCreator.createData();
        Human human = Root.INSTANCE.getHuman().get(0);
        Hand hand = human.getHand().iterator().next();
        TinkerSequence<Ring> rings = execute(hand);
        Assert.assertEquals(5, rings.size());

    }

    private TinkerSequence<Ring> execute(Hand hand) {
        TinkerSequence<Ring> result = hand.getFinger().<Ring, Ring>collect(new BodyExpressionEvaluator<Ring, Finger>() {
            @Override
            public Ring evaluate(Finger temp1) {
                return temp1.getRing();
            }
        });
        return result;
    }
}

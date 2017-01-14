package org.test.restlet.ocl;

import org.junit.Assert;
import org.junit.Test;
import org.test.restlet.TestRestletDefaultDataCreator;
import org.umlg.model.RestAndJson;
import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgSequence;
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
        Human human = RestAndJson.getHuman().asSequence().get(0);
        UmlgBag<Human> humans = execute(human);
        Assert.assertEquals(10, humans.size());

    }

    private UmlgBag<Human> execute(Human human) {
        UmlgBag<Human> result = human.getHand().<Finger, UmlgOrderedSet<Finger>>collect(new BodyExpressionEvaluator<UmlgOrderedSet<Finger>, Hand>() {
            @Override
            public UmlgOrderedSet<Finger> evaluate(Hand temp1) {
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
        Human human = RestAndJson.getHuman().asSequence().get(0);
        Hand hand = human.getHand().iterator().next();
        UmlgSequence<Ring> rings = execute(hand);
        Assert.assertEquals(5, rings.size());

    }

    private UmlgSequence<Ring> execute(Hand hand) {
        UmlgSequence<Ring> result = hand.getFinger().<Ring, Ring>collect(new BodyExpressionEvaluator<Ring, Finger>() {
            @Override
            public Ring evaluate(Finger temp1) {
                return temp1.getRing();
            }
        });
        return result;
    }
}

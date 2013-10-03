package org.umlg.sequence.test;

import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.test.standard.*;

/**
 * Date: 2013/05/05
 * Time: 4:06 PM
 */
public class TestHandFinger extends BaseLocalDbTest {

    @Test
    public void testDeletion() {

        Human h1 = new Human(true);
        h1.setName("human1");
        h1.setGender(Gender.FEMALE);
        Home home = new Home(h1);
        home.setEmail("aaa@aaa.aaa");
        HomeOneComponent homeOneComponent = new HomeOneComponent(home);
        homeOneComponent.setName("homeOneComponent");
        ComponentManyDeep1 componentManyDeep1 = new ComponentManyDeep1(h1);
        componentManyDeep1.setName("componentManyDeep1");
        componentManyDeep1.setEmail("sss@sss.sss");
        ComponentManyDeep2 componentManyDeep2 = new ComponentManyDeep2(componentManyDeep1);
        componentManyDeep2.setName("componentManyDeep2");
        ComponentOneDeep3 componentOneDeep3 = new ComponentOneDeep3(componentManyDeep2);
        componentOneDeep3.setName("componentOneDeep3");

        ComponentMany componentMany1 = new ComponentMany(h1);
        componentMany1.setName("componentMany1");
        ComponentMany componentMany2 = new ComponentMany(h1);
        componentMany2.setName("componentMany2");

        Hand hand1 = new Hand(h1);
        hand1.setName("hand1");
        hand1.setTestNumber(6);
        hand1.setTestUnlimitedNatural(1L);
        Hand hand2 = new Hand(h1);
        hand2.setName("hand2");
        hand2.setTestNumber(6);
        hand2.setTestUnlimitedNatural(1L);

        Finger finger1 = new Finger(hand1);
        finger1.setName("finger1");
        finger1.addToManyRequiredInteger(1);
        Finger finger2 = new Finger(hand1);
        finger2.setName("finger2");
        finger2.addToManyRequiredInteger(1);
        Finger finger3 = new Finger(hand1);
        finger3.setName("finger3");
        finger3.addToManyRequiredInteger(1);
        Finger finger4 = new Finger(hand1);
        finger4.setName("finger4");
        finger4.addToManyRequiredInteger(1);
        Finger finger5 = new Finger(hand1);
        finger5.setName("finger5");
        finger5.addToManyRequiredInteger(1);
        db.commit();

        finger3.delete();
        finger4.delete();
        finger5.delete();
        db.commit();

        Finger finger6 = new Finger(hand1);
        finger6.setName("finger6");
        finger6.addToManyRequiredInteger(1);
        db.commit();

    }
}

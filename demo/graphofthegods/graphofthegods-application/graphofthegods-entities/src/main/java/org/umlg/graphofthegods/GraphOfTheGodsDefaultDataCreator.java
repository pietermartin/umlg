package org.umlg.graphofthegods;

import org.umlg.*;
import org.umlg.runtime.adaptor.DefaultDataCreator;
import org.umlg.runtime.adaptor.UMLG;

/**
 * Date: 2014/03/02
 * Time: 9:23 AM
 */
public class GraphOfTheGodsDefaultDataCreator implements DefaultDataCreator {

    @Override
    public void createData() {

        if (Titan.allInstances().isEmpty()) {
            Titan saturn = new Titan();
            saturn.setName("Saturn");
            saturn.setAge(10000);
            saturn.setGender(Gender.MALE);

            God jupiter = new God();
            jupiter.setName("Jupiter");
            jupiter.setAge(5000);
            jupiter.setGender(Gender.MALE);

            God neptune = new God();
            neptune.setName("Neptune");
            neptune.setAge(4500);
            neptune.setGender(Gender.MALE);

            God pluto = new God();
            pluto.setName("Pluto");
            pluto.setAge(4000);
            pluto.setGender(Gender.MALE);

            DemiGod hercules = new DemiGod();
            hercules.setName("Hercules");
            hercules.setAge(30);
            hercules.setGender(Gender.MALE);

            Human alcmene = new Human();
            alcmene.setName("Alcmene");
            alcmene.setAge(45);
            alcmene.setGender(Gender.FEMALE);

            //Set up family relationships
            saturn.addToFatherOfChild(jupiter);
            jupiter.addToFatherOfChild(hercules);
            alcmene.addToMotherOfChild(hercules);

            jupiter.addToBrother(neptune);
            jupiter.addToBrother(pluto);
            neptune.addToBrother(jupiter);
            neptune.addToBrother(pluto);
            pluto.addToBrother(neptune);
            pluto.addToBrother(jupiter);


            Monster nemean = new Monster();
            nemean.setName("Nemean");
            Monster hydra = new Monster();
            hydra.setName("Hydra");
            Monster cerberus = new Monster();
            cerberus.setName("Cerberus");

            Location sky = new Location();
            sky.setName("sky");
            Location sea = new Location();
            sea.setName("sea");
            Location tartarus = new Location();
            tartarus.setName("tartarus");

            //Set lives in
            Lives jupiterLives = new Lives();
            jupiterLives.setReason("loves fresh breezes");
            jupiter.addToLocation(sky, jupiterLives);

            Lives neptuneLives = new Lives();
            neptuneLives.setReason("loves waves");
            neptune.addToLocation(sea, neptuneLives);

            Lives plutoLives = new Lives();
            plutoLives.setReason("no fear of death");
            pluto.addToLocation(tartarus, plutoLives);

            Lives cerberusLives = new Lives();
            cerberus.addToLocation(tartarus, cerberusLives);

            //Set pet
            pluto.addToPet(cerberus);

            //Set battles
            Battle battle1 = new Battle();
            battle1.setTime(1);
            battle1.setPlace("[38.1,23.7]");
            hercules.addToMonster(nemean, battle1);

            Battle battle2 = new Battle();
            battle2.setTime(2);
            battle2.setPlace("[37.7,23.9]");
            hercules.addToMonster(hydra, battle2);

            Battle battle3 = new Battle();
            battle3.setTime(12);
            battle3.setPlace("[39,22]");
            hercules.addToMonster(cerberus, battle3);

            UMLG.get().commit();
        }
    }

}

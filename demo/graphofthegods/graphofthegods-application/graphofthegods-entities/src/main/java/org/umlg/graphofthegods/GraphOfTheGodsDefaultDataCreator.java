package org.umlg.graphofthegods;

import org.umlg.*;
import org.umlg.runtime.adaptor.DefaultDataCreator;

/**
 * Date: 2014/03/02
 * Time: 9:23 AM
 */
public class GraphOfTheGodsDefaultDataCreator implements DefaultDataCreator {

    @Override
    public void createData() {

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
        neptune.addToBrother(neptune);

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



    }

}

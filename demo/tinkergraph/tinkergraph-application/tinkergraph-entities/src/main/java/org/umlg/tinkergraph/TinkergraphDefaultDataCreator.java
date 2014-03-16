package org.umlg.tinkergraph;

import org.umlg.runtime.adaptor.DefaultDataCreator;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.tinkergraph.meta.PersonMeta;

/**
 * Date: 2012/12/31
 * Time: 3:30 PM
 */
public class TinkergraphDefaultDataCreator implements DefaultDataCreator {

    public void createData() {

        if (PersonMeta.getInstance().getAllInstances().isEmpty()) {

            Person marko = new Person();
            marko.setName("marko");
            marko.setAge(29);
            Person peter = new Person();
            peter.setName("peter");
            peter.setAge(35);
            Person vadas = new Person();
            vadas.setName("vadas");
            vadas.setAge(27);
            Person josh = new Person();
            josh.setName("josh");
            josh.setAge(32);

            //Set the friendships
            Friendship markoVadasFriendship = new Friendship();
            markoVadasFriendship.setWeight(0.5);
            marko.addToKnows(vadas, markoVadasFriendship);

            Friendship markoJoshFriendship = new Friendship();
            markoJoshFriendship.setWeight(0.5);
            marko.addToKnows(josh, markoJoshFriendship);

            Program lop = new Program();
            lop.setName("lop");
            lop.setLanguage(LanguageEnum.JAVA);

            Program ripple = new Program();
            ripple.setName("ripple");
            ripple.setLanguage(LanguageEnum.JAVA);

            //Set created
            Work markoLop = new Work();
            markoLop.setWeight(0.4);
            marko.addToCreated(lop, markoLop);

            Work joshLop = new Work();
            joshLop.setWeight(0.4);
            josh.addToCreated(lop, joshLop);

            Work peterLop = new Work();
            peterLop.setWeight(0.2);
            peter.addToCreated(lop, peterLop);

            Work joshRipple = new Work();
            joshRipple.setWeight(1.0);
            josh.addToCreated(ripple, joshRipple);

            UMLG.getDb().commit();
        }
    }
}

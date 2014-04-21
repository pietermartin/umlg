package org.umlg.tinkergraph;

import org.umlg.meta.RootQuery;
import org.umlg.meta.meta.RootQueryMeta;
import org.umlg.query.QueryEnum;
import org.umlg.runtime.adaptor.DefaultDataCreator;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.tinkergraph.meta.PersonMeta;

/**
 * Date: 2012/12/31
 * Time: 3:30 PM
 */
public class TinkergraphDefaultDataCreator implements DefaultDataCreator {

    public void createData() {

        if (Person.allInstances().isEmpty()) {

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

            RootQuery deleteAll = new RootQuery(true);
            deleteAll.setName("deleteAll");
            deleteAll.setDescription("Deletes all persons and programs");
            deleteAll.setQueryEnum(QueryEnum.GROOVY);
            deleteAll.setQueryString("for (Person person : Person.allInstances()) {\\n    person.delete();\\n}\\nfor (Program program : Program.allInstances()) {\\n    program.delete();\\n}\\nUMLG.get().commit();");

            RootQuery createDefaultData = new RootQuery(true);
            createDefaultData.setName("createDefaultData");
            createDefaultData.setDescription("Creates tinkergraph default data.");
            createDefaultData.setQueryEnum(QueryEnum.GROOVY);

            StringBuilder sb = new StringBuilder();
            sb.append("if (Person.allInstances().isEmpty()) {\\n");
            sb.append("    Person marko = new Person();\\n");
            sb.append("    marko.setName(\"marko\");\\n");
            sb.append("    marko.setAge(29);\\n");
            sb.append("    Person peter = new Person();\\n");
            sb.append("    peter.setName(\"peter\");\\n");
            sb.append("    peter.setAge(35);\\n");
            sb.append("    Person vadas = new Person();\\n");
            sb.append("    vadas.setName(\"vadas\");\\n");
            sb.append("    vadas.setAge(27);\\n");
            sb.append("    Person josh = new Person();\\n");
            sb.append("    josh.setName(\"josh\");\\n");
            sb.append("    josh.setAge(32);\\n");
            sb.append("\\n");
            sb.append("    //Set the friendships\\n");
            sb.append("    Friendship markoVadasFriendship = new Friendship();\\n");
            sb.append("    markoVadasFriendship.setWeight(0.5);\\n");
            sb.append("    marko.addToKnows(vadas, markoVadasFriendship);\\n");
            sb.append("    Friendship markoJoshFriendship = new Friendship();\\n");
            sb.append("    markoJoshFriendship.setWeight(0.5);\\n");
            sb.append("    marko.addToKnows(josh, markoJoshFriendship);\\n");
            sb.append("    Program lop = new Program();\\n");
            sb.append("    lop.setName(\"lop\");\\n");
            sb.append("    lop.setLanguage(LanguageEnum.JAVA);\\n");
            sb.append("    Program ripple = new Program();\\n");
            sb.append("    ripple.setName(\"ripple\");\\n");
            sb.append("    ripple.setLanguage(LanguageEnum.JAVA);\\n");
            sb.append("\\n");
            sb.append("    //Set created\\n");
            sb.append("    Work markoLop = new Work();\\n");
            sb.append("    markoLop.setWeight(0.4);\\n");
            sb.append("    marko.addToCreated(lop, markoLop);\\n");
            sb.append("    Work joshLop = new Work();\\n");
            sb.append("    joshLop.setWeight(0.4);\\n");
            sb.append("    josh.addToCreated(lop, joshLop);\\n");
            sb.append("    Work peterLop = new Work();\\n");
            sb.append("    peterLop.setWeight(0.2);\\n");
            sb.append("    peter.addToCreated(lop, peterLop);\\n");
            sb.append("    Work joshRipple = new Work();\\n");
            sb.append("    joshRipple.setWeight(1.0);\\n");
            sb.append("    josh.addToCreated(ripple, joshRipple);\\n");
            sb.append("    UMLG.get().commit();\\n");
            sb.append("}");

            createDefaultData.setQueryString(sb.toString());
            UMLG.get().commit();
        }
    }
}
